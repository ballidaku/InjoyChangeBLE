package com.ble.sharan.mainScreen.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by brst-pc93 on 12/26/16.
 */

public class AlarmFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    String TAG = AlarmFragment.class.getSimpleName();





    Context context;
    View view;

   // TextView txtv_currentDateTime;
    TextView txtv_date;
    TextView txtv_time;
    Button btn_set;

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

            myLog("currentDateTime",currentDateTime);

            String commandToSetDateTime="dt" + currentDateTime;

            ((MainActivityNew)context).setDateTimeORAlarm(commandToSetDateTime);
        }


        return view;
    }

    private void setUpIds()
    {

       // txtv_currentDateTime = (TextView) view.findViewById(R.id.txtv_currentDateTime);
        (txtv_date = (TextView) view.findViewById(R.id.txtv_date)).setOnClickListener(this);
        (txtv_time = (TextView) view.findViewById(R.id.txtv_time)).setOnClickListener(this);


        (btn_set = (Button) view.findViewById(R.id.btn_set)).setOnClickListener(this);
    }

    Calendar c;

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_date:

                Log.e("Hello", "Hello");

                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(getActivity(), AlarmFragment.this, year, month, day).show();


                break;

            case R.id.txtv_time:

                c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                new TimePickerDialog(getActivity(), this, hour, minute, false).show();

                break;


            case R.id.btn_set:

                // To set use dtyymoddhhmiss format


                if(!txtv_date.getText().toString().isEmpty() && !txtv_time.getText().toString().isEmpty())
                {
                    String commandToSetAlarm="alm0" + time + "3E1000505";
                    Log.e("Command", commandToSetAlarm);

                    ((MainActivityNew)context).setDateTimeORAlarm(commandToSetAlarm);

                }
                else
                {
                    MyUtil.showToast(context,"Please enter date and time");
                }



                break;

        }
    }

    String date = "";
    String time = "";

    public void onDateSet(DatePicker view, int year, int month, int day)
    {


        //  String myFormat = "dd/MM/yy"; //In which you need put here
        //  SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat mSDF = new SimpleDateFormat("dd MMM yyyy");

        SimpleDateFormat mSDF1 = new SimpleDateFormat("yyMMdd");

        date = mSDF1.format(c.getTime());


        txtv_date.setText("" + mSDF.format(c.getTime()));

        Log.e("Date", "" + date);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {

        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat mSDF2 = new SimpleDateFormat("HHmm");


        time = mSDF2.format(c.getTime());

        txtv_time.setText(mSDF.format(c.getTime()));

        Log.e("Time", "" + time);
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



    public void myLog(String msg,String value)
    {
        Log.e(TAG, "---"+msg+"---" +value);
    }


   /*static Calendar c;
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

           Log.e("Date",""+sdf.format(c.getTime()));

        }
    }*/
}