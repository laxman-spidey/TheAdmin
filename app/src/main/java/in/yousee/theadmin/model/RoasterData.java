package in.yousee.theadmin.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.yousee.theadmin.util.LogUtil;
import in.yousee.theadmin.util.Utils;

/**
 * Created by mittu on 24-08-2016.
 */
public class RoasterData extends ModelObject {

    public static final String TAG_COUNT = "count";
    public static final String TAG_ROASTER_DETAILS = "roasterDetails";

    public int count;
    public ArrayList<Record> roasterRecords;

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
        JSONArray array;
        try
        {
            this.count = JSONObject.getInt(TAG_COUNT);
            if(count > 0)
            {
                this.roasterRecords = new ArrayList<>();
                array = JSONObject.getJSONArray(TAG_ROASTER_DETAILS);
                for(int i=0; i< array.length(); i++)
                {
                    JSONObject item = (JSONObject) array.get(i);
                    this.roasterRecords.add(new Record(item));
                    LogUtil.print("adding "+i);
                }
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
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
    public Record getTodayData()
    {

        String today = Utils.getSqlDateString(Calendar.getInstance());
        LogUtil.print("today  -- " + today);
        for(Record record : roasterRecords)
        {
            LogUtil.print(record.date);
            if(today.equals(record.date))
            {
                return record;
            }
        }
        return null;

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
        public String leaveStatus;

        public Date dateTimeIn;
        public Date dateTimeOut;

        public static final String TAG_ROASTER_ID = "roasterId";
        public static final String TAG_DATE = "date";
        public static final String TAG_SHIFT_ID = "shiftId";
        public static final String TAG_SHIFT = "shift";
        public static final String TAG_DESCRIPTION = "description";
        public static final String TAG_SHIFT_TIME_IN = "shiftTimeIn";
        public static final String TAG_SHIFT_TIME_OUT = "shiftTimeOut";
        public static final String TAG_TIME_IN = "timeIn";
        public static final String TAG_TIME_OUT = "timeOut";
        public static final String TAG_LEAVE_STATUS = "leaveStatus";


        public Record(String string) {
            super(string);
        }
        public Record(JSONObject obj) {
            parseJSON(obj);
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
                this.leaveStatus = JSONObject.getString(TAG_LEAVE_STATUS);

                dateTimeIn = Utils.getEncodedDateFromSqlString(date, shiftTimeIn);
                dateTimeOut = Utils.getEncodedDateFromSqlString(date, shiftTimeOut);
            } catch (JSONException e)
            {
                LogUtil.print("parse failed");
                e.printStackTrace();
            }
        }
    }
}
