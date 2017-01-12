package com.ble.sharan.mainScreen.fragments;

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

/**
 * Created by brst-pc93 on 1/12/17.
 */

public class PreviousWeek extends Fragment implements View.OnClickListener
{

    Context context;
    View view;


    TextView txtv_activity;
    TextView txtv_sleep;


    FrameLayout framelayout_activity;
    FrameLayout framelayout_sleep;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_previous_week, container, false);


            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {
        (txtv_activity = (TextView) view.findViewById(R.id.txtv_activity)).setOnClickListener(this);
        (txtv_sleep = (TextView) view.findViewById(R.id.txtv_sleep)).setOnClickListener(this);


        framelayout_activity = (FrameLayout) view.findViewById(R.id.framelayout_activity);
        framelayout_sleep = (FrameLayout) view.findViewById(R.id.framelayout_sleep);

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


            case R.id.txtv_sleep:

                change(MyConstant.SLEEP);

                changeF("S");


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
        }

    }


}
