package com.ble.sharan.mainScreen.activities;

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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.MyUartService;
import com.ble.sharan.R;
import com.ble.sharan.adapters.DrawerList_Adapter;
import com.ble.sharan.mainScreen.fragments.AboutUsFragment;
import com.ble.sharan.mainScreen.fragments.AlarmFragment;
import com.ble.sharan.mainScreen.fragments.FragmentDrawer;
import com.ble.sharan.mainScreen.fragments.HomeFragmentNew;
import com.ble.sharan.mainScreen.fragments.ProfileFragment;
import com.ble.sharan.mainScreen.fragments.TestingFragment;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by brst-pc93 on 12/27/16.
 */

public class MainActivityNew extends AppCompatActivity implements View.OnClickListener
{

    public static final String TAG = MainActivityNew.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 2;

    public String BLE_STATUS = MyConstant.DISCONNECTED;

    public String stepsTaken="0";

    public String deviceName="";


    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    FragmentDrawer drawerFragment;
    ListView listv_drawer;
    ArrayList<String> listDataHeader;

    DrawerList_Adapter drawer_adapter;
    Context context;

    FrameLayout frame_layout_profile;
    Fragment fragment;

    boolean isDisconnectedByRange = false;


    ReconnectTimer reconnectTimer;


    //Bluetooth
    private MyUartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;


    MyDialogs myDialogs = new MyDialogs();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = this;

        setUpIds();
        prepareListData();


        // Bluetooth
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null)
        {
            MyUtil.showToast(context, "Bluetooth is not available");
        }

        service_init();

        reconnectTimer = new ReconnectTimer(6000, 3500);


    }

    private void setUpIds()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.findViewById(R.id.imgv_refresh).setOnClickListener(this);

        listv_drawer = (ListView) findViewById(R.id.listv_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        frame_layout_profile = (FrameLayout) findViewById(R.id.frame_layout_profile);
        frame_layout_profile.setOnClickListener(this);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    private void prepareListData()
    {
        listDataHeader = new ArrayList<>();

        listDataHeader.add("Home");
        listDataHeader.add("Alarm");
        listDataHeader.add("Testing");
        listDataHeader.add("Invite Friends");
        listDataHeader.add("Share App");


        drawer_adapter = new DrawerList_Adapter(context, listDataHeader, 0);
        listv_drawer.setAdapter(drawer_adapter);

        listv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                displayView(position);

                drawer_adapter.changeSelectedBackground(position);
                drawer_adapter.notifyDataSetChanged();
            }
        });

        displayView(0);

    }

    public void displayView(int groupPosition)
    {


        switch (groupPosition)
        {

            case 0:
                fragment = new HomeFragmentNew();
                break;


            case 1:
                fragment = new AlarmFragment();
                break;

            case 2:
                fragment = new TestingFragment();
                break;

            case 3:
                fragment = new AboutUsFragment();
                break;

            case 4:
                fragment = new AboutUsFragment();
                break;


            default:
                break;
        }


        changeFragment(fragment);


    }


    private void changeFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.frame_layout_profile:

