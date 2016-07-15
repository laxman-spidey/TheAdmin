package in.yousee.theadmin;

import android.content.Context;

public interface OnResponseReceivedListener
{
	public void onResponseReceived(Object response, int requestCode);
	public Context getContext();
}
