package com.ble.sharan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.BeanSleepRecord;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.List;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class MyWeekSleepAdapter extends BaseAdapter
{
    Context context;
    List<BeanSleepRecord> list;
    MyUtil myUtil = new MyUtil();


    public MyWeekSleepAdapter(Context context, List<BeanSleepRecord> list)
    {
        this.context = context;
        this.list = list;
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

        row = inflater.inflate(R.layout.custom_myweeksleep_item, parent, false);

        TextView txtv_date = (TextView) row.findViewById(R.id.txtv_date);
        TextView txtv_time = (TextView) row.findViewById(R.id.txtv_time);

      /*  SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM");

        try
        {
            txtv_date.setText(output.format(input.parse(list.get(position).getDate())));    // format output
        } catch (ParseException e)
        {
            e.printStackTrace();
        }*/

        txtv_date.setText(list.get(position).getDate());
        txtv_time.setText(list.get(position).getTime());


        return row;
    }


}