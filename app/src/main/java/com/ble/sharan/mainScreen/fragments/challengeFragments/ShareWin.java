package com.ble.sharan.mainScreen.fragments.challengeFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ViewPagerAdapter;
import com.ble.sharan.myUtilities.AutoScrollViewPager;
import com.ble.sharan.myUtilities.CirclePageIndicator;
import com.ble.sharan.myUtilities.MyUtil;

import java.util.ArrayList;

/**
 * Created by brst-pc93 on 2/6/17.
 */

public class ShareWin extends Fragment
{

    Context context;
    View view;

    AutoScrollViewPager autoScrollViewPager;

    CirclePageIndicator circlePageIndicator;

    ArrayList<Integer> imageIdList = new ArrayList<>();


    MyUtil myUtil = new MyUtil();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {

            view = inflater.inflate(R.layout.fragment_share_win, container, false);


            setUpIds();

        }

        return view;
    }

    private void setUpIds()
    {
        imageIdList.add(R.mipmap.ic_module_image);
        imageIdList.add(R.mipmap.ic_module_image);
        imageIdList.add(R.mipmap.ic_module_image);


        autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.autoScrollViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, imageIdList);
        autoScrollViewPager.setAdapter(viewPagerAdapter);


        circlePageIndicator = (CirclePageIndicator)view.findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(autoScrollViewPager);


        autoScrollViewPager.startAutoScroll(2000);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setAutoScrollDurationFactor(17);

    }


}
