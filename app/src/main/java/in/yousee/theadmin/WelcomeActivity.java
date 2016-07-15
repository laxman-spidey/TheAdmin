package in.yousee.jeevandaan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class WelcomeActivity extends Activity implements OnResponseRecievedListener
{

	Thread t;
	String sessionId = null;
	String regid;

	//GoogleCloudMessaging gcmHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity);
/*
		gcmHelper = GoogleCloudMessaging.getInstance(context);



		regid = gcmHelper.getRegistrationId(this);
		Log.i("tag", "registration ID" + regid + "gvnsdhfjs");
		if (gcmHelper.isGcmIdEmpty())
		{
			Log.i("tag", "first time run");
			processFirstTime();
		} else
		{
			Log.i("tag", "running normally");
			defaultProcess();
		}
*/
		defaultProcess();
	}

	private void processFirstTime()
	{
		String msg = "App is loading for the first time. Please wait.";
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		//gcmHelper.implementGcm(true);
	}

	public void showLoginActivity()
	{
		Log.i("tag", "in Show menu activity");
		Intent intent = new Intent();
		intent.putExtra("sessionId", sessionId);
		intent.setClass(this, in.yousee.jeevandaan.LoginActivity.class);
		startActivity(intent);
	}
	/*
	public boolean checkPlayServices()
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS)
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{

				//GooglePlayServicesUtil.getErrorDialog(resultCode, this, GCMHelper.PLAY_SERVICES_RESOLUTION_REQUEST).show();

			} else
			{
				Log.i("tag", "This device is not supported.");
				// finish();
			}
			return false;
		}
		return true;
	}
	*/
	private void defaultProcess()
	{
		Thread splashThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					int waited = 0;
					while (waited < 2000)
					{
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e)
				{
					// do nothing
				} finally
				{
					finish();
					if(SessionHandler.isSessionIdExists(WelcomeActivity.this))
					{
						showMainActivity();
					}
					else {
						showLoginActivity();
					}

				}
			}
		};
		splashThread.start();
	}

	public void showMainActivity()
	{

		Log.i("tag", "in Show menu activity");
		Intent intent = new Intent();
		intent.putExtra("sessionId", sessionId);
		intent.setClass(this, in.yousee.jeevandaan.MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void onResponseRecieved(Object response, int requestCode)
	{
		finish();
		if(requestCode == RequestCodes.NETWORK_REQUEST_SEND_GCM_ID)
		{
			boolean success = (Boolean) response;
			if(success)
			{
				showMainActivity();
			}
			else
			{
				showMainActivity();
			}
		}
		
	}

	@Override
	public Context getContext()
	{
		return getApplicationContext();
	}
}
