package in.yousee.theadmin.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mittu on 14-08-2016.
 */
public class Utils {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public static void setDateInEditText(EditText editText, int year, int month, int day)
    {

        month=month+1;
        String monthString;
        String dayString;
        if(day < 10)
        {
            dayString = "0"+day;
        }
        else
        {
            dayString = ""+day;
        }
        if(month <10)
        {
            monthString = "0"+month;
        }
        else
        {
            monthString = ""+month;
        }
        editText.setText(year + "-" + monthString +"-"+ dayString );
    }

    public static String getDisplayDateString(Calendar calendar)
    {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        Date date = calendar.getTime();
        return format.format(date);
    }
    public static String getSqlDateString(Calendar calendar)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = calendar.getTime();
        return format.format(date);
    }
    public static Date getEncodedDateFromDisplayString(String date)
    {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;

    }
    public static Calendar getFirstOfMonth()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        return calendar;
    }


}