//                txt_titleTV.setText("Profile");
                fragment = new ProfileFragment();
                changeFragment(fragment);

                break;

            case R.id.imgv_refresh:

              /* if( fragment instanceof Pedometer)
               {
                   ((Pedometer)fragment).resetValues(true);
               }*/
                break;

            default:
                break;
        }
    }


    //**********************************************************************************************
    // Bluetooth
    //**********************************************************************************************

    private void service_init()
    {
        Intent bindIntent = new Intent(context, MyUartService.class);
        context.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(context).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyUartService.ACTION_GATT_CONNECTING);
        intentFilter.addAction(MyUartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(MyUartService.ACTION_GATT_DISCONNECTING);
        intentFilter.addAction(MyUartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(MyUartService.ACTION_GATT_DISCONNECTED_DUE_TO_RANGE);
        intentFilter.addAction(MyUartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(MyUartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(MyUartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }


    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder rawBinder)
        {
            mService = ((MyUartService.LocalBinder) rawBinder).getService();
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


    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver()
    {

        public void onReceive(final Context context, Intent intent)
        {
            String action = intent.getAction();

            Log.e(TAG, "Action : " + action);

            if (action.equals(MyUartService.ACTION_GATT_CONNECTING))
            {
                if (!isDisconnectedByRange)
                {
//                    txtv_connect_disconnect.setText("Connecting...");
//                    txtv_deviceName.setText(deviceName + " : Connecting...");
//
                    Log.e(TAG, "Connecting");

                    BLE_STATUS = MyConstant.CONNECTING;

                    if (fragment instanceof HomeFragmentNew)
                    {
                        //((HomeFragmentNew) fragment).connecting(mDevice.getName());

                        ((HomeFragmentNew) fragment).bleStatus(BLE_STATUS);
                    }
                }

            }
            else if (action.equals(MyUartService.ACTION_GATT_DISCONNECTING))
            {
//                txtv_connect_disconnect.setText("Disconnecting...");
//                txtv_deviceName.setText(deviceName + " : Disconnecting...");

                Log.e(TAG, "Disconnecting");

                BLE_STATUS = MyConstant.DISCONNECTING;

                if (fragment instanceof HomeFragmentNew)
                {
                   // ((HomeFragmentNew) fragment).disconnecting(mDevice.getName());

                    ((HomeFragmentNew) fragment).bleStatus(BLE_STATUS);
                }

            }
            else if (action.equals(MyUartService.ACTION_GATT_CONNECTED))
            {
              /*  getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {*/
                Log.e(TAG, "Connected");

                BLE_STATUS = MyConstant.CONNECTED;


                if (fragment instanceof HomeFragmentNew)
                {
                    ((HomeFragmentNew) fragment).bleStatus(BLE_STATUS);
                }


                MyUtil.showToast(context, "Device Connected");

                MySharedPreference.getInstance().saveIsConnectedNow(context, true);

            }
            else if (action.equals(MyUartService.ACTION_GATT_DISCONNECTED_DUE_TO_RANGE))
            {
                Log.e(TAG, "Disconnected due to range");

                isDisconnectedByRange = true;

                BLE_STATUS = MyConstant.DISCONNECTED;


                if (fragment instanceof HomeFragmentNew)
                {
                    ((HomeFragmentNew) fragment).bleStatus(BLE_STATUS);
                }


                mService.close();

                // If Connection Lost
                MySharedPreference.getInstance().saveIsConnectedNow(context, false);

                reconnectTimer.start();
            }
            else if (action.equals(MyUartService.ACTION_GATT_DISCONNECTED))
            {
                isDisconnectedByRange = false;

                Log.e(TAG, "Disconnected");

                BLE_STATUS = MyConstant.DISCONNECTED;

                if (fragment instanceof HomeFragmentNew)
                {
                    ((HomeFragmentNew) fragment).bleStatus(BLE_STATUS);
                }

                MyUtil.showToast(context, "Device Disconnected");

                mService.close();
            }
            else if (action.equals(MyUartService.ACTION_GATT_SERVICES_DISCOVERED))
            {
                mService.enableTXNotification();

                MyCountDownTimer countDownTimer = new MyCountDownTimer(2000, 1000);
                countDownTimer.start();

            }
            else if (action.equals(MyUartService.ACTION_DATA_AVAILABLE))
            {

                final byte[] txValue = intent.getByteArrayExtra(MyUartService.EXTRA_DATA);
         /*       getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {*/
                try
                {
                    if (fragment instanceof HomeFragmentNew)
                    {
                        ((HomeFragmentNew) fragment).calculate(stepsTaken=new String(txValue, "UTF-8"));
                    }

                } catch (Exception e)
                {
                    Log.e(TAG, e.toString());
                }
//                    }
//                });
            }
            else if (action.equals(MyUartService.DEVICE_DOES_NOT_SUPPORT_UART))
            {
                MyUtil.showToast(context, "Device doesn't support UART. Disconnecting");
                mService.disconnect();
            }
        }
    };



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
            }
            myDialogs.dialog.dismiss();

        }
    };


    public void connectDisconnect()
    {
        if (!mBtAdapter.isEnabled())
        {
            Log.i(TAG, "onClick - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        else
        {
//            if (txtv_connect_disconnect.getText().equals("Connect"))

            if (BLE_STATUS.equals(MyConstant.DISCONNECTED))
            {
                myDialogs.bleDeviceAvailable(context, mDeviceClickListener);
            }
            else
            {
                //Disconnect button pressed
                if (mDevice != null)
                {
                    mService.disconnect();
                }
            }
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
            getTotalSteps();
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
        }
    }

    public void getTotalSteps()
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

    public void setDateTimeORAlarm(String dateTime)
    {
        try
        {
            byte[] value = dateTime.getBytes("UTF-8");

            mService.writeRXCharacteristic(value);

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }



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


    //**********************************************************************************************
    //**********************************************************************************************


    @Override
    protected void onResume()
    {
        super.onResume();

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
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
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


    //**********************************************************************************************
    //**********************************************************************************************


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK)
                {
                    MyUtil.showToast(context, "Bluetooth has turned on ");
                }
                else
                {
                    MyUtil.showToast(context, "Problem in BT Turning ON ");
                }
                break;

            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }

}