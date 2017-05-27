package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.ThemeChanger;

/**
 * Created by brst-pc93 on 1/4/17.
 */

public class HealthData extends Fragment implements View.OnClickListener
{

    String TAG = HealthData.class.getSimpleName();



    FrameLayout frameLayout_today;
    FrameLayout frameLayout_myweek;
    FrameLayout frameLayout_mygoal;
    FrameLayout frameLayout_overall;


    Context context;

    View view;

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_health_data, container, false);

            setUpIds();


        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((MainActivityNew) getActivity()).setTitleHeader("Health Data");
    }

    private void setUpIds()
    {
        view.findViewById(R.id.linearLayoutBackground).setBackground((Drawable) ThemeChanger.getInstance().getBackground(context, MyConstant.BACKGROUND));
        ((ImageView)view.findViewById(R.id.imageViewToday)).setImageResource((Integer) ThemeChanger.getInstance().getBackground(context,MyConstant.TODAY));
        ((ImageView)view.findViewById(R.id.imageViewThisWeek)).setImageResource((Integer) ThemeChanger.getInstance().getBackground(context,MyConstant.THIS_WEEK));
        ((ImageView)view.findViewById(R.id.imageViewMyGoals)).setImageResource((Integer) ThemeChanger.getInstance().getBackground(context,MyConstant.MY_GOALS));
        ((ImageView)view.findViewById(R.id.imageViewOverall)).setImageResource((Integer) ThemeChanger.getInstance().getBackground(context,MyConstant.OVERALL));


        (frameLayout_today = (FrameLayout) view.findViewById(R.id.frameLayout_today)).setOnClickListener(this);
        (frameLayout_myweek = (FrameLayout) view.findViewById(R.id.frameLayout_myweek)).setOnClickListener(this);
        (frameLayout_mygoal = (FrameLayout) view.findViewById(R.id.frameLayout_mygoal)).setOnClickListener(this);
        (frameLayout_overall = (FrameLayout) view.findViewById(R.id.frameLayout_overall)).setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.frameLayout_today:

                ((MainActivityNew)getActivity()).displayView(1);

                break;


            case R.id.frameLayout_myweek:

                ((MainActivityNew)getActivity()).displayView(2);

                break;

            case R.id.frameLayout_mygoal:

                ((MainActivityNew)getActivity()).displayView(3);

                break;

            case R.id.frameLayout_overall:

                ((MainActivityNew)getActivity()).displayView(4);


               // startActivity(new Intent(getActivity(), MusicPlayer.class));
                break;
        }
    }








}
