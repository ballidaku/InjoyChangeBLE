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

//        String  alarm1 = getPreference(context).getString(MyConstant.ALARM_ONE, "");
//        String  alarm2 = getPreference(context).getString(MyConstant.ALARM_TWO, "");
//        String  alarm3 = getPreference(context).getString(MyConstant.ALARM_THREE, "");


        SharedPreferences.Editor editor = getPreference(context).edit();


        editor.putString(alarmKey, alarm);
//        if(alarm1.isEmpty())
//        {
//            editor.putString(MyConstant.ALARM_ONE, alarm);
//        }
//        else if(alarm2.isEmpty())
//        {
//            editor.putString(MyConstant.ALARM_TWO, alarm);
//        }
//        else if(alarm3.isEmpty())
//        {
//            editor.putString(MyConstant.ALARM_THREE, alarm);
//        }

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


}
