package com.ble.sharan.myUtilities;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by brst-pc93 on 1/24/17.
 */

public class MyApplication extends Application
{

    private static MyApplication mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        mInstance = this;
    }

    public static synchronized MyApplication getInstance()
    {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener)
    {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}