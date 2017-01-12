package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.MyWeekAdapter;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyDatabase;

import java.util.Collections;
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
        listView_weekRecords=(ListView)view.findViewById(R.id.listView_weekRecords);

        updateUI();
    }

    private void updateUI()
    {
        List<BeanRecords> list = myDatabase.getAllContacts();

        Collections.reverse(list);

        myWeekAdapter=new MyWeekAdapter(context,list);

        listView_weekRecords.setAdapter(myWeekAdapter);

    }
}
