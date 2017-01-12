package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ble.sharan.R;

/**
 * Created by brst-pc93 on 1/2/17.
 */

public class SleepFragmentTesting extends Fragment implements View.OnClickListener
{

    String TAG = SleepFragmentTesting.class.getSimpleName();


    Button btn_refresh;

    TextView txtv_sleepTime;

    Context context;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_sleep_testing, container, false);

            setUpIds();

        }

        return view;
    }



    private void setUpIds()
    {

        txtv_sleepTime = (TextView) view.findViewById(R.id.txtv_sleepTime);
        // (txtv_date = (TextView) view.findViewById(R.id.txtv_date)).setOnClickListener(this);

        (btn_refresh = (Button) view.findViewById(R.id.btn_refresh)).setOnClickListener(this);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_refresh:

                break;
        }
    }

}
