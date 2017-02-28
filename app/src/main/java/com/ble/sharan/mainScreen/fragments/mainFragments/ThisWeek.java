package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ThisWeekAdapter;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by brst-pc93 on 2/15/17.
 */

public class ThisWeek extends Fragment implements View.OnClickListener
{
    String TAG = ThisWeek.class.getSimpleName();


    Context context;
    View view;


    ListView listView_weekRecords;

    ImageView imgv_leftArrow;
    ImageView imgv_rightArrow;

    ImageView imgv_viewGraph;

    TextView txtv_heading;

    ThisWeekAdapter thisWeekAdapter;

    MyDatabase myDatabase;

    CardView cardView_myWeekActivity;
    CardView cardView_noResult;


    MyUtil myUtil = new MyUtil();

    Calendar calendar = Calendar.getInstance();

    final int currentWeekFinal = calendar.get(Calendar.WEEK_OF_YEAR);

    int kinneHafteaTak = 0;

    int currentWeek = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_thisweek, container, false);

            myDatabase = new MyDatabase(getActivity());

            // calendar = Calendar.getInstance();
            //currentWeek=calendar.get(Calendar.WEEK_OF_YEAR);
            //currentWeekFinal=currentWeek;

            currentWeek = currentWeekFinal;
            kinneHafteaTak = currentWeekFinal - 3;

        //    Log.e(TAG, "onCreateView----currentWeek" + currentWeek);


            setUpIds();




        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().findViewById(R.id.txtv_right).setVisibility(View.VISIBLE);


    }

    private void setUpIds()
    {



        cardView_noResult = (CardView) view.findViewById(R.id.cardView_noResult);
        cardView_myWeekActivity = (CardView) view.findViewById(R.id.cardView_myWeekActivity);


        (imgv_viewGraph = (ImageView) view.findViewById(R.id.imgv_viewGraph)).setOnClickListener(this);


        listView_weekRecords = (ListView) view.findViewById(R.id.listView_weekRecords);

        txtv_heading = (TextView) view.findViewById(R.id.txtv_heading);
        imgv_leftArrow = (ImageView) view.findViewById(R.id.imgv_leftArrow);
        imgv_rightArrow = (ImageView) view.findViewById(R.id.imgv_rightArrow);

        imgv_leftArrow.setOnClickListener(this);
        imgv_rightArrow.setOnClickListener(this);


        // updateUI();

//        Calendar calendar = Calendar.getInstance();
//
//        getStartEndOFWeek(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));

        imgv_leftArrow.setVisibility(View.VISIBLE);
        imgv_rightArrow.setVisibility(View.INVISIBLE);


        refresh();
    }

    private void updateUI(ArrayList<HashMap<String, String>> list)
    {
        // List<BeanRecords> list = myDatabase.getAllStepRecords(context);

        if (list.size() > 0)
        {
            cardView_noResult.setVisibility(View.GONE);
            cardView_myWeekActivity.setVisibility(View.VISIBLE);
//            imgv_viewGraph.setVisibility(View.VISIBLE);


            thisWeekAdapter = new ThisWeekAdapter(context, list);

            listView_weekRecords.setAdapter(thisWeekAdapter);
        }
        else
        {
            cardView_noResult.setVisibility(View.VISIBLE);
            cardView_myWeekActivity.setVisibility(View.GONE);
//            imgv_viewGraph.setVisibility(View.GONE);
        }

    }


    @Override
    public void onDestroyView()
    {
        imgv_leftArrow.setVisibility(View.GONE);
        imgv_rightArrow.setVisibility(View.GONE);


        super.onDestroyView();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgv_viewGraph:

                ViewGraphFragment viewGraphFragment = new ViewGraphFragment();
                Bundle args = new Bundle();
                args.putString(MyConstant.FROM_WHERE, MyConstant.MY_WEEK_ACTIVITY_FRAGMENT);
                viewGraphFragment.setArguments(args);


                myUtil.switchfragment(ThisWeek.this, viewGraphFragment);

                break;


            case R.id.imgv_leftArrow:


                if ((kinneHafteaTak < currentWeek) && (currentWeek < currentWeekFinal || currentWeek == currentWeekFinal))
                {

                   // txtv_heading.setText("Previous Week");
                    imgv_rightArrow.setVisibility(View.VISIBLE);

                    if(kinneHafteaTak ==  currentWeek-1)
                    {
                        imgv_leftArrow.setVisibility(View.INVISIBLE);
                    }

                    calendar.add(Calendar.WEEK_OF_YEAR, -1);

                    refresh();
                }
                else
                {
                    MyUtil.showToast(context, "No More Data Found.");
                }


                break;

            case R.id.imgv_rightArrow:

                if (currentWeek < currentWeekFinal)
                {
                    imgv_leftArrow.setVisibility(View.VISIBLE);
                    if (currentWeek == currentWeekFinal-1)
                    {
                        imgv_rightArrow.setVisibility(View.INVISIBLE);
                     //   txtv_heading.setText("This Week");
                    }

                    calendar.add(Calendar.WEEK_OF_YEAR, +1);
                    refresh();
                }
                else
                {
                    MyUtil.showToast(context, "No More Data Found.");
                }

                break;
        }
    }


    public void refresh()
    {

        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        int YEAR = calendar.get(Calendar.YEAR);

      //  Log.e(TAG, "refresh----currentWeek" + currentWeek);

        String[] startEndDates= getStartEndOFWeek(currentWeek, YEAR).split(":");

        // Data which we are ghoing to show
        ArrayList<HashMap<String, String>> finalList=  getDataList(myUtil.getDates(startEndDates[0], startEndDates[1]));


        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("MM/dd");

        String heading = null;
        try
        {
            heading =output.format(input.parse(finalList.get(finalList.size()-1).get(MyConstant.DATE)))+" - "+ output.format(input.parse(finalList.get(0).get(MyConstant.DATE)));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        txtv_heading.setText(heading);

        updateUI(finalList);




        //******************************************************************************************
        // if previous week data is not there then we have to hide
        //*****************************************************************************************(

        String[] startEndDatesPreviousWeek= getStartEndOFWeek(currentWeek-1, YEAR).split(":");

        // Data which we are ghoing to show
        ArrayList<HashMap<String, String>> localList=  getDataList(myUtil.getDates(startEndDatesPreviousWeek[0], startEndDatesPreviousWeek[1]));

        boolean containStepsvalue=false;
        boolean containSleepvalue=false;

        for (int i = 0; i <localList.size() ; i++)
        {
            if(Integer.parseInt(localList.get(i).get(MyConstant.STEPS))>0)
            {
                containStepsvalue=true;
            }


            if(Integer.parseInt(localList.get(i).get(MyConstant.SLEEP))>0)
            {
                containSleepvalue=true;
            }
        }

        if(!containStepsvalue && !containSleepvalue)
        {
            imgv_leftArrow.setVisibility(View.INVISIBLE);
        }

    }


    //**********************************************************************************************

    String  getStartEndOFWeek(int enterWeek, int enterYear)
    {

        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        // System.out.println("...date..." + startDateInStr);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDateString = formatter.format(enddate);
        // System.out.println("...date..." + endDaString);


        return startDateInStr+":"+endDateString;

//        Log.e("Missing List", "-----" + myUtil.getDates(startDateInStr, endDaString));

    }


    public ArrayList<HashMap<String, String>>  getDataList(List<String> dates)
    {

      //  Log.e(TAG, "List of week days----" + dates);

        List<BeanRecords> list = myDatabase.getAllStepRecords(context);

        HashMap<String, Integer> stepsMap = new HashMap<>();

        for (String date : dates)
        {

            stepsMap.put(date, 0);

            for (int i = 0; i < list.size(); i++)
            {
                if (date.equals(list.get(i).getDate()))
                {
                    int steps = Integer.parseInt(list.get(i).getSteps());
                    stepsMap.put(date, steps);
                }
            }
        }

      //  Log.e(TAG, "StepsMap===" + stepsMap);

        ArrayList<HashMap<String, String>> localList = new ArrayList<>();


        for (String date : dates)
        {

            if (myUtil.convertStringToDate(date).before(myUtil.convertStringToDate(myUtil.getTodaydate())) || myUtil.convertStringToDate(date).equals(myUtil.convertStringToDate(myUtil.getTodaydate())))
            {
                HashMap<String, String> map = new HashMap<>();
                map.put(MyConstant.STEPS, stepsMap.get(date).toString());
                map.put(MyConstant.DATE, date);
                map.put(MyConstant.SLEEP, String.valueOf(myDatabase.getSleepMillisOnDate(date)));

                localList.add(map);
            }
        }

        Collections.reverse(localList);

     //   Log.e(TAG, "finalList===" + localList);


        return localList;

    }


}
