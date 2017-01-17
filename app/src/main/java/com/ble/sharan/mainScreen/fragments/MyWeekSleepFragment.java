package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.MyWeekSleepAdapter;
import com.ble.sharan.myUtilities.MyDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 1/11/17.
 */

public class MyWeekSleepFragment extends Fragment /*implements View.OnClickListener*/
{

    String TAG = MyWeekSleepFragment.class.getSimpleName()
 ;


    ListView listView_weekSleepRecords;

    MyWeekSleepAdapter myWeekSleepAdapter;

    MyDatabase myDatabase;

    Context context;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        myDatabase= new MyDatabase(context);

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_myweeksleep, container, false);

            setUpIds();

        }

        return view;
    }



    private void setUpIds()
    {

        listView_weekSleepRecords=(ListView)view.findViewById(R.id.listView_weekSleepRecords);


        updateUI();
    }



    private void updateUI()
    {
        ArrayList<HashMap<String,String>> list = myDatabase.getAllSleepData();
//        List<BeanSleepRecord> list = new ArrayList<>();

//        list.add(new BeanSleepRecord("12/11","3 Hrs. 23 Mins"));
//        list.add(new BeanSleepRecord("12/11","3 Hrs. 23 Mins"));
//        list.add(new BeanSleepRecord("12/11","3 Hrs. 23 Mins"));
//        list.add(new BeanSleepRecord("12/11","3 Hrs. 23 Mins"));
//        list.add(new BeanSleepRecord("12/11","3 Hrs. 23 Mins"));


        myWeekSleepAdapter=new MyWeekSleepAdapter(context,list);

        listView_weekSleepRecords.setAdapter(myWeekSleepAdapter);

    }




}
