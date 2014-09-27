package sh.jay.xposed.whatsapp;

import android.content.Intent;
import android.content.res.XModuleResources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;

/**
 * Adds an Preference entry inside WhatsApp Settings Activity which acts like a shortcut
 * to the module's settings
 */
public class ShowShortcutOnSettings implements IXposedHookZygoteInit, IXposedHookLoadPackage,
        IXposedHookInitPackageResources {

    // Useful package constants.
    private static final String WHATSAPP_SETTINGS_CLASS = Utils.WHATSAPP_PACKAGE_NAME + ".Settings";

    // Preference resources provided by the module
    private static int MODULE_PREFERENCE_ICON_ID = R.drawable.ic_launcher;
    private static int MODULE_PREFERENCE_TITLE_ID = R.string.app_name;

    // Stores the module's path in order to use it to locate the icon that will be used in Settings
    private static String MODULE_PATH = null;

    // Stores the id for the resource icon to be used within the WhatsApp context
    private int mPreferenceIconId = 0;

    // Stores the id for the resource title string used within the WhatsApp context
    private int mPreferenceTitleId = 0;

    /**
     * This is called by Xposed as soon as the Zygote is loaded
     */
    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
        Utils.debug(MODULE_PATH);
    }

    /**
     * This is called as soon as the app's resources are being processed
     */
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(resparam.packageName))
            return;

        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        // Inject the preference resources from the module to the app, keeping a reference to the new,
        // generated IDs to use with WhatsApp context
        mPreferenceIconId = resparam.res.addResource(modRes, MODULE_PREFERENCE_ICON_ID);
        mPreferenceTitleId = resparam.res.addResource(modRes, MODULE_PREFERENCE_TITLE_ID);
    }

    /**
     * This is initially called by the Xposed Framework when we register this
     * android application as an Xposed Module.
     */
    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        // This method is called once per package, so we only want to apply hooks to WhatsApp.
        if (Utils.WHATSAPP_PACKAGE_NAME.equals(lpparam.packageName)) {
            // Find Settings Activity
            Class<?> settingsClass = findClass(WHATSAPP_SETTINGS_CLASS, lpparam.classLoader);
            if (settingsClass != null){
                Utils.debug("We found the Settings Activity");
                // Find onCreate method so we can hook it
                Method onCreateSettingsMethod = findMethodBestMatch(settingsClass,
                        "onCreate", Bundle.class);
                if (onCreateSettingsMethod != null) {
                    Utils.debug("We found Settings Activity's onCreate");
                    // Hook after onCreate's execution
                    XposedBridge.hookMethod(onCreateSettingsMethod, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Utils.debug("Start of after-onCreate callback");
                            // Get a reference to the WhatsApp Settings Activity
                            final PreferenceActivity waSettingsActivity =
                                    (PreferenceActivity) param.thisObject;

                            // Create a new Preference to link to the module
                            Preference jumpToModsPreference = new Preference(waSettingsActivity);
                            jumpToModsPreference.setTitle(mPreferenceTitleId);
                            jumpToModsPreference.setIcon(mPreferenceIconId);

                            // Set listener to trigger the jump to the module's settings
                            jumpToModsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                                @Override
                                public boolean onPreferenceClick(Preference preference) {
                                    Intent i = new Intent();
                                    i.setClassName("sh.jay.xposed.whatsapp",
                                            "sh.jay.xposed.whatsapp.SettingsActivity");
                                    waSettingsActivity.startActivity(i);
                                    return true;
                                }
                            });

                            // Add preference to Settings' PreferenceScreen
                            waSettingsActivity.getPreferenceScreen().addPreference(
                                    jumpToModsPreference);

                            Utils.debug("End of after-onCreate callback");
                        }
                    });
                }
            }


        }
    }
}