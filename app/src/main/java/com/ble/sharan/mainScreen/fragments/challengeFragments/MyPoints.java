package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.MyPointsAdapter;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class MyPoints extends Fragment
{

    Context context;
    View view;


    TextView txtv_total;

    ListView listViewPoints;

    MyPointsAdapter myPointsAdapter;

    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_mypoints, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER();

        }

        return view;
    }

    private void setUpIds()
    {
        listViewPoints = (ListView) view.findViewById(R.id.listViewPoints);

        txtv_total = (TextView) view.findViewById(R.id.txtv_total);


        listViewPoints.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {


            }
        });


    }

    public void GET_DATA_FROM_SERVER()
    {

        MyUtil.execute(new Super_AsyncTask(context, MyConstant.TOP_USERS, new Super_AsyncTask_Interface()
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
                        String total = object.getString(MyConstant.TOTAL_ELEVATION_ACTIONS);

                        List<HashMap> list = new ArrayList<>();

                        JSONArray jsonArray = object.getJSONArray(MyConstant.DATA);
                        ;
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap hashMap = new HashMap();

                            hashMap.put(MyConstant.UID, jsonObject.get(MyConstant.UID));
                            hashMap.put(MyConstant.NAME, jsonObject.get(MyConstant.NAME));
                            hashMap.put(MyConstant.IMAGE, jsonObject.get(MyConstant.IMAGE));
                            hashMap.put(MyConstant.POINTS, jsonObject.get(MyConstant.POINTS));

                            list.add(hashMap);
                        }
                        setData(list, total);
                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, true));


    }

    private void setData(List<HashMap> list, String total)
    {
        myPointsAdapter = new MyPointsAdapter(context, list);
        listViewPoints.setAdapter(myPointsAdapter);
        myUtil.setListViewHeight(listViewPoints);

        txtv_total.setText(total);
    }


}