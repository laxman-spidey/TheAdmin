package in.yousee.theadmin;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.yousee.theadmin.model.AttendanceHistory;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.model.RoasterData;
import in.yousee.theadmin.util.LogUtil;
import in.yousee.theadmin.util.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends CustomFragment  implements View.OnClickListener, DialogInterface.OnDismissListener, OnResponseReceivedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    ListView listView;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button checkinButton = (Button) view.findViewById(R.id.check_in);
        Button checkoutButton = (Button) view.findViewById(R.id.check_out);

        listView = (ListView) view.findViewById(R.id.attendanceListView);
        checkinButton.setOnClickListener(this);
        checkoutButton.setOnClickListener(this);
        View header = inflater.inflate(R.layout.roaster_listview_header, null);
        listView.addHeaderView(header);
        getAttendanceHistory();
        //onResponseReceived(null, 0, 0);
        return view;
    }

    private void getAttendanceHistory()
    {
        DashboardMiddleware dashboardMiddleware = new DashboardMiddleware(this);
        dashboardMiddleware.getDashboardData();
        requestSenderMiddleware = dashboardMiddleware;
        sendRequest();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.check_in:
            {
                showLocationDialog(LocationFragment.CHECK_IN);
                break;
            }
            case R.id.check_out:
            {
                showLocationDialog(LocationFragment.CHECK_OUT);
                break;

            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

        LogUtil.print("onDismiss -  in parent fragment");

        LocationMiddleware locationMiddleware = new LocationMiddleware(this);
        try {
            Date datetime = Calendar.getInstance().getTime();
            //datetime.ee
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(datetime);
            String timeString = new SimpleDateFormat("HH:mm:ss").format(datetime);
            if(checkInOut == LocationFragment.CHECK_IN)
            {
                locationMiddleware.checkin(dateString,"9505878984",timeString);
            }
            else if(checkInOut == LocationFragment.CHECK_OUT)
            {
                locationMiddleware.checkout(dateString,"9505878984",timeString);
            }
            
        } catch (CustomException e) {
            //TODO: show dialogbox
        }


    }

    @Override
    public void onResponseReceived(Object response, int requestCode, int resultCode) {
        LogUtil.print("onresponserecieved()"+this.isVisible());

        //if(this.isVisible())
        {
//            AttendanceHistory attendanceHistory = (AttendanceHistory) response;
//            AttendanceAdapter attendanceAdapter = new AttendanceAdapter(this.getActivity(), R.layout.attendance_row, attendanceHistory.historyRecords);
//            listView.setAdapter(attendanceAdapter);
            LogUtil.print("roaster----------");
            RoasterData data = (RoasterData)response;
            RoasterAdapter roasterAdapter = new RoasterAdapter(this.getActivity(), R.layout.attendance_row, data.roasterRecords);
            listView.setAdapter(roasterAdapter);
            Utils.setListViewHeightBasedOnChildren(listView);

        }

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private short checkInOut;
    void showLocationDialog(short checkin) {
        //mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        checkInOut = checkin;
        LocationFragment newFragment = null;


        // Create and show the dialog.
        if(checkin == LocationFragment.CHECK_IN)
        {
            newFragment = LocationFragment.newInstance(LocationFragment.CHECK_IN);
        }
        else if(checkin == LocationFragment.CHECK_OUT)
        {
            newFragment = LocationFragment.newInstance(LocationFragment.CHECK_OUT);
        }
        newFragment.setTargetFragment(this,1);
        newFragment.show(ft, "dialog");
    }

}
