package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class HealthData extends Fragment implements View.OnClickListener
{

    String TAG = HealthData.class.getSimpleName();

    FrameLayout frameLayout_today;
    FrameLayout frameLayout_myweek;
    FrameLayout frameLayout_mygoal;
    FrameLayout frameLayout_overall;


    Context context;

    View view;




    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDSTOP = "stop";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    public static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";
    public static final String PAUSE_ACTION = "com.android.music.musicservicecommand.pause";
    public static final String PREVIOUS_ACTION = "com.android.music.musicservicecommand.previous";
    public static final String NEXT_ACTION = "com.android.music.musicservicecommand.next";





    @Override
    public void onSaveInstanceState(Bundle outState)
    {
    }


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

    @Override
    public void onResume()
    {
        super.onResume();
        ((MainActivityNew) getActivity()).setTitleHeader("Health Data");
    }

    private void setUpIds()
    {
        (frameLayout_today = (FrameLayout) view.findViewById(R.id.frameLayout_today)).setOnClickListener(this);
        (frameLayout_myweek = (FrameLayout) view.findViewById(R.id.frameLayout_myweek)).setOnClickListener(this);
        (frameLayout_mygoal = (FrameLayout) view.findViewById(R.id.frameLayout_mygoal)).setOnClickListener(this);
        (frameLayout_overall = (FrameLayout) view.findViewById(R.id.frameLayout_overall)).setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.frameLayout_today:

                ((MainActivityNew)getActivity()).displayView(1);

                break;


            case R.id.frameLayout_myweek:

                ((MainActivityNew)getActivity()).displayView(2);

                break;

            case R.id.frameLayout_mygoal:

                ((MainActivityNew)getActivity()).displayView(3);

                break;

            case R.id.frameLayout_overall:

                ((MainActivityNew)getActivity()).displayView(4);


               // startActivity(new Intent(getActivity(), MusicPlayer.class));
                break;
        }
    }








}
