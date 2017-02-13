package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ShareWinEndlessAdapter;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.EndlessListView;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 2/9/17.
 */

public class ShareWinSeeAll extends Fragment implements EndlessListView.EndlessListener
{

    Context context;
    View view;


    MyUtil myUtil = new MyUtil();

    EndlessListView endLessListViewShareWin;


    ShareWinEndlessAdapter shareWinEndlessAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {

            view = inflater.inflate(R.layout.fragment_share_win_see_all, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER();


        }

        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew)getActivity()).setTitleHeader("Share a Win");


    }

    private void setUpIds()
    {
        endLessListViewShareWin = (EndlessListView) view.findViewById(R.id.endLessListViewShareWin);
        endLessListViewShareWin.setLoadingView(R.layout.loading_layout);
    }


    public void GET_DATA_FROM_SERVER()
    {


        MyUtil.execute(new Super_AsyncTask(context, MyConstant.SHARE_WIN_SEE_ALL_API+val, new Super_AsyncTask_Interface()
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
                        ArrayList<HashMap> list = new ArrayList<>();


                        JSONArray jsonArrayShareWin = object.getJSONArray(MyConstant.DATA);

                        for (int i = 0; i < jsonArrayShareWin.length(); i++)
                        {
                            JSONObject jsonObject = jsonArrayShareWin.getJSONObject(i);
                            HashMap<String, String> hashMap = new HashMap<String, String>();

                            hashMap.put(MyConstant.UID, jsonObject.getString(MyConstant.UID));
                            hashMap.put(MyConstant.COMMENT, jsonObject.getString(MyConstant.COMMENT));
                            hashMap.put(MyConstant.NAME, jsonObject.getString(MyConstant.NAME));
                            hashMap.put(MyConstant.IMAGE, jsonObject.getString(MyConstant.IMAGE));
                            hashMap.put(MyConstant.DATE, jsonObject.getString(MyConstant.DATE));

                            list.add(hashMap);
                        }




                        setData(list);
                    }
                    else
                    {
                        endLessListViewShareWin.removeFooter();
                        MyUtil.showToast(context,"No more data");
                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, true));


    }

    int val = 0;

    public void setData(List<HashMap> list)
    {


        if (val == 0)
        {
            shareWinEndlessAdapter = new ShareWinEndlessAdapter(context, list, R.layout.custom_share_win_list);
            endLessListViewShareWin.setAdapter(shareWinEndlessAdapter);
            endLessListViewShareWin.setListener(this);
        }
        else
        {
            endLessListViewShareWin.addNewData(list);
        }


    }


    @Override
    public void loadData()
    {

        val = val + 10;

        Log.e("VAL", "" + val);

        GET_DATA_FROM_SERVER();

    }


}
