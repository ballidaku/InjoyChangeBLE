package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.ThemeChanger;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class Today extends Fragment implements View.OnClickListener
{
    public static final String TAG = Today.class.getSimpleName();


    Context context;
    View view;


    TextView txtv_activity;
    TextView txtv_overall;

    FrameLayout framelayout_activity;
    FrameLayout framelayout_sleep;

    Fragment fragment_activity;

    Fragment fragment_overall;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_today, container, false);

            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {
        (txtv_activity = (TextView) view.findViewById(R.id.txtv_activity)).setOnClickListener(this);
        (txtv_overall = (TextView) view.findViewById(R.id.txtv_overall)).setOnClickListener(this);


        framelayout_activity = (FrameLayout) view.findViewById(R.id.framelayout_activity);
        framelayout_sleep = (FrameLayout) view.findViewById(R.id.framelayout_sleep);


        fragment_activity = getChildFragmentManager().findFragmentById(R.id.fragment_activity);
        fragment_overall = getChildFragmentManager().findFragmentById(R.id.fragment_overall);

        changeF("A");

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_activity:

                change(MyConstant.ACTIVITY);
                changeF("A");

                break;


            case R.id.txtv_overall:

                change(MyConstant.OVERALL);
                changeF("O");

                break;

        }
    }


    public void change(String fromWhere)
    {
        txtv_activity.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

        //txtv_activity.setBackgroundResource(R.drawable.left_selector_blue);
        txtv_activity.setBackgroundResource(ThemeChanger.getInstance().getActivityOverallBackground(MyConstant.ACTIVITY));

        txtv_overall.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        //txtv_overall.setBackgroundResource(R.drawable.right_selector_blue);
        txtv_overall.setBackgroundResource(ThemeChanger.getInstance().getActivityOverallBackground(MyConstant.OVERALL));


        if (fromWhere.equals(MyConstant.ACTIVITY))
        {
            txtv_activity.setTextColor(ThemeChanger.getInstance().getThemePrimaryColor(context));
            //txtv_activity.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            txtv_activity.setBackgroundResource(R.drawable.left_selector_white);
        }
        else
        {
            txtv_overall.setTextColor(ThemeChanger.getInstance().getThemePrimaryColor(context));
            //txtv_overall.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            txtv_overall.setBackgroundResource(R.drawable.right_selector_white);
        }
    }


    public void changeF(String value)
    {
        if (value.equals("A"))
        {
            framelayout_activity.setVisibility(View.VISIBLE);
            framelayout_sleep.setVisibility(View.GONE);
        }
        else
        {
            framelayout_sleep.setVisibility(View.VISIBLE);
            framelayout_activity.setVisibility(View.GONE);


            ((Overall) fragment_overall).updateUI();

        }

    }


    // Roots to  child fragment
    public void bleStatus(String BLE_STATUS)
    {

        ((TodayActivityFragment) fragment_activity).bleStatus(BLE_STATUS);

    }

    public void calculate(int data)
    {
        ((TodayActivityFragment) fragment_activity).calculate(data, true);
    }

    public void sleepTime()
    {
        ((TodayActivityFragment) fragment_activity).refreshSleepTextView();
    }


}
