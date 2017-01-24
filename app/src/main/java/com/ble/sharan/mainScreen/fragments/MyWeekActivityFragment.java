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
import com.ble.sharan.myUtilities.MyDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class MyWeekActivityFragment extends Fragment
{

    Context context;
    View view;


    ListView listView_weekRecords;

    MyWeekAdapter myWeekAdapter;

    MyDatabase myDatabase;

    CardView cardView_myWeekActivity;

    ImageView imgv_viewGraph;

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

        cardView_myWeekActivity=(CardView) view.findViewById(R.id.cardView_myWeekActivity);

        imgv_viewGraph=(ImageView) view.findViewById(R.id.imgv_viewGraph);

        imgv_viewGraph.setVisibility(View.GONE);

        listView_weekRecords=(ListView)view.findViewById(R.id.listView_weekRecords);

        updateUI();
    }

    private void updateUI()
    {
        List<BeanRecords> list = myDatabase.getAllContacts();

        if(list.size()>0)
        {
            cardView_myWeekActivity.setVisibility(View.VISIBLE);
//            imgv_viewGraph.setVisibility(View.VISIBLE);

//            Collections.reverse(list);

            Collections.sort(list, new Comparator<BeanRecords>() {
                public int compare(BeanRecords o1, BeanRecords o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o2.getDate().compareTo(o1.getDate());
                }
            });



            myWeekAdapter = new MyWeekAdapter(context, list);

            listView_weekRecords.setAdapter(myWeekAdapter);
        }
        else
        {
            cardView_myWeekActivity.setVisibility(View.GONE);
            imgv_viewGraph.setVisibility(View.GONE);
        }

    }
}
