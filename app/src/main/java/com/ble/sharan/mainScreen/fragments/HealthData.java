package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class HealthData extends Fragment implements View.OnClickListener
{

    String TAG = SleepFragment.class.getSimpleName();

    CardView cardv_today;
    CardView cardv_myweek;
    CardView cardv_mygoal;
    CardView cardv_overall;

    Context context;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_health_data, container, false);

            setUpIds();

        }

        return view;
    }


    private void setUpIds()
    {
        (cardv_today = (CardView) view.findViewById(R.id.cardv_today)).setOnClickListener(this);
        (cardv_myweek = (CardView) view.findViewById(R.id.cardv_myweek)).setOnClickListener(this);
        (cardv_mygoal = (CardView) view.findViewById(R.id.cardv_mygoal)).setOnClickListener(this);
        (cardv_overall = (CardView) view.findViewById(R.id.cardv_overall)).setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.cardv_today:

                ((MainActivityNew)getActivity()).displayView(2);

                break;


            case R.id.cardv_myweek:

                ((MainActivityNew)getActivity()).displayView(3);

                break;

            case R.id.cardv_mygoal:

                ((MainActivityNew)getActivity()).displayView(4);

                break;

            case R.id.cardv_overall:

                ((MainActivityNew)getActivity()).displayView(5);

                break;
        }
    }

}
