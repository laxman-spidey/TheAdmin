package in.yousee.theadmin.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mittu on 11-09-2016.
 */
public class UserData extends ModelObject {

    public static final String TAG_STAFF_ID  = "staff_id";
    public static final String TAG_STAFF_NAME  = "staff_name";

    public int staffId;
    public String name;
    @Override
    public void parseJSON(JSONObject JSONObject)
    {
        try
        {
            this.staffId = JSONObject.getInt(TAG_STAFF_ID);
            this.name = JSONObject.getString(TAG_STAFF_NAME);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
