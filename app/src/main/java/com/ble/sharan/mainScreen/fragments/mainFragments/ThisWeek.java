package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.ThisWeekAdapter;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;
import com.ble.sharan.myUtilities.ThemeChanger;

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

    Calendar calendar = Calendar.getInstance(Locale.GERMANY);

    final int currentWeekFinal = calendar.get(Calendar.WEEK_OF_YEAR);

    int kinneHafteaTak = 0;

    int currentWeek = 0;

    ArrayList<HashMap<String, String>> listMain = new ArrayList<>();

    int index = 0;

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

            //    MyUtil.myLog(TAG, "onCreateView----currentWeek" + currentWeek);


            setUpIds();


            imgv_leftArrow.setVisibility(View.VISIBLE);
            imgv_rightArrow.setVisibility(View.INVISIBLE);
            newRefresh();




        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().findViewById(R.id.txtv_right).setVisibility(View.VISIBLE);

        ((MainActivityNew) getActivity()).setTitleHeader("This Week");


//        imgv_leftArrow.setVisibility(View.VISIBLE);
//        imgv_rightArrow.setVisibility(View.INVISIBLE);

        //  refresh();


    }

    private void setUpIds()
    {
        view.findViewById(R.id.linearLayoutBackground).setBackground((Drawable) ThemeChanger.getInstance().getBackground(context, MyConstant.BACKGROUND));

        cardView_noResult = (CardView) view.findViewById(R.id.cardView_noResult);
        cardView_myWeekActivity = (CardView) view.findViewById(R.id.cardView_myWeekActivity);


        (imgv_viewGraph = (ImageView) view.findViewById(R.id.imgv_viewGraph)).setOnClickListener(this);


        listView_weekRecords = (ListView) view.findViewById(R.id.listView_weekRecords);

        txtv_heading = (TextView) view.findViewById(R.id.txtv_heading);
        imgv_leftArrow = (ImageView) view.findViewById(R.id.imgv_leftArrow);
        imgv_rightArrow = (ImageView) view.findViewById(R.id.imgv_rightArrow);

        imgv_leftArrow.setOnClickListener(this);
        imgv_rightArrow.setOnClickListener(this);


        listView_weekRecords.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // This functionality is stopped by mrinal sir.

               /* String rawTime=myDatabase.getRawSleepDataOnDate(listMain.get(i).get(MyConstant.DATE));

                if (rawTime != null && !rawTime.isEmpty())
                {
                    SleepDetails sleepDetails = new SleepDetails();
                    Bundle bundle = new Bundle();
                    bundle.putString(MyConstant.SLEEP, rawTime);
                    bundle.putString(MyConstant.DATE, listMain.get(i).get(MyConstant.DATE));
                    sleepDetails.setArguments(bundle);
                    myUtil.switchfragment(ThisWeek.this, sleepDetails);
                }*/

            }
        });


        // updateUI();

