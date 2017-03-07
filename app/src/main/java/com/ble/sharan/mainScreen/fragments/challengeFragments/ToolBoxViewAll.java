package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ToolBoxViewAllAdapter;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.ToolBoxModel;
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

public class ToolBoxViewAll extends Fragment
{

    String TAG=ToolBoxViewAll.class.getSimpleName();

    Context context;
    View view;

    ListView listViewToolBox;

    ToolBoxViewAllAdapter toolBoxViewAllAdapter;

    MyUtil myUtil=new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_tool_box_view_all, container, false);


            setUpIds();

//            GET_DATA_FROM_SERVER();
            GET_DATA_FROM_SERVER_RETROFIT();
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

        listViewToolBox=(ListView)view.findViewById(R.id.listViewToolBox);

    }


   /* public void GET_DATA_FROM_SERVER()
    {
        MyUtil.execute(new Super_AsyncTask(context, MyConstant.TOOL_BOX, new Super_AsyncTask_Interface()
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
                            map.put(MyConstant.DESCRIPTION, object1.getString(MyConstant.DESCRIPTION));

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

    private void setData(ArrayList<ToolBoxModel.SubData> toolBoxlList)
    {
        toolBoxViewAllAdapter=new ToolBoxViewAllAdapter(context,toolBoxlList);

        listViewToolBox.setAdapter(toolBoxViewAllAdapter);
    }

    // RETROFIT
    public void GET_DATA_FROM_SERVER_RETROFIT()
    {
        myUtil.showProgressDialog(context);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ToolBoxModel> call = apiService.getToolBoxAllData();

        call.enqueue(new Callback<ToolBoxModel>()
        {
            @Override
            public void onResponse(Call<ToolBoxModel> call, Response<ToolBoxModel> response)
            {
                myUtil.hideProgressDialog();
                Log.e(TAG, "Response----"+response.body());

                ToolBoxModel toolBoxModel = response.body();

                if(toolBoxModel.getStatus().equals(MyConstant.TRUE))
                {
                    setData(toolBoxModel.getToolBoxlList());
                }
            }

            @Override
            public void onFailure(Call<ToolBoxModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                Log.e(TAG, t.getMessage());
//                MyUtil.showToast(context, "Server side error");

            }
        });
    }



}
