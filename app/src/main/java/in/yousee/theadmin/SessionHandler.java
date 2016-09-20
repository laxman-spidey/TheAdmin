package in.yousee.theadmin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ResultCodes;
import in.yousee.theadmin.constants.ServerFiles;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.Response;
import in.yousee.theadmin.model.UserData;
import in.yousee.theadmin.util.LogUtil;

public class SessionHandler extends Middleware
{
	private Context context;

	private String sessionID;
	private String userID;
	private String userType;
	private UsesLoginFeature loginFeatureClient;
	private OnResponseReceivedListener responseListener;
	private static final String SESSION_DEBUG_TAG = "session_tag";
	private static final String TAG_RESPONSE_MSG= "msg";
	public static final String TAG_USERDATA = "userdata";

	public static boolean isLoggedIn = false;
	private String username = "";
	private String password = "";
	private String phone = "";

	private static final String LOGIN_DATA = "login_data";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	//private static final String KEY_USER_ID = "userId";
	public static final String KEY_STAFF_ID = "staffId";
	private static final String KEY_USER_DATA = "userData";

	public static final String KEY_SESSION_ID = "sessionID";



	public SessionHandler(OnResponseReceivedListener responseListener)
	{
		super(responseListener);
		this.context = responseListener.getContext();
		this.responseListener = responseListener;
	}
	private static SharedPreferences getLoginSharedPrefs(Context context)
	{
		return context.getSharedPreferences(LOGIN_DATA, Activity.MODE_PRIVATE);
	}

	private boolean getLoginCredentials(String username, String password)
	{
		Log.i(SESSION_DEBUG_TAG, "in getLogin credentials----------------------------------");
		if (isLoginCredentialsExists(context))
		{
			SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
			username = sharedPrefs.getString(KEY_USERNAME, null);
			this.username = username;
			Log.i(SESSION_DEBUG_TAG, "username " + this.username);
			password = sharedPrefs.getString(KEY_PASSWORD, null);
			this.password = password;
			Log.i(SESSION_DEBUG_TAG, "password " + this.password);
			return true;
		}
		return false;

	}

	public static boolean isUserDataExists(Context context)
	{

		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		if (sharedPrefs.contains(KEY_USER_DATA) && sharedPrefs.getString(KEY_USER_DATA, "") != null)
		{
			LogUtil.print("userdata exists");
			return true;
		}
		LogUtil.print("userdata does not exists");
		return false;

	}

	public static String getUserData(Context context)
	{
		Log.i(SESSION_DEBUG_TAG, "getUserData()");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		if (isUserDataExists(context))
		{

			String userData = sharedPrefs.getString(KEY_USER_DATA, "");
			Log.i(SESSION_DEBUG_TAG, "Userdata = " + userData);
			return userData;
		}
		Log.i(SESSION_DEBUG_TAG, "phone false");
		return null;

	}
	private void storeUserData(String userdata)
	{
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(KEY_USER_DATA, userdata);
		editor.apply();
		LogUtil.print("setting userData:  " + getUserData(context));


	}
	private void setPhoneNumber(String phone)
	{
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(KEY_PHONE, phone);
		this.phone = phone;
		editor.apply();
		LogUtil.print("setting phone number" + getPhoneNumber(context));


	}
	public static String getPhoneNumber(Context context)
	{
		Log.i(SESSION_DEBUG_TAG, "getphonenumber()");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		if (isPhoneExists(context))
		{

			String phone = sharedPrefs.getString(KEY_PHONE, "");
			Log.i(SESSION_DEBUG_TAG, "Phone = " + phone);
			return phone;
		}
		Log.i(SESSION_DEBUG_TAG, "phone false");
		return null;

	}
	public static boolean isPhoneExists(Context context)
	{

		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		if (sharedPrefs.contains(KEY_PHONE) && sharedPrefs.getString(KEY_PHONE, "") != null)
		{
			return true;
		}
		return false;

	}

	private void setLoginCredentials(String username, String password)
	{
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(KEY_USERNAME, username);
		editor.putString(KEY_PASSWORD, password);
		this.username = username;
		this.password = password;
		editor.apply();

	}

	private void setStaffId(int staffId)
	{
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putInt(KEY_STAFF_ID, staffId);
		Log.i(SESSION_DEBUG_TAG, "userid set to : " + staffId);
		editor.apply();
	}

	public static boolean isStaffIdExists(Context context)
	{

		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		if (sharedPrefs.contains(KEY_STAFF_ID) && sharedPrefs.getInt(KEY_STAFF_ID, 0) != 0)
		{
			return true;
		}
		return false;

	}

	public static int getStaffId(Context context)
	{
		Log.i(SESSION_DEBUG_TAG, "getUserId()");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		if (isStaffIdExists(context))
		{

			int userId = sharedPrefs.getInt(KEY_STAFF_ID, -1);
			Log.i(SESSION_DEBUG_TAG, "userId = " + userId);
			return userId;
		}
		Log.i(SESSION_DEBUG_TAG, "userId false");
		return 0;

	}


	public static boolean isLoginCredentialsExists(Context context)
	{
		Log.i(SESSION_DEBUG_TAG, "is LoginCredentialExists()");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);

