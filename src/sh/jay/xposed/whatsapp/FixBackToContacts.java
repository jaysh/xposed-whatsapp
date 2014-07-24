package sh.jay.xposed.whatsapp;

import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * This hook allows allows us to return back to the contact list when we use it to
 * launch a conversation. The original implementation does this:
 * 
 * - See conversations list
 * - Click "contacts list" (from the action bar)
 * - Click a contact
 * - Conversation for that contact appears
 * - Now, if you press back, it goes back to the conversation list, not contact list.
 * 
 * The reason for this appears to be that conversations uses startActivityForResult() in order
 * to see which contact the user selected, and then when it gets that result, launches the
 * appropriate conversation. What this fix does is, when you select a contact from the list, 
 * it launches a new activity for that conversation directly, skipping the logic that would
 * otherwise call setResult() with the contact information followed by finish() to go back to
 * the Conversations activity.
 */
public class FixBackToContacts implements IXposedHookLoadPackage {
    // This is a contact, of the form "<phone number>@s.whatsapp.net".
    private static String contactClickedInList;
    
    // Useful package constants.
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static final String WHATSAPP_CONTACT_PICKER_CLASS_NAME = WHATSAPP_PACKAGE_NAME + ".ContactPicker";

    /**
     * This is initially called by the Xposed Framework when we register this
     * android application as an Xposed Module.
     */
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        // This method is called once per package, so we only want to apply hooks to WhatsApp.
        if (WHATSAPP_PACKAGE_NAME.equals(lpparam.packageName)) {
            Class<?> contactPickerClass = findClass(WHATSAPP_CONTACT_PICKER_CLASS_NAME, lpparam.classLoader);
            Method setResultMethod = findMethodBestMatch(contactPickerClass, "setResult", int.class, Intent.class);
            XposedBridge.hookMethod(setResultMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Utils.debug("Start of setResult callback");

                    if (!WHATSAPP_CONTACT_PICKER_CLASS_NAME.equals(param.thisObject.getClass().getName())) {
                        // Because setResult() isn't implemented any of WhatsApp's methods, we're forced
                        // to hook it on a base class. So, ignore calls from classes we didn't really
                        // want to hook in the first place.
                        Utils.debug("Ignoring call from " + param.thisObject.getClass().getName());
                        return;
                    }
                    
                    if (!Preferences.hasFixBackToContacts()) {
                        Utils.debug("Not fixing the back button to contacts list because that's what the user preference indicated");
                        return;
                    }

                    Intent whatsappContactSelected = (Intent) param.args[1];
                    if (whatsappContactSelected.getExtras() == null) {
                        // If there are no extras, then we can't pull out the contact information.
                        Utils.debug("Intent bundle had no extras, skipping contact extraction");
                    } else {
                        contactClickedInList = whatsappContactSelected.getExtras().getString("contact", /* default */ null);
                        if (null == contactClickedInList) {
                            Utils.debug("Contact is null!");
                        } else {
                        	Utils.debug("Contact is: " + contactClickedInList);
                            // We prevent the original setResult() call from happening because otherwise after
                            // we launch the conversation activity, use of the back button will result in 
                            // confusion (Conversations -> ContactPicker -> Conversation -> (back) ContactPicker
                            // -> (back) Conversation) since the result will be passed to the original
                            // Conversations activity and that'll start the conversation again.
                            param.setResult(null);
                        }
                    }

                    Utils.debug("End of setResult callback");
                }
            });

            Method finishMethod = findMethodBestMatch(contactPickerClass, "finish");
            XposedBridge.hookMethod(finishMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Utils.debug("Start of finish callback");

                    if (!WHATSAPP_CONTACT_PICKER_CLASS_NAME.equals(param.thisObject.getClass().getName())) {
                        // Because finish() isn't implemented any of WhatsApp's methods, we're forced
                        // to hook it on a base class. So, ignore calls from classes we didn't really
                        // want to hook in the first place.
                        Utils.debug("Ignoring call from " + param.thisObject.getClass().getName());
                        return;
                    }

                    if (!Preferences.hasFixBackToContacts()) {
                        Utils.debug("Not fixing the back button to contacts list because that's what the user preference indicated");
                        return;
                    }

                    if (null == contactClickedInList) {
                        Utils.debug("finish was called but there is no contact ID, so not doing anything");
                    } else {
                        Activity contactPicker = (Activity) param.thisObject;

                        // Taken from http://stackoverflow.com/questions/16121163/how-can-i-open-whatsapps-conversation-activity-using-contact-data
                        // If anyone has a neater way of doing this (since we're in Xposed, especially, it
                        // feels like there should be a neat hack we can cheat with to launch the conversation idiomatically).
                        Cursor c = contactPicker.getContentResolver().query(
                            ContactsContract.Data.CONTENT_URI,
                            new String[] { ContactsContract.Contacts.Data._ID },
                            ContactsContract.Data.DATA1 + "=?",
                            new String[] { contactClickedInList },
                            null
                        );
    
                        c.moveToFirst();
                        try {
                            Intent whatsappConversation = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));
                            contactPicker.startActivity(whatsappConversation);
                            
                            Utils.debug("Launched conversation for: " + contactClickedInList);

                            // So we don't re-launch the conversation on a duplicate finish().
                            contactClickedInList = null;
                            
                            // Don't allow this finish() call to propagate. This is the magic that allows us to
                            // press the back key on a conversation, and it'll return to the ContactPicker.
                            param.setResult(null);
                            Utils.debug("Suppressed original call to setResult()");
                        } catch (Exception e) {
                            Utils.debug("Failed to start activity for the new conversation");
                        }
                        c.close();
                    }

                    Utils.debug("End of finish callback");
                }
            });

        }
    }
}
