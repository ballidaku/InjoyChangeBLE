package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.CheckInAdapter;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.HashMap;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class CheckIn  extends Fragment
{

    Context context;
    View view;

    MyUtil myUtil = new MyUtil();

    ListView listViewCheckIn;


    int images[] = {R.mipmap.ic_steps_small,
            R.mipmap.ic_calories_small,
            R.mipmap.ic_km_miles_small,
            R.mipmap.ic_sleep_small
    };

    String stringvalue[] = {"STEPS", "CALORIES", "MILES", "SLEEP"};

//    String num_value[] = {"5800", "2240", "2.1", "4:00h"};


    MyDatabase myDatabase;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_checkin, container, false);


            myDatabase=new MyDatabase(context);
            setUpIds();

        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew)getActivity()).setTitleHeader("Check-In with Yourself");


    }

    private void setUpIds()
    {
        int steps =myDatabase.getTodaySteps(context);

        HashMap<String,String>map = new HashMap<>();
        map.put(MyConstant.STEPS,  String.valueOf(steps));
        map.put(MyConstant.CALORIES,  myUtil.stepsToCalories(context, steps));
        map.put(MyConstant.DISTANCE,  myUtil.stepsToDistance(context, steps));
        map.put(MyConstant.SLEEP,  myUtil.getSleepHr(context,myDatabase));





        listViewCheckIn = (ListView)view.findViewById(R.id.listViewCheckIn);

        CheckInAdapter checkInAdapter = new CheckInAdapter(context, images, stringvalue, map);
        listViewCheckIn.setAdapter(checkInAdapter);

        myUtil.setListViewHeight(listViewCheckIn);


    }


}