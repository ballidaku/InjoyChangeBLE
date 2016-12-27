package com.ble.sharan.mainScreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;
import com.ble.sharan.myUtilities.MyConstant;

/**
 * Created by brst-pc93 on 12/27/16.
 */

public class HomeFragmentNew extends Fragment implements View.OnClickListener
{
    //    private static final int REQUEST_SELECT_DEVICE = 1;
//    private static final int REQUEST_ENABLE_BT = 2;
    //    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = HomeFragmentNew.class.getSimpleName();
//    private static final int UART_PROFILE_CONNECTING = 19;
//    private static final int UART_PROFILE_CONNECTED = 20;
//    private static final int UART_PROFILE_DISCONNECTED = 21;
//    private static final int UART_PROFILE_DISCONNECTING = 22;
//    private static final int STATE_OFF = 10;


    TextView txtv_connect_disconnect;
    TextView txtv_calories;
    TextView txtv_stepsTaken;
    TextView txtv_distance;
    TextView txtv_refresh;
    TextView txtv_deviceName;


    View view = null;

    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_home, container, false);

            setUpIds();
        }

        return view;
    }


    private void setUpIds()
    {

        (txtv_connect_disconnect = (TextView) view.findViewById(R.id.txtv_connect_disconnect)).setOnClickListener(this);
        txtv_calories = (TextView) view.findViewById(R.id.txtv_calories);
        txtv_stepsTaken = (TextView) view.findViewById(R.id.txtv_stepsTaken);
        txtv_distance = (TextView) view.findViewById(R.id.txtv_distance);

        txtv_deviceName = (TextView) view.findViewById(R.id.txtv_deviceName);


        (txtv_refresh = (TextView) view.findViewById(R.id.txtv_refresh)).setOnClickListener(this);
        txtv_refresh.setEnabled(false);



    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_refresh:

                ((MainActivityNew)context).getTotalSteps();

                break;


            case R.id.txtv_connect_disconnect:

                ((MainActivityNew)context).connectDisconnect();

                break;
        }
    }


    //**********************************************************************************************
    // Update UI
    //**********************************************************************************************


    public void bleStatus(String BLE_STATUS)
    {
        String deviceName=((MainActivityNew)context).deviceName;

        if(BLE_STATUS.equals(MyConstant.CONNECTING))
        {
            txtv_connect_disconnect.setText("Connecting...");
            txtv_deviceName.setText(deviceName + " : Connecting...");
        }
        else if(BLE_STATUS.equals(MyConstant.DISCONNECTING))
        {
            txtv_connect_disconnect.setText("Disconnecting...");
            txtv_deviceName.setText(deviceName + " : Disconnecting...");
        }
        else if(BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            txtv_connect_disconnect.setText("Disconnect");
            txtv_deviceName.setText(deviceName + " : Connected Successfully");
            txtv_refresh.setEnabled(true);
        }
        else if(BLE_STATUS.equals(MyConstant.DISCONNECTED))
        {
            txtv_connect_disconnect.setText("Connect");
            txtv_deviceName.setText("Not Connected");
            txtv_refresh.setEnabled(false);
        }
    }


    //**********************************************************************************************


    public void calculate(String text)
    {

        int steps = Integer.parseInt(text);

        txtv_stepsTaken.setText("" + steps);


        int weight = 75;

        double Energy = (weight - 15) * 0.000693 + 0.005895;
        double Calories = Energy * steps;

        txtv_calories.setText(String.valueOf((int) Calories));


        double stride = 0.000475;
        double distance = steps * stride;

        txtv_distance.setText(String.format("%.2f", distance));

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

    }

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onPause()
    {
        Log.d(TAG, "onPause");
        super.onPause();
    }


    @Override
    public void onResume()
    {
        super.onResume();


        // onRefresh get the previous Count
        calculate(((MainActivityNew)context).stepsTaken);

        bleStatus(((MainActivityNew)context).BLE_STATUS);

        Log.d(TAG, "onResume");
    }
}
