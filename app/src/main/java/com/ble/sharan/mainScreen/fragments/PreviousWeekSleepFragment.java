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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 1/12/17.
 */

public class PreviousWeekSleepFragment extends Fragment /*implements View.OnClickListener*/
{

    String TAG = MyWeekSleepFragment.class.getSimpleName()
            ;


    ListView listView_weekSleepRecords;

    MyWeekSleepAdapter myWeekSleepAdapter;

    MyDatabase myDatabase;

    CardView cardView_myWeekSleep;

    ImageView imgv_viewGraph;


    Context context;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        myDatabase= new MyDatabase(context);

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_myweeksleep, container, false);

            setUpIds();

        }

        return view;
    }



    private void setUpIds()
    {

        cardView_myWeekSleep = (CardView) view.findViewById(R.id.cardView_myWeekSleep);

        imgv_viewGraph=(ImageView) view.findViewById(R.id.imgv_viewGraph);
        imgv_viewGraph.setVisibility(View.GONE);



        listView_weekSleepRecords=(ListView)view.findViewById(R.id.listView_weekSleepRecords);


        updateUI();
    }



    private void updateUI()
    {
        ArrayList<HashMap<String, String>> list = myDatabase.getAllSleepData();
        if (list.size() > 0)
        {


            cardView_myWeekSleep.setVisibility(View.VISIBLE);
            imgv_viewGraph.setVisibility(View.GONE);




            Collections.sort(list, new Comparator<HashMap<String,String>>() {
                public int compare(HashMap<String,String> o1, HashMap<String,String> o2) {
                    if (o1.get(MyConstant.DATE) == null || o2.get(MyConstant.DATE) == null)
                        return 0;
                    return o2.get(MyConstant.DATE).compareTo(o1.get(MyConstant.DATE));
                }
            });

            myWeekSleepAdapter = new MyWeekSleepAdapter(context, list);

            listView_weekSleepRecords.setAdapter(myWeekSleepAdapter);
        }
        else
        {
            cardView_myWeekSleep.setVisibility(View.GONE);
            imgv_viewGraph.setVisibility(View.GONE);
        }

    }




}
