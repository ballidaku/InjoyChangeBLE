package com.ble.sharan.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by brst-pc93 on 4/13/17.
 */

public class SleepDetailsAdapter extends BaseAdapter
{
    Context context;
    String[] sleepData;
    MyUtil myUtil = new MyUtil();
    MyDatabase myDatabase;


    public SleepDetailsAdapter(Context context, String[] sleepData)
    {
        this.context = context;
        this.sleepData = sleepData;
        myDatabase = new MyDatabase(context);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return sleepData.length;
    }

    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View row, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.custom_sleep_data_item, parent, false);


        if (position % 2 != 0)
        {
            row.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrayWhite));
        }

        TextView textView_SleepTime = (TextView) row.findViewById(R.id.textView_SleepTime);
        TextView textView_SleepHour = (TextView) row.findViewById(R.id.textView_SleepHour);


        String[] time = sleepData[position].split("-");


        SimpleDateFormat input = new SimpleDateFormat("HH:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm a");

        try
        {
            textView_SleepTime.setText(output.format(input.parse(time[0])) + " To " + output.format(input.parse(time[1])));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        textView_SleepHour.setText(myUtil.getDifferenceBetweenTwoTimes(time[0], time[1]));


        return row;
    }


}