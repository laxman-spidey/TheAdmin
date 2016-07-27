package in.yousee.theadmin;

/**
 * Created by Laxman on 25-07-2016.
 */
public class LocationMiddleware extends Middleware {


    public LocationMiddleware(OnResponseReceivedListener listener) {
        super(listener);
    }

    @Override
    public void assembleRequest() {

    }


    @Override
    public void serveResponse(String result, int requestCode) {

    }
}
