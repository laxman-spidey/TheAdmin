package in.yousee.theadmin;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.CustomException;

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
        setRequestCode(RequestCodes.ACTIVITY_SWAP_DATE);
        addKeyValue("date", date);
        request.setParameters(nameValuePairs);
        sendRequest();
    }


    @Override
    public void serveResponse(String result, int requestCode) {

    }
}
