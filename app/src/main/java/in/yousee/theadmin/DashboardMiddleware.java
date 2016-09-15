package in.yousee.theadmin;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.AttendanceHistory;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.Response;
import in.yousee.theadmin.model.RoasterData;
import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 13-08-2016.
 */
public class DashboardMiddleware extends Middleware {
    public DashboardMiddleware(OnResponseReceivedListener listener) {
        super(listener);
    }

    @Override
    public void assembleRequest() {

    }

    public void getDashboardData() throws CustomException
    {
        request.setUrl(NetworkConnectionHandler.DOMAIN_2 + ServerFiles.GET_DASHBOARD_DATA);
        setRequestCode(RequestCodes.NETWORK_REQUEST_DASHBOARD);
        request.put("staffId", "1");
        request.put("limit", "3");
        LogUtil.print("request code---"+request.getRequestCode());
        sendRequest();
    }

    public void getFutureRoaster(int staffId, int limit) throws CustomException
    {

        request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.GET_FUTURE_ROASTER);
        setRequestCode(RequestCodes.NETWORK_REQUEST_FUTURE_ROASTER);
        request.put("staffId", "1");
        //request.put("limit", "3");
        LogUtil.print("request code---"+request.getRequestCode());
        sendRequest();
    }

    @Override
    public void serveResponse(Response response) {
        LogUtil.print("serving response - "+response.requestCode);
        if(response.requestCode == RequestCodes.NETWORK_REQUEST_DASHBOARD)
        {
            RoasterData roasterData= new RoasterData(response.responseString);
            listener.onResponseReceived(roasterData, response.requestCode, response.resultCode);
        }

    }
}
