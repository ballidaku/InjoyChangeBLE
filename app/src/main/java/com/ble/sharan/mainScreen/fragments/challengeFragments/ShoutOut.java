package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ShoutOutEndlessAdapter;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.EndlessListView;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MySharedPreference;
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

public class ShoutOut extends Fragment implements EndlessListView.EndlessListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener
{

    Context context;
    View view;

    MyUtil myUtil = new MyUtil();

    EndlessListView endLesslistViewShoutOut;


    SwipeRefreshLayout swipeRefreshLayout;


    ImageView check1_iv, check2_iv, check3_iv;

    FrameLayout frameLayoutSubmit;


    ShoutOutEndlessAdapter shoutOutEndlessAdapter;

    ImageView imgv_user;

    EditText editTextComment;

    int val = 0;

    boolean showProgressBar = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_shoutout, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER();

        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew) getActivity()).setTitleHeader("Shout Outs and Appreciations");


    }


    private void setUpIds()
    {
        imgv_user = (ImageView) view.findViewById(R.id.imgv_user);
        editTextComment = (EditText) view.findViewById(R.id.editTextComment);

        (frameLayoutSubmit=(FrameLayout) view.findViewById(R.id.frameLayoutSubmit)).setOnClickListener(this);


        myUtil.showCircularImageWithPicasso(context, imgv_user, MySharedPreference.getInstance().getPhoto(context));


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        endLesslistViewShoutOut = (EndlessListView) view.findViewById(R.id.endLesslistViewShoutOut);


        endLesslistViewShoutOut.setLoadingView(R.layout.loading_layout);


        check1_iv = (ImageView) view.findViewById(R.id.check1_iv);

        check2_iv = (ImageView) view.findViewById(R.id.check2_iv);

        check3_iv = (ImageView) view.findViewById(R.id.check3_iv);


//        check1_iv.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                check1_iv.setImageResource(R.mipmap.ic_check);
//            }
//        });
//
//        check2_iv.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                check2_iv.setImageResource(R.mipmap.ic_check);
//            }
//        });
//
//        check3_iv.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                check3_iv.setImageResource(R.mipmap.ic_check);
//            }
//        });


    }

    public void GET_DATA_FROM_SERVER()
    {


        MyUtil.execute(new Super_AsyncTask(context, MyConstant.SHOUT_OUT_USERS + val, new Super_AsyncTask_Interface()
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
                        List<HashMap> list = new ArrayList<>();

                        JSONArray jsonArray = object.getJSONArray(MyConstant.DATA);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap hashMap = new HashMap();

                            hashMap.put(MyConstant.ID, jsonObject.get(MyConstant.ID));
                            hashMap.put(MyConstant.UID, jsonObject.get(MyConstant.UID));
                            hashMap.put(MyConstant.POP_UP_COMMENT, jsonObject.get(MyConstant.POP_UP_COMMENT));
                            hashMap.put(MyConstant.OUTER_COMMENT, jsonObject.get(MyConstant.OUTER_COMMENT));
                            hashMap.put(MyConstant.NAME, jsonObject.get(MyConstant.NAME));
                            hashMap.put(MyConstant.IMAGE, jsonObject.get(MyConstant.IMAGE));
                            hashMap.put(MyConstant.DATE, jsonObject.get(MyConstant.DATE));
                            hashMap.put(MyConstant.TIME, jsonObject.get(MyConstant.TIME));
                            hashMap.put(MyConstant.LIKES, jsonObject.get(MyConstant.LIKES));

                            list.add(hashMap);

                        }
                        setData(list);
                    }
                    else
                    {
                        endLesslistViewShoutOut.removeFooter();
                        MyUtil.showToast(context, "No more data");
                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, showProgressBar));


    }

    public void setData(List<HashMap> list)
    {
        //Log.e("Refreshing",""+swipeRefreshLayout.isRefreshing());
        if (swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }


        if (val == 0)
        {
            shoutOutEndlessAdapter = new ShoutOutEndlessAdapter(context, list, R.layout.custom_shoutout_list);
            endLesslistViewShoutOut.setAdapter(shoutOutEndlessAdapter);
            endLesslistViewShoutOut.setListener(this);
        }
        else
        {
            endLesslistViewShoutOut.addNewData(list);
        }


    }


    @Override
    public void loadData()
    {
        showProgressBar = true;

        val = val + 10;

        Log.e("VAL", "" + val);

        GET_DATA_FROM_SERVER();

    }


    @Override
    public void onRefresh()
    {
        showProgressBar = false;
        val = 0;
        endLesslistViewShoutOut.isLoading = false;
        GET_DATA_FROM_SERVER();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.frameLayoutSubmit:

                checkHit();

                break;
        }
    }

    private void checkHit()
    {
        String comment = editTextComment.getText().toString().trim();

        if (comment.isEmpty())
        {
            MyUtil.showToast(context, "Please enter comment");
        }
        else
        {
            SEND_COMMENT_TO_SERVER(comment);
        }
    }


    public void SEND_COMMENT_TO_SERVER(final String comment)
    {
        String url = MyConstant.SHOUT_OUT_COMMENT + comment + "&uid=" + MySharedPreference.getInstance().getUID(context);
        Log.e("URL", url);
        MyUtil.execute(new Super_AsyncTask(context, url, new Super_AsyncTask_Interface()
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
                       int count = object.getInt(MyConstant.COUNT);

                        if(count>0)
                        {
                            if(count == 1)
                            {
                                check1_iv.setImageResource(R.mipmap.ic_check);
                            }
                            else if(count == 2)
                            {
                                check1_iv.setImageResource(R.mipmap.ic_check);
                                check2_iv.setImageResource(R.mipmap.ic_check);
                            }
                            else
                            {
                                check1_iv.setImageResource(R.mipmap.ic_check);
                                check2_iv.setImageResource(R.mipmap.ic_check);
                                check3_iv.setImageResource(R.mipmap.ic_check);

                                frameLayoutSubmit.setEnabled(false);
                            }
                        }

                        editTextComment.setText("");
                        GET_DATA_FROM_SERVER();
                    }
//                    else
//                    {
//                        endLesslistViewShoutOut.removeFooter();
//                        MyUtil.showToast(context, "No more data");
//                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, true));
    }
}