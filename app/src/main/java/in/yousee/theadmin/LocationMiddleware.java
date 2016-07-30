package in.yousee.theadmin;

import java.util.Date;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.CustomException;

/**
 * Created by Laxman on 25-07-2016.
 */
public class LocationMiddleware extends Middleware{


    public LocationMiddleware(OnResponseReceivedListener listener) {
        super(listener);
    }

    @Override
    public void assembleRequest() {

    }

    public void checkin(Date date, String phone, String time) throws  CustomException
    {
        request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.CHECKIN);
        setRequestCode(RequestCodes.NETWORK_REQUEST_CHECK_IN);
        addKeyValue("date", date.toString());
        addKeyValue("phone", phone);
        addKeyValue("checkin",time);
        request.setParameters(nameValuePairs);
        sendRequest();
    }

    @Override
    public void serveResponse(String result, int requestCode) {

    }
}