		if (sharedPrefs.contains(KEY_USERNAME) && sharedPrefs.contains(KEY_PASSWORD))
		{
			if (!(sharedPrefs.getString(KEY_USERNAME, "").equals("")) && !(sharedPrefs.getString(KEY_PASSWORD, "").equals("")))
			{
				Log.i(SESSION_DEBUG_TAG, "returning true");
				return true;
			}
			Log.i(SESSION_DEBUG_TAG, "returning false");
			return false;
		}
		Log.i(SESSION_DEBUG_TAG, "returning false");
		return false;

	}

	public static String getSessionId(Context context)
	{
		Log.i(SESSION_DEBUG_TAG, "getsessionId()");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		if (isSessionIdExists(context))
		{

			String sessionId = sharedPrefs.getString(KEY_SESSION_ID, "error");
			Log.i(SESSION_DEBUG_TAG, "sessiocheppan id exixsts = " + sessionId);
			return sessionId;
		}
		Log.i(SESSION_DEBUG_TAG, "session id false");
		return null;

	}

	private void setSessionId(String sessionId)
	{
		Log.i(SESSION_DEBUG_TAG, "setsessionId()");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);

		SharedPreferences.Editor editor = sharedPrefs.edit();

		editor.putString(KEY_SESSION_ID, sessionId);
		this.sessionID = sessionId;
		editor.commit();
		Log.i(SESSION_DEBUG_TAG, "sessionId set to = " + sharedPrefs.getString(KEY_SESSION_ID, ""));

	}

	public static boolean isSessionIdExists(Context context)
	{
		Log.i(SESSION_DEBUG_TAG, "isSessionIdExists()");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);

		if (sharedPrefs.contains(KEY_SESSION_ID) && sharedPrefs.getString(KEY_SESSION_ID, "").equals("") == false)
		{
			Log.i(SESSION_DEBUG_TAG, "returning " + true);
			return true;
		}
		Log.i(SESSION_DEBUG_TAG, "returning " + false);
		return false;

	};


	public void verifyExec(String phone, LoginActivity responseListener) throws CustomException
	{
		this.responseListener = responseListener;

		request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.VERIFY_EXEC);
		request.setRequestCode(RequestCodes.NETWORK_REQUEST_VERIFY);
		request.put("phoneNumber", phone);
		this.phone = phone;
		setPhoneNumber(phone);
		sendRequest();

	}

	public void submitOTP(String phone, String otp, LoginActivity loginFeatureClient) throws CustomException
	{
		request.setUrl(NetworkConnectionHandler.DOMAIN + ServerFiles.LOGIN_EXEC);
		request.put("phoneNumber", phone);
		request.put("otp", otp);
		request.setRequestCode(RequestCodes.NETWORK_REQUEST_OTP_SUBMIT);
		setPhoneNumber(phone);
		this.phone = phone;
		sendRequest();
	}

	public void logout(OnResponseReceivedListener listener) throws CustomException
	{

		Log.i(SESSION_DEBUG_TAG, "logging out");
		SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.remove(KEY_SESSION_ID);
		editor.remove(KEY_PHONE);
		editor.apply();

	}

	@Override
	public void serveResponse(Response response)
	{
		LogUtil.print("serving response ==---- " + response.requestCode);

		if (response.requestCode == RequestCodes.NETWORK_REQUEST_VERIFY)
		{

			LogUtil.print("request success -- " + response.responseString);
			try {
				JSONObject json = new JSONObject(response.responseString);

				String msg = json.getString("msg");
				LogUtil.print(msg);
				listener.onResponseReceived(msg,response.requestCode, response.resultCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(response.requestCode == RequestCodes.NETWORK_REQUEST_OTP_SUBMIT)
		{
			LogUtil.print("OTP submit");
			JSONObject json;
			String msg = null;
			String userDataString = null;
			UserData userData = null;
			try
			{
				json = new JSONObject(response.responseString);
				if(response.resultCode == ResultCodes.LOGIN_SUCCESS)
				{
					userDataString = json.getString(TAG_USERDATA);
					userData = new UserData(userDataString);
					LogUtil.print("user data -- ");
					this.setPhoneNumber(userData.phone);
					this.setStaffId(userData.staffId);
					this.storeUserData(userData.string);
					listener.onResponseReceived(userData,response.requestCode,response.resultCode);
				}
				else
				{
					msg = json.getString(TAG_RESPONSE_MSG);
					listener.onResponseReceived(msg,response.requestCode,response.resultCode);

				}
			} catch (Exception e) {
				LogUtil.print(e.getMessage());
			}

		}
		/*
		else if (requestCode == RequestCodes.NETWORK_REQUEST_LOGOUT)
		{
			Log.i(SESSION_DEBUG_TAG, "logging out");
			SharedPreferences sharedPrefs = getLoginSharedPrefs(context);
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.remove(KEY_SESSION_ID);
			editor.commit();
			//logoutListener.onResponseRecieved(null, requestCode);

		}
		*/
	}

	@Override
	public void assembleRequest()
	{

	}

	@Override
	public Context getContext()
	{
		return context;
	}
}
