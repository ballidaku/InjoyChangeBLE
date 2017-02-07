package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brst-pc93 on 1/27/17.
 */

public class ViewGraphFragment extends Fragment implements View.OnClickListener
{

    String TAG = ViewGraphFragment.class.getSimpleName();

    Context context;
    View view;

    BarChart mChart1;
    BarChart mChart2;
    BarChart mChart3;
    BarChart mChart4;


    TextView txtv_totalSteps;
    TextView txtv_totalCalories;
    TextView txtv_totalDistance;
    TextView txtv_totalSleep;


    TextView txtv_stepsWeeklyAverage;
    TextView txtv_caloriesWeeklyAverage;
    TextView txtv_distanceWeeklyAverage;
    TextView txtv_sleepWeeklyAverage;


    ScrollView scrollView;

    MyDatabase myDatabase;

    MyUtil myUtil = new MyUtil();


    public ViewGraphFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_view_graph, container, false);


            myDatabase = new MyDatabase(getActivity());

            setUpIds();


            if (getArguments().getString(MyConstant.FROM_WHERE).equals(MyConstant.MY_WEEK_SLEEP_FRAGMENT))
            {


                scrollView.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);

            }


        }

        return view;
    }


    private void setUpIds()
    {

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        txtv_totalSteps = (TextView) view.findViewById(R.id.txtv_totalSteps);
        txtv_totalCalories = (TextView) view.findViewById(R.id.txtv_totalCalories);
        txtv_totalDistance = (TextView) view.findViewById(R.id.txtv_totalDistance);
        txtv_totalSleep = (TextView) view.findViewById(R.id.txtv_totalSleep);


        txtv_stepsWeeklyAverage = (TextView) view.findViewById(R.id.txtv_stepsWeeklyAverage);
        txtv_caloriesWeeklyAverage = (TextView) view.findViewById(R.id.txtv_caloriesWeeklyAverage);
        txtv_distanceWeeklyAverage = (TextView) view.findViewById(R.id.txtv_distanceWeeklyAverage);
        txtv_sleepWeeklyAverage = (TextView) view.findViewById(R.id.txtv_sleepWeeklyAverage);


        mChart1 = (BarChart) view.findViewById(R.id.chart1);
        mChart2 = (BarChart) view.findViewById(R.id.chart2);
        mChart3 = (BarChart) view.findViewById(R.id.chart3);
        mChart4 = (BarChart) view.findViewById(R.id.chart4);


        Calendar calendar = Calendar.getInstance();
      //  Log.v("Current Week", String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));


        XAxis xAxis1 = mChart1.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxis2 = mChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxis3 = mChart3.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxis4 = mChart4.getXAxis();
        xAxis4.setPosition(XAxis.XAxisPosition.BOTTOM);


        getStartEndOFWeek(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
    }


    void getStartEndOFWeek(int enterWeek, int enterYear)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
       // System.out.println("...date..." + startDateInStr);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
       // System.out.println("...date..." + endDaString);


//        Log.e("Missing List", "-----" + myUtil.getDates(startDateInStr, endDaString));


        setData(myUtil.getDates(startDateInStr, endDaString));

    }


    public void setData(List<String> dates)
    {

       // Log.e(TAG,"List of week days----"+dates);


        // STEPS

        int totalSteps = 0;

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

                    totalSteps += steps;
                }
            }
        }

//        Log.e(TAG, "MAP----" + stepsMap);

        ArrayList<String> xAxisValues = getXAxisWeakValues();


//        percentage(MyConstant.STEPS,dates.get(0), (float)stepsMap.get(dates.get(0)));
        ;


        ArrayList<BarEntry> yAxisStepData = new ArrayList<>();

        yAxisStepData.add(new BarEntry(percentage(MyConstant.STEPS,dates.get(0),(float)stepsMap.get(dates.get(0))), 0));
        yAxisStepData.add(new BarEntry(percentage(MyConstant.STEPS,dates.get(1),(float)stepsMap.get(dates.get(1))), 1));
        yAxisStepData.add(new BarEntry(percentage(MyConstant.STEPS,dates.get(2),(float)stepsMap.get(dates.get(2))), 2));
        yAxisStepData.add(new BarEntry(percentage(MyConstant.STEPS,dates.get(3),(float)stepsMap.get(dates.get(3))), 3));
        yAxisStepData.add(new BarEntry(percentage(MyConstant.STEPS,dates.get(4),(float)stepsMap.get(dates.get(4))), 4));
        yAxisStepData.add(new BarEntry(percentage(MyConstant.STEPS,dates.get(5),(float)stepsMap.get(dates.get(5))), 5));
        yAxisStepData.add(new BarEntry(percentage(MyConstant.STEPS,dates.get(6),(float)stepsMap.get(dates.get(6))), 6));
