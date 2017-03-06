package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ViewPagerAdapter;
import com.ble.sharan.apiModels.ApiClient;
import com.ble.sharan.apiModels.ApiInterface;
import com.ble.sharan.apiModels.ShareWinModel;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.AutoScrollViewPager;
import com.ble.sharan.myUtilities.CirclePageIndicator;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class ShareWin extends Fragment implements View.OnClickListener
{


    String TAG = ShareWin.class.getSimpleName();

    Context context;
    View view;

    AutoScrollViewPager autoScrollViewPager;

    CirclePageIndicator circlePageIndicator;


    ImageView imgv_checkShareWin;
    ImageView imgv_checkWeeklyVideo;

    CardView cardViewShareWin;
    CardView cardViewWeeklyVideo;

    YouTubePlayerSupportFragment youTubePlayerFragment;


    MyUtil myUtil = new MyUtil();
    MyDialogs myDialogs = new MyDialogs();

    int weeklyCount = 0;
    int shareWinCount = 0;
    String weeklyComment = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {

            view = inflater.inflate(R.layout.fragment_share_win, container, false);


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

        ((MainActivityNew) getActivity()).setTitleHeader("Share a Win & Weekly Video");
    }


    private void setUpIds()
    {

        autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.autoScrollViewPager);

        imgv_checkShareWin = (ImageView) view.findViewById(R.id.imgv_checkShareWin);
        imgv_checkWeeklyVideo = (ImageView) view.findViewById(R.id.imgv_checkWeeklyVideo);

        cardViewShareWin = (CardView) view.findViewById(R.id.cardViewShareWin);
        cardViewWeeklyVideo = (CardView) view.findViewById(R.id.cardViewWeeklyVideo);

        cardViewShareWin.setOnClickListener(this);
        cardViewWeeklyVideo.setOnClickListener(this);


        view.findViewById(R.id.txtv_seeAll).setOnClickListener(this);


        //You Tube
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();


    }


    private void setData(ArrayList<ShareWinModel.Data.SubData> list, final String videoUrl)
    {


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, list);
        autoScrollViewPager.setAdapter(viewPagerAdapter);


        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(autoScrollViewPager);


        autoScrollViewPager.startAutoScroll(2000);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setAutoScrollDurationFactor(17);


        // myUtil.showImageWithPicasso(context,imgv_videoThumbnail,hashMapWeeklyChallenge.get(MyConstant.IMAGE));


        if (shareWinCount > 0)
        {
            imgv_checkShareWin.setImageResource(R.mipmap.ic_check);
        }

        if (weeklyCount > 0)
        {
            imgv_checkWeeklyVideo.setImageResource(R.mipmap.ic_check);
        }



        youTubePlayerFragment.initialize(MyConstant.YOU_TUBE_KEY, new YouTubePlayer.OnInitializedListener()
        {


            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
            {
                if (!wasRestored)
                {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    String a = videoUrl;
                    // Log.e(TAG,"ABC   "+a.substring(a.lastIndexOf("/")+1,a.length()));
                    player.cueVideo(a.substring(a.lastIndexOf("/") + 1, a.length()));


//                    player.play();
                }
            }


            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error)
            {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });


    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_seeAll:

                ((MainActivityNew) getActivity()).changeFragment2(new ShareWinSeeAll());

                break;


            case R.id.cardViewShareWin:

//                imgv_checkShareWin.setImageResource(R.mipmap.ic_check);
                edtv_comment = myDialogs.showShareWinCheckInDialog(context, "ShareWin", onClickListenerShareWin);

                break;


            case R.id.cardViewWeeklyVideo:

                edtv_comment = myDialogs.showShareWinCheckInDialog(context, "WeeklyVideo", onClickListenerWeeklyVideo);

                break;
        }
    }


    EditText edtv_comment;

    View.OnClickListener onClickListenerShareWin = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            String comment = edtv_comment.getText().toString().trim();

            if (!comment.isEmpty())
            {
                myDialogs.dialog.dismiss();
                SHARE_COMMENT_DATA_TO_SERVER_RETROFIT(comment);
            }
            else
            {
                MyUtil.showToast(context, "Please enter comment");
            }

        }
    };


    View.OnClickListener onClickListenerWeeklyVideo = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            String comment = edtv_comment.getText().toString().trim();

            if (!comment.isEmpty())
            {
                myDialogs.dialog.dismiss();
                WEEKLY_VIDEO_COMMENT_DATA_TO_SERVER_RETROFIT(comment);
            }
            else
            {
                MyUtil.showToast(context, "Please enter comment");
            }
        }
    };


    //**********************************************************************************************
    //**********************************************************************************************
    // SERVICES HERE
    //**********************************************************************************************
    //**********************************************************************************************


