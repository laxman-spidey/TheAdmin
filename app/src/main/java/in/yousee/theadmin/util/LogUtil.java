package in.yousee.theadmin.util;

import android.util.Log;

/**
 * Created by mittu on 3/7/2015.
 */
public class LogUtil {

    static boolean debugMode = true;
    public static void print(String text)
    {

        if(debugMode == true) {
            Log.i("bloodbank_tag", text);
        }
    }
}
