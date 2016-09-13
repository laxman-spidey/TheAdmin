package in.yousee.theadmin.model;

import org.json.JSONException;
import org.json.JSONObject;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 11-09-2016.
 */
public class UserData extends ModelObject {

    public static final String TAG_STAFF_ID  = "staff_id";
    public static final String TAG_STAFF_NAME  = "staff_name";
    public static final String TAG_PHONE  = "phone_number";

    public int staffId;
    public String name;
    public String phone;
    public String string;

    public UserData(String userData)
    {
        super(userData);
        LogUtil.print(userData);
        string = userData;
    }


    @Override
    public void parseJSON(JSONObject JSONObject)
    {
        try
        {
            this.staffId = JSONObject.getInt(TAG_STAFF_ID);
            this.name = JSONObject.getString(TAG_STAFF_NAME);
            this.phone = JSONObject.getString(TAG_PHONE);

        } catch (JSONException e)
        {
            LogUtil.print("parse failed");
            e.printStackTrace();
        }
    }
}
