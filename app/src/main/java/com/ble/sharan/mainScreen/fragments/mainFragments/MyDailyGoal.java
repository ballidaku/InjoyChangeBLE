package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class MyDailyGoal extends Fragment implements TimePickerDialog.OnTimeSetListener
{

    String TAG = MyDailyGoal.class.getSimpleName();

    EditText edtv_steps;
    EditText edtv_miles;
    EditText edtv_calories;
    EditText edtv_sleep;


    KeyListener edtv_stepsListener;
    KeyListener edtv_milesListener;
    KeyListener edtv_caloriesListener;
    KeyListener edtv_sleepListener;

    Context context;

    View view;

    Drawable RIGHT_ICON_GREEN;
    Drawable EDIT_ICON;


    Calendar c;


    MyDatabase myDatabase;
    MyUtil myUtil = new MyUtil();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_my_daily_goal, container, false);

            RIGHT_ICON_GREEN = getContext().getResources().getDrawable(R.drawable.ic_tick);
            EDIT_ICON = getContext().getResources().getDrawable(R.mipmap.ic_edit);


            myDatabase = new MyDatabase(context);

            setUpIds();

        }

        return view;
    }


    private void setUpIds()
    {


        edtv_steps = (EditText) view.findViewById(R.id.edtv_steps);
        edtv_stepsListener = edtv_steps.getKeyListener();
        edtv_steps.setKeyListener(null);
        // edtv_steps.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_steps));
        edtv_steps.setOnTouchListener(new MyTouchListerner(edtv_steps));


        edtv_miles = (EditText) view.findViewById(R.id.edtv_miles);
        edtv_milesListener = edtv_miles.getKeyListener();
        edtv_miles.setKeyListener(null);
        // edtv_miles.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_miles));
        edtv_miles.setOnTouchListener(new MyTouchListerner(edtv_miles));


        edtv_calories = (EditText) view.findViewById(R.id.edtv_calories);
        edtv_caloriesListener = edtv_calories.getKeyListener();
        edtv_calories.setKeyListener(null);
        //edtv_calories.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_calories));
        edtv_calories.setOnTouchListener(new MyTouchListerner(edtv_calories));


        edtv_sleep = (EditText) view.findViewById(R.id.edtv_sleep);
        edtv_sleepListener = edtv_sleep.getKeyListener();
        edtv_sleep.setKeyListener(null);
        //edtv_sleep.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_sleep));
        edtv_sleep.setOnTouchListener(new MyTouchListerner(edtv_sleep));


        refreshUIData();

    }

    class MyTouchListerner implements View.OnTouchListener
    {
        EditText editText;

        public MyTouchListerner(EditText editText)
        {
            this.editText = editText;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                {


                    if (editText == edtv_steps)
                    {
                        edtv_steps.setKeyListener(edtv_stepsListener);
                    }
                    else if (editText == edtv_miles)
                    {
                        edtv_miles.setKeyListener(edtv_milesListener);
                        edtv_miles.setText(editText.getText().toString().replace("miles", "").trim());

                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(3);
                        edtv_miles.setFilters(filterArray);
                    }
                    else if (editText == edtv_calories)
                    {
                        edtv_calories.setKeyListener(edtv_caloriesListener);
                        edtv_calories.setText(editText.getText().toString()/*.replace("per day", "")*/.trim());

                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        edtv_calories.setFilters(filterArray);
                    }
                    else if (editText == edtv_sleep)
                    {
                        //edtv_sleep.setKeyListener(edtv_sleepListener);
                        // edtv_sleep.setText(editText.getText().toString().replace("hours", "").trim());

                        //InputFilter[] filterArray = new InputFilter[1];
                        //filterArray[0] = new InputFilter.LengthFilter(1);
                        //edtv_sleep.setFilters(filterArray);



//                        int hour = c.get(Calendar.HOUR_OF_DAY);
//                        int minute = c.get(Calendar.MINUTE);

                        new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, MyDailyGoal.this, 7, 0, true).show();
                    }


                    if (editText != edtv_sleep)
                    {
                        editText.requestFocus();


                        Drawable[] drawables = editText.getCompoundDrawables();


//                    MyUtil.myLog("Bitmap1", "---" + RIGHT_ICON_GREEN);
//                    MyUtil.myLog("Bitmap2", "---" + drawables[2]);

                        if (RIGHT_ICON_GREEN == drawables[2])
                        {
//                        MyUtil.myLog("hello", "Hello");
                            UpdateProfile(editText);
                        }
                        else
                        {
                            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, RIGHT_ICON_GREEN, null);

                            MyUtil.showKeyBoard(getActivity(), editText);
                        }
                    }


                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1)
    {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, i);
        c.set(Calendar.MINUTE, i1);

        SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm");

        //SimpleDateFormat mSDF2 = new SimpleDateFormat("HHmm");


        // String alarmTime = mSDF2.format(c.getTime());

        String time = mSDF.format(c.getTime());

        MySharedPreference.getInstance().setDailySleep(context, time);

        saveAndRefresh();

    }


    private void UpdateProfile(EditText editText)
    {
        editText.setKeyListener(null);


        String value = editText.getText().toString().trim();

        if (editText == edtv_steps)
        {
            if (!value.isEmpty())
                MySharedPreference.getInstance().setDailySteps(context, value);

            //edtv_steps.setText(MySharedPreference.getInstance().getDailySteps(context));

        }
        else if (editText == edtv_miles)
        {
            if (!value.isEmpty())
                MySharedPreference.getInstance().setDailyMiles(context, value);

            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(9);
            edtv_miles.setFilters(filterArray);


            //edtv_miles.setText(MySharedPreference.getInstance().getDailyMiles(context));
        }
        else if (editText == edtv_calories)
        {
            if (!value.isEmpty())
                MySharedPreference.getInstance().setDailyCalories(context, value);


            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(4);
            edtv_calories.setFilters(filterArray);

            // edtv_calories.setText(MySharedPreference.getInstance().getDailyCalories(context));
        }
        /*else if (editText == edtv_sleep)
        {
            if (!value.isEmpty())
                MySharedPreference.getInstance().setDailySleep(context, value);

            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(7);
            edtv_sleep.setFilters(filterArray);


            // edtv_sleep.setText(MySharedPreference.getInstance().getDailySleep(context));
        }*/

        saveAndRefresh();

        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, EDIT_ICON, null);

        MyUtil.hideKeyBoard(getActivity());
    }


    public void saveAndRefresh()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstant.STEPS, MySharedPreference.getInstance().getDailySteps(context));
        map.put(MyConstant.DISTANCE, MySharedPreference.getInstance().getDailyMiles(context)/*.replace("miles per day", "")*/.trim());
        map.put(MyConstant.CALORIES, MySharedPreference.getInstance().getDailyCalories(context)/*.replace("per day", "")*/.trim());
        map.put(MyConstant.SLEEP, MySharedPreference.getInstance().getDailySleep(context)/*.replace("hours per day", "")*/.trim());

        myDatabase.addDailyGoalData(map);

        refreshUIData();
    }


    private void refreshUIData()
    {
//        edtv_steps.setText(MySharedPreference.getInstance().getDailySteps(context));
//        edtv_miles.setText(MySharedPreference.getInstance().getDailyMiles(context));
//        edtv_calories.setText(MySharedPreference.getInstance().getDailyCalories(context));
//        edtv_sleep.setText(MySharedPreference.getInstance().getDailySleep(context));

        HashMap<String, String> map = myDatabase.getGoalDataOnDateIfExistsOrNot(myUtil.getTodaydate());

        edtv_steps.setText(map.get(MyConstant.STEPS));
        edtv_miles.setText(map.get(MyConstant.DISTANCE) + " miles");
        edtv_calories.setText(map.get(MyConstant.CALORIES) /*+ " per day"*/);

        String[] st=  map.get(MyConstant.SLEEP).split(":");

        edtv_sleep.setText(String.valueOf(Integer.parseInt(st[0]))+" hours "+String.valueOf(Integer.parseInt(st[1]))+" minutes");


       // MyUtil.myLog(TAG, "Map----" + map);


    }

}
