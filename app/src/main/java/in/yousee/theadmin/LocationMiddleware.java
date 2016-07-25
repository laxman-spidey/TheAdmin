package in.yousee.theadmin;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by mittu on 25-07-2016.
 */
public class LocationMiddleware extends Middleware {

    OnResponseReceivedListener listener;

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
