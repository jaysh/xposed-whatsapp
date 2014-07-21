package sh.jay.xposed.whatsapp;

import java.util.Calendar;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class RemoveActionShortcuts implements IXposedHookInitPackageResources {
    // This current package.
    private static final String PACKAGE_NAME = "sh.jay.xposed.whatsapp";
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    private boolean debugMode = false;

    /**
     * Load the preferences from our shared preference file.
     */
    private void loadPreferences() {
        XSharedPreferences prefApps = new XSharedPreferences(PACKAGE_NAME);
        prefApps.makeWorldReadable();

        this.debugMode = prefApps.getBoolean("whatsapp_remove_action_shortcuts_debug", false);
    }
 
    /**
     * Called when the resources are loaded. Allows us to hook the conversation layout, and
     * modify it on-the-fly.
     */
    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(WHATSAPP_PACKAGE_NAME)) {
            return;
        }
        
        // Modifies the conversation layout to make the buttons we don't want to be zero pixels
        // by zero pixels.
        resparam.res.hookLayout(WHATSAPP_PACKAGE_NAME, "layout", "conversation", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                loadPreferences();

                debug("=================== Start of Conversation layout inflated callback ===================");
                
                ImageButton voiceButton = (ImageButton) liparam.view.findViewById(
                        liparam.res.getIdentifier("voice_note_btn", "id", WHATSAPP_PACKAGE_NAME)
                );
                if (null == voiceButton) {
                    debug("Could not locate the voice button, skipping removal");
                } else {
                    FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(0, 0);
                    voiceButton.setLayoutParams(frameLayoutParams);
                }

                ImageButton cameraButton = (ImageButton) liparam.view.findViewById(
                        liparam.res.getIdentifier("camera_btn", "id", WHATSAPP_PACKAGE_NAME)
                );
                if (null == cameraButton) {
                    debug("Could not locate the camera button, skipping removal");
                } else {
                    LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0, 0);
                    cameraButton.setLayoutParams(linearLayoutParams);                    
                }
                
                debug("=================== End of Conversation layout inflated callback ===================");
            }
        }); 
    }

    /**
     * Capture debugging messages.
     * 
     * @param message
     */
    private void debug(String message) {
        if (this.debugMode) {
            String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            XposedBridge.log(currentDateTime + ": " + message);
        }
    }
}
