package com.ble.sharan.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
        return getPreference(context).getString(MyConstant.DEVICE_ADDRESS,"");
    }


    public void saveIsConnectedNow(Context context, boolean isConnected)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(MyConstant.IS_CONNECTED, isConnected);
        editor.apply();
    }


    public boolean getIsConnectedNow(Context context)
    {
        return getPreference(context).getBoolean(MyConstant.IS_CONNECTED,false);
    }


    public void saveIsManualDisconnected(Context context, boolean isTrue)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(MyConstant.IS_MANUAL_DISCONNECTED, isTrue);
        editor.apply();
    }

    public boolean getIsManualDisconnected(Context context)
    {
        return getPreference(context).getBoolean(MyConstant.IS_MANUAL_DISCONNECTED,false);
    }



}
