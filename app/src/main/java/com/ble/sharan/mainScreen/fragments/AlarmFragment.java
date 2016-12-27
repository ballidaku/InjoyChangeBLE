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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by brst-pc93 on 12/26/16.
 */

public class AlarmFragment extends Fragment implements View.OnClickListener ,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener
{

    Context context;
    View view;

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
        }


        return view;
    }

    private void setUpIds()
    {

        (txtv_date=(TextView)view.findViewById(R.id.txtv_date)).setOnClickListener(this);
        (txtv_time=(TextView)view.findViewById(R.id.txtv_time)).setOnClickListener(this);


        (btn_set=(Button)view.findViewById(R.id.btn_set)).setOnClickListener(this);
    }

    Calendar c;

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_date:

                Log.e("Hello","Hello");

                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(getActivity(),AlarmFragment.this, year, month, day).show();


                break;

            case R.id.txtv_time:

                c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                new TimePickerDialog(getActivity(), this, hour, minute, false).show();

                break;


            case R.id.btn_set:

                break;

        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {


      //  String myFormat = "dd/MM/yy"; //In which you need put here
      //  SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat mSDF = new SimpleDateFormat("dd/MM/yyyy");




        txtv_date.setText(""+mSDF.format(c.getTime()));

        Log.e("Date",""+mSDF.format(c.getTime()));

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {

        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");
        String time = mSDF.format(c.getTime());


        txtv_time.setText(time);
        Log.e("Time",""+time);
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