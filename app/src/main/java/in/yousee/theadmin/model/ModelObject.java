package in.yousee.theadmin.model;

import org.json.JSONObject;

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
            JSONObject object = new JSONObject(string);
            parseJSON(object);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public ModelObject(JSONObject jsonObject)
    {
        parseJSON(jsonObject);
    }

}
