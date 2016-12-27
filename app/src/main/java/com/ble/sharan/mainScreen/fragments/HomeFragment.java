package com.ble.sharan.mainScreen.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.sharan.R;
import com.ble.sharan.UartService;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by brst-pc93 on 11/3/16.
 */

public class HomeFragment extends Fragment implements View.OnClickListener
{
//    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
//    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "HomeFragment";
//    private static final int UART_PROFILE_CONNECTING = 19;
//    private static final int UART_PROFILE_CONNECTED = 20;
//    private static final int UART_PROFILE_DISCONNECTED = 21;
//    private static final int UART_PROFILE_DISCONNECTING = 22;
//    private static final int STATE_OFF = 10;

//    TextView mRemoteRssiVal;






//    RadioGroup mRg;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;
    //    private ListView messageListView;
//    private ArrayAdapter<String> listAdapter;
    private TextView txtv_connect_disconnect;
//    Button btnSend;
//    private EditText edtMessage;


    TextView txtv_calories;
    TextView txtv_stepsTaken;
    TextView txtv_distance;
    TextView txtv_refresh;
    TextView txtv_deviceName;


//    ImageView imgvRefresh;

    View view = null;

    Context context;

    ReconnectTimer reconnectTimer;

    //static boolean isConnectedEarlier;


    Animation animFadein ;


    MyDialogs myDialogs = new MyDialogs();

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

            // isConnectedEarlier = false;

            view = inflater.inflate(R.layout.fragment_home, container, false);


            setUpIds();

            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBtAdapter == null)
            {
                Toast.makeText(context, "Bluetooth is not available", Toast.LENGTH_LONG).show();

            }

            service_init();


            reconnectTimer = new ReconnectTimer(6000, 3500);

        }


        // Handle Disconnect & Connect button
        txtv_connect_disconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!mBtAdapter.isEnabled())
                {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                else
                {
                    if (txtv_connect_disconnect.getText().equals("Connect"))
                    {

                        //Connect button pressed, open DeviceListActivity class, with popup windows that scan for devices


                        //connect();

                        myDialogs.bleDeviceAvailable(context, mDeviceClickListener);

                    }
                    else
                    {
                        //Disconnect button pressed
                        if (mDevice != null)
                        {
                            //MySharedPreference.getInstance().saveIsManualDisconnected(context,true);
                            mService.disconnect();


                        }
                    }
                }
            }
        });

        return view;
    }