//        yAxisStepData.add(new BarEntry(stepsMap.get(dates.get(6)), 6));


        drawGraph(mChart1, yAxisStepData, xAxisValues, R.color.colorYellowNew);


        // Set Data
        txtv_totalSteps.setText(String.valueOf(totalSteps));
        txtv_stepsWeeklyAverage.setText(String.valueOf(totalSteps / 7));


        // CALORIES

        ArrayList<BarEntry> yAxisCaloriesData = new ArrayList<>();



//        percentage(MyConstant.CALORIES,dates.get(0),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(0)))));

        yAxisCaloriesData.add(new BarEntry(percentage(MyConstant.CALORIES,dates.get(0),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(0))))), 0));
        yAxisCaloriesData.add(new BarEntry(percentage(MyConstant.CALORIES,dates.get(1),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(1))))), 1));
        yAxisCaloriesData.add(new BarEntry(percentage(MyConstant.CALORIES,dates.get(2),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(2))))), 2));
        yAxisCaloriesData.add(new BarEntry(percentage(MyConstant.CALORIES,dates.get(3),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(3))))), 3));
        yAxisCaloriesData.add(new BarEntry(percentage(MyConstant.CALORIES,dates.get(4),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(4))))), 4));
        yAxisCaloriesData.add(new BarEntry(percentage(MyConstant.CALORIES,dates.get(5),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(5))))), 5));
        yAxisCaloriesData.add(new BarEntry(percentage(MyConstant.CALORIES,dates.get(6),Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(6))))), 6));



//        yAxisCaloriesData.add(new BarEntry(Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(0)))), 0));
//        yAxisCaloriesData.add(new BarEntry(Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(1)))), 1));
//        yAxisCaloriesData.add(new BarEntry(Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(2)))), 2));
//        yAxisCaloriesData.add(new BarEntry(Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(3)))), 3));
//        yAxisCaloriesData.add(new BarEntry(Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(4)))), 4));
//        yAxisCaloriesData.add(new BarEntry(Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(5)))), 5));
//        yAxisCaloriesData.add(new BarEntry(Float.parseFloat(myUtil.stepsToCalories(context, stepsMap.get(dates.get(6)))), 6));


        drawGraph(mChart2, yAxisCaloriesData, xAxisValues, R.color.colorBlueNew);


        txtv_totalCalories.setText(String.valueOf(Math.round(Float.parseFloat(myUtil.stepsToCalories(context, totalSteps)))));
        txtv_caloriesWeeklyAverage.setText(String.valueOf(Math.round(Float.parseFloat(myUtil.stepsToCalories(context, totalSteps / 7)))));


        // DISTANCE

        ArrayList<BarEntry> yAxisDistanceData = new ArrayList<>();

        yAxisDistanceData.add(new BarEntry(percentage(MyConstant.DISTANCE,dates.get(0),Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(0))))), 0));
        yAxisDistanceData.add(new BarEntry(percentage(MyConstant.DISTANCE,dates.get(1),Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(1))))), 1));
        yAxisDistanceData.add(new BarEntry(percentage(MyConstant.DISTANCE,dates.get(2),Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(2))))), 2));
        yAxisDistanceData.add(new BarEntry(percentage(MyConstant.DISTANCE,dates.get(3),Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(3))))), 3));
        yAxisDistanceData.add(new BarEntry(percentage(MyConstant.DISTANCE,dates.get(4),Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(4))))), 4));
        yAxisDistanceData.add(new BarEntry(percentage(MyConstant.DISTANCE,dates.get(5),Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(5))))), 5));
        yAxisDistanceData.add(new BarEntry(percentage(MyConstant.DISTANCE,dates.get(6),Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(6))))), 6));

//        yAxisDistanceData.add(new BarEntry(Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(0)))), 0));
//        yAxisDistanceData.add(new BarEntry(Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(1)))), 1));
//        yAxisDistanceData.add(new BarEntry(Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(2)))), 2));
//        yAxisDistanceData.add(new BarEntry(Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(3)))), 3));
//        yAxisDistanceData.add(new BarEntry(Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(4)))), 4));
//        yAxisDistanceData.add(new BarEntry(Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(5)))), 5));
//        yAxisDistanceData.add(new BarEntry(Float.parseFloat(myUtil.stepsToDistance(context, stepsMap.get(dates.get(6)))), 6));


        drawGraph(mChart3, yAxisDistanceData, xAxisValues, R.color.colorOrangeNew);


        txtv_totalDistance.setText(String.valueOf(Float.parseFloat(myUtil.stepsToDistance(context, totalSteps))));
        txtv_distanceWeeklyAverage.setText(String.valueOf(Float.parseFloat(myUtil.stepsToDistance(context, totalSteps / 7))));


        // SLEEP


        long t0 = myDatabase.getSleepMillisOnDate(dates.get(0));
        long t1 = myDatabase.getSleepMillisOnDate(dates.get(1));
        long t2 = myDatabase.getSleepMillisOnDate(dates.get(2));
        long t3 = myDatabase.getSleepMillisOnDate(dates.get(3));
        long t4 = myDatabase.getSleepMillisOnDate(dates.get(4));
        long t5 = myDatabase.getSleepMillisOnDate(dates.get(5));
        long t6 = myDatabase.getSleepMillisOnDate(dates.get(6));

        String all = myUtil.convertMillisToHrMins(t0 + t1 + t2 + t3 + t4 + t5 + t6);
        String average = myUtil.convertMillisToHrMins((t0 + t1 + t2 + t3 + t4 + t5 + t6) / 7);


        ArrayList<BarEntry> yAxisSleepData = new ArrayList<>();


        yAxisSleepData.add(new BarEntry(percentage(MyConstant.SLEEP,dates.get(0),(float)t0), 0));
        yAxisSleepData.add(new BarEntry(percentage(MyConstant.SLEEP,dates.get(1),(float)t1), 1));
        yAxisSleepData.add(new BarEntry(percentage(MyConstant.SLEEP,dates.get(2),(float)t2), 2));
        yAxisSleepData.add(new BarEntry(percentage(MyConstant.SLEEP,dates.get(3),(float)t3), 3));
        yAxisSleepData.add(new BarEntry(percentage(MyConstant.SLEEP,dates.get(4),(float)t4), 4));
        yAxisSleepData.add(new BarEntry(percentage(MyConstant.SLEEP,dates.get(5),(float)t5), 5));
        yAxisSleepData.add(new BarEntry(percentage(MyConstant.SLEEP,dates.get(6),(float)t6), 6));

