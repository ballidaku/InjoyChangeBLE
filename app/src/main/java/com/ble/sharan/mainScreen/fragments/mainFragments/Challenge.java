package com.ble.sharan.mainScreen.fragments.mainFragments;

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
import com.ble.sharan.adapters.ChallengeAdapter;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class Challenge extends Fragment
{

    Context context;
    View view;

    ListView listView_challenge;

    ImageView imgv_user;
    TextView txtv_userName;
    TextView txtv_totalPoints;
    TextView txtv_entry;


    String points[] = {
            "Daily Inspiration",
            "Check-In with Yourself",
            "My Points",
            "Shout Outs and Appreciations",
            "Tool Box",
            "Share A Win & Weekly Video"
    };


    MyUtil myUtil = new MyUtil();





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_challenge, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER();

        }

        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew) getActivity()).setTitleHeader("Challenge");


    }

    private void setUpIds()
    {
        imgv_user = (ImageView) view.findViewById(R.id.imgv_user);
        txtv_userName = (TextView) view.findViewById(R.id.txtv_userName);

        txtv_totalPoints = (TextView) view.findViewById(R.id.txtv_totalPoints);
        txtv_entry = (TextView) view.findViewById(R.id.txtv_entry);

        myUtil.showCircularImageWithPicasso(context, imgv_user, MySharedPreference.getInstance().getPhoto(context));
        txtv_userName.setText(MySharedPreference.getInstance().getName(context));


        listView_challenge = (ListView) view.findViewById(R.id.listView_challenge);


        ChallengeAdapter challengeAdapter = new ChallengeAdapter(context, points);
        listView_challenge.setAdapter(challengeAdapter);
        myUtil.setListViewHeight(listView_challenge);

        listView_challenge.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {


                ((MainActivityNew) getActivity()).displayView2(i);


            }
        });

    }


    public void GET_DATA_FROM_SERVER()
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


}
