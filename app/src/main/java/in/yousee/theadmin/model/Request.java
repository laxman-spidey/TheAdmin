package in.yousee.theadmin.model;

import android.content.ContentValues;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by mittu on 14-07-2016.
 */
public class Request {
    private String url;
    private int requestCode;
    private ContentValues parameters;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    /*
    public List<NameValuePair> getParameters() {
        return parameters;
    }
    */

    public void setParameters(ContentValues parameters) {

        this.parameters = parameters;
    }
    public String getParameters() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> entry : parameters.valueSet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }
}
