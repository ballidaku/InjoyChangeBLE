package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyUtil;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class DailyInspiration  extends Fragment
{

    Context context;
    View view;



    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_daily_inspiration, container, false);


            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {


    }


}