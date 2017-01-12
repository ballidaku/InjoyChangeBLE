package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ble.sharan.R;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class TodaySleepFragment   extends Fragment implements View.OnClickListener
{
    public static final String TAG = TodaySleepFragment.class.getSimpleName();


    Context context;
    View view;


    ImageView imgv_refresh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_today_sleep, container, false);

            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {
        (imgv_refresh = (ImageView) view.findViewById(R.id.imgv_refresh)).setOnClickListener(this);




    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgv_refresh:


                break;


        }
    }









}
