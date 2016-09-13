package in.yousee.theadmin.model;

import org.json.JSONObject;

import java.util.ArrayList;

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
        public String shiftDesc;
        public String timeIn;
        public String timeOut;

        public Record(String string) {
            super(string);
        }
        public Record()
        {

        }

        @Override
        public void parseJSON(JSONObject JSONObject) {

        }
    }
}
