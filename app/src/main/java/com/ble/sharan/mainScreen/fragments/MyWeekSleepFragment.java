package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.MyWeekSleepAdapter;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class MyWeekSleepFragment extends Fragment implements View.OnClickListener
{

    String TAG = MyWeekSleepFragment.class.getSimpleName();


    ListView listView_weekSleepRecords;

    MyWeekSleepAdapter myWeekSleepAdapter;

    MyDatabase myDatabase;


    CardView cardView_myWeekSleep;
    CardView cardView_noResult;

    ImageView imgv_viewGraph;

    Context context;

    View view;

    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        myDatabase = new MyDatabase(context);

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_myweeksleep, container, false);

            setUpIds();

        }

        return view;
    }


    private void setUpIds()
    {
        cardView_noResult = (CardView) view.findViewById(R.id.cardView_noResult);
        cardView_myWeekSleep = (CardView) view.findViewById(R.id.cardView_myWeekSleep);

        (imgv_viewGraph = (ImageView) view.findViewById(R.id.imgv_viewGraph)).setOnClickListener(this);


        listView_weekSleepRecords = (ListView) view.findViewById(R.id.listView_weekSleepRecords);




        updateUI();
    }


    private void updateUI()
    {
        ArrayList<HashMap<String, String>> list = myDatabase.getAllSleepData(context);
        if (list.size() > 0)
        {
            cardView_noResult.setVisibility(View.GONE);
            cardView_myWeekSleep.setVisibility(View.VISIBLE);
            imgv_viewGraph.setVisibility(View.VISIBLE);

            myWeekSleepAdapter = new MyWeekSleepAdapter(context, list);

            listView_weekSleepRecords.setAdapter(myWeekSleepAdapter);
        }
        else
        {
            cardView_noResult.setVisibility(View.VISIBLE);
            cardView_myWeekSleep.setVisibility(View.GONE);
            imgv_viewGraph.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgv_viewGraph:

                ViewGraphFragment viewGraphFragment = new ViewGraphFragment ();
                Bundle args = new Bundle();
                args.putString(MyConstant.FROM_WHERE, MyConstant.MY_WEEK_SLEEP_FRAGMENT);
                viewGraphFragment.setArguments(args);

                myUtil.switchfragment(MyWeekSleepFragment.this, viewGraphFragment);

                break;
        }
    }


}
