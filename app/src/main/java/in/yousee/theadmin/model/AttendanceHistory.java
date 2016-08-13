package in.yousee.theadmin.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mittu on 13-08-2016.
 */
public class AttendanceHistory implements JSONParsable {

    public int count;
    public ArrayList<AttendanceHistoryRecord> historyRecords = new ArrayList<>();

    public static final String TAG_HISTORY = "history";
    public static final String TAG_COUNT = "count";

    public AttendanceHistory(String string)
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
    public AttendanceHistory(JSONObject jsonObject)
    {
        parseJSON(jsonObject);
    }

    @Override
    public void parseJSON(JSONObject JSONObject) {
        JSONArray array;
        try
        {
            this.count = JSONObject.getInt(TAG_COUNT);
            array = JSONObject.getJSONArray(TAG_HISTORY);
            for(int i=0; i< array.length(); i++)
            {
                JSONObject item = (JSONObject) array.get(i);
                this.historyRecords.add(new AttendanceHistoryRecord(item));

            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
