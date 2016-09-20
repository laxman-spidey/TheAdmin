package in.yousee.theadmin.model;

import org.json.JSONException;
import org.json.JSONObject;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 19-09-2016.
 */
public class StringResponse extends ModelObject {

    public StringResponse(String string) {
        super(string);
    }
    public String msg;
    public static final String TAG_MSG = "msg";
    @Override
    public void parseJSON(JSONObject JSONObject) {
        try
        {
            this.msg = JSONObject.getString(TAG_MSG);

        } catch (JSONException e)
        {
            LogUtil.print("parse failed");
            e.printStackTrace();
        }

    }
}
