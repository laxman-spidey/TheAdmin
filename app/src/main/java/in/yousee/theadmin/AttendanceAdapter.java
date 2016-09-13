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

import in.yousee.theadmin.model.AttendanceHistoryRecord;
import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 14-08-2016.
 */
public class AttendanceAdapter extends ArrayAdapter<AttendanceHistoryRecord> {

    private Context context;
    private ArrayList<AttendanceHistoryRecord> historyRecords;

    public AttendanceAdapter(Context context, int resource,
                           ArrayList<AttendanceHistoryRecord> objects) {
        super(context, resource, objects);
        this.context = context;
        this.historyRecords = new ArrayList<>();
        this.historyRecords.addAll(objects);
    }

    public int getCount()
    {
        int count = 0;
        if(historyRecords !=null)
        {
            count = historyRecords.size();
        }

        LogUtil.print("count = "+count);
        return count;

    }
    public AttendanceHistoryRecord getItem(int position)
    {
        return historyRecords.get(position);
    }
    public class ViewHolder {
        TextView date;
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
        AttendanceHistoryRecord record = historyRecords.get(position);
        holder.date.setText(record.date);
        holder.timeIn.setText(record.timeIn);
        holder.timeOut.setText(record.timeOut);

        return convertView;

    }


}