/*    public void GET_DATA_FROM_SERVER()
    {


        MyUtil.execute(new Super_AsyncTask(context, MyConstant.SHARE_WIN_API + "?date=" + myUtil.getCurrentTimeStamp() + "&uid=" + MySharedPreference.getInstance().getUID(context), new Super_AsyncTask_Interface()
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

                        JSONObject object1 = object.getJSONObject(MyConstant.DATA);

                        JSONArray jsonArrayShareWin = object1.getJSONArray(MyConstant.SHARE_WIN);

                        for (int i = 0; i < jsonArrayShareWin.length(); i++)
                        {
                            JSONObject jsonObject = jsonArrayShareWin.getJSONObject(i);
                            HashMap<String, String> hashMap = new HashMap<String, String>();

                            hashMap.put(MyConstant.UID, jsonObject.getString(MyConstant.UID));
                            hashMap.put(MyConstant.COMMENT, jsonObject.getString(MyConstant.COMMENT));
                            hashMap.put(MyConstant.NAME, jsonObject.getString(MyConstant.NAME));
                            hashMap.put(MyConstant.IMAGE, jsonObject.getString(MyConstant.IMAGE));

                            list.add(hashMap);
                        }

                        JSONObject object2 = object1.getJSONObject(MyConstant.WEEKLY_CHALLENGE);

                        HashMap<String, String> hashMapWeeklyChallenge = new HashMap<String, String>();

                        hashMapWeeklyChallenge.put(MyConstant.IMAGE, object2.getString(MyConstant.IMAGE));
                        hashMapWeeklyChallenge.put(MyConstant.TITLE, object2.getString(MyConstant.TITLE));
                        hashMapWeeklyChallenge.put(MyConstant.URL, object2.getString(MyConstant.URL));


                        weeklyCount = object1.getInt(MyConstant.WEEKLY_COUNT);
                        shareWinCount = object1.getInt(MyConstant.SHAREWIN_COUNT);
                        weeklyComment = object1.getString(MyConstant.WEEKLY_COMMENT);


                       // setData(list, hashMapWeeklyChallenge);
                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, true));


    }*/

    // RETROFIT
    public void GET_DATA_FROM_SERVER_RETROFIT()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ShareWinModel> call = apiService.getShareWinData(myUtil.getCurrentTimeStamp(), MySharedPreference.getInstance().getUID(context));

        call.enqueue(new Callback<ShareWinModel>()
        {
            @Override
            public void onResponse(Call<ShareWinModel> call, Response<ShareWinModel> response)
            {
                Log.e(TAG, "Response----" + response.body());

                ShareWinModel shareWinModel = response.body();

                if (shareWinModel.getStatus().equals(MyConstant.TRUE))
                {
                    ShareWinModel.Data data = shareWinModel.getData();


                    weeklyCount = data.getWeeklyCount();
                    shareWinCount = data.getShareWinCount();


                    setData(data.getshareWinList(), data.getWeeklyChallenge().getUrl());

                }
            }

            @Override
            public void onFailure(Call<ShareWinModel> call, Throwable t)
            {
                Log.e(TAG, t.getMessage());
                MyUtil.showToast(context, "Server side error");

            }
        });
    }


    public void SHARE_COMMENT_DATA_TO_SERVER_RETROFIT(String comment)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ShareWinModel> call = apiService.shareShareWinComment(comment, MySharedPreference.getInstance().getUID(context),myUtil.getCurrentTimeStamp());

        call.enqueue(new Callback<ShareWinModel>()
        {
            @Override
            public void onResponse(Call<ShareWinModel> call, Response<ShareWinModel> response)
            {
                Log.e(TAG, "Response----" + response.body());

                ShareWinModel shareWinModel = response.body();

                if (shareWinModel.getStatus().equals(MyConstant.TRUE))
                {
                    if (shareWinModel.getCount() > 0)
                    {
                        imgv_checkWeeklyVideo.setImageResource(R.mipmap.ic_check);
                    }

                }
            }

            @Override
            public void onFailure(Call<ShareWinModel> call, Throwable t)
            {
                Log.e(TAG, t.getMessage());
                MyUtil.showToast(context, "Server side error");

            }
        });
    }



    public void WEEKLY_VIDEO_COMMENT_DATA_TO_SERVER_RETROFIT(String comment)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ShareWinModel> call = apiService.shareWeeklyVideoComment(comment, MySharedPreference.getInstance().getUID(context),myUtil.getCurrentTimeStamp());

        call.enqueue(new Callback<ShareWinModel>()
        {
            @Override
            public void onResponse(Call<ShareWinModel> call, Response<ShareWinModel> response)
            {
                Log.e(TAG, "Response----" + response.body());

                ShareWinModel shareWinModel = response.body();

                if (shareWinModel.getStatus().equals(MyConstant.TRUE))
                {
                    if (shareWinModel.getCount() > 0)
                    {
                        imgv_checkShareWin.setImageResource(R.mipmap.ic_check);
                    }

                    GET_DATA_FROM_SERVER_RETROFIT();


                }
            }

            @Override
            public void onFailure(Call<ShareWinModel> call, Throwable t)
            {
                Log.e(TAG, t.getMessage());
                MyUtil.showToast(context, "Server side error");

            }
        });
    }
}
