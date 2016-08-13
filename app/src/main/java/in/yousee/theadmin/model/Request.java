package in.yousee.theadmin.model;

import android.content.ContentValues;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by Laxman on 14-07-2016.
 */
public class Request {
    private String url;
    private int requestCode;
    //private ContentValues parameters;
    private JSONObject parameters;

    public Request()
    {
        parameters = new JSONObject();
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestCode() {

        LogUtil.print("getting requestCode = "+requestCode);
        return requestCode;
    }
    public void put(String key, String value)
    {
        try {
            parameters.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAllParameters()
    {
        return parameters.toString();
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
