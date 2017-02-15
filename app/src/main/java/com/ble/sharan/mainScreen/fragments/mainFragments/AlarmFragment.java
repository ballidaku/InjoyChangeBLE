package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 12/26/16.
 */

public class AlarmFragment extends Fragment implements View.OnClickListener, /*DatePickerDialog.OnDateSetListener, */TimePickerDialog.OnTimeSetListener
{

    String TAG = AlarmFragment.class.getSimpleName();


    Context context;

    View view;

    TextView txtv_alarmFirst;
    TextView txtv_alarmSecond;
    TextView txtv_alarmThird;

    TextView txtv_firstAmPm;
    TextView txtv_secondAmPm;
    TextView txtv_thirdAmPm;


    Switch switch_firstOnOff;
    Switch switch_secondOnOff;
    Switch switch_thirdOnOff;


    String ALARM_NUMBER = "";


    boolean startWork=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_alarm, container, false);

            setUpIds();

//            SimpleDateFormat mSDF1 = new SimpleDateFormat("yyMMddHHmmss");
//            String currentDateTime = mSDF1.format(new Date());
//
//            MyLog("currentDateTime", currentDateTime);
//
//            String commandToSetDateTime = "dt" + currentDateTime;
//
//
//            if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
//            {
//                ((MainActivityNew) context).commandToBLE(commandToSetDateTime);
//            }

            ((MainActivityNew) context).setDateToBLE();



            getDataAndShow();


        }

