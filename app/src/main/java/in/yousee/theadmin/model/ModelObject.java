package in.yousee.theadmin.model;

import org.json.JSONObject;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 24-08-2016.
 */
public abstract class ModelObject implements JSONParsable {
    public ModelObject()
    {

    }
    public ModelObject(String string)
    {
        try
        {
            LogUtil.print("before parsing");
            JSONObject object = new JSONObject(string);
            parseJSON(object);
        }
        catch (Exception e)
        {
            LogUtil.print("parsing failed");
            e.printStackTrace();
        }
    }
    public ModelObject(JSONObject jsonObject)
    {
        parseJSON(jsonObject);
    }

}
