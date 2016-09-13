package in.yousee.theadmin;

import java.util.Calendar;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.AttendanceHistory;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.Response;
import in.yousee.theadmin.util.LogUtil;
import in.yousee.theadmin.util.Utils;

/**
 * Created by mittu on 13-08-2016.
 */
public class AttendanceMiddleware extends Middleware {

    public AttendanceMiddleware(OnResponseReceivedListener listener) {
        super(listener);
    }

    @Override
    public void assembleRequest() {

    }

    public void getAttendanceHistoryData(Calendar from, Calendar to) throws CustomException
    {
        request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.GET_ATTENDANCE_HISTORY);
        setRequestCode(RequestCodes.NETWORK_REQUEST_ATTENDANCE_HISTORY);
        request.put("staffId", "6");
        request.put("limit", "10");
        request.put("from", Utils.getSqlDateString(from));
        request.put("to",Utils.getSqlDateString(to));
        sendRequest();
    }


    @Override
    public void serveResponse(Response response) {
        LogUtil.print("serving response - "+response.requestCode);
        if(response.requestCode == RequestCodes.NETWORK_REQUEST_ATTENDANCE_HISTORY)
        {
            AttendanceHistory attendanceHistory = new AttendanceHistory(response.responseString);
            listener.onResponseReceived(attendanceHistory, response.requestCode, response.resultCode);
        }

    }
}