//          convert();
        return view;
    }


    public void convert()
    {
        int decimal = Integer.parseInt("1111111", 2);
        String hexStr = Integer.toString(decimal, 16);

        Log.e("HelloBalli", hexStr);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        startWork=true;



    }


    private void setUpIds()
    {
        (txtv_alarmFirst = (TextView) view.findViewById(R.id.txtv_alarmFirst)).setOnClickListener(this);
        ;
        (txtv_alarmSecond = (TextView) view.findViewById(R.id.txtv_alarmSecond)).setOnClickListener(this);
        (txtv_alarmThird = (TextView) view.findViewById(R.id.txtv_alarmThird)).setOnClickListener(this);


        txtv_firstAmPm = (TextView) view.findViewById(R.id.txtv_firstAmPm);
        txtv_secondAmPm = (TextView) view.findViewById(R.id.txtv_secondAmPm);
        txtv_thirdAmPm = (TextView) view.findViewById(R.id.txtv_thirdAmPm);


        switch_firstOnOff = (Switch) view.findViewById(R.id.switch_firstOnOff);
        switch_secondOnOff = (Switch) view.findViewById(R.id.switch_secondOnOff);
        switch_thirdOnOff = (Switch) view.findViewById(R.id.switch_thirdOnOff);

        switch_firstOnOff.setOnCheckedChangeListener(new SwitchListener(switch_firstOnOff));
        switch_secondOnOff.setOnCheckedChangeListener(new SwitchListener(switch_secondOnOff));
        switch_thirdOnOff.setOnCheckedChangeListener(new SwitchListener(switch_thirdOnOff));

    }


    private void getDataAndShow()
    {
        HashMap<String, String> map = MySharedPreference.getInstance().getAllAlarm(context);

        String firstAlarm = map.get(MyConstant.ALARM_FIRST);
        String secondAlarm = map.get(MyConstant.ALARM_SECOND);
        String thirdAlarm = map.get(MyConstant.ALARM_THIRD);


//        Log.e("firstAlarm",firstAlarm);
//        Log.e("secondAlarm",secondAlarm);
//        Log.e("thirdAlarm",thirdAlarm);

        String[] arrayfirst = firstAlarm.split(",");
        String[] arraySecond = secondAlarm.split(",");
        String[] arrayThird = thirdAlarm.split(",");


        String[] arrayfirstNew = arrayfirst[0].split(" ");
        String[] arraySecondNew = arraySecond[0].split(" ");
        String[] arrayThirdNew = arrayThird[0].split(" ");


        txtv_alarmFirst.setText(firstAlarm.isEmpty() ? "00:00" : arrayfirstNew[0]);
        txtv_firstAmPm.setText(firstAlarm.isEmpty() ? "" : arrayfirstNew[1]);

        txtv_alarmSecond.setText(secondAlarm.isEmpty() ? "00:00" : arraySecondNew[0]);
        txtv_secondAmPm.setText(secondAlarm.isEmpty() ? "" : arraySecondNew[1]);

        txtv_alarmThird.setText(thirdAlarm.isEmpty() ? "00:00" : arrayThirdNew[0]);
        txtv_thirdAmPm.setText(thirdAlarm.isEmpty() ? "" : arrayThirdNew[1]);


        switch_firstOnOff.setChecked(MySharedPreference.getInstance().getIsAlarmActivated(context, MyConstant.IS_ALARM_FIRST_ACTIVATED));
        switch_secondOnOff.setChecked(MySharedPreference.getInstance().getIsAlarmActivated(context, MyConstant.IS_ALARM_SECOND_ACTIVATED));
        switch_thirdOnOff.setChecked(MySharedPreference.getInstance().getIsAlarmActivated(context, MyConstant.IS_ALARM_THIRD_ACTIVATED));


    }


    class SwitchListener implements CompoundButton.OnCheckedChangeListener
    {
        Switch mySwitch;

        boolean previousValue=false;

        public SwitchListener(Switch mySwitch)
        {
            this.mySwitch = mySwitch;

            previousValue = mySwitch.isChecked();
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {

          //  Log.e(TAG, "Balli----"+b);


            if (!((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED) && startWork)
            {
                MyUtil.showToast(context, "Your device is not connected yet!!");
                mySwitch.setChecked(!b);
            }
            else if (mySwitch == switch_firstOnOff && startWork)
            {
                String alarm = MySharedPreference.getInstance().getPreference(context).getString(MyConstant.ALARM_FIRST, "00:00 AM,alm000007F1000000");
                String[] commandString = alarm.split(",");

              //  Log.e(TAG,"Alarm inside----" + alarm);

                if (!alarm.isEmpty())
                {
                    if (b)
                    {
                        MySharedPreference.getInstance().setTrueIsAlarmActivated(context, MyConstant.ALARM_FIRST);
                    //    Log.e(TAG,"commandString inside" + commandString[1]);
                        // To set alarm
                        setAlarm(commandString[1]);
                    }
                    else
                    {
                        MySharedPreference.getInstance().deleteAlarm(context, MyConstant.ALARM_FIRST);
                        disableAlarm(commandString[1]);

                    }
                }
            }
            else if (mySwitch == switch_secondOnOff && startWork)
            {
                String alarm = MySharedPreference.getInstance().getPreference(context).getString(MyConstant.ALARM_SECOND, "00:00 AM,alm100007F1000000");
                String[] commandString = alarm.split(",");

                if (!alarm.isEmpty())
                {

                    if (b)
                    {
                        MySharedPreference.getInstance().setTrueIsAlarmActivated(context, MyConstant.ALARM_SECOND);
                      //  Log.e(TAG,"commandString" + commandString[1]);
                        // To set alarm
                        setAlarm(commandString[1]);
                    }
                    else
                    {
                        MySharedPreference.getInstance().deleteAlarm(context, MyConstant.ALARM_SECOND);
                        disableAlarm(commandString[1]);
                    }
                }
            }
            else if (mySwitch == switch_thirdOnOff && startWork)
            {
                String alarm = MySharedPreference.getInstance().getPreference(context).getString(MyConstant.ALARM_THIRD, "00:00 AM,alm200007F1000000");

                String[] commandString = alarm.split(",");

                if (!alarm.isEmpty())
                {
                    if (b)
                    {
                        MySharedPreference.getInstance().setTrueIsAlarmActivated(context, MyConstant.ALARM_THIRD);
                     //   Log.e(TAG,"commandString---" + commandString[1]);
                        // To set alarm
                        setAlarm(commandString[1]);
                    }
                    else
                    {
                        MySharedPreference.getInstance().deleteAlarm(context, MyConstant.ALARM_THIRD);
                        disableAlarm(commandString[1]);
                    }
                }
            }
        }
    }


    Calendar c;

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.txtv_alarmFirst:

                ALARM_NUMBER = MyConstant.ALARM_FIRST;
                addNewAlarm();

                break;

            case R.id.txtv_alarmSecond:

                ALARM_NUMBER = MyConstant.ALARM_SECOND;
                addNewAlarm();

                break;


            case R.id.txtv_alarmThird:

                ALARM_NUMBER = MyConstant.ALARM_THIRD;
                addNewAlarm();

                break;

        }
    }


    private void addNewAlarm()
    {
        if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
        {

            c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            new TimePickerDialog(getActivity(), this, hour, minute, false).show();
        }
        else
        {
            MyUtil.showToast(context, "Your device is not connected yet!!");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {

        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat mSDF2 = new SimpleDateFormat("HHmm");


        String alarmTime = mSDF2.format(c.getTime());

        String showTime = mSDF.format(c.getTime());


        String alarmKey = "";

        if (ALARM_NUMBER.equals(MyConstant.ALARM_FIRST))
        {
            String[] array = showTime.split(" ");
            txtv_alarmFirst.setText(array[0]);
            txtv_firstAmPm.setText(array[1]);

            alarmKey = "0";

            //switch_firstOnOff.setChecked(false);
            switch_firstOnOff.setChecked(true);
        }
        else if (ALARM_NUMBER.equals(MyConstant.ALARM_SECOND))
        {
            String[] array = showTime.split(" ");
            txtv_alarmSecond.setText(array[0]);
            txtv_secondAmPm.setText(array[1]);

            alarmKey = "1";

            //switch_secondOnOff.setChecked(false);
        }
        else if (ALARM_NUMBER.equals(MyConstant.ALARM_THIRD))
        {
            String[] array = showTime.split(" ");
            txtv_alarmThird.setText(array[0]);
            txtv_thirdAmPm.setText(array[1]);

            alarmKey = "2";

            //switch_thirdOnOff.setChecked(false);
        }

        saveAlarm(alarmKey, showTime, alarmTime);


       // Log.e(TAG,"alarmTime---" + alarmTime + "----" + showTime);
    }


    public void saveAlarm(String alarmKey, String showTime, String alarmTime)
    {
        String commandToSetAlarm = "alm" + alarmKey + alarmTime + "7F1000000";

     //   Log.e(TAG,"commandToSetAlarm Map" + commandToSetAlarm);


        // BY ME NOW
        setAlarm(commandToSetAlarm);


        MySharedPreference.getInstance().saveAlarm(context, ALARM_NUMBER, showTime + "," + commandToSetAlarm);

    }


    public void setAlarm(String commandToSetAlarm)
    {
        Log.e(TAG,"Command"+ commandToSetAlarm);

        ((MainActivityNew) context).commandToBLE(commandToSetAlarm);

        MyUtil.showToast(context,"Alarm is set");
    }

    public void disableAlarm(String command)
    {

        StringBuilder stringBuilder = new StringBuilder(command);
        stringBuilder.setCharAt(10, '0');

        Log.e(TAG,"disableAlarm"+ stringBuilder.toString());

        ((MainActivityNew) context).commandToBLE(stringBuilder.toString());

        MyUtil.showToast(context,"Alarm is removed");
    }



/*    if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
    {
        HashMap<String, String> map = MySharedPreference.getInstance().getAlarm(context);

        String value = "";
        for (String key : map.keySet())
        {
            value = map.get(key);

            if (value.isEmpty())
            {
                alarmKey = key;
                break;
            }
        }


        if (value.isEmpty())
        {
            c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            new TimePickerDialog(getActivity(), this, hour, minute, false).show();
        }
        else
        {
            MyUtil.showToast(context, "No alarm is available now.");
        }
    }
    else
    {
        MyUtil.showToast(context, "Your device is not connected yet!!");
    }*/


//    public void makeView()
//    {
//        HashMap<String, String> map = MySharedPreference.getInstance().getAllAlarm(context);
//
//        MyLog("Alarm Map", "" + map);
//
////        linearLayout_holder.removeAllViews();
//
//        int count = 0;
//
//        for (final String key : map.keySet())
//        {
//            final String value = map.get(key);
//
////            if (!value.isEmpty())
////            {
//            count++;
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = inflater.inflate(R.layout.custom_alarm, null);
//
//            TextView txtv_alarmTime = (TextView) v.findViewById(R.id.txtv_alarmTime);
//
//
//            ImageView imgv_cross = (ImageView) v.findViewById(R.id.imgv_cross);
//
//            if (!value.isEmpty())
//            {
//
//                imgv_cross.setVisibility(View.VISIBLE);
//
//                String[] showTime = value.split(",");
//
//                txtv_alarmTime.setText("" + count + ".   " + showTime[0]);
//            }
//            else
//            {
//                imgv_cross.setVisibility(View.INVISIBLE);
//
//                txtv_alarmTime.setText("" + count + ".   Set Alarm");
//            }
//
//
//            txtv_alarmTime.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//
////                    alarmKey = key;
//
//                    c = Calendar.getInstance();
//                    int hour = c.get(Calendar.HOUR_OF_DAY);
//                    int minute = c.get(Calendar.MINUTE);
//
//                    new TimePickerDialog(getActivity(), AlarmFragment.this, hour, minute, false).show();
//                }
//            });
//
//
//            imgv_cross.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    if (!value.isEmpty())
//                    {
//                        String[] commandTime = value.split(",");
//
//                        disableAlarm(commandTime[1]);
//
//                        MySharedPreference.getInstance().deleteAlarm(context, value);
//
//                        makeView();
//                    }
//                }
//            });
//
//
////            linearLayout_holder.addView(v);
//        }
//
//
////        }
//    }


    //    String date = "";
    //  String time = "";

//    public void onDateSet(DatePicker view, int year, int month, int day)
//    {
//
//
//        //  String myFormat = "dd/MM/yy"; //In which you need put here
//        //  SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, month);
//        c.set(Calendar.DAY_OF_MONTH, day);
//
//        SimpleDateFormat mSDF = new SimpleDateFormat("dd MMM yyyy");
//
//        SimpleDateFormat mSDF1 = new SimpleDateFormat("yyMMdd");
//
//        date = mSDF1.format(c.getTime());
//
//
//        txtv_date.setText("" + mSDF.format(c.getTime()));
//
//        Log.e("Date", "" + date);
//
//    }


//    private void setDateTime(String currentDateTime)
//    {
//
//
//        try
//        {
//            byte[] value = currentDateTime.getBytes("UTF-8");
//
//            mService.writeRXCharacteristic(value);
//
//
//        } catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//
//    }





}