package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class DailyInspiration extends Fragment implements View.OnClickListener
{

    Context context;
    View view;

    ImageView imgv_background;

    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_daily_inspiration, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER();

        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew) getActivity()).setTitleHeader("Daily Inspiration");


    }


    private void setUpIds()
    {
        imgv_background = (ImageView) view.findViewById(R.id.imgv_background);

        view.findViewById(R.id.txtv_seeAll).setOnClickListener(this);

    }

    public void GET_DATA_FROM_SERVER()
    {
        MyUtil.execute(new Super_AsyncTask(context, MyConstant.DAILY_INSPIRATION + "?time=" + myUtil.getCurrentTimeStamp(), new Super_AsyncTask_Interface()
        {
            @Override
            public void onTaskCompleted(String output)
            {
                try
                {
                    JSONObject object = new JSONObject(output);

                    String status = object.getString(MyConstant.STATUS);

                    if (status.equals(MyConstant.TRUE))
                    {
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(MyConstant.IMAGE, object.getString(MyConstant.DATA));

                        setData(map);
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, true));


    }

    private void setData(HashMap<String, String> map)
    {
        myUtil.showImageWithPicasso(context, imgv_background, map.get(MyConstant.IMAGE));
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_seeAll:

                ((MainActivityNew) getActivity()).changeFragment2(new DailyInspirationSeeAll());


                break;
        }
    }


}