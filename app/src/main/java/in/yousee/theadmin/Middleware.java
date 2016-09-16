package in.yousee.theadmin;

import android.content.ContentValues;
import android.content.Context;



import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.Request;
import in.yousee.theadmin.model.Response;
import in.yousee.theadmin.util.LogUtil;

public abstract class Middleware
{
	public static final String TAG_NETWORK_REQUEST_CODE = "requestCode";
	public static final String TAG_NETWORK_RESULT_CODE = "resultCode";
	public static final String TAG_USER_ID = "userId";
	public static final String TAG_PHONE_NUMBER = "phone";
	public static final String TAG_SESSION_ID = "session_id";
	protected ContentValues nameValuePairs = new ContentValues();

	protected void addKeyValue(String key, String value)
	{
		nameValuePairs.put(key,value);
	}

	OnResponseReceivedListener listener;

	public Middleware(OnResponseReceivedListener listener)
	{
		this.listener = listener;
	}

	//protected List<NameValuePair> nameValuePairs;

	protected Request request = new Request();

	protected void setRequestCode(int requestCode)
	{
		request.setRequestCode(requestCode);
	}

	protected void addPhoneNumberToPost()
	{
		if(SessionHandler.isSessionIdExists(getContext()))
		{
			addKeyValue(TAG_PHONE_NUMBER, "" + SessionHandler.getPhoneNumber(getContext()));
		}
	}
	
	protected void addSessionIdToPost()
	{
		if (SessionHandler.isSessionIdExists(getContext()))
		{
			addKeyValue(TAG_SESSION_ID, "" + SessionHandler.getSessionId(getContext()));
		}
	}

	public abstract void assembleRequest();

	public void sendRequest() throws CustomException
	{
		//request.setParameters(nameValuePairs);

		NetworkConnectionHandler connectionHandler = new NetworkConnectionHandler(getContext(), this);
		if (NetworkConnectionHandler.isNetworkConnected(getContext()))
		{
			LogUtil.print("sending request");
			connectionHandler.execute(request);

		}

	}


	public abstract void serveResponse(Response response);

	public Context getContext()
	{
		return listener.getContext();
	}
}
