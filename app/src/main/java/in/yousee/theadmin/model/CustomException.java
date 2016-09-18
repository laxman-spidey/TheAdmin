package in.yousee.theadmin.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

public class CustomException extends Exception implements JSONParsable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234567890;

	public static final int ERROR_PHONE_INVALID = 11;
	public static final int ERROR_OTP_INVALID = 12;

	public static final int ERROR_NETWORK_NOT_FOUND = 13;
	public static final int ERROR_NO_INTERNET_CONNECTIVITY = 14;
	public static final int ERROR_SERVER = 18;

	public static final int ERROR_INVALID_URL = 15;
	public static final int ERROR_LOGIN_ERROR = 16;
	public static final int ERROR_CUSTOM = 17;
	public static final int IO_ERROR = -1;
	
	
	public static final int REGISTRATION_EMAIL_ALREADY_TAKEN = 120;
	public static final int REGISTRATION_USERNAME_EXISTS= 121;
	public static final int REGISTRATION_PHONE_NUMBER_ALREADY_EXISTS= 121;
	public static final int ERROR_CODE = 0;
	public static final int SUCCESS_CODE = 1;
	
	

	private String errorMsg;
	public int errorCode;

	public CustomException(int errorCode)
	{
		this.errorCode = errorCode;
		setErrorMsg(errorCode);

	}
	public CustomException(String errorMsg)
	{
		setErrorCode(ERROR_CUSTOM);
		setErrorMsg(errorMsg);
	}
	public CustomException(String errorMsg, int errorCode)
	{
		setErrorCode(errorCode);
		setErrorMsg(errorMsg);
	}

	private void setErrorMsg(int errorCode)
	{
		switch (errorCode)
			{
			case ERROR_PHONE_INVALID:
				setErrorMsg("Phone invalid");
				break;
			case ERROR_OTP_INVALID:
				setErrorMsg("OTP invalid");
				break;
			case ERROR_NETWORK_NOT_FOUND:
				setErrorMsg("No network found. Please enable wifi or mobile data and try again.");
				break;
			case ERROR_NO_INTERNET_CONNECTIVITY:
				setErrorMsg("Your device is not connected to internet.");
				break;
			case ERROR_LOGIN_ERROR:
				setErrorMsg("Details you have entered are incorrect.");
				break;
			default:
				break;
			}

	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

	private void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

	@Override
	public void parseJSON(JSONObject JSONObject)
	{

	}

	public static void showToastError(Context context, CustomException e)
	{
		Toast.makeText(context, e.getErrorMsg(), Toast.LENGTH_LONG).show();
	}
	public static void showToastError(Context context, String string)
	{
		Toast.makeText(context, string, Toast.LENGTH_LONG).show();
	}

}
