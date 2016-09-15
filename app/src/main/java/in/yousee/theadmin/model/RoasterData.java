package in.yousee.theadmin.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 24-08-2016.
 */
public class RoasterData extends ModelObject {

    public ArrayList<Record> roasterRecords = new ArrayList<>();


    public RoasterData(String string) {
        super(string);
    }

    public RoasterData(JSONObject jsonObject) {
        super(jsonObject);
    }
    public RoasterData()
    {

    }

    @Override
    public void parseJSON(JSONObject JSONObject) {

    }

    public static RoasterData getDummyData()
    {
        RoasterData roasterData = new RoasterData();
        for(int i = 0; i<3;i++) {
            Record record = new Record();
            record.date = "24 Aug, 2016";
            record.shiftDesc = "morning";
            record.timeIn = "10:00";
            record.timeOut = "17:00";
            roasterData.roasterRecords.add(record);
        }
        roasterData.roasterRecords.get(0).date = "Today";
        roasterData.roasterRecords.get(1).date = "Tomorrow";
        roasterData.roasterRecords.get(2).date = "Day after Tomorrow";
        return roasterData;
    }

    public static class Record extends ModelObject
    {

        public int roasterId;
        public String date;
        public String shiftId;
        public String shift;
        public String shiftDesc;
        public String shiftTimeIn;
        public String shiftTimeOut;
        public String timeIn;
        public String timeOut;

        public static final String TAG_ROASTER_ID = "roasterId";
        public static final String TAG_DATE = "date";
        public static final String TAG_SHIFT_ID = "shiftId";
        public static final String TAG_SHIFT = "shift";
        public static final String TAG_DESCRIPTION = "description";
        public static final String TAG_SHIFT_TIME_IN = "shiftTimeIn";
        public static final String TAG_SHIFT_TIME_OUT = "shiftTimeOut";
        public static final String TAG_TIME_IN = "timeIn";
        public static final String TAG_TIME_OUT = "timeOut";


        public Record(String string) {
            super(string);
        }
        public Record()
        {

        }

        @Override
        public void parseJSON(JSONObject JSONObject) {
            try
            {
                this.roasterId = JSONObject.getInt(TAG_ROASTER_ID);
                this.date = JSONObject.getString(TAG_DATE);
                this.shiftId = JSONObject.getString(TAG_SHIFT_ID);
                this.shift = JSONObject.getString(TAG_SHIFT);
                this.shiftDesc = JSONObject.getString(TAG_DESCRIPTION);
                this.shiftTimeIn = JSONObject.getString(TAG_SHIFT_TIME_IN);
                this.shiftTimeOut = JSONObject.getString(TAG_SHIFT_TIME_OUT);
                this.timeIn = JSONObject.getString(TAG_TIME_IN);
                this.timeOut = JSONObject.getString(TAG_TIME_OUT);

            } catch (JSONException e)
            {
                LogUtil.print("parse failed");
                e.printStackTrace();
            }
        }
    }
}
