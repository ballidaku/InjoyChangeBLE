package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.UploadDataModel;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;
import com.ble.sharan.myUtilities.ThemeChanger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


            int time = 10 * 60 * 1000;
//            autoRefreshTimer = new AutoRefreshTimer(50000, 40000);

            autoRefreshTimer = new AutoRefreshTimer(time, 300000);

            /*ManupulateSleepDataNew manupulateSleepdata = new ManupulateSleepDataNew();

            manupulateSleepdata.gettingSleepData(context,myDatabase);*/


        }

        return view;
    }


    private void setUpIds()
    {

        view.findViewById(R.id.linearLayoutBackground).setBackground((Drawable) ThemeChanger.getInstance().getBackground(context, MyConstant.BACKGROUND));

        ImageView imageViewRefresh = (ImageView) view.findViewById(R.id.imageViewRefresh);
        imageViewRefresh.setImageResource(ThemeChanger.getInstance().getActivityOverallBackground(MyConstant.REFRESH));

        ImageView imageViewConnect = (ImageView) view.findViewById(R.id.imageViewConnect);
        imageViewConnect.setImageResource(ThemeChanger.getInstance().getActivityOverallBackground(MyConstant.CONNECT));

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

                MyUtil.showToast(context, "Please wait... data is refreshing.");

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
        // MyUtil.myLog(TAG, "onResume");

        // onRefresh get the previous Count
        calculate(((MainActivityNew) context).stepsTaken, false);

        bleStatus(((MainActivityNew) context).BLE_STATUS);

    }


    /*@Override
    public void onPause()
    {
        super.onPause();
       // MyUtil.myLog(TAG, "onPause");
        autoRefreshTimer.cancel();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
       // MyUtil.myLog(TAG, "onDestroy");
        autoRefreshTimer.cancel();
    }*/


    //**********************************************************************************************


    public void calculate(int stepsFromBand, boolean wantToUpdate)
    {
        int steps;

        String todayCalories;
        String todayMilesCovered;

        // DATABASE UPDATE
        if (stepsFromBand > 0)
        {
            steps = stepsFromBand;
            myDatabase.addStepData(context, new BeanRecords(myUtil.getTodaydate(), String.valueOf(stepsFromBand)));
        }
        else if (myDatabase.getTodaySteps(context) == 0)
        {
            myDatabase.addStepData(context, new BeanRecords(myUtil.getTodaydate(), String.valueOf(stepsFromBand)));
            steps = myDatabase.getTodaySteps(context);
        }
        else
        {
            steps = myDatabase.getTodaySteps(context);
        }


//          myDatabase.addStepData(new BeanRecords("05-01-2017", "2005"));


        //     List<BeanRecords> list = myDatabase.getAllStepRecords(context);

//        for (int i = 0; i < list.size(); i++)
//        {
////            MyUtil.myLog(TAG, "Ballidaku----" + list.get(i).getID() + "----" + list.get(i).getDate() + "----" + list.get(i).getSteps()+ "----" + list.get(i).getAccess_token());
//
//            // myDatabase.deleteContact(list.g///et(i));
//        }


//        DecimalFormat formatter = new DecimalFormat("#,###,###");
//        String yourFormattedString = formatter.format(steps);


        // steps= 12121;

        txtv_steps.setText(String.valueOf(steps));

        txtv_stepsToGo.setText(myUtil.getRemainingSteps(context, steps));


//        todayCalories = String.valueOf(Math.round(Double.parseDouble(myUtil.stepsToCalories(context, steps))));
        todayCalories = myUtil.stepsToCalories(context, steps);

        //txtv_calories.setText(todayCalories = myUtil.stepsToCalories(context, steps));
        txtv_calories.setText(todayCalories);

//        String remainingCalories=String.valueOf(Math.round(Double.parseDouble(myUtil.stepsToRemainingCalories(context, steps))));
        String remainingCalories = myUtil.stepsToRemainingCalories(context, steps);

        txtv_caloriesToGo.setText(remainingCalories);

        txtv_milesKm.setText(todayMilesCovered = myUtil.stepsToDistance(context, steps));

        MyUtil.myLog(TAG, "Total Miles Activity " + todayMilesCovered);

        txtv_milesKmToGo.setText(myUtil.stepsToRemainingDistance(context, steps));


        refreshSleepTextView();


        if (wantToUpdate && myUtil.checkConnection())
        {

            try
            {
                //SEND_DATA_TO_SERVER(steps, todayCalories, todayMilesCovered);
                POST_DATA_TO_SERVER_RETROFIT(steps, todayCalories, todayMilesCovered);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }

    }

    public void refreshSleepTextView()
    {
//        txtv_sleepHour.setText(sleepTime());


        String sleepTime = myUtil.getSleepHr(context, myDatabase);

        txtv_sleepHour.setText(sleepTime);

        // MyUtil.myLog(TAG,"sleepTime------"+sleepTime);


        txtv_sleepHourToGo.setText(myUtil.sleepHrToRemainingHr(context, sleepTime));
    }

//    public String sleepTime()
//    {
//        String sleepTime = "";
//
//        try
//        {
//            long millis = myDatabase.getTodaySleepTime(context);
//            SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");
//
//            int Hours = (int) (millis / (1000 * 60 * 60));
//            int Mins = (int) (millis / (1000 * 60)) % 60;
//
//            String diff = Hours + ":" + Mins; // updated value every1 second
//
////            MyUtil.myLog("dakuu","---"+millis+"----"+myFormat.format(myFormat.parse(diff)));
//
//            sleepTime = myFormat.format(myFormat.parse(diff));
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//        return sleepTime;
////        MyUtil.myLog("Sleep Database",""+myDatabase.getAllSleepData());
//    }


    //**********************************************************************************************
    // Update UI
    //**********************************************************************************************


    public void bleStatus(String BLE_STATUS)
    {
        // String deviceName = ((MainActivityNew) context).deviceName;

        if (isAdded())
        {

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


                autoRefreshTimer.cancel();
                autoRefreshTimer.start();
            }
            else if (BLE_STATUS.equals(MyConstant.DISCONNECTED))
            {
                txtv_connect_disconnect.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._16sdp));
                txtv_connect_disconnect.setText("Connect");


                linearLayout_refresh.setEnabled(false);

                autoRefreshTimer.cancel();
            }
        }
    }


    //**********************************************************************************************
    // API TO UPLOAD DATA TO SERVER
    //**********************************************************************************************

   /* public void SEND_DATA_TO_SERVER(int steps, String calories, String todayMilesCovered)
    {
        String todayDate = myUtil.getTodaydate2();

        String todaySteps = String.valueOf(steps);

//        String todaySleepTime = sleepTime();
        String[] spltm = myUtil.getSleepHr(context,myDatabase).split(":");

        String todaySleepTime=spltm[0]+"h:"+spltm[1]+"m";

        HashMap<String, String> map = new HashMap<>();

        map.put(MyConstant.UID, MySharedPreference.getInstance().getUID(context));
        map.put(MyConstant.STEPS, todaySteps);
        map.put(MyConstant.DATE, todayDate);
        map.put("sleephr", todaySleepTime);
        map.put(MyConstant.CALORIES, calories);
        map.put(MyConstant.DISTANCE, todayMilesCovered);

        MyUtil.myLog(TAG, "input-------" + map);
        MyUtil.myLog(TAG, "URL-------" + MyConstant.UPLOAD_USER_DATA);


        MyUtil.execute(new Super_AsyncTask(context, map, MyConstant.UPLOAD_USER_DATA, new Super_AsyncTask_Interface()
        {
            @Override
            public void onTaskCompleted(String output)
            {
//                MyUtil.myLog(TAG, "OUTPUT RESPONSE-------" + output);
            }
        }, false));


    }*/

    // RETROFIT
    public void POST_DATA_TO_SERVER_RETROFIT(int steps, String calories, String todayMilesCovered)
    {
        String todayDate = myUtil.getTodaydate2();

        String todaySteps = String.valueOf(steps);

        String[] spltm = myUtil.getSleepHr(context, myDatabase).split(":");

        String todaySleepTime = spltm[0] + "h:" + spltm[1] + "m";


        MyUtil.myLog(TAG, "Send Items " + " Date " + todayDate + " todaySteps " + todaySteps + " calories " + calories + " todaySleepTime " + todaySleepTime + " UID " + MySharedPreference.getInstance().getUID(context));


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<UploadDataModel> call = apiService.postData(todayDate, todaySteps, calories, todaySleepTime, todayMilesCovered, MySharedPreference.getInstance().getUID(context));

        call.enqueue(new Callback<UploadDataModel>()
        {
            @Override
            public void onResponse(Call<UploadDataModel> call, Response<UploadDataModel> response)
            {
                MyUtil.myLog(TAG, "Response----" + response.body());

                UploadDataModel uploadDataModel = response.body();

                if (uploadDataModel.getStatus() != null && uploadDataModel.getStatus().equals("200"))
                {

                    MyUtil.myLog(TAG, "Upload Response ---" + uploadDataModel.getData());
                }
            }

            @Override
            public void onFailure(Call<UploadDataModel> call, Throwable t)
            {
                MyUtil.myLog(TAG, t.getMessage());
                MyUtil.showToast(context, "Server side error");

            }
        });
    }


    //**********************************************************************************************
    // AUTOMATIC REFRESH
    //**********************************************************************************************


    private class AutoRefreshTimer extends CountDownTimer
    {

        private AutoRefreshTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l)
        {
            MyUtil.myLog(TAG, "onTick");
        }

        @Override
        public void onFinish()
        {
            MyUtil.myLog("BLE STATUS", "" + ((MainActivityNew) context).BLE_STATUS);
            if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
            {
                onRefresh();
                autoRefreshTimer.cancel();
                autoRefreshTimer.start();
            }
            else
            {
                autoRefreshTimer.cancel();
            }
        }
    }

}
