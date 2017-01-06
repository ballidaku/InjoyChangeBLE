package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.List;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class Today extends Fragment implements View.OnClickListener
{
    public static final String TAG = Today.class.getSimpleName();


    Context context;
    View view;


    TextView txtv_activity;
    TextView txtv_sleep;


    TextView txtv_steps;
    TextView txtv_stepsToGo;

    TextView txtv_calories;
    TextView txtv_caloriesToGo;

    TextView txtv_milesKm;
    TextView txtv_milesKmToGo;

    TextView txtv_sleepHour;
    TextView txtv_sleepHourToGo;

    TextView txtv_refresh;
    TextView txtv_connect_disconnect;


    MyUtil myUtil = new MyUtil();

    MyDatabase myDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_today, container, false);

            setUpIds();


            myDatabase = new MyDatabase(getActivity());

        }

        return view;
    }

    private void setUpIds()
    {
        (txtv_activity = (TextView) view.findViewById(R.id.txtv_activity)).setOnClickListener(this);
        (txtv_sleep = (TextView) view.findViewById(R.id.txtv_sleep)).setOnClickListener(this);


        txtv_steps = (TextView) view.findViewById(R.id.txtv_steps);
        txtv_stepsToGo = (TextView) view.findViewById(R.id.txtv_stepsToGo);

        txtv_calories = (TextView) view.findViewById(R.id.txtv_calories);
        txtv_caloriesToGo = (TextView) view.findViewById(R.id.txtv_caloriesToGo);

        txtv_milesKm = (TextView) view.findViewById(R.id.txtv_milesKm);
        txtv_milesKmToGo = (TextView) view.findViewById(R.id.txtv_milesKmToGo);

        txtv_sleepHour = (TextView) view.findViewById(R.id.txtv_sleepHour);
        txtv_sleepHourToGo = (TextView) view.findViewById(R.id.txtv_sleepHourToGo);


        (txtv_refresh = (TextView) view.findViewById(R.id.txtv_refresh)).setOnClickListener(this);
        (txtv_connect_disconnect = (TextView) view.findViewById(R.id.txtv_connect_disconnect)).setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_activity:

                change(MyConstant.ACTIVITY);

                break;


            case R.id.txtv_sleep:

                change(MyConstant.SLEEP);

                break;

            case R.id.txtv_refresh:

                ((MainActivityNew) context).getTotalSteps();

                break;

            case R.id.txtv_connect_disconnect:

                ((MainActivityNew) context).connectDisconnect();

                break;

        }
    }


    public void change(String fromWhere)
    {
        txtv_activity.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        txtv_activity.setBackgroundResource(R.drawable.left_selector_blue);

        txtv_sleep.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        txtv_sleep.setBackgroundResource(R.drawable.right_selector_blue);


        if (fromWhere.equals(MyConstant.ACTIVITY))
        {
            txtv_activity.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
            txtv_activity.setBackgroundResource(R.drawable.left_selector_white);
        }
        else
        {
            txtv_sleep.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
            txtv_sleep.setBackgroundResource(R.drawable.right_selector_white);
        }


    }


    @Override
    public void onResume()
    {
        super.onResume();


        // onRefresh get the previous Count
        calculate(((MainActivityNew) context).stepsTaken);

        bleStatus(((MainActivityNew) context).BLE_STATUS);

        Log.d(TAG, "onResume");
    }


    //**********************************************************************************************


    public void calculate(String data)
    {
        int steps = 0;
        int stepsFromBand =Integer.parseInt(data);

        // DATABASE UPDATE
        if (stepsFromBand > 0)
        {
            steps = stepsFromBand;
            myDatabase.addData(new BeanRecords(myUtil.getTodaydate(), String.valueOf(stepsFromBand)));
        }
        else
        {
            steps = myDatabase.getTodaySteps();
        }

//          myDatabase.addData(new BeanRecords("05-01-2017", "2005"));


        List<BeanRecords> list = myDatabase.getAllContacts();

        for (int i = 0; i < list.size(); i++)
        {
            Log.e("Data", "----" + list.get(i).getID() + "----" + list.get(i).getDate() + "----" + list.get(i).getSteps());

             // myDatabase.deleteContact(list.get(i));
        }





        txtv_steps.setText(String.valueOf(steps));

        txtv_stepsToGo.setText(myUtil.getRemainingSteps(context, steps));

        txtv_calories.setText(myUtil.stepsToCalories(steps));

        txtv_caloriesToGo.setText(myUtil.stepsToRemainingCalories(context, steps));

        txtv_milesKm.setText(myUtil.stepsToDistance(steps));

        txtv_milesKmToGo.setText(myUtil.stepsToRemainingDistance(context, steps));

    }




    //**********************************************************************************************
    // Update UI
    //**********************************************************************************************


    public void bleStatus(String BLE_STATUS)
    {
        // String deviceName = ((MainActivityNew) context).deviceName;

        if (BLE_STATUS.equals(MyConstant.CONNECTING))
        {
            txtv_connect_disconnect.setText("Connecting...");
        }
        else if (BLE_STATUS.equals(MyConstant.DISCONNECTING))
        {
            txtv_connect_disconnect.setText("Disconnecting...");
        }
        else if (BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            txtv_connect_disconnect.setText("Disconnect");
            txtv_refresh.setEnabled(true);
        }
        else if (BLE_STATUS.equals(MyConstant.DISCONNECTED))
        {
            txtv_connect_disconnect.setText("Connect");
            txtv_refresh.setEnabled(false);
        }
    }


}