//        Calendar calendar = Calendar.getInstance();
//
//        getStartEndOFWeek(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));


    }

    private void updateUI(ArrayList<HashMap<String, String>> list)
    {
        // List<BeanRecords> list = myDatabase.getAllStepRecords(context);

        listMain = list;

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


          /*      if ((kinneHafteaTak < currentWeek) && (currentWeek < currentWeekFinal || currentWeek == currentWeekFinal))
                {

                    // txtv_heading.setText("Previous Week");
                    imgv_rightArrow.setVisibility(View.VISIBLE);

                    if (kinneHafteaTak == currentWeek - 1)
                    {
                        imgv_leftArrow.setVisibility(View.INVISIBLE);
                    }

                    calendar.add(Calendar.WEEK_OF_YEAR, -1);

                    refresh();
                }
                else
                {
                    MyUtil.showToast(context, "No More Data Found.");
                }*/


                indexWork("L");


                break;

            case R.id.imgv_rightArrow:

              /*  if (currentWeek < currentWeekFinal)
                {
                    imgv_leftArrow.setVisibility(View.VISIBLE);
                    if (currentWeek == currentWeekFinal - 1)
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
                }*/

                indexWork("R");

                break;
        }
    }

    public void indexWork(String LR)
    {
       // MyUtil.myLog(TAG, "INDEX BEFORE " + index);


        if (LR.equals("L") && index >= 0 && index <= 2)
        {
            index = index + 1;
            update();

            if (index > 0)
                imgv_rightArrow.setVisibility(View.VISIBLE);
            if (index == 3)
                imgv_leftArrow.setVisibility(View.INVISIBLE);

        }
        else if (LR.equals("L"))
        {
            MyUtil.showToast(context, "No More Data Found.");
        }
        else if ( LR.equals("R") && index >= 1 && index <= 3)
        {
            index = index - 1;
            update();

            if (index == 3)
                imgv_leftArrow.setVisibility(View.INVISIBLE);
            if (index == 0)
                imgv_rightArrow.setVisibility(View.INVISIBLE);
            if (index < 3)
                imgv_leftArrow.setVisibility(View.VISIBLE);
        }
        else if(LR.equals("R"))
        {
            MyUtil.showToast(context, "No More Data Found.");
        }

        checkLeftArrow();


      //  MyUtil.myLog(TAG, "INDEX AFTER " + index);
    }

    public void checkLeftArrow()
    {

           if( !containsValue[3] && index == 2)
           {
               imgv_leftArrow.setVisibility(View.INVISIBLE);
           }
           else if( !containsValue[3] && !containsValue[2] && index == 1)
           {
               imgv_leftArrow.setVisibility(View.INVISIBLE);
           }
           else if( !containsValue[3] && !containsValue[2] && !containsValue[1] && index == 0)
           {
               imgv_leftArrow.setVisibility(View.INVISIBLE);
           }

    }


    public void refresh()
    {

        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        int YEAR = calendar.get(Calendar.YEAR);

        //  MyUtil.myLog(TAG, "refresh----currentWeek   " + currentWeek + "   kinneHafteaTak " + kinneHafteaTak);

        String[] startEndDates = getStartEndOFWeek(currentWeek, YEAR).split(":");

//        for (int i = 0; i < startEndDates.length; i++)
//        {
//            MyUtil.myLog(TAG, "refresh----startEndDates   " + startEndDates[i]);
//        }


        // Data which we are ghoing to show
        ArrayList<HashMap<String, String>> finalList = getDataList(myUtil.getDates(startEndDates[0], startEndDates[1]));


        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("MM/dd");

        String heading = null;
        try
        {
            heading = output.format(input.parse(finalList.get(finalList.size() - 1).get(MyConstant.DATE))) + " - " + output.format(input.parse(finalList.get(0).get(MyConstant.DATE)));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        txtv_heading.setText(heading);

        updateUI(finalList);


        //******************************************************************************************
        // if previous week data is not there then we have to hide
        //*****************************************************************************************(

        String[] startEndDatesPreviousWeek = getStartEndOFWeek(currentWeek - 1, YEAR).split(":");


        for (int i = 0; i < startEndDatesPreviousWeek.length; i++)
        {
            MyUtil.myLog(TAG, "refresh----startEndDatesPreviousWeekDates   " + startEndDatesPreviousWeek[i]);
        }

        // Data which we are ghoing to show
        ArrayList<HashMap<String, String>> localList = getDataList(myUtil.getDates(startEndDatesPreviousWeek[0], startEndDatesPreviousWeek[1]));

        boolean containStepsvalue = false;
        boolean containSleepvalue = false;

        for (int i = 0; i < localList.size(); i++)
        {
            if (Integer.parseInt(localList.get(i).get(MyConstant.STEPS)) > 0)
            {
                containStepsvalue = true;
            }


            if (Integer.parseInt(localList.get(i).get(MyConstant.SLEEP)) > 0)
            {
                containSleepvalue = true;
            }
        }

        if (!containStepsvalue && !containSleepvalue)
        {
            imgv_leftArrow.setVisibility(View.INVISIBLE);
        }


        // To  right arrow when we comes back from nested fragment
        if (currentWeek < kinneHafteaTak + 3)
        {
            imgv_rightArrow.setVisibility(View.VISIBLE);
        }

    }

    String[] myHeading = new String[4];
    boolean[] containsValue = new boolean[4];

    ArrayList<ArrayList<HashMap<String, String>>> mainList = new ArrayList<>();

    // We have to show data of other weeks if we donot have previous week data
    public void newRefresh()
    {
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        int YEAR = calendar.get(Calendar.YEAR);

        //MyUtil.myLog(TAG, "refresh----cuMyUtil.myLogWeek   " + currentWeek + "   kinneHafteaTak " + kinneHafteaTak);


        for (int i = 0; i <= 3; i++)
        {
            String[] startEndDates = getStartEndOFWeek(currentWeek - i, YEAR).split(":");

            /*for (int j = 0; j < startEndDates.length; j++)
            {
                MyUtil.myLog(TAG, "refresh----startEndDates   " + startEndDates[j]);
            }*/

            // Data which we are going to show
            ArrayList<HashMap<String, String>> finalList = getDataList(myUtil.getDates(startEndDates[0], startEndDates[1]));


            mainList.add(finalList);
            //MyUtil.myLog(TAG,"Heading  "+getHeading(finalList));

            myHeading[i] = getHeading(finalList);

        }


        //******************************************************************************************
        // if previous week data is not there then we have to hide
        //*****************************************************************************************(

       /* String[] startEndDatesPreviousWeek = getStartEndOFWeek(currentWeek - 1, YEAR).split(":");


        for (int i = 0; i < startEndDatesPreviousWeek.length; i++)
        {
            MyUtil.myLog(TAG, "refresh----startEndDatesPreviousWeekDates   " + startEndDatesPreviousWeek[i]);
        }

        // Data which we are ghoing to show
        ArrayList<HashMap<String, String>> localList = getDataList(myUtil.getDates(startEndDatesPreviousWeek[0], startEndDatesPreviousWeek[1]));*/


        for (int i = 0; i < mainList.size(); i++)
        {
            boolean containValue = false;

            for (int j = 0; j < mainList.get(i).size(); j++)
            {
                if (Integer.parseInt(mainList.get(i).get(j).get(MyConstant.STEPS)) > 0 || Integer.parseInt(mainList.get(i).get(j).get(MyConstant.SLEEP)) > 0)
                {
                    containValue = true;
                }

            }

           // MyUtil.myLog(TAG,"containValue  "+containValue);
            containsValue[i] = containValue;

        }

        update();

       checkLeftArrow();

        /*if(!containStepsvalue && !containSleepvalue)
        {
            imgv_leftArrow.setVisibility(View.INVISIBLE);
        }*/


        // To  right arrow when we comes back from nested fragment
        /*if (currentWeek < kinneHafteaTak + 3)
        {
            imgv_rightArrow.setVisibility(View.VISIBLE);
        }*/
    }

    private void update()
    {
        txtv_heading.setText(myHeading[index]);

        updateUI(mainList.get(index));
    }

    private String getHeading(ArrayList<HashMap<String, String>> finalList)
    {

        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("MM/dd");

        String heading = null;
        try
        {
            heading = output.format(input.parse(finalList.get(finalList.size() - 1).get(MyConstant.DATE))) + " - " + output.format(input.parse(finalList.get(0).get(MyConstant.DATE)));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return heading;
    }


    //**********************************************************************************************

    String getStartEndOFWeek(int enterWeek, int enterYear)
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


        return startDateInStr + ":" + endDateString;

//        MyUtil.myLog("Missing List", "-----" + myUtil.getDates(startDateInStr, endDaString));

    }


    public ArrayList<HashMap<String, String>> getDataList(List<String> dates)
    {

        //  MyUtil.myLog(TAG, "List of week days----" + dates);

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

        //  MyUtil.myLog(TAG, "StepsMap===" + stepsMap);

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

        //   MyUtil.myLog(TAG, "finalList===" + localList);


        return localList;

    }


}
