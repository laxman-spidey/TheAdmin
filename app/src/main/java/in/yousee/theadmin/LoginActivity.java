package in.yousee.theadmin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ResultCodes;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.UserData;
import in.yousee.theadmin.util.LogUtil;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends YouseeCustomActivity implements OnResponseReceivedListener {

    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // public void onLoginFailed() {
    // super.onLoginFailed();
    //}



    public void onLoginSuccess() {
        LogUtil.print("in Show main activity");
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        setProgressVisible(this,false);
        finish();
    }

    @Override
    public void onResponseReceived(Object response, int requestCode, int resultCode) {
        //showProgress(false);
        LogUtil.print("calling progress bar -  false");

        LogUtil.print("onressponse recieved " + requestCode + "  " + response.toString());
        if (requestCode == RequestCodes.NETWORK_REQUEST_VERIFY) {
            LogUtil.print("result code : "+resultCode +" = "+ ResultCodes.CHECK_AUTHORIZATION_SUCCESS);
            if (resultCode == ResultCodes.CHECK_AUTHORIZATION_SUCCESS) {
                LogUtil.print("prompt otp");
                promptOtp();
                //onLoginSuccess();
            } else {
                mPhoneView.setError("Mobile number is not registered");
            }
            setProgressVisible(this,false);
        }
        if (requestCode == RequestCodes.NETWORK_REQUEST_OTP_SUBMIT) {
            LogUtil.print("otp submission");
            if(resultCode == ResultCodes.USER_DATA_SUCCESS)
            {
                UserData userData = (UserData) response;
                LogUtil.print("success");
                onLoginSuccess();
                mOtpView.setEnabled(false);
                mEmailSignInButton.setEnabled(false);
                Toast.makeText(this,"Logging in..",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == ResultCodes.INVALID_OTP)
            {
                String msg = (String) response;
                LogUtil.print("biscuit");
                mOtpView.setText(msg);
            }
            else if(resultCode == ResultCodes.VALIDATE_OTP_STATUS_UPDATE_FAIL)
            {
                String msg = (String) response;
                LogUtil.print("biscuit 1");
                Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void showMainActivity() {

        Log.i("tag", "in Show menu activity");
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void promptOtp() {
        mOtpLayout.setVisibility(View.VISIBLE);
        mOtpView.setText("123456");
        mPhoneView.setEnabled(false);
        verifyButton.setEnabled(false);

    }

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mPhoneView;
    private EditText mOtpView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout mOtpLayout;
    private Button verifyButton;
    private Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        mPhoneView.setText("9505878984");
        populateAutoComplete();

        mOtpView = (EditText) findViewById(R.id.otp);
        mOtpLayout = (LinearLayout) findViewById(R.id.otpLayout);
        mOtpView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    submitOtp();
                    return true;
                }
                return false;
            }
        });

        verifyButton = (Button) findViewById(R.id.button_verify);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptVerify();
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOtp();
            }
        });

        // mLoginFormView = findViewById(R.id.login_form);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        //getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptVerify() {
        // Reset errors.
        //onLoginSuccess();
        //return;
        mPhoneView.setError(null);
        String phoneNumber = mPhoneView.getText().toString();
        if (phoneNumber.length() != 10) {
            mPhoneView.setError("Invalid Phone number");
            return;
        } else {
            LogUtil.print("sending request");
            SessionHandler sessionHandler = new SessionHandler(this);
            requestSenderMiddleware = sessionHandler;
            try {
                sessionHandler.verifyExec(phoneNumber, this);
                sendRequest();
            } catch (CustomException e) {
                LogUtil.print(e.getErrorMsg());
            }
        }
        //onLoginSuccess();

    }

    private void submitOtp() {
        LogUtil.print("in submit OTP");
        // Reset errors.
        mOtpView.setError(null);
        mPhoneView.setError(null);
        boolean error = false;
        String phoneNumber = mPhoneView.getText().toString();
        String otp = mOtpView.getText().toString();
        if (phoneNumber.length() != 10) {
            mPhoneView.setError("Invalid Phone number");
            error = true;
        }
        if (otp.length() != 6) {
            mOtpView.setError("invlid OTP");
            error = true;
        }
        if (error == false) {
            SessionHandler sessionHandler = new SessionHandler(this);
            requestSenderMiddleware = sessionHandler;
            try {
                sessionHandler.submitOTP(phoneNumber, otp, this);
                sendRequest();
            } catch (CustomException e) {
                LogUtil.print(e.getErrorMsg());
            }
        }


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        // if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
        //  mPasswordView.setError(getString(R.string.error_invalid_password));
        //focusView = mPasswordView;
        //  cancel = true;
        //  }

        // Check for a valid email address.
        //(see once) if (TextUtils.isEmpty(email)) {
        // mEmailView.setError(getString(R.string.error_field_required));
        //  focusView = mEmailView;
        //cancel = true;
        //  } else if (!isEmailValid(email)) {
        // mEmailView.setError(getString(R.string.error_invalid_email));
        //  focusView = mEmailView;
        //cancel = true;
        //  }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showProgress(final boolean show) {
        LogUtil.print("showing progress bar");
        setProgressBarIndeterminateVisibility(show);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            /*
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            */
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }


    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }


    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }


    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.yousee.theadmin/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.yousee.theadmin/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

           /* if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }*/
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
