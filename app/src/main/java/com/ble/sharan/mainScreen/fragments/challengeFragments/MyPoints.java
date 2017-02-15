package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.MyPointsAdapter;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class MyPoints extends Fragment
{

    Context context;
    View view;

    ImageView imgv_user;
    TextView txtv_userName;
    TextView txtv_total;
    TextView txtv_totalPoints;
    TextView txtv_entry;

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


            GET_USER_POINTS_DATA_FROM_SERVER();
            GET_DATA_FROM_SERVER();

        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew) getActivity()).setTitleHeader("My Points");


    }


    private void setUpIds()
    {

        imgv_user=(ImageView)view.findViewById(R.id.imgv_user);
        txtv_userName=(TextView) view.findViewById(R.id.txtv_userName);

        myUtil.showCircularImageWithPicasso(context,imgv_user, MySharedPreference.getInstance().getPhoto(context));
        txtv_userName.setText(MySharedPreference.getInstance().getName(context));

        txtv_totalPoints = (TextView) view.findViewById(R.id.txtv_totalPoints);
        txtv_entry = (TextView) view.findViewById(R.id.txtv_entry);

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
                            hashMap.put(MyConstant.RANK, jsonObject.get(MyConstant.RANK));

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



    public void GET_USER_POINTS_DATA_FROM_SERVER()
    {


        MyUtil.execute(new Super_AsyncTask(context, MyConstant.USER_POINTS + MySharedPreference.getInstance().getUID(context), new Super_AsyncTask_Interface()
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
                        String totalPoints = object.getString(MyConstant.TOTAL_POINTS);
                        String raffleTicket = object.getString(MyConstant.RAFFLE_TICKET);

                        txtv_totalPoints.setText(totalPoints);
                        txtv_entry.setText(raffleTicket +" ENTRY");


                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, false));


    }

    private void setData(List<HashMap> list, String total)
    {

        Collections.sort(list, new Comparator<HashMap>()
        {
            public int compare(HashMap o1, HashMap o2)
            {
                if (o1.get(MyConstant.RANK) == null || o2.get(MyConstant.RANK) == null)
                    return 0;
                return o1.get(MyConstant.RANK).toString().compareTo(o2.get(MyConstant.RANK).toString());
            }
        });


        myPointsAdapter = new MyPointsAdapter(context, list);
        listViewPoints.setAdapter(myPointsAdapter);
        myUtil.setListViewHeight(listViewPoints);

        txtv_total.setText(total);
    }


}