package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.SleepDetailsAdapter;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by brst-pc93 on 4/13/17.
 */

public class SleepDetails  extends Fragment
{
    String TAG = SleepDetails.class.getSimpleName();

    Context context;
    View view;

    ListView listView_SleepDetails;

    MyDatabase myDatabase;

    String sleepData="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_sleep_details, container, false);

            myDatabase = new MyDatabase(getActivity());

            sleepData=this.getArguments().getString(MyConstant.SLEEP);

            String date=this.getArguments().getString(MyConstant.DATE);

            MyUtil.myLog(TAG, "SLEEP DATA "+sleepData+"  Date "+date);

            setUpIds();


            SimpleDateFormat inputFormat=new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat outputFormat=new SimpleDateFormat("dd MMM yyyy");


            try
            {
                ((MainActivityNew) getActivity()).setTitleHeader(outputFormat.format(inputFormat.parse(date)));
            } catch (ParseException e)
            {
                e.printStackTrace();
            }


            setData();

        }


        return view;
    }




    private void setUpIds()
    {
        listView_SleepDetails = (ListView) view.findViewById(R.id.listView_SleepDetails);
    }

    private void setData()
    {
        String [] sleepArray=sleepData.split(",");
        SleepDetailsAdapter SleepDetailsAdapter = new SleepDetailsAdapter(context, sleepArray);

        listView_SleepDetails.setAdapter(SleepDetailsAdapter);

    }


}
