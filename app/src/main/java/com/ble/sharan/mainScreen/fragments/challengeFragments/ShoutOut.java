package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.EndlessAdapter;
import com.ble.sharan.myUtilities.EndlessListView;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class ShoutOut extends Fragment implements EndlessListView.EndlessListener
{

    Context context;
    View view;

    MyUtil myUtil = new MyUtil();

    EndlessListView endLesslistViewShoutOut;

    List<HashMap> list;

    ImageView check1_iv, check2_iv, check3_iv;


    int val = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_shoutout, container, false);


            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {

        endLesslistViewShoutOut = (EndlessListView) view.findViewById(R.id.endLesslistViewShoutOut);



        endLesslistViewShoutOut.setLoadingView(R.layout.loading_layout);

        EndlessAdapter endlessAdapter = new EndlessAdapter(context, createlist(list, val), R.layout.custom_shoutout_list);

        endLesslistViewShoutOut.setAdapter(endlessAdapter);
        endLesslistViewShoutOut.setListener(this);


        check1_iv = (ImageView)view. findViewById(R.id.check1_iv);

        check2_iv = (ImageView)view. findViewById(R.id.check2_iv);

        check3_iv = (ImageView)view. findViewById(R.id.check3_iv);



        check1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check1_iv.setImageResource(R.mipmap.ic_check);
            }
        });

        check2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check2_iv.setImageResource(R.mipmap.ic_check);
            }
        });

        check3_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check3_iv.setImageResource(R.mipmap.ic_check);
            }
        });


    }


    private List<HashMap> createlist(List<HashMap> list, int val)
    {

        Log.d("val", val + "");
        list = new ArrayList<HashMap>();
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        try
        {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://hydraflow.injoyglobal.com/api/shout_out?scroll_id=" + val);
            Log.d("api", val + " " + "http://hydraflow.injoyglobal.com/api/shout_out?scroll_id=" + val);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String str = EntityUtils.toString(httpResponse.getEntity());

            if (str != null)

            {
                try
                {

                    JSONArray jsonArray = new JSONArray(str);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap hashMap = new HashMap();
                        hashMap.put(MyConstant.NAME, jsonObject.get("name"));
                        hashMap.put("image", jsonObject.get("image"));

                        list.add(hashMap);

                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;
    }


    @Override
    public void loadData()
    {

        FakeNetLoader fl = new FakeNetLoader();

        fl.execute();
    }

    private class FakeNetLoader extends AsyncTask<String, Void, List<HashMap>>
    {
        @Override
        protected List<HashMap> doInBackground(String... strings)
        {
            val = val + 10;
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            return createlist(list, val);
        }

        @Override
        protected void onPostExecute(List<HashMap> been)
        {
            super.onPostExecute(been);
            endLesslistViewShoutOut.addNewData(been);
        }
    }


}