package sh.jay.xposed.whatsapp;

import java.util.Calendar;

import android.util.Log;
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
    private boolean removeCameraPreference = false;
    private boolean removeVoicePreference = false;

    /**
     * Load the preferences from our shared preference file.
     */
    private void loadPreferences() {
        XSharedPreferences prefApps = new XSharedPreferences(PACKAGE_NAME);
        prefApps.makeWorldReadable();

        // TODO: Not DRY. Get XSharedPreferences to read the defaults from XML.
        this.debugMode = prefApps.getBoolean("whatsapp_remove_action_shortcuts_debug", false);
        this.removeCameraPreference = prefApps.getBoolean("whatsapp_remove_action_shortcuts_camera", true);
        this.removeVoicePreference = prefApps.getBoolean("whatsapp_remove_action_shortcuts_voice", true);
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
                
                if (removeVoicePreference) {
                	try {
	                	ImageButton voiceButton = (ImageButton) liparam.view.findViewById(
		                        liparam.res.getIdentifier("voice_note_btn", "id", WHATSAPP_PACKAGE_NAME)
		                );
		                if (null == voiceButton) {
		                    debug("Could not locate the voice button, skipping removal");
		                } else {
		                    FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(0, 0);
		                    voiceButton.setLayoutParams(frameLayoutParams);
		                }
                	} catch (Exception e) {
                		debug("Failed to hook voice button: " + Log.getStackTraceString(e));
                	}
                } else {
                	debug("Not removing voice button because that's what the user preference indicated");
                }

                if (removeCameraPreference) {
                	try {
		                ImageButton cameraButton = (ImageButton) liparam.view.findViewById(
		                        liparam.res.getIdentifier("camera_btn", "id", WHATSAPP_PACKAGE_NAME)
		                );
		                if (null == cameraButton) {
		                    debug("Could not locate the camera button, skipping removal");
		                } else {
		                    LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0, 0);
		                    cameraButton.setLayoutParams(linearLayoutParams);                    
		                }
                	} catch (Exception e) {
                		debug("Failed to hook camera button: " + Log.getStackTraceString(e));
                	}
                } else {
                	debug("Not removing camera button because that's what the user preference indicated");
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