//    private void connect()
//    {
//        Intent newIntent = new Intent(context, DeviceListActivity.class);
//        startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
//    }


    private void setUpIds()
    {

        animFadein= AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.hover_effect);

        txtv_connect_disconnect = (TextView) view.findViewById(R.id.txtv_connect_disconnect);
        txtv_calories = (TextView) view.findViewById(R.id.txtv_calories);
        txtv_stepsTaken = (TextView) view.findViewById(R.id.txtv_stepsTaken);
        txtv_distance = (TextView) view.findViewById(R.id.txtv_distance);

        txtv_deviceName = (TextView) view.findViewById(R.id.txtv_deviceName);

       // (imgvRefresh= (ImageView) view.findViewById(R.id.imgvRefresh)).setOnClickListener(this);


        (txtv_refresh = (TextView) view.findViewById(R.id.txtv_refresh)).setOnClickListener(this);
        txtv_refresh.setEnabled(false);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtv_refresh:

                getData();

                break;
        }
    }

    private void getData()
    {
        String msg = "stepR";

        try
        {
            byte[] value = msg.getBytes("UTF-8");

            mService.writeRXCharacteristic(value);


        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

    }


    private void calculate(String text)
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


    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder rawBinder)
        {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            Log.e(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize())
            {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }

        }

        public void onServiceDisconnected(ComponentName classname)
        {
            ////     mService.disconnect(mDevice);
            mService = null;
        }
    };

    String deviceName;
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            String deviceAddress = ((TextView) view.findViewById(R.id.address)).getText().toString();
            deviceName = ((TextView) view.findViewById(R.id.name)).getText().toString();

            mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

            MySharedPreference.getInstance().saveDeviceAddress(context, deviceAddress);

            // Log.e("Device Address ", "--" + deviceAddress);
            if (!deviceAddress.isEmpty() && !deviceName.isEmpty())
            {
                mService.connect(deviceAddress);
//                txtv_connect_disconnect.setText("Connecting...");
//                txtv_deviceName.setText(deviceName+ " : Connecting...");
            }

            myDialogs.dialog.dismiss();

        }
    };

    boolean isDonnectedByRange = false;
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver()
    {

        public void onReceive(final Context context, Intent intent)
        {
            String action = intent.getAction();

            Log.e(TAG, "Action : " + action);

            if (action.equals(UartService.ACTION_GATT_CONNECTING))
            {
                if (!isDonnectedByRange)
                {
                    txtv_connect_disconnect.setText("Connecting...");
                    txtv_deviceName.setText(deviceName + " : Connecting...");

                    Log.e(TAG, "Connecting");
                }

            }
            else if (action.equals(UartService.ACTION_GATT_DISCONNECTING))
            {
                txtv_connect_disconnect.setText("Disconnecting...");
                txtv_deviceName.setText(deviceName + " : Disconnecting...");

                Log.e(TAG, "Disconnecting");

            }
            else if (action.equals(UartService.ACTION_GATT_CONNECTED))
            {
              /*  getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {*/
                Log.e(TAG, "Connected");
                txtv_connect_disconnect.setText("Disconnect");
                txtv_deviceName.setText(mDevice.getName() + " : Connected Successfully");


                MyUtil.showToast(context, "Device Connected");
                // For Reconnection
                // isConnectedEarlier = true;
                MySharedPreference.getInstance().saveIsConnectedNow(context, true);
                // MySharedPreference.getInstance().saveIsManualDisconnected(context,false);


                // }
//                });
            }
            else if (action.equals(UartService.ACTION_GATT_DISCONNECTED_DUE_TO_RANGE))
            {
                isDonnectedByRange = true;
                txtv_connect_disconnect.setText("Connect");
                txtv_deviceName.setText("Not Connected");
                txtv_refresh.setEnabled(false);

                mService.close();

                // If Connection Lost
                MySharedPreference.getInstance().saveIsConnectedNow(context, false);

                // if(!MySharedPreference.getInstance().getIsManualDisconnected(context))
                // {
                reconnectTimer.start();
                // }
            }
            else if (action.equals(UartService.ACTION_GATT_DISCONNECTED))
            {
                isDonnectedByRange = false;
                /*getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {*/
                Log.e(TAG, "Disconnected");
                txtv_connect_disconnect.setText("Connect");
                txtv_deviceName.setText("Not Connected");

                MyUtil.showToast(context, "Device Disconnected");

                mService.close();

                /*    }
                });*/

            }
            else if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED))
            {
                mService.enableTXNotification();

                MyCountDownTimer countDownTimer = new MyCountDownTimer(2000, 1000);
                countDownTimer.start();

            }
            else if (action.equals(UartService.ACTION_DATA_AVAILABLE))
            {

                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
         /*       getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {*/
                try
                {
                    String text = new String(txValue, "UTF-8");

                    calculate(text);

                } catch (Exception e)
                {
                    Log.e(TAG, e.toString());
                }
//                    }
//                });
            }
            else if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART))
            {
                MyUtil.showToast(context, "Device doesn't support UART. Disconnecting");
                mService.disconnect();
            }


        }
    };

    public class ReconnectTimer extends CountDownTimer
    {
        public ReconnectTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {

            if (!MySharedPreference.getInstance().getIsConnectedNow(context))
            {
                reconnectTimer.start();
            }

        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            mService.connect(MySharedPreference.getInstance().getDeviceAddress(context));


            //  MyUtil.showToast(context, "Searching...");
            Log.e(TAG, "Searching...");

        }
    }


    public class MyCountDownTimer extends CountDownTimer
    {
        public MyCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {

            txtv_refresh.setEnabled(true);
            getData();
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
        }
    }


    private void service_init()
    {
        Intent bindIntent = new Intent(context, UartService.class);
        context.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(context).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTING);
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTING);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED_DUE_TO_RANGE);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        // sharan Work for reconnection
        reconnectTimer.cancel();
        MySharedPreference.getInstance().getPreference(context).edit().clear().apply();


        try
        {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore)
        {
            Log.e(TAG, ignore.toString());
        }
        context.unbindService(mServiceConnection);
        mService.stopSelf();
        mService = null;

    }


    public void onDestroyManual()
    {
        // sharan Work for reconnection
//        reconnectTimer.cancel();
//        MySharedPreference.getInstance().getPreference(context).edit().clear().apply();
//
//
//        try
//        {
//            LocalBroadcastManager.getInstance(context).unregisterReceiver(UARTStatusChangeReceiver);
//        } catch (Exception ignore)
//        {
//            Log.e(TAG, ignore.toString());
//        }
//        context.unbindService(mServiceConnection);
//        mService.stopSelf();
//        mService = null;
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
        Log.d(TAG, "onResume");
        if (!mBtAdapter.isEnabled())
        {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
//
//
//        Log.e("BalliDaku onResume", "" + MySharedPreference.getInstance().getIsConnectedNow(context) + "---" + isConnectedEarlier);
//
//        if (!MySharedPreference.getInstance().getIsConnectedNow(context) && isConnectedEarlier)
//        {
//            reconnectTimer.start();
//        }
//
    }

    public void onResumeManual()
    {
//        if (!mBtAdapter.isEnabled())
//        {
//            Log.i(TAG, "onResume - BT not enabled yet");
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//        }


//        Log.e("BalliDaku onResume", "" + MySharedPreference.getInstance().getIsConnectedNow(context) + "---" + isConnectedEarlier);

//        if (!MySharedPreference.getInstance().getIsConnectedNow(context) && isConnectedEarlier)
//        {
//            reconnectTimer.start();
//        }
    }

 /*   @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {

//            case REQUEST_SELECT_DEVICE:
//                //When the DeviceListActivity return, with the selected device address
//                if (resultCode == Activity.RESULT_OK && data != null)
//                {
//                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
//                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
//
//                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
//                    txtv_deviceName.setText(mDevice.getName() + " - connecting");
//                    mService.connect(deviceAddress);
//
//
//                }
//                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK)
                {
                    MyUtil.showToast(context, "Bluetooth has turned on ");

                }
                else
                {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    MyUtil.showToast(context, "Problem in BT Turning ON ");
                    //finish();
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }




   /* @Override
    public void onBackPressed()
    {
        if (mState == UART_PROFILE_CONNECTED)
        {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            showMessage("nRFUART's running in background.\n             Disconnect to exit");
        }
        else
        {
            new AlertDialog.Builder(context)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.popup_title)
                    .setMessage(R.string.popup_message)
                    .setPositiveButton(R.string.popup_yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.popup_no, null)
                    .show();
        }
    }*/
}
