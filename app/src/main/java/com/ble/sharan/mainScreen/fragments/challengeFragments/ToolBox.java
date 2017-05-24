package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.ToolBoxModel;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brst-pc93 on 2/10/17.
 */

public class ToolBox extends Fragment implements View.OnClickListener
{

    String TAG = ToolBox.class.getSimpleName();

    Context context;
    View view;


    MyUtil myUtil = new MyUtil();


    ImageView imgv_running;
    ImageView check1_iv;

    String currentDay = "";

    MyDialogs myDialogs = new MyDialogs();

    String image = "";
    String title = "";
    String description = "";
    int count = 0;


    CardView cardViewTues;
    CardView cardViewFri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_tool_box, container, false);


            setUpIds();

            currentDay = myUtil.getCurrentDay();

            makeClickableOrNot();


        }

        if (currentDay.equals("Tuesday") ||currentDay.equals("Wednesday") || currentDay.equals("Thursday"))
        {
            hitDay = "Tuesday";
        }
        else if (currentDay.equals("Friday") || currentDay.equals("Saturday") || currentDay.equals("Sunday") || currentDay.equals("Monday") )
        {
            hitDay = "Friday";
        }

        READ_NOW_RETROFIT();

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

        imgv_running = (ImageView) view.findViewById(R.id.imgv_running);
        check1_iv = (ImageView) view.findViewById(R.id.check1_iv);

        (cardViewTues = (CardView) view.findViewById(R.id.cardViewTuesday)).setOnClickListener(this);
        (cardViewFri = (CardView) view.findViewById(R.id.cardViewFriday)).setOnClickListener(this);
        view.findViewById(R.id.txtv_viewAll).setOnClickListener(this);


        view.findViewById(R.id.cardViewReadNow).setOnClickListener(this);


        myUtil.showCircularImageWithPicasso(context, imgv_running, R.drawable.ic_running);

    }



    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            /*case R.id.cardViewMonday:

                hitDay = "Monday";
                READ_NOW_RETROFIT();

                break;*/


            case R.id.cardViewTuesday:


                hitDay = "Tuesday";
                READ_NOW_RETROFIT();

                break;


            case R.id.cardViewFriday:


                hitDay = "Friday";
                READ_NOW_RETROFIT();

                break;


            case R.id.txtv_viewAll:
                firstTimeOnly=false;

                ((MainActivityNew) getActivity()).changeFragment2(new ToolBoxViewAll());


                break;


            case R.id.cardViewReadNow:

                if (currentDay.equals("Tuesday") ||currentDay.equals("Wednesday") || currentDay.equals("Thursday"))
                {
                    hitDay = "Tuesday";
                }
                else if (currentDay.equals("Friday") || currentDay.equals("Saturday") || currentDay.equals("Sunday") || currentDay.equals("Monday") )
                {
                    hitDay = "Friday";
                }

                READ_NOW_RETROFIT();


                break;
        }
    }


    public void makeClickableOrNot()
    {
//        cardViewMon.setEnabled(false);
        cardViewTues.setEnabled(false);
        cardViewFri.setEnabled(false);

        if (currentDay.equals("Friday") || currentDay.equals("Saturday") || currentDay.equals("Sunday") || currentDay.equals("Monday"))
        {
//            cardViewMon.setEnabled(true);
//            cardViewTues.setEnabled(true);
            cardViewFri.setEnabled(true);
        }
        else if (currentDay.equals("Tuesday") || currentDay.equals("Wednesday") || currentDay.equals("Thursday"))
        {
//            cardViewMon.setEnabled(true);
            cardViewTues.setEnabled(true);
        }

    }


    boolean firstTimeOnly = false;

    public void READ_NOW_RETROFIT()
    {

        myUtil.showProgressDialog(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ToolBoxModel> call = apiService.readNowToolBox(myUtil.getCurrentTimeStamp(), hitDay, MySharedPreference.getInstance().getUID(context));

        call.enqueue(new Callback<ToolBoxModel>()
        {
            @Override
            public void onResponse(Call<ToolBoxModel> call, Response<ToolBoxModel> response)
            {

                myUtil.hideProgressDialog();

                MyUtil.myLog(TAG, "Response----" + response.body());

                ToolBoxModel toolBoxModel = response.body();

                if (toolBoxModel.getStatus().equals(MyConstant.TRUE))
                {
                    image = toolBoxModel.getImage();
                    title = toolBoxModel.getTitle();
                    description = toolBoxModel.getDescription();
                    count = toolBoxModel.getCount();


                    if (count > 0)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                    }


                    if (firstTimeOnly)
                    {
                        if (!title.isEmpty() && !image.isEmpty() && !description.isEmpty())
                        {
                            myDialogs.showToolBoxDialog(context, title, image, description, onClickListener, hitDay, count);
                        }
                    }
                    firstTimeOnly = true;


                }

            }

            @Override
            public void onFailure(Call<ToolBoxModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, t.getMessage());
//                MyUtil.showToast(context, "Server side error");

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            myDialogs.dialog.dismiss();
            SUBMIT_POINTS_RETROFIT();
        }
    };

    String hitDay = "";

    public void SUBMIT_POINTS_RETROFIT()
    {
        myUtil.showProgressDialog(context);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ToolBoxModel> call = apiService.submitPointsToolBox(myUtil.getCurrentTimeStamp(), MySharedPreference.getInstance().getUID(context), hitDay);

        call.enqueue(new Callback<ToolBoxModel>()
        {
            @Override
            public void onResponse(Call<ToolBoxModel> call, Response<ToolBoxModel> response)
            {

                myUtil.hideProgressDialog();

                MyUtil.myLog(TAG, "Response----" + response.body());

                ToolBoxModel toolBoxModel = response.body();

                if (toolBoxModel.getStatus().equals(MyConstant.TRUE))
                {
                    count = toolBoxModel.getCount();

                    if (count > 0)
                    {
                        check1_iv.setImageResource(R.mipmap.ic_check);
                    }
                }
            }

            @Override
            public void onFailure(Call<ToolBoxModel> call, Throwable t)
            {
                myUtil.hideProgressDialog();
                MyUtil.myLog(TAG, t.getMessage());
//                MyUtil.showToast(context, "Server side error");

            }
        });
    }


}