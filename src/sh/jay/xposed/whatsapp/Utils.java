package sh.jay.xposed.whatsapp;

import java.util.Calendar;

import de.robv.android.xposed.XposedBridge;

public class Utils {
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
