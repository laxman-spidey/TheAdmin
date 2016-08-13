package in.yousee.theadmin.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mittu on 13-08-2016.
 */
public class AttendanceHistoryRecord implements JSONParsable {

    public String date;
    public String timeIn;
    public String timeOut;

    public static final String TAG_DATE = "date";
    public static final String TAG_TIME_IN = "timeIn";
    public static final String TAG_TIME_OUT = "timeOut";

    public AttendanceHistoryRecord(String string)
    {
        try
        {
            JSONObject object = new JSONObject(string);
            parseJSON(object);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public AttendanceHistoryRecord(JSONObject jsonObject)
    {
        parseJSON(jsonObject);
    }

    @Override
    public void parseJSON(JSONObject JSONObject) {
        try
        {
            this.date = JSONObject.getString(TAG_DATE);
            this.timeIn = JSONObject.getString(TAG_TIME_IN);
            this.timeOut = JSONObject.getString(TAG_TIME_OUT);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
