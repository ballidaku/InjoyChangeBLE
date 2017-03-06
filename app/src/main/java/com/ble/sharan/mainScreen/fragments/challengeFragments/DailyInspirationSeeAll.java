package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.DailyInspirationSeeAllAdapter;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.DailyInspirationViewAllModel;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brst-pc93 on 2/10/17.
 */

public class DailyInspirationSeeAll extends Fragment
{

    String TAG=DailyInspiration.class.getSimpleName();

    Context context;
    View view;

    GridView gridViewDailyInspiration;

    DailyInspirationSeeAllAdapter dailyInspirationSeeAllAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_daily_inspiration_see_all, container, false);


            setUpIds();

            //GET_DATA_FROM_SERVER();
            GET_DATA_FROM_SERVER_RETROFIT();

        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew)getActivity()).setTitleHeader("Daily Inspiration");


    }

    private void setUpIds()
    {
        gridViewDailyInspiration=(GridView)view.findViewById(R.id.gridViewDailyInspiration);
    }

 /*   public void GET_DATA_FROM_SERVER()
    {
        MyUtil.execute(new Super_AsyncTask(context, MyConstant.DAILY_INSPIRATION, new Super_AsyncTask_Interface()
        {
            @Override
            public void onTaskCompleted(String output)
            {
                try
                {
                    ArrayList<HashMap<String, String>> list = new ArrayList<>();

                    JSONObject object = new JSONObject(output);

                    String status = object.getString(MyConstant.STATUS);

                    if (status.equals(MyConstant.TRUE))
                    {

                        JSONArray jsonArray = object.getJSONArray(MyConstant.DATA);


                        for (int i = 0; i < jsonArray.length() ; i++)
                        {

                            JSONObject object1=jsonArray.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(MyConstant.IMAGE, object1.getString(MyConstant.IMAGE));

                            list.add(map);
                        }

                        setData(list);
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, true));


    }*/

    private void setData(ArrayList<DailyInspirationViewAllModel.Image> list)
    {
        dailyInspirationSeeAllAdapter=new DailyInspirationSeeAllAdapter(context,list);

        gridViewDailyInspiration.setAdapter(dailyInspirationSeeAllAdapter);
    }



    // RETROFIT
    public void GET_DATA_FROM_SERVER_RETROFIT()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<DailyInspirationViewAllModel> call = apiService.getDailyInspirationViewAll();

        call.enqueue(new Callback<DailyInspirationViewAllModel>()
        {
            @Override
            public void onResponse(Call<DailyInspirationViewAllModel> call, Response<DailyInspirationViewAllModel> response)
            {
                Log.e(TAG, "Response----"+response.body());

                DailyInspirationViewAllModel dailyInspirationViewAllModel = response.body();

                if(dailyInspirationViewAllModel.getStatus().equals(MyConstant.TRUE))
                {

                    ArrayList<DailyInspirationViewAllModel.Image> list= dailyInspirationViewAllModel.getDailyInspirationViewallList();

                   /* for (int i = 0; i < list.size(); i++)
                    {
                        Log.e(TAG, ""+list.get(i).getImage());
                    }*/

                    setData(list);

                }

            }

            @Override
            public void onFailure(Call<DailyInspirationViewAllModel> call, Throwable t)
            {
                Log.e(TAG, t.getMessage());
                MyUtil.showToast(context, "Server side error");

            }
        });
    }

}
