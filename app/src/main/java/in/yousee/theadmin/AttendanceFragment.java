package in.yousee.theadmin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.yousee.theadmin.constants.ResultCodes;
import in.yousee.theadmin.model.AttendanceHistory;
import in.yousee.theadmin.model.CustomException;
import in.yousee.theadmin.util.LogUtil;
import in.yousee.theadmin.util.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends CustomFragment implements DialogInterface.OnDismissListener, OnResponseReceivedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int checkType;

    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    ListView listView;

    private Calendar fromDate;
    private Calendar toDate;

    private OnFragmentInteractionListener mListener;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
            case R.id.fromdate:
            {
                showDatePickerDialog(fromDateListener, fromDate, true);
                break;
            }
            case R.id.todate:
            {
                showDatePickerDialog(toDateListener, toDate, false);
                break;
            }
            case R.id.getHistoryButton:
            {
                getAttendanceHistory();
            }
        }
    }
    DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            fromDate.set(year,month,day);
            fromDateEtxt.setText(Utils.getDisplayDateString(fromDate));
        }
    };

    DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            toDate.set(year,month,day);
            toDateEtxt.setText(Utils.getDisplayDateString(toDate));
        }
    };



    void showDatePickerDialog(DatePickerDialog.OnDateSetListener listener, Calendar selection, boolean fromto) {
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

        Calendar calendar = Calendar.getInstance();


        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),listener, selection.get(Calendar.YEAR), selection.get(Calendar.MONTH), selection.get(Calendar.DAY_OF_MONTH));
        if(fromto == true)
        {
            datePickerDialog.getDatePicker().setMaxDate(toDate.getTimeInMillis());
        }
        else
        {
            datePickerDialog.getDatePicker().setMinDate(fromDate.getTimeInMillis());
        }
        datePickerDialog.show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button checkin;
    Button checkout;
    Button getDataButton;
    TextView attendanceListErrorView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        checkin = (Button) view.findViewById(R.id.check_in);
        checkout = (Button) view.findViewById(R.id.check_out);
        getDataButton = (Button) view.findViewById(R.id.getHistoryButton);

        checkin.setOnClickListener(this);
        checkout.setOnClickListener(this);
        getDataButton.setOnClickListener(this);

        fromDateEtxt = (EditText) view.findViewById(R.id.fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        fromDateEtxt.setOnClickListener(this);
        fromDate = Utils.getFirstOfMonth();
        fromDateEtxt.setText(Utils.getDisplayDateString(fromDate));

        toDateEtxt = (EditText) view.findViewById(R.id.todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt.setOnClickListener(this);
        toDate = Calendar.getInstance();
        toDateEtxt.setText(Utils.getDisplayDateString(toDate));
        listView = (ListView) view.findViewById(R.id.attendanceListView);

        View header = inflater.inflate(R.layout.attendance_listview_header, null);
        listView.addHeaderView(header);
        getAttendanceHistory();

        attendanceListErrorView = (TextView) view.findViewById(R.id.attendanceListError);
        //LogUtil.print(attendanceListErrorView.);
        getAttendanceHistory();



        return view;

    }



    @Override
    public void onDismiss(DialogInterface dialogInterface) {

        LogUtil.print("onDismiss -  in parent fragment");

        LocationMiddleware locationMiddleware = new LocationMiddleware(this);
        try {
            Date datetime = Calendar.getInstance().getTime();
            //datetime.
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(datetime);
            String timeString = new SimpleDateFormat("HH:mm:ss").format(datetime);
            locationMiddleware.checkin(dateString,"9505878984",timeString);
        } catch (CustomException e) {
            //TODO: show dialogbox
        }


    }


    private void getAttendanceHistory()
    {
        AttendanceMiddleware attendanceMiddleware = new AttendanceMiddleware(this);
        attendanceMiddleware.getAttendanceHistoryData(fromDate, toDate);
        requestSenderMiddleware = attendanceMiddleware;
        sendRequest();
    }
    @Override
    public void onResponseReceived(Object response, int requestCode, int resultCode) {
        LogUtil.print("onresponserecieved()");
        stopProgress();
        if(this.isVisible())
        {
            if(resultCode == ResultCodes.ATTENDANCE_HISTORY_EXIST)
            {
                AttendanceHistory attendanceHistory = (AttendanceHistory) response;
                AttendanceAdapter attendanceAdapter = new AttendanceAdapter(this.getActivity(), R.layout.attendance_row, attendanceHistory.historyRecords);
                listView.setAdapter(attendanceAdapter);
                Utils.setListViewHeightBasedOnChildren(listView);
                attendanceListErrorView.setVisibility(View.GONE);
            }
            else
            {
                attendanceListErrorView.setVisibility(View.VISIBLE);
            }

        }

    }

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
}
