package sh.jay.xposed.whatsapp;

import android.graphics.Color;
import de.robv.android.xposed.XSharedPreferences;

public class Preferences {
    // This current package.
    private static final String PACKAGE_NAME = "sh.jay.xposed.whatsapp";

    // Our single instance of an XSharedPreferences.
    private static XSharedPreferences instance = null;
    
    /**
     * Load the preferences from our shared preference file.
     * 
     * TODO: Not methods in this class are not DRY. Get XSharedPreferences to read the defaults from XML.
     */
    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(PACKAGE_NAME);
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        
        return instance;
    }
    
    public static boolean isDebug() {
        return getInstance().getBoolean("whatsapp_remove_action_shortcuts_debug", false);
    }
    
    public static boolean hasRemoveCameraPreference() {
        return getInstance().getBoolean("whatsapp_remove_action_shortcuts_camera", true);
    }

    public static boolean hasRemoveVoicePreference() {
        return getInstance().getBoolean("whatsapp_remove_action_shortcuts_voice", true);
    }

    public static boolean hasFixBackToContacts() {
        return getInstance().getBoolean("whatsapp_fix_back_to_contacts", true);
    }

    public static boolean hasHighlightGroups() {
        return getInstance().getBoolean("whatsapp_highlight_groups", true);
    }
    
    public static boolean hasDisableExpiryCheck() {
        return getInstance().getBoolean("whatsapp_disable_expiry_check", true);
    }

    public static int getHighlightGroupColor() {
        return getInstance().getInt("whatsapp_highlight_groups_color", 0xFFF0F0F0);
    }
}