//        yAxisSleepData.add(new BarEntry(convertMillisToDecimalHr(t0), 0));
//        yAxisSleepData.add(new BarEntry(convertMillisToDecimalHr(t1), 1));
//        yAxisSleepData.add(new BarEntry(convertMillisToDecimalHr(t2), 2));
//        yAxisSleepData.add(new BarEntry(convertMillisToDecimalHr(t3), 3));
//        yAxisSleepData.add(new BarEntry(convertMillisToDecimalHr(t4), 4));
//        yAxisSleepData.add(new BarEntry(convertMillisToDecimalHr(t5), 5));
//        yAxisSleepData.add(new BarEntry(convertMillisToDecimalHr(t6), 6));


        drawGraph(mChart4, yAxisSleepData, xAxisValues, R.color.colorGreenNew);


        txtv_totalSleep.setText(all);
        txtv_sleepWeeklyAverage.setText(average);


    }


    public float percentage(String type,String date, float obtained)
    {

       // Log.e(TAG, "--Type---"+type+"----date---"+date+"-----obtained---=-" + obtained);

        HashMap<String, String> mapGoal = myDatabase.getGoalDataOnDateIfExistsOrNot(date);

//        Log.e(TAG, "---------Database  Goal Data   " + mapGoal);


        float total=0.0f;


        if(MyConstant.STEPS.equals(type))
        {

            total = Float.parseFloat(mapGoal.get(MyConstant.STEPS));
        }
        else  if(MyConstant.CALORIES.equals(type))
        {

            total = Float.parseFloat(mapGoal.get(MyConstant.CALORIES));
        }
        else if (MyConstant.DISTANCE.equals(type))
        {

            total = Float.parseFloat(mapGoal.get(MyConstant.DISTANCE));
        }
        else if (MyConstant.SLEEP.equals(type))
        {

            total = Integer.parseInt(mapGoal.get(MyConstant.SLEEP))*60*60*1000;
        }

        float percentage = (obtained*100)/total;


//        Log.e(TAG, "--Type---"+type+"----obtained---"+obtained+"-----percentage data    " + percentage);


        return percentage;

    }


    public float convertMillisToDecimalHr(long millis)
    {
        float time = 0.0f;
        try
        {
            SimpleDateFormat myFormat = new SimpleDateFormat("HH.mm");

            int Hours = (int) (millis / (1000 * 60 * 60));
            int Mins = (int) (millis / (1000 * 60)) % 60;

            String diff = Hours + "." + Mins;
//            time = myFormat.format(myFormat.parse(diff));
            time = Float.parseFloat(myFormat.format(myFormat.parse(diff)));


        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return time;
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {


//            case R.id.linearLayout_refresh:
//
//                break;
//
//            case R.id.linearLayout_connect_disconnect:
//
//
//                break;

        }
    }


    public void drawGraph(BarChart mChart, ArrayList<BarEntry> yAxisValues, ArrayList<String> xAxisValues, int color)
    {


        BarDataSet data = new BarDataSet(yAxisValues, "");
        data.setColor(ContextCompat.getColor(context, color));


        BarData data1 = new BarData(xAxisValues, data);


        mChart.setData(data1);
        mChart.getLegend().setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);

        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(false);


        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setEnabled(false);


        mChart.setPinchZoom(false);
        mChart.setDescription("");
        mChart.animateXY(2000, 2000);
        mChart.invalidate();
    }


    private ArrayList<String> getXAxisWeakValues()
    {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Sun");
        xAxis.add("Mon");
        xAxis.add("Tue");
        xAxis.add("Wed");
        xAxis.add("Thu");
        xAxis.add("Fri");
        xAxis.add("Sat");


        return xAxis;
    }
}
