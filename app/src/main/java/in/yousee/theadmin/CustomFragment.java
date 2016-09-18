package in.yousee.theadmin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.widget.Toast;

import in.yousee.theadmin.constants.RequestCodes;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 17-09-2016.
 */
public class CustomFragment extends Fragment {

    public boolean refresh = false;

    protected Middleware requestSenderMiddleware;

    public void promptRetry(String msg)
    {
        mListener.promptRetry(msg);
    }

    public void startProgress()
    {
        mListener.setProgressVisible(true);
    }
    public void stopProgress()
    {
        mListener.setProgressVisible(false);
    }


    public void sendRequest()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        startProgress();
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
            //Toast.makeText(CustomFragment.this, "", Toast.LENGTH_SHORT).show();
        }

    }


    public void reloadFragment()
    {
        sendRequest();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void promptRetry(String msg);
        void setProgressVisible(boolean visible);
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


}
