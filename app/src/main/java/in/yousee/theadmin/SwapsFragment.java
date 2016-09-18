package in.yousee.theadmin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import in.yousee.theadmin.model.CustomException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SwapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwapsFragment extends Fragment implements View.OnClickListener, OnResponseReceivedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SwapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwapsFragment newInstance(String param1, String param2) {
        SwapsFragment fragment = new SwapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    MyCustomAdapter dataAdapter = null;
    Button apply;
    private EditText DateEtxt;
    ListView listView;
    Button myButton;

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
        View view = inflater.inflate(R.layout.fragment_swaps, container, false);

        //under construction
        TextView underConstructionText = (TextView) view.findViewById(R.id.underConstructionText);
        underConstructionText.setText("This guy again.. He never completes it..!!");

        DateEtxt = (EditText) view.findViewById(R.id.date);
        DateEtxt.setInputType(InputType.TYPE_NULL);
        DateEtxt.requestFocus();
        DateEtxt.setOnClickListener(this);

        apply = (Button) view.findViewById(R.id.button);
        apply.setOnClickListener(this);

        listView = (ListView) view.findViewById(R.id.listView1);

        myButton = (Button) view.findViewById(R.id.send);
        myButton.setOnClickListener(this);

        displayListView();

        return view;

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
        if(view.getId() == R.id.date)
        {
            showDialog(DateListerner);
        }
        else if(view.getId() == R.id.button){
            listView.setVisibility(View.VISIBLE);
            myButton.setVisibility(View.VISIBLE);
        }
        else if(view.getId() == R.id.send){
            displayAlertDialog();
        }
    }

    private void displayAlertDialog() {

        Context context = getActivity();
        String title = "Confirmation Box";
        String message = "Are you sure want to send request ?";
        String button1String = "Proceed";
        String button2String = "Cancel";

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);

        ad.setPositiveButton(
                button1String,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        SwapsMiddleware swapsMiddleware = new SwapsMiddleware(SwapsFragment.this);
                        try {
                            swapsMiddleware.sendSwapdate(DateEtxt.toString());
                        } catch (CustomException e) {
                            //TODO: show dialog
                        }
                    }
                }
        );

        ad.setNegativeButton(
                button2String,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int arg1) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "You pressed cancel button.",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );

        //
        ad.show();
    }

    void showDialog(DatePickerDialog.OnDateSetListener listener) {
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    DatePickerDialog.OnDateSetListener DateListerner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String monthString;
            if(month <10)
            {
                monthString = "0"+month;
            }
            else
            {
                monthString = ""+month;
            }
            DateEtxt.setText(year + "-" + monthString +"-"+ day);

        }
    };

    @Override
    public void onResponseReceived(Object response, int requestCode, int resultCode) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
     }

    private void displayListView() {


        ArrayList<Shiftlist> shiftList = new ArrayList<Shiftlist>();
        Shiftlist shift = new Shiftlist("spy", "Laxman", false);
        shiftList.add(shift);
        shift = new Shiftlist("Romeo", "Raj", false);
        shiftList.add(shift);
        shift = new Shiftlist("kesana", "Sowjanya", false);
        shiftList.add(shift);
        shift = new Shiftlist("vvns", "Raja rao", false);
        shiftList.add(shift);
        shift = new Shiftlist("Siriki", "Prasad", false);
        shiftList.add(shift);
        shift = new Shiftlist("Chinta", "Kishore", false);
        shiftList.add(shift);
        shift = new Shiftlist("Tummala", "Madhu", false);
        shiftList.add(shift);
        shift = new Shiftlist("Allu", "Arjun", false);
        shiftList.add(shift);
        shift = new Shiftlist("Allu", "Naidu", false);
        shiftList.add(shift);
        shift = new Shiftlist("Ch", "Sravani", false);
        shiftList.add(shift);
        shift = new Shiftlist("Kavs", "Kavya", false);
        shiftList.add(shift);
        shift = new Shiftlist("Gollapudi", "Gowthami", false);
        shiftList.add(shift);

        // create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(R.layout.check_info,shiftList);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Shiftlist shift = (Shiftlist) parent.getItemAtPosition(position);
               Toast.makeText(getActivity().getApplicationContext(),
                        "Clicked on Row: " + shift.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }



    public class MyCustomAdapter extends ArrayAdapter<Shiftlist> {

        private ArrayList<Shiftlist> shiftList;

        public MyCustomAdapter(int textViewResourceId,
                               ArrayList<Shiftlist> shiftList) {
            super(getActivity(), textViewResourceId, shiftList);
            this.shiftList = new ArrayList<Shiftlist>();
            this.shiftList.addAll(shiftList);
        }

        public class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.check_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Shiftlist shift = (Shiftlist) cb.getTag();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        shift.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Shiftlist shift = shiftList.get(position);
            holder.code.setText(" (" + shift.getCode() + ")");
            holder.name.setText(shift.getName());
            holder.name.setChecked(shift.isSelected());
            holder.name.setTag(shift);

            return convertView;

        }

    }
}

