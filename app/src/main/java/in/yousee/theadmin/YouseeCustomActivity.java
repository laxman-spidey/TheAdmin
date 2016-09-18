package in.yousee.theadmin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.constants.ResultCodes;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 17-09-2016.
 */
public class YouseeCustomActivity extends AppCompatActivity implements CustomFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    ProgressBar progressBar;

    @Override
    protected void onStart()
    {
        super.onStart();
        //progressBar.setVisibility(ProgressBar.VISIBLE);
    }
//    @Override
//    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
//    {
//
//        if(parent != null)
//        {
//            LogUtil.print("--------------------------------------------------------------setting progress----------------------------------------------------------------------------------------------------------");
//            progressBar = (ProgressBar) findViewById(R.id.toolbarProgress);
//            progressBar.setIndeterminate(true);
//            progressBar.setVisibility(ProgressBar.VISIBLE);
//        }
//        else
//        {
//            LogUtil.print("--------------------------------------------------------------buiscuit aindhi rooo----------------------------------------------------------------------------------------------------------");
//        }
//        return parent;
//    }

    protected void setWindowProgressBar()
    {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminate(true);
        setSupportProgressBarIndeterminateVisibility(true);
    }
    public boolean refresh = false;

    protected Middleware requestSenderMiddleware;

    public void sendRequest()
    {
        setSupportProgressBarIndeterminateVisibility(true);

        if (NetworkConnectionHandler.isExecuting == false)
        {
            try
            {
                requestSenderMiddleware.sendRequest();
            }
            catch (CustomException e)
            {

                LogUtil.print("network not connected exception found");
                promptRetry(e.getErrorMsg());
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(YouseeCustomActivity.this, "", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Log.i(LOG_TAG, "retrying");
        // requestCode = RESULT_OK;
        LogUtil.print("On activity results = "+requestCode + " -- "+resultCode);
        if (requestCode == RequestCodes.ACTIVITY_REQUEST_RETRY)
        {
            LogUtil.print("ACTIVITY_REQUEST_RETRY");
            if (resultCode == RESULT_OK)
            {
                LogUtil.print("RESULT_OK");
                reloadActivity();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void reloadActivity()
    {
        LogUtil.print("reload activity");
        ArrayList<Fragment> visibleFragments = getVisibleFragments();
        for(Fragment activeFragment : visibleFragments)
        {
            CustomFragment fragment = (CustomFragment) activeFragment;
            fragment.reloadFragment();
        }

    }

    public ArrayList<Fragment> getVisibleFragments()
    {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        ArrayList<Fragment> visibleFragments = new ArrayList<>();
        if(fragments != null)
        {
            for(Fragment fragment : fragments)
            {
                if(fragment!=null && fragment.isVisible())
                {
                    visibleFragments.add(fragment);
                }
            }
        }
        return visibleFragments;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void promptRetry(String msg)
    {
        Intent intent = new Intent();
        intent.setClass(this, RetryActivity.class);
        intent.putExtra("errorMsg", msg);
        startActivityForResult(intent, RequestCodes.ACTIVITY_REQUEST_RETRY);
    }

    @Override
    public void setProgressVisible(boolean visible)
    {
        progressBar = (ProgressBar) findViewById(R.id.toolbarProgress);
        progressBar.setIndeterminate(true);
        if(visible)
        {
            LogUtil.print("progressbar - visible");
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else
        {
            LogUtil.print("progressbar - gone");
            progressBar.setVisibility(ProgressBar.GONE);
        }

    }

}
