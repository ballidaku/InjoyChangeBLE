package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class TodayActivityFragment extends Fragment implements View.OnClickListener
{
    public static final String TAG = TodayActivityFragment.class.getSimpleName();


    Context context;
    View view;

    TextView txtv_steps;
    TextView txtv_stepsToGo;

    TextView txtv_calories;
    TextView txtv_caloriesToGo;

    TextView txtv_milesKm;
    TextView txtv_milesKmToGo;

    TextView txtv_sleepHour;
    TextView txtv_sleepHourToGo;

    LinearLayout linearLayout_refresh;
    LinearLayout linearLayout_connect_disconnect;

    TextView txtv_connect_disconnect;


    MyUtil myUtil = new MyUtil();

    MyDatabase myDatabase;

    AutoRefreshTimer autoRefreshTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_today_activity, container, false);

            setUpIds();


            myDatabase = new MyDatabase(getActivity());


            autoRefreshTimer = new AutoRefreshTimer(20000, 16000);


        }

        return view;
    }


    private void setUpIds()
    {

        txtv_steps = (TextView) view.findViewById(R.id.txtv_steps);
        txtv_stepsToGo = (TextView) view.findViewById(R.id.txtv_stepsToGo);

        txtv_calories = (TextView) view.findViewById(R.id.txtv_calories);
        txtv_caloriesToGo = (TextView) view.findViewById(R.id.txtv_caloriesToGo);

        txtv_milesKm = (TextView) view.findViewById(R.id.txtv_milesKm);
        txtv_milesKmToGo = (TextView) view.findViewById(R.id.txtv_milesKmToGo);

        txtv_sleepHour = (TextView) view.findViewById(R.id.txtv_sleepHour);
        txtv_sleepHourToGo = (TextView) view.findViewById(R.id.txtv_sleepHourToGo);


        txtv_connect_disconnect = (TextView) view.findViewById(R.id.txtv_connect_disconnect);


        (linearLayout_refresh = (LinearLayout) view.findViewById(R.id.linearLayout_refresh)).setOnClickListener(this);
        (linearLayout_connect_disconnect = (LinearLayout) view.findViewById(R.id.linearLayout_connect_disconnect)).setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {


            case R.id.linearLayout_refresh:

                MyUtil.showToast(context,"Please wait... data is refreshing.");

                onRefresh();

                break;

            case R.id.linearLayout_connect_disconnect:

//                getActivity().runOnUiThread(new Runnable()
//                {
//                    public void run()
//                    {
                ((MainActivityNew) context).connectDisconnect();


//                    }
//                });

                break;

        }
    }


    public void onRefresh()
    {
        ((MainActivityNew) context).commandToBLE(MyConstant.GET_STEPS);
    }


    @Override
    public void onResume()
    {
        super.onResume();
       // Log.e(TAG, "onResume");

        // onRefresh get the previous Count
        calculate(((MainActivityNew) context).stepsTaken,false);

        bleStatus(((MainActivityNew) context).BLE_STATUS);

    }


    @Override
    public void onPause()
    {
        super.onPause();
       // Log.e(TAG, "onPause");
        autoRefreshTimer.cancel();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
       // Log.e(TAG, "onDestroy");
        autoRefreshTimer.cancel();
    }


    //**********************************************************************************************


    public void calculate(int data,boolean wantToUpdate)
    {
        int steps = 0;
        int stepsFromBand = data;

        String todayCalories = "";
        String todayMilesCovered = "";

        // DATABASE UPDATE
        if (stepsFromBand > 0)
        {
            steps = stepsFromBand;
            myDatabase.addStepData(context,new BeanRecords(myUtil.getTodaydate(), String.valueOf(stepsFromBand)));
        }
        else if (myDatabase.getTodaySteps(context) == 0)
        {
            myDatabase.addStepData(context,new BeanRecords(myUtil.getTodaydate(), String.valueOf(stepsFromBand)));
            steps = myDatabase.getTodaySteps(context);
        }
        else
        {
            steps = myDatabase.getTodaySteps(context);
        }


//          myDatabase.addStepData(new BeanRecords("05-01-2017", "2005"));


        List<BeanRecords> list = myDatabase.getAllStepRecords(context);

        for (int i = 0; i < list.size(); i++)
        {
//            Log.e(TAG, "Ballidaku----" + list.get(i).getID() + "----" + list.get(i).getDate() + "----" + list.get(i).getSteps()+ "----" + list.get(i).getAccess_token());

            // myDatabase.deleteContact(list.get(i));
        }


//        DecimalFormat formatter = new DecimalFormat("#,###,###");
//        String yourFormattedString = formatter.format(steps);


        txtv_steps.setText(String.valueOf(steps));

        txtv_stepsToGo.setText(myUtil.getRemainingSteps(context, steps));

        txtv_calories.setText(todayCalories = myUtil.stepsToCalories(context, steps));

        txtv_caloriesToGo.setText(myUtil.stepsToRemainingCalories(context, steps));

        txtv_milesKm.setText(todayMilesCovered = myUtil.stepsToDistance(context, steps));

        txtv_milesKmToGo.setText(myUtil.stepsToRemainingDistance(context, steps));


        refreshSleepTextView();


        if(wantToUpdate && myUtil.checkConnection())
        {
            SEND_DATA_TO_SERVER(steps, todayCalories, todayMilesCovered);
        }

    }

    public void refreshSleepTextView()
    {
        txtv_sleepHour.setText(sleepTime());

        txtv_sleepHourToGo.setText(myUtil.sleepHrToRemainingHr(context, sleepTime()));
    }

    public String sleepTime()
    {
        String sleepTime = "";

        try
        {
            long millis = myDatabase.getTodaySleepTime(context);
            SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");

            int Hours = (int) (millis / (1000 * 60 * 60));
            int Mins = (int) (millis / (1000 * 60)) % 60;

            String diff = Hours + ":" + Mins; // updated value every1 second

//            Log.e("dakuu","---"+millis+"----"+myFormat.format(myFormat.parse(diff)));

            sleepTime = myFormat.format(myFormat.parse(diff));

        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return sleepTime;
//        Log.e("Sleep Database",""+myDatabase.getAllSleepData());
    }


    //**********************************************************************************************
    // Update UI
    //**********************************************************************************************


    public void bleStatus(String BLE_STATUS)
    {
        // String deviceName = ((MainActivityNew) context).deviceName;

        if (BLE_STATUS.equals(MyConstant.CONNECTING))
        {
            txtv_connect_disconnect.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._16sdp));
            txtv_connect_disconnect.setText("Connecting");
        }
        else if (BLE_STATUS.equals(MyConstant.DISCONNECTING))
        {
            txtv_connect_disconnect.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._14sdp));
            txtv_connect_disconnect.setText("Disconnecting");

        }
        else if (BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            txtv_connect_disconnect.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._16sdp));
            txtv_connect_disconnect.setText("Disconnect");
            linearLayout_refresh.setEnabled(true);


            if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
            {
                autoRefreshTimer.start();
            }
        }
        else if (BLE_STATUS.equals(MyConstant.DISCONNECTED))
        {
            txtv_connect_disconnect.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._16sdp));
            txtv_connect_disconnect.setText("Connect");
            linearLayout_refresh.setEnabled(false);
        }
    }


    //**********************************************************************************************
    // API TO UPLOAD DATA TO SERVER
    //**********************************************************************************************

    public void SEND_DATA_TO_SERVER(int steps, String calories, String todayMilesCovered)
    {
        String todayDate = myUtil.getTodaydate2();
        String todaySteps = String.valueOf(steps);
        String todaySleepTime = sleepTime();

        HashMap<String, String> map = new HashMap<>();

        map.put(MyConstant.ACCESS_TOKEN, MySharedPreference.getInstance().getAccessToken(context));
        map.put(MyConstant.STEPS, todaySteps);
        map.put(MyConstant.DATE, todayDate);
        map.put("sleephr", todaySleepTime);
        map.put(MyConstant.CALORIES, calories);
        map.put(MyConstant.DISTANCE, todayMilesCovered);


        MyUtil.execute(new Super_AsyncTask(context, map, MyConstant.UPLOAD_USER_DATA, new Super_AsyncTask_Interface()
        {
            @Override
            public void onTaskCompleted(String output)
            {
//                Log.e(TAG, "OUTPUT RESPONSE-------" + output);
            }
        }, false));


    }



    //**********************************************************************************************
    // AUTOMATIC REFRESH
    //**********************************************************************************************


    public class AutoRefreshTimer extends CountDownTimer
    {

        public AutoRefreshTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l)
        {
            Log.e(TAG, "onTick");
        }

        @Override
        public void onFinish()
        {
            if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
            {
                onRefresh();
                autoRefreshTimer.start();
            }
        }
    }

}
