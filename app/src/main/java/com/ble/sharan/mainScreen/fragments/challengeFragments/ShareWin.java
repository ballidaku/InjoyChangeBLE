package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ViewPagerAdapter;
import com.ble.sharan.asyncTask.Super_AsyncTask;
import com.ble.sharan.asyncTask.Super_AsyncTask_Interface;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.AutoScrollViewPager;
import com.ble.sharan.myUtilities.CirclePageIndicator;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class ShareWin extends Fragment implements View.OnClickListener
{

    Context context;
    View view;

    AutoScrollViewPager autoScrollViewPager;

    CirclePageIndicator circlePageIndicator;


   // ImageView imgv_videoThumbnail;

    YouTubePlayerSupportFragment youTubePlayerFragment;



    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {

            view = inflater.inflate(R.layout.fragment_share_win, container, false);


            setUpIds();

            GET_DATA_FROM_SERVER();


        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ((MainActivityNew)getActivity()).setTitleHeader("Share a Win & Weekly Video");


    }


    private void setUpIds()
    {

        autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.autoScrollViewPager);

      //  imgv_videoThumbnail=(ImageView)view.findViewById(R.id.imgv_videoThumbnail);

        view.findViewById(R.id.txtv_seeAll).setOnClickListener(this);


        //You Tube

         youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();


    }


    public void GET_DATA_FROM_SERVER()
    {


        MyUtil.execute(new Super_AsyncTask(context, MyConstant.SHARE_WIN_API, new Super_AsyncTask_Interface()
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


                        setData(list,hashMapWeeklyChallenge);
                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, true));


    }

    private void setData(ArrayList<HashMap> list,final HashMap<String, String> hashMapWeeklyChallenge)
    {


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, list);
        autoScrollViewPager.setAdapter(viewPagerAdapter);


        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(autoScrollViewPager);


        autoScrollViewPager.startAutoScroll(2000);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setAutoScrollDurationFactor(17);


       // myUtil.showImageWithPicasso(context,imgv_videoThumbnail,hashMapWeeklyChallenge.get(MyConstant.IMAGE));


        youTubePlayerFragment.initialize(MyConstant.YOU_TUBE_KEY, new YouTubePlayer.OnInitializedListener() {


            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.cueVideo(hashMapWeeklyChallenge.get(MyConstant.URL).replace("https://www.youtube.com/embed/",""));
//                    player.play();
                }
            }


            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
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
        }
    }
}
