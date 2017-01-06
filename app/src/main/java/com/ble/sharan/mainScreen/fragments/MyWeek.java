package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.MyWeekAdapter;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;

import java.util.List;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class MyWeek extends Fragment implements View.OnClickListener
{

    Context context;
    View view;


    TextView txtv_activity;
    TextView txtv_sleep;


    ListView listView_weekRecords;

    MyWeekAdapter myWeekAdapter;

    MyDatabase myDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_myweek, container, false);

            myDatabase = new MyDatabase(getActivity());

            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {
        (txtv_activity = (TextView) view.findViewById(R.id.txtv_activity)).setOnClickListener(this);
        (txtv_sleep = (TextView) view.findViewById(R.id.txtv_sleep)).setOnClickListener(this);

        listView_weekRecords=(ListView)view.findViewById(R.id.listView_weekRecords);


        updateUI();
    }

    private void updateUI()
    {
        List<BeanRecords> list = myDatabase.getAllContacts();

        myWeekAdapter=new MyWeekAdapter(context,list);

        listView_weekRecords.setAdapter(myWeekAdapter);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_activity:

                change(MyConstant.ACTIVITY);

                break;


            case R.id.txtv_sleep:

                change(MyConstant.SLEEP);

                break;

        }
    }


    public void change(String fromWhere)
    {
        txtv_activity.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        txtv_activity.setBackgroundResource(R.drawable.left_selector_blue);

        txtv_sleep.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        txtv_sleep.setBackgroundResource(R.drawable.right_selector_blue);


        if(fromWhere.equals(MyConstant.ACTIVITY))
        {
            txtv_activity.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
            txtv_activity.setBackgroundResource(R.drawable.left_selector_white);
        }
        else
        {
            txtv_sleep.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
            txtv_sleep.setBackgroundResource(R.drawable.right_selector_white);
        }


    }


}
