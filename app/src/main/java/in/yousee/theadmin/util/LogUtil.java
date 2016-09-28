package in.yousee.theadmin.util;

import android.util.Log;

/**
 * Created by mittu on 3/7/2015.
 */
public class LogUtil {

    static String TAG_DEBUG = "TAG_DEBUG";

    static boolean debugMode = true;
    public static void print(String text)
    {
        print(TAG_DEBUG,text);
    }
    public static void print(String tag, String text)
    {

        if(debugMode == true) {
            Log.i(tag, text);
        }
    }
}
