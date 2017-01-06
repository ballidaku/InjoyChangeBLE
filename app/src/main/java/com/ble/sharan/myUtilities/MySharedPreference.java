package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by brst-pc93 on 12/20/16.
 */

public class MySharedPreference
{
    public final String PreferenceName = "MyPreference";

    public MySharedPreference()
    {
    }

    public static MySharedPreference instance = null;

    public static MySharedPreference getInstance()
    {
        if (instance == null)
        {
            instance = new MySharedPreference();
        }
        return instance;
    }

    public SharedPreferences getPreference(Context context)
    {
        return context.getSharedPreferences(PreferenceName, Activity.MODE_PRIVATE);
    }

    public void saveDeviceAddress(Context context, String deviceAddress)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.DEVICE_ADDRESS, deviceAddress);
        editor.apply();
    }


    public String getDeviceAddress(Context context)
    {
        return getPreference(context).getString(MyConstant.DEVICE_ADDRESS, "");
    }


    public void saveIsConnectedNow(Context context, boolean isConnected)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(MyConstant.IS_CONNECTED, isConnected);
        editor.apply();
    }

    public boolean getIsConnectedNow(Context context)
    {
        return getPreference(context).getBoolean(MyConstant.IS_CONNECTED, false);
    }



    //**********************************************************************************************
    // Clear Connection Data
    //**********************************************************************************************

    public void clearConnectionData(Context context)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.remove(MyConstant.IS_CONNECTED);
        editor.remove(MyConstant.DEVICE_ADDRESS);
        editor.apply();
    }

    //**********************************************************************************************
    //**********************************************************************************************







    public void saveIsManualDisconnected(Context context, boolean isTrue)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(MyConstant.IS_MANUAL_DISCONNECTED, isTrue);
        editor.apply();
    }

    public boolean getIsManualDisconnected(Context context)
    {
        return getPreference(context).getBoolean(MyConstant.IS_MANUAL_DISCONNECTED, false);
    }


    public void saveAlarm(Context context,String alarmKey, String alarm)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();

        editor.putString(alarmKey, alarm);


        editor.apply();
    }


    public void deleteAlarm(Context context, String alarm)
    {

        String  alarm1 = getPreference(context).getString(MyConstant.ALARM_ONE, "");
        String  alarm2 = getPreference(context).getString(MyConstant.ALARM_TWO, "");
        String  alarm3 = getPreference(context).getString(MyConstant.ALARM_THREE, "");


        SharedPreferences.Editor editor = getPreference(context).edit();

        if(alarm1.equals(alarm))
        {
            editor.putString(MyConstant.ALARM_ONE, "");
        }
        else if(alarm2.equals(alarm))
        {
            editor.putString(MyConstant.ALARM_TWO, "");
        }
        else if(alarm3.equals(alarm))
        {
            editor.putString(MyConstant.ALARM_THREE, "");
        }

        editor.apply();
    }

    public HashMap<String,String> getAlarm(Context context)
    {
        HashMap<String,String> map=new HashMap<>();

        map.put(MyConstant.ALARM_ONE,getPreference(context).getString(MyConstant.ALARM_ONE, ""));
        map.put(MyConstant.ALARM_TWO,getPreference(context).getString(MyConstant.ALARM_TWO, ""));
        map.put(MyConstant.ALARM_THREE,getPreference(context).getString(MyConstant.ALARM_THREE, ""));

       return map;
    }


    //**********************************************************************************************
    // Pofile
    //**********************************************************************************************


    public void saveName(Context context, String name)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.NAME, name);
        editor.apply();
    }

    public String getName(Context context)
    {
        return getPreference(context).getString(MyConstant.NAME, "");
    }

    public void saveHeight(Context context, String height)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.HEIGHT, height);
        editor.apply();
    }

    public String getHeight(Context context)
    {
        return getPreference(context).getString(MyConstant.HEIGHT, "172")+" Cm";
    }


    public void saveWeight(Context context, String weight)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.WEIGHT, weight);
        editor.apply();
    }

    public String getWeight(Context context)
    {
        return getPreference(context).getString(MyConstant.WEIGHT, "60")+" Kg";
    }

    public void saveStride(Context context, String stride)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.STRIDE, stride);
        editor.apply();
    }

    public String getStride(Context context)
    {
        return getPreference(context).getString(MyConstant.STRIDE, "45")+" Cm";
    }

    public void saveGender(Context context, String gender)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.GENDER, gender);
        editor.apply();
    }

    public String getGender(Context context)
    {
        return getPreference(context).getString(MyConstant.GENDER, "");
    }

    public void saveDistanceUnit(Context context, String distanceUnit)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.DISTANCE_UNIT, distanceUnit);
        editor.apply();
    }

    public String getDistanceUnit(Context context)
    {
        return getPreference(context).getString(MyConstant.DISTANCE_UNIT, "");
    }


    public void saveWeightUnit(Context context, String distanceUnit)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.WEIGHT_UNIT, distanceUnit);
        editor.apply();
    }

    public String getWeightUnit(Context context)
    {
        return getPreference(context).getString(MyConstant.WEIGHT_UNIT, "");
    }




    //**********************************************************************************************
    // Set Goals
    //**********************************************************************************************


    // DAILY STEPS
    public void setDailySteps(Context context, String steps)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.STEPS, steps);
        editor.apply();
    }

    public String getDailySteps(Context context)
    {
        return getPreference(context).getString(MyConstant.STEPS,"1000");
    }
    //**********************************************************************************************

    // DAILY MILES
    public void setDailyMiles(Context context, String miles)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.MILES, miles);
        editor.apply();
    }

    public String getDailyMiles(Context context)
    {
        return getPreference(context).getString(MyConstant.MILES,"2.5")+ " per day";
    }
    //**********************************************************************************************


    // DAILY CALORIES
    public void setDailyCalories(Context context, String calories)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.CALORIES, calories);
        editor.apply();
    }

    public String getDailyCalories(Context context)
    {
        return getPreference(context).getString(MyConstant.CALORIES,"1000")+" per day";
    }
    //**********************************************************************************************

    // DAILY SLEEP
    public void setDailySleep(Context context, String sleep)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.SLEEP, sleep);
        editor.apply();
    }

    public String getDailySleep(Context context)
    {
        return getPreference(context).getString(MyConstant.SLEEP,"7")+" hour per day";

    }
    //**********************************************************************************************




}
