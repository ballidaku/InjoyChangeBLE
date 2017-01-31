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
import com.ble.sharan.adapters.MyWeekAdapter;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.List;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class MyWeekActivityFragment extends Fragment implements View.OnClickListener
{

    Context context;
    View view;


    ListView listView_weekRecords;

    MyWeekAdapter myWeekAdapter;

    MyDatabase myDatabase;

    CardView cardView_myWeekActivity;
    CardView cardView_noResult;

    ImageView imgv_viewGraph;


    MyUtil myUtil = new MyUtil();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_myweekactivity, container, false);

            myDatabase = new MyDatabase(getActivity());

            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {

        cardView_noResult = (CardView) view.findViewById(R.id.cardView_noResult);
        cardView_myWeekActivity = (CardView) view.findViewById(R.id.cardView_myWeekActivity);


        (imgv_viewGraph = (ImageView) view.findViewById(R.id.imgv_viewGraph)).setOnClickListener(this);


        listView_weekRecords = (ListView) view.findViewById(R.id.listView_weekRecords);

        updateUI();
    }

    private void updateUI()
    {
        List<BeanRecords> list = myDatabase.getAllStepRecords(context);

        if (list.size() > 0)
        {
            cardView_noResult.setVisibility(View.GONE);
            cardView_myWeekActivity.setVisibility(View.VISIBLE);
            imgv_viewGraph.setVisibility(View.VISIBLE);


            myWeekAdapter = new MyWeekAdapter(context, list);

            listView_weekRecords.setAdapter(myWeekAdapter);
        }
        else
        {
            cardView_noResult.setVisibility(View.VISIBLE);
            cardView_myWeekActivity.setVisibility(View.GONE);
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
                args.putString(MyConstant.FROM_WHERE, MyConstant.MY_WEEK_ACTIVITY_FRAGMENT);
                viewGraphFragment.setArguments(args);


                myUtil.switchfragment(MyWeekActivityFragment.this,viewGraphFragment);

                break;
        }
    }
}
