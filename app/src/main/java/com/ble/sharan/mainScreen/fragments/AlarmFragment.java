package com.ble.sharan.mainScreen.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 12/26/16.
 */

public class AlarmFragment extends Fragment implements View.OnClickListener, /*DatePickerDialog.OnDateSetListener, */TimePickerDialog.OnTimeSetListener
{

    String TAG = AlarmFragment.class.getSimpleName();


    Context context;

    View view;

    LinearLayout linearLayout_holder;

    FloatingActionButton fab;


    // TextView txtv_currentDateTime;
    //  TextView txtv_date;
//    TextView txtv_time;
//    Button btn_set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_alarm, container, false);

            setUpIds();

            SimpleDateFormat mSDF1 = new SimpleDateFormat("yyMMddHHmmss");
            String currentDateTime = mSDF1.format(new Date());

            MyLog("currentDateTime", currentDateTime);

            String commandToSetDateTime = "dt" + currentDateTime;

            ((MainActivityNew) context).setDateTimeORAlarm(commandToSetDateTime);
        }

          convert();
        return view;
    }


    public void convert()
    {
        int decimal = Integer.parseInt("1111111", 2);
        String hexStr = Integer.toString(decimal, 16);

        Log.e("HelloBalli", hexStr);
    }

    private void setUpIds()
    {

        // txtv_currentDateTime = (TextView) view.findViewById(R.id.txtv_currentDateTime);
        // (txtv_date = (TextView) view.findViewById(R.id.txtv_date)).setOnClickListener(this);
        linearLayout_holder = (LinearLayout) view.findViewById(R.id.linearLayout_holder);

        (fab = (FloatingActionButton) view.findViewById(R.id.fab)).setOnClickListener(this);

//        (btn_set = (Button) view.findViewById(R.id.btn_set)).setOnClickListener(this);
    }

    Calendar c;

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {


            case R.id.fab:

                if (((MainActivityNew) context).BLE_STATUS.equals(MyConstant.CONNECTED))
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
                }


                break;
  /*          case R.id.txtv_date:

                Log.e("Hello", "Hello");

                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(getActivity(), AlarmFragment.this, year, month, day).show();


                break;*/

//            case R.id.txtv_time:
//
//                c = Calendar.getInstance();
//                int hour = c.get(Calendar.HOUR_OF_DAY);
//                int minute = c.get(Calendar.MINUTE);
//
//                new TimePickerDialog(getActivity(), this, hour, minute, false).show();
//
//                break;
//
//
//            case R.id.btn_set:
//
//                // To set use dtyymoddhhmiss format
//
//                if( !txtv_time.getText().toString().isEmpty())
//                {
//                    String commandToSetAlarm="alm0" + time + "3E1000505";
//                    Log.e("Command", commandToSetAlarm);
//
//                    ((MainActivityNew)context).setDateTimeORAlarm(commandToSetAlarm);
//
//                }
//                else
//                {
//                    MyUtil.showToast(context,"Please enter date and time");
//                }
//
//
//
//                break;

        }
    }

    String alarmKey;

    public void addView(String showTime, String alarmTime)
    {
        String commandToSetAlarm = "alm" + alarmKey + alarmTime + "7F1000000";

        MyLog("commandToSetAlarm Map", "" + commandToSetAlarm);


        MySharedPreference.getInstance().saveAlarm(context,alarmKey, showTime + "," + commandToSetAlarm);

        makeView();


        // To set alarm
        setAlarm(commandToSetAlarm);

    }


    public void setAlarm(String commandToSetAlarm)
    {

        MyLog("Command", commandToSetAlarm);

        ((MainActivityNew) context).setDateTimeORAlarm(commandToSetAlarm);
    }

    public void disableAlarm(String command)
    {

        StringBuilder stringBuilder = new StringBuilder(command);
        stringBuilder.setCharAt(10, '0');

        MyLog("disableAlarm", stringBuilder.toString());

        ((MainActivityNew) context).setDateTimeORAlarm(stringBuilder.toString());
    }

    @Override
    public void onResume()
    {
        super.onResume();

        makeView();
    }


    public void makeView()
    {
        HashMap<String, String> map = MySharedPreference.getInstance().getAlarm(context);

        MyLog("Alarm Map", "" + map);

        linearLayout_holder.removeAllViews();

        int count = 0;

        for (final String key : map.keySet())
        {
            final String value = map.get(key);

            if (!value.isEmpty())
            {
                count++;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.custom_alarm, null);

                TextView txtv_alarmTime = (TextView) v.findViewById(R.id.txtv_alarmTime);



                ImageView imgv_cross = (ImageView) v.findViewById(R.id.imgv_cross);

                imgv_cross.setVisibility(View.VISIBLE);


                String[] showTime = value.split(",");

                txtv_alarmTime.setText("" + count + ".   " + showTime[0]);


                txtv_alarmTime.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        alarmKey = key;

                        c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        new TimePickerDialog(getActivity(), AlarmFragment.this, hour, minute, false).show();
                    }
                });


                imgv_cross.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!value.isEmpty())
                        {
                            String[] commandTime = value.split(",");

                            disableAlarm(commandTime[1]);

                            MySharedPreference.getInstance().deleteAlarm(context, value);

                            makeView();
                        }
                    }
                });


                linearLayout_holder.addView(v);
            }


        }
    }


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

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {

        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat mSDF2 = new SimpleDateFormat("HHmm");


        String alarmTime = mSDF2.format(c.getTime());

        String showTime = mSDF.format(c.getTime());


        addView(showTime, alarmTime);
//        txtv_time.setText(mSDF.format(c.getTime()));

        MyLog("alarmTime", "" + alarmTime);
    }


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


    public void MyLog(String msg, String value)
    {
        Log.e(TAG, "---" + msg + "---" + value);
    }


}