package com.ble.sharan.myUtilities;

import android.content.Context;
import android.util.Log;

import com.ble.sharan.mainScreen.activities.MainActivityNew;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by brst-pc93 on 2/22/17.
 */

public class GetStepsData
{
    public static final String TAG = GetStepsData.class.getSimpleName();

    Context context;

    public int count=0;

    MyDatabase myDatabase;

    String[] command ={"d0","d1","d2","d3","d4","d5","d6","d7","d8","d9","da","db","dc","dd","de"};

    public GetStepsData(Context context)
    {
        this.context=context;

        myDatabase = new MyDatabase(context);
    }


    public void st()
    {

        Thread thread =new Thread(runnable);
        thread.start();
    }


    Runnable runnable=new Runnable()
    {
        @Override
        public void run()
        {
           // Log.e(TAG,"GetStepsData"+command[count]);


            ((MainActivityNew)context).commandToBLE(command[count],new BleResponseInterface()
            {
                @Override
                public void onResponse(String result)
                {

                    Log.e(TAG,"onResponse     "+result);


                    if(count <= 13 && !result.contains("No Record"))
                    {
                        String[] commandDateSteps = result.split(":");
                        String[] dareSteps=commandDateSteps[1].split(",");



                        SimpleDateFormat input = new SimpleDateFormat("yy-MM-dd");
                        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                        String date = "";
                        try
                        {
                            date =output.format(input.parse(dareSteps[0]));

                            myDatabase.addStepData(context,new BeanRecords(date, String.valueOf(Integer.parseInt(dareSteps[1]))));

                            Log.e(TAG,"Date---"+date+"  Steps----"+ String.valueOf(Integer.parseInt(dareSteps[1])));
                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                        }

                        count++;
                        st();
                    }
                    else if(result.contains("d14") ||result.contains("No Record"))
                    {
                        clearMemory();
                    }

                }
            });
        }
    };


    public void clearMemory()
    {
        //To clear steps data
        ((MainActivityNew)context).commandToBLE("clrRecord");
    }
    }
