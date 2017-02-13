package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyUtil;

/**
 * Created by brst-pc93 on 2/10/17.
 */

public class ToolBox extends Fragment implements View.OnClickListener
{

    Context context;
    View view;


    MyUtil myUtil = new MyUtil();


    ImageView imgv_running;

    String currentDay = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_tool_box, container, false);


            setUpIds();

            currentDay = myUtil.getCurrentDay();


        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew) getActivity()).setTitleHeader("Tool Box");


    }


    private void setUpIds()
    {

        imgv_running = (ImageView) view.findViewById(R.id.imgv_running);

        view.findViewById(R.id.cardViewMonday).setOnClickListener(this);
        view.findViewById(R.id.cardViewWednesday).setOnClickListener(this);
        view.findViewById(R.id.cardViewFriday).setOnClickListener(this);
        view.findViewById(R.id.txtv_viewAll).setOnClickListener(this);


        myUtil.showCircularImageWithPicasso(context, imgv_running, R.drawable.ic_running);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.cardViewMonday:

                if (currentDay.equals("Monday") || currentDay.equals("Tuesday"))
                {
                    hit();
                }

                break;


            case R.id.cardViewWednesday:

                if (currentDay.equals("Wednesday") || currentDay.equals("Thursday"))
                {
                    hit();
                }

                break;


            case R.id.cardViewFriday:

                if (currentDay.equals("Friday") || currentDay.equals("Saturday") || currentDay.equals("Sunday"))
                {
                    hit();
                }

                break;


            case R.id.txtv_viewAll:

                ((MainActivityNew) getActivity()).changeFragment2(new ToolBoxViewAll());


                break;
        }
    }


    public void hit()
    {
        ((MainActivityNew) getActivity()).changeFragment2(new ToolBoxDetails());
    }


}