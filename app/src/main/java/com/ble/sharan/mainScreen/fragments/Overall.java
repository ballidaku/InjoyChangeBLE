package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.List;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class Overall extends Fragment
{

    Context context;
    View view;


    TextView txtv_totalSteps;
    TextView txtv_totalKm;
    TextView txtv_totalCalories;

    MyDatabase myDatabase;

    MyUtil myUtil=new MyUtil();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_overall, container, false);

            myDatabase = new MyDatabase(getActivity());

            setUpIds();

        }



        return view;
    }

    private void setUpIds()
    {
        txtv_totalSteps = (TextView) view.findViewById(R.id.txtv_totalSteps);
        txtv_totalKm = (TextView) view.findViewById(R.id.txtv_totalKm);
        txtv_totalCalories = (TextView) view.findViewById(R.id.txtv_totalCalories);

        updateUI();
    }


   long totalSteps=0;


    public void updateUI()
    {

        List<BeanRecords> list = myDatabase.getAllContacts();

        for (int i = 0; i < list.size(); i++)
        {
            Log.e("Data", "----" + list.get(i).getID() + "----" + list.get(i).getDate() + "----" + list.get(i).getSteps());

            totalSteps+=Long.parseLong(list.get(i).getSteps());
        }


        txtv_totalSteps.setText(String.valueOf(totalSteps));
        txtv_totalKm.setText(myUtil.stepsToDistance((int)totalSteps));
        txtv_totalCalories.setText(myUtil.stepsToCalories((int)totalSteps));




    }


}
