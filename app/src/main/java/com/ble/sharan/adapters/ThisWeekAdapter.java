package com.ble.sharan.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 2/15/17.
 */

public class ThisWeekAdapter extends BaseAdapter
{
    Context context;
    ArrayList<HashMap<String, String>> list;
    MyUtil myUtil = new MyUtil();
    MyDatabase myDatabase;



    public ThisWeekAdapter(Context context, ArrayList<HashMap<String, String>> list)
    {
        this.context = context;
        this.list = list;
        myDatabase= new MyDatabase(context);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return list.size();
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

        row = inflater.inflate(R.layout.custom_thisweek_item, parent, false);


        if(position%2 != 0)
        {
            row.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGrayWhite));
        }

        TextView txtv_date = (TextView) row.findViewById(R.id.txtv_date);
        TextView txtv_calories = (TextView) row.findViewById(R.id.txtv_calories);
        TextView txtv_steps = (TextView) row.findViewById(R.id.txtv_steps);
        TextView txtv_km = (TextView) row.findViewById(R.id.txtv_km);
        TextView txtv_sleep = (TextView) row.findViewById(R.id.txtv_sleep);

        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("MM/dd");

        try
        {
            txtv_date.setText(output.format(input.parse(list.get(position).get(MyConstant.DATE))));    // format output
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        String steps =list.get(position).get(MyConstant.STEPS);

        txtv_calories.setText(myUtil.stepsToCalories(context,Integer.parseInt(steps)));
        txtv_steps.setText(steps);
        txtv_km.setText(myUtil.stepsToDistance(context,Integer.parseInt(steps)));

//        long millis = myDatabase.getSleepMillisOnDate(list.get(position).get(MyConstant.DATE));

        txtv_sleep.setText(myUtil.convertMillisToHrMins(Long.parseLong(list.get(position).get(MyConstant.SLEEP))));


        return row;
    }


}