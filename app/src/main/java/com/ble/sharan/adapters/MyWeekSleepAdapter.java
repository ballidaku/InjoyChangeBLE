package com.ble.sharan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class MyWeekSleepAdapter extends BaseAdapter
{
    Context context;
    ArrayList<HashMap<String,String>> list;
    MyUtil myUtil = new MyUtil();


    public MyWeekSleepAdapter(Context context, ArrayList<HashMap<String,String>> list)
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

        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM");

        try
        {
            txtv_date.setText(output.format(input.parse(list.get(position).get(MyConstant.DATE))));    // format output
        } catch (ParseException e)
        {
            e.printStackTrace();
        }



//        myUtil.convertMillisToHrMins(list.get(position).get(MyConstant.SLEEP));


        try
        {

            long millis = Long.parseLong(list.get(position).get(MyConstant.SLEEP));
            //SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");

            int Hours = (int) (millis / (1000 * 60 * 60));
            int Mins = (int) (millis / (1000 * 60)) % 60;

           // String diff = Hours + ":" + Mins; // updated value every1 second

            //Log.e("dakuu","---"+millis+"----"+myFormat.format(myFormat.parse(diff)));
            txtv_time.setText(""+Hours +" Hrs. "+Mins+" Mins");

        } catch (Exception e)
        {
            e.printStackTrace();
        }




        return row;
    }


}