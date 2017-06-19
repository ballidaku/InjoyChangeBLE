package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.DataModel;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class DailyInspiration extends Fragment implements View.OnClickListener
{

    String TAG = DailyInspiration.class.getSimpleName();

    Context context;
    View view;

    ImageView check1_iv;

    MyUtil myUtil = new MyUtil();

    String imageUrl = "";

    int count = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_daily_inspiration, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER_RETROFIT();
        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew) getActivity()).setTitleHeader("Daily Inspiration");


    }


    private void setUpIds()
    {
        check1_iv = (ImageView) view.findViewById(R.id.check1_iv);

        view.findViewById(R.id.txtv_seeAll).setOnClickListener(this);
        view.findViewById(R.id.cardViewReadNow).setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_seeAll:

                ((MainActivityNew) getActivity()).changeFragment2(new DailyInspirationSeeAll());

                break;

            case R.id.cardViewReadNow:

                if (!imageUrl.isEmpty())
                {
                    MyDialogs.getInstance().showDailyInspirationDialog(context, "DailyInspiration", imageUrl, count, submitPointsClickListener);
                }

                break;
        }
    }


    View.OnClickListener submitPointsClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            MyDialogs.getInstance().dialog.dismiss();
            SUBMIT_POINTS_TO_SERVER_RETROFIT();
        }
    };


    // RETROFIT
    public void GET_DATA_FROM_SERVER_RETROFIT()
    {

        myUtil.showProgressDialog(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<DataModel> call = apiService.getDailyInspiration(myUtil.getCurrentTimeStamp(), MySharedPreference.getInstance().getUID(context));

        call.enqueue(new Callback<DataModel>()
        {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response)
            {
                myUtil.hideProgressDialog();

                MyUtil.myLog(TAG, "Response----" + response.body());

                DataModel dataModel = response.body();

                if (dataModel.getStatus().equals(MyConstant.TRUE))
                {

                    imageUrl = dataModel.getData();
                    count = dataModel.getCount();

                    //MyUtil.myLog("ImageUrl",imageUrl);
                    if (count > 0)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                    }
                }

            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, t.getMessage());
                MyUtil.showToast(context, "Server side error");

            }
        });
    }


    public void SUBMIT_POINTS_TO_SERVER_RETROFIT()
    {
        myUtil.showProgressDialog(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<DataModel> call = apiService.submitDailyInspirationPoints(MySharedPreference.getInstance().getUID(context), myUtil.getCurrentTimeStamp());

        call.enqueue(new Callback<DataModel>()
        {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response)
            {

                myUtil.hideProgressDialog();

                MyUtil.myLog(TAG, "Response----" + response.body());

                DataModel dataModel = response.body();

                if (dataModel.getStatus().equals(MyConstant.TRUE))
                {
                    count = dataModel.getCount();

                    if (count > 0)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                    }
                }

            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, t.getMessage());
                // MyUtil.showToast(context, "Server side error");

            }
        });
    }


}