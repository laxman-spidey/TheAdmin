package in.yousee.theadmin;

import android.support.v4.app.Fragment;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 12-08-2016.
 */
public class AuthenticationThread implements Runnable {
    Thread t;

    private static AuthenticationThread instance = null;
    private static final short MAX_TIME_TO_WAIT = 3;
    public  short timeWaited;
    public boolean threadStoppedManually = false;

    LocationFragment  fragment;

    boolean insideWorkLocation = false;
    private AuthenticationThread(LocationFragment  fragment)
    {
        t = new Thread(this);
        timeWaited = MAX_TIME_TO_WAIT;
        this.fragment = fragment;
        //isAuthenticationThreadStarted = true;
        t.start();
    }

    public static AuthenticationThread getInstance(LocationFragment fragment)
    {
        if(instance == null)
        {
            LogUtil.print("creating new auth thread");
            instance = new AuthenticationThread(fragment);
        }
        LogUtil.print("returning auth thread");
        return instance;
    }
    /*
    @Override
    public void run() {

        while(insideWorkLocation && timeWaited >= 0 && threadStoppedManually == false)
        {
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragment.setMsgText("authenticating in "+timeWaited+"s");
                }
            });

            try {
                LogUtil.print("count down : "+timeWaited+"s");
                Thread.sleep(1000);
                timeWaited--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!insideWorkLocation)
        {
            LogUtil.print("resetting..");
            resetTimeWaited();
        }

        if(timeWaited < 0)
        {
            LogUtil.print("timeWaited > 3 - stopping service");
            //after TIME_TO_WAIT seconds: authenticated
            fragment.stopListeningToLocationUpdates();
            //any changes to be updated on UI must run on UI thread.
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragment.authenticate();
                }
            });
        }
    }
    */

    @Override
    public void run() {
        LogUtil.print("thread running");
        while(timeWaited >= 0 && threadStoppedManually == false)
        {
            try {
                //LogUtil.print("sleeping");
                Thread.sleep(1000);
                //LogUtil.print("waking");
                if(insideWorkLocation)
                {
                    setMsg("authenticating in "+timeWaited+"s");
                    timeWaited--;
                }
                else
                {
                    resetTimeWaited();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(timeWaited < 0)
        {
            LogUtil.print("timeWaited > 3 - stopping service");
            //after TIME_TO_WAIT seconds: authenticated
            fragment.stopListeningToLocationUpdates();
            //any changes to be updated on UI must run on UI thread.
            if(fragment.isVisible()) {
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment.authenticate();
                    }
                });
            }
        }
        instance = null;
        LogUtil.print("Thread finished :( ");


    }
    public void setMsg(final String string)
    {
        if(fragment.isVisible())
        {
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    fragment.setMsgText(string);
                }
            });
        }
    }

    public void stopThread()
    {
        threadStoppedManually = true;
    }

    public void resetTimeWaited()
    {
        LogUtil.print("resetting");
        timeWaited = MAX_TIME_TO_WAIT;
    }
}
