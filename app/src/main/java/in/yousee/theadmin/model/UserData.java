package in.yousee.theadmin.model;

import org.json.JSONException;
import org.json.JSONObject;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 11-09-2016.
 */
public class UserData extends ModelObject {

    public static final String TAG_STAFF_ID  = "staffId";
    public static final String TAG_FIRST_NAME  = "firstName";
    public static final String TAG_LAST_NAME  = "lastName";
    public static final String TAG_STAFF_ROLE  = "staffRole";
    public static final String TAG_STAFF_IMAGE  = "staffImage";
    public static final String TAG_STAFF_STATUS  = "staffStatus";
    public static final String TAG_PHONE  = "phoneNumber";

    public int staffId;
    public String name;
    public String phone;
    public String firstName;
    public String lastName;
    public String staffRole;
    public String staffImage;
    public String staffStatus;

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
            this.firstName  = JSONObject.getString(TAG_FIRST_NAME);
            this.lastName = JSONObject.getString(TAG_LAST_NAME);
            this.staffRole= JSONObject.getString(TAG_STAFF_ROLE);
            this.staffImage = JSONObject.getString(TAG_STAFF_IMAGE);
            this.staffStatus = JSONObject.getString(TAG_STAFF_STATUS);
            this.phone = JSONObject.getString(TAG_PHONE);

        } catch (JSONException e)
        {
            LogUtil.print("parse failed");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return string;
    }
}
