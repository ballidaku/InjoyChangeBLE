package com.ble.sharan.myUtilities;

import android.app.Application;
import android.content.Context;

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
        //
       // Fabric.with(this, new Crashlytics());

        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
//        MultiDex.install(this);
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