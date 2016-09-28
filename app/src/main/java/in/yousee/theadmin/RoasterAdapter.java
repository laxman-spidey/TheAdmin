package in.yousee.theadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.yousee.theadmin.model.AttendanceHistoryRecord;
import in.yousee.theadmin.model.RoasterData;
import in.yousee.theadmin.util.LogUtil;
import in.yousee.theadmin.util.Utils;


/**
 * Created by mittu on 24-08-2016.
 */
public class RoasterAdapter extends ArrayAdapter{
    /**
     * Created by mittu on 14-08-2016.
     */
    private Context context;
    private ArrayList<RoasterData.Record> roasterRecords;

    public RoasterAdapter(Context context, int resource,
                             ArrayList<RoasterData.Record> objects) {
        super(context, resource, objects);
        this.context = context;
        this.roasterRecords = new ArrayList<>();
        this.roasterRecords.addAll(objects);
    }

    public int getCount()
    {
        int count = 0;
        if(roasterRecords !=null)
        {
            count = roasterRecords.size();
        }
        return count;

    }
    public RoasterData.Record getItem(int position)
    {
        return roasterRecords.get(position);
    }
    public class ViewHolder {
        TextView date;
        TextView shiftDec;
        TextView timeIn;
        TextView timeOut;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.attendance_row, parent,false);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.attendance_date);
            holder.timeIn = (TextView) convertView.findViewById(R.id.attendance_timeIn);
            holder.timeOut = (TextView) convertView.findViewById(R.id.attendance_timeOut);
            convertView.setTag(holder);

//            holder.name.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    CheckBox cb = (CheckBox) v;
//                    Shiftlist shift = (Shiftlist) cb.getTag();
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "Clicked on Checkbox: " + cb.getText() +
//                                    " is " + cb.isChecked(),
//                            Toast.LENGTH_LONG).show();
//                    shift.setSelected(cb.isChecked());
//                }
//            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RoasterData.Record record = roasterRecords.get(position);
        holder.date.setText(record.date);
        holder.timeIn.setText(record.shiftTimeIn);
        holder.timeOut.setText(record.shiftTimeOut);

        return convertView;

    }

}
