package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.MyPointsAdapter;
import com.ble.sharan.myUtilities.MyUtil;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class MyPoints extends Fragment
{

    Context context;
    View view;

    ListView listViewPoints;



    int[] images = {R.mipmap.ic_imag1,
            R.mipmap.ic_imag2,
            R.mipmap.ic_imag3,
            R.mipmap.ic_imag4,
            R.mipmap.ic_imag5
    };

    String[] names = {"Victor White", "Rachal Hall", "Donald Lopez", "Paule Bell", "Shania Twain"};

    String[] rank = {"Rank 1", "Rank 2", "Rank 3", "Rank 4", "Rank 5"};

    String[] num_val = {"1920 Pts", "1600 Pts", "1470 Pts", "1366 Pts", "1280 Pts"};



    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_mypoints, container, false);


            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {
        listViewPoints = (ListView)view.findViewById(R.id.listViewPoints);

        MyPointsAdapter myPointsAdapter = new MyPointsAdapter(context, images, names, rank, num_val);
        listViewPoints.setAdapter(myPointsAdapter);
        myUtil.setListViewHeight(listViewPoints);

        listViewPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Intent intent = new Intent(PointsActivity.this, ShoutOutActivity.class);
//                startActivity(intent);
            }
        });


    }


}