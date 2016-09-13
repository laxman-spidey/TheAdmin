package in.yousee.theadmin;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.Response;

/**
 * Created by Romeo Raj on 30-Jul-16.
 */
public class SwapsMiddleware extends Middleware {
    public SwapsMiddleware(OnResponseReceivedListener listener) {
        super(listener);
    }

    @Override
    public void assembleRequest() {


    }
    public void sendSwapdate(String date) throws CustomException {
        request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.SWAP_DATE);
        request.setRequestCode(RequestCodes.ACTIVITY_SWAP_DATE);
        request.put("date", date);
        sendRequest();
    }

    //public void sendselectedlist(String  ) throws CustomException

    @Override
    public void serveResponse(Response response) {

    }
}
