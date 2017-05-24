package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ShareWinEndlessAdapter;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.ShareWinSeeAllModel;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.EndlessListView;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brst-pc93 on 2/9/17.
 */

public class ShareWinSeeAll extends Fragment implements EndlessListView.EndlessListener,SwipeRefreshLayout.OnRefreshListener
{

    String TAG=ShareWinSeeAll.class.getSimpleName();

    Context context;
    View view;


    MyUtil myUtil = new MyUtil();

    EndlessListView endLessListViewShareWin;

    SwipeRefreshLayout swipeRefreshLayout;


    ShareWinEndlessAdapter shareWinEndlessAdapter;

    boolean showProgressBar = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {

            view = inflater.inflate(R.layout.fragment_share_win_see_all, container, false);


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

        ((MainActivityNew)getActivity()).setTitleHeader("Share a Win");


    }

    private void setUpIds()
    {

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        endLessListViewShareWin = (EndlessListView) view.findViewById(R.id.endLessListViewShareWin);
        endLessListViewShareWin.setLoadingView(R.layout.loading_layout);
    }


   /* public void GET_DATA_FROM_SERVER()
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
        }, showProgressBar));


    }*/

    int val = 0;

    public void setData(List<ShareWinSeeAllModel.SubData> list)
    {

        if(swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }


        if (val == 0)
        {
            shareWinEndlessAdapter = new ShareWinEndlessAdapter(context, list, R.layout.custom_share_win_list);
            endLessListViewShareWin.setShareWinAdapter(shareWinEndlessAdapter);
            endLessListViewShareWin.setListener(this);
        }
        else
        {
            endLessListViewShareWin.addShareWinNewData(list);
        }


    }


    @Override
    public void loadData()
    {
        showProgressBar = true;

        val = val + 10;

        MyUtil.myLog("VAL", "" + val);

//        GET_DATA_FROM_SERVER();
        GET_DATA_FROM_SERVER_RETROFIT();

    }

    @Override
    public void onRefresh()
    {
        showProgressBar = false;

        val=0;
        endLessListViewShareWin.isLoading=false;
//        GET_DATA_FROM_SERVER();
        GET_DATA_FROM_SERVER_RETROFIT();
    }



    // RETROFIT
    public void GET_DATA_FROM_SERVER_RETROFIT()
    {

        if(showProgressBar)
        {
            myUtil.showProgressDialog(context);
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ShareWinSeeAllModel> call = apiService.getShareWinSeeAllData(val);

        call.enqueue(new Callback<ShareWinSeeAllModel>()
        {
            @Override
            public void onResponse(Call<ShareWinSeeAllModel> call, Response<ShareWinSeeAllModel> response)
            {

                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, "Response----" + response.body());

                ShareWinSeeAllModel shareWinSeeAllModel = response.body();

                if (shareWinSeeAllModel.getStatus().equals(MyConstant.TRUE))
                {
                    setData(shareWinSeeAllModel.getShareWinList());

                }
                else
                {
                    endLessListViewShareWin.removeFooter();
                    MyUtil.showToast(context,"No more data");
                }
            }

            @Override
            public void onFailure(Call<ShareWinSeeAllModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, t.getMessage());
//                MyUtil.showToast(context, "Server side error");

            }
        });
    }


}
