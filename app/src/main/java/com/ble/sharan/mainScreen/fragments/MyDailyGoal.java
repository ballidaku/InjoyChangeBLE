package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class MyDailyGoal extends Fragment
{

    String TAG = SleepFragment.class.getSimpleName();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_my_daily_goal, container, false);

            setUpIds();

        }

        return view;
    }


    private void setUpIds()
    {


        edtv_steps = (EditText) view.findViewById(R.id.edtv_steps);
        edtv_stepsListener = edtv_steps.getKeyListener();
        edtv_steps.setKeyListener(null);
        edtv_steps.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_steps));
        edtv_steps.setOnTouchListener(new MyTouchListerner(edtv_steps));


        edtv_miles = (EditText) view.findViewById(R.id.edtv_miles);
        edtv_milesListener = edtv_miles.getKeyListener();
        edtv_miles.setKeyListener(null);
        edtv_miles.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_miles));
        edtv_miles.setOnTouchListener(new MyTouchListerner(edtv_miles));


        edtv_calories = (EditText) view.findViewById(R.id.edtv_calories);
        edtv_caloriesListener = edtv_calories.getKeyListener();
        edtv_calories.setKeyListener(null);
        edtv_calories.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_calories));
        edtv_calories.setOnTouchListener(new MyTouchListerner(edtv_calories));


        edtv_sleep = (EditText) view.findViewById(R.id.edtv_sleep);
        edtv_sleepListener = edtv_sleep.getKeyListener();
        edtv_sleep.setKeyListener(null);
        edtv_sleep.addTextChangedListener(new MyUtil.MyTextWatcher(edtv_sleep));
        edtv_sleep.setOnTouchListener(new MyTouchListerner(edtv_sleep));


        refreshUI();

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


            if (editText == edtv_steps)
            {
                edtv_steps.setKeyListener(edtv_stepsListener);
            }
            else if (editText == edtv_miles)
            {
                edtv_miles.setKeyListener(edtv_milesListener);
                edtv_miles.setText(editText.getText().toString().replace("per day", "").trim());
            }
            else if (editText == edtv_calories)
            {
                edtv_calories.setKeyListener(edtv_caloriesListener);
                edtv_calories.setText(editText.getText().toString().replace("per day", "").trim());
            }
            else if (editText == edtv_sleep)
            {
                edtv_sleep.setKeyListener(edtv_sleepListener);
                edtv_sleep.setText(editText.getText().toString().replace("hour per day", "").trim());
            }


            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                {
                    UpdateProfile(editText);

                    return true;
                }
            }
            return false;
        }
    }

    private void UpdateProfile(EditText editText)
    {
        editText.setKeyListener(null);



        String value = editText.getText().toString().trim();


        if (editText == edtv_steps)
        {
            MySharedPreference.getInstance().setDailySteps(context, value);

        }
        else if (editText == edtv_miles)
        {
            MySharedPreference.getInstance().setDailyMiles(context, value);
            edtv_miles.setText(MySharedPreference.getInstance().getDailyMiles(context));
        }
        else if (editText == edtv_calories)
        {
            MySharedPreference.getInstance().setDailyCalories(context, value);
            edtv_calories.setText(MySharedPreference.getInstance().getDailyCalories(context));
        }
        else if (editText == edtv_sleep)
        {
            MySharedPreference.getInstance().setDailySleep(context, value);
            edtv_sleep.setText(MySharedPreference.getInstance().getDailySleep(context));
        }

        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);



        MyUtil.hideKeyBoard(getActivity());
    }


    private void refreshUI()
    {
        edtv_steps.setText(MySharedPreference.getInstance().getDailySteps(context));
        edtv_miles.setText(MySharedPreference.getInstance().getDailyMiles(context));
        edtv_calories.setText(MySharedPreference.getInstance().getDailyCalories(context));
        edtv_sleep.setText(MySharedPreference.getInstance().getDailySleep(context));

        edtv_steps.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        edtv_miles.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        edtv_calories.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);
        edtv_sleep.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_selected, 0);


    }

}
