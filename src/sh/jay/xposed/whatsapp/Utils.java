package sh.jay.xposed.whatsapp;

import java.util.Calendar;

import de.robv.android.xposed.XposedBridge;

public class Utils {
    // TODO: Is this really the best place for this? Doesn't seem like a utility
    // at all. Where else? A class just for constants?
    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    /**
     * Capture debugging messages.
     * 
     * @param message
     */
    public static void debug(String message) {
        if (Preferences.isDebug()) {
            String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            XposedBridge.log(currentDateTime + ": " + message);
        }
    }
}
