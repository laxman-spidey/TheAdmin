package in.yousee.theadmin;

import java.util.Calendar;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.AttendanceHistory;
import in.yousee.theadmin.model.CustomException;
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
        setRequestCode(RequestCodes.NETWORK_REQUEST_DASHBOARD);
        request.put("staffId", "6");
        request.put("limit", "10");
        request.put("from", Utils.getSqlDateString(from));
        request.put("to",Utils.getSqlDateString(to));
        sendRequest();
    }


    @Override
    public void serveResponse(String result, int requestCode) {
        LogUtil.print("serving response - "+requestCode);
        if(requestCode == RequestCodes.NETWORK_REQUEST_DASHBOARD)
        {
            AttendanceHistory attendanceHistory = new AttendanceHistory(result);
            listener.onResponseReceived(attendanceHistory, requestCode);
        }

    }
}
