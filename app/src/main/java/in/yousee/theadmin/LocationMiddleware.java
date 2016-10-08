package in.yousee.theadmin;

import java.util.Date;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ResultCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.Response;
import in.yousee.theadmin.model.StringResponse;

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

    public void checkin(String date, String phone, String time) throws  CustomException
    {
        request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.CHECK_IN);
        request.setRequestCode(RequestCodes.NETWORK_REQUEST_CHECK_IN);
        addStaffIdToPost();
        request.put("date", date);
        request.put("phone", phone);
        request.put("timeIn",time);
        //sendRequest();
    }

    public void checkout(String date, String phone, String time) throws  CustomException
    {
        request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.CHECK_OUT);
        request.setRequestCode(RequestCodes.NETWORK_REQUEST_CHECK_OUT);
        addStaffIdToPost();
        request.put("date", date);
        request.put("phone", phone);
        request.put("timeOut",time);
        //sendRequest();
    }

    @Override
    public void serveResponse(Response response) {
        StringResponse stringResponse = new StringResponse(response.responseString);
        listener.onResponseReceived(stringResponse,response.requestCode,response.resultCode);
    }
}
