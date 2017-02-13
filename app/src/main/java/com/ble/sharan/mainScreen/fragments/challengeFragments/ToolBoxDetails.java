package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by brst-pc93 on 2/10/17.
 */

public class ToolBoxDetails extends Fragment
{

    Context context;
    View view;


    MyUtil myUtil = new MyUtil();


    ImageView imgv_toolBoxDetails;

    TextView txtv_description;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_tool_box_details, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER();

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

        imgv_toolBoxDetails = (ImageView) view.findViewById(R.id.imgv_toolBoxDetails);
        txtv_description = (TextView) view.findViewById(R.id.txtv_description);


    }

    public void GET_DATA_FROM_SERVER()
    {
        MyUtil.execute(new Super_AsyncTask(context, MyConstant.TOOL_BOX + "?time=" + myUtil.getCurrentTimeStamp(), new Super_AsyncTask_Interface()
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

                        map.put(MyConstant.IMAGE, object.getString(MyConstant.IMAGE));
                        map.put(MyConstant.DESCRIPTION, object.getString(MyConstant.DESCRIPTION));

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
        myUtil.showCircularImageWithPicasso(context, imgv_toolBoxDetails, map.get(MyConstant.IMAGE));

        txtv_description.setText(map.get(MyConstant.DESCRIPTION));
    }


}
