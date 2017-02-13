package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ChallengeAdapter;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyUtil;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class Challenge extends Fragment
{

    Context context;
    View view;

    ListView listView_challenge;


    String points[] = {
            "Daily Inspiration",
            "Check-In with Yourself",
            "My Points",
            "Shout Outs and Appreciations",
            "Tool Box",
            "Share A Win & Weekly Video"
    };


    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_challenge, container, false);


            setUpIds();

        }

        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew)getActivity()).setTitleHeader("Challenge");


    }

    private void setUpIds()
    {
        listView_challenge = (ListView) view.findViewById(R.id.listView_challenge);


        ChallengeAdapter challengeAdapter = new ChallengeAdapter(context, points);
        listView_challenge.setAdapter(challengeAdapter);
        myUtil.setListViewHeight(listView_challenge);

        listView_challenge.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {


                ((MainActivityNew) getActivity()).displayView2(i);


            }
        });

    }


}
