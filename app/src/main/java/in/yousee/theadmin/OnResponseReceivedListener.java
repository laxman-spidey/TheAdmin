package in.yousee.theadmin;

import android.content.Context;

public interface OnResponseReceivedListener
{
	public void onResponseReceived(Object response, int requestCode, int resultCode);
	public Context getContext();
}
