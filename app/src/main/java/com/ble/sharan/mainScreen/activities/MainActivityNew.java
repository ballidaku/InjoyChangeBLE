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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.MyUartService;
import com.ble.sharan.R;
import com.ble.sharan.adapters.DrawerList_Adapter;
import com.ble.sharan.mainScreen.fragments.AlarmFragment;
import com.ble.sharan.mainScreen.fragments.FragmentDrawer;
import com.ble.sharan.mainScreen.fragments.HealthData;
import com.ble.sharan.mainScreen.fragments.HomeFragmentNew;
import com.ble.sharan.mainScreen.fragments.MyDailyGoal;
import com.ble.sharan.mainScreen.fragments.MyWeek;
import com.ble.sharan.mainScreen.fragments.Overall;
import com.ble.sharan.mainScreen.fragments.PreviousWeek;
import com.ble.sharan.mainScreen.fragments.ProfileFragment;
import com.ble.sharan.mainScreen.fragments.Today;
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



    ImageView imgv_profile;
    TextView txtv_username;

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    FragmentDrawer drawerFragment;
    ListView listv_drawer;
    ArrayList<String> listDataHeader;

    DrawerList_Adapter drawer_adapter;
    Context context;

    LinearLayout frame_layout_profile;
    Fragment fragment;

    boolean isDisconnectedByRange = false;


    ReconnectTimer reconnectTimer;


    //Bluetooth
    private MyUartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;


    MyDialogs myDialogs = new MyDialogs();


    //    Bottom Tabs actuallt button

    View view_challenge;
    View view_data;
    View view_myinfo;
    View view_alarm;


    ImageView imgv_challenge;
    ImageView imgv_data;
    ImageView imgv_myinfo;
    ImageView imgv_alarm;


    TextView txtv_challenge;
    TextView txtv_data;
    TextView txtv_myinfo;
    TextView txtv_alarm;






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

    ImageView imgv_headerLogo;
    TextView txtv_heading;
    TextView txtv_right;

    private void setUpIds()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

//        mToolbar.findViewById(R.id.imgv_refresh).setOnClickListener(this);

        imgv_headerLogo=(ImageView) mToolbar.findViewById(R.id.imgv_headerLogo);
        txtv_heading=(TextView) mToolbar.findViewById(R.id.txtv_heading);

        (txtv_right=(TextView) mToolbar.findViewById(R.id.txtv_right)).setOnClickListener(this);

        listv_drawer = (ListView) findViewById(R.id.listv_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        imgv_profile = (ImageView) mDrawerLayout.findViewById(R.id.imgv_profile);
        txtv_username = (TextView) mDrawerLayout.findViewById(R.id.txtv_username);



        frame_layout_profile = (LinearLayout) findViewById(R.id.frame_layout_profile);
        frame_layout_profile.setOnClickListener(this);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);


        view_challenge=findViewById(R.id.view_challenge);
        view_data=findViewById(R.id.view_data);
        view_myinfo=findViewById(R.id.view_myinfo);
        view_alarm=findViewById(R.id.view_alarm);


        imgv_challenge=(ImageView)findViewById(R.id.imgv_challenge);
        imgv_data=(ImageView)findViewById(R.id.imgv_data);
        imgv_myinfo=(ImageView)findViewById(R.id.imgv_myinfo);
        imgv_alarm=(ImageView)findViewById(R.id.imgv_alarm);


        txtv_challenge=(TextView)findViewById(R.id.txtv_challenge);
        txtv_data=(TextView)findViewById(R.id.txtv_data);
        txtv_myinfo=(TextView)findViewById(R.id.txtv_myinfo);
        txtv_alarm=(TextView)findViewById(R.id.txtv_alarm);


        findViewById(R.id.linearLayout_challenge).setOnClickListener(this);
        findViewById(R.id.linearLayout_data).setOnClickListener(this);
        findViewById(R.id.linearLayout_myinfo).setOnClickListener(this);
        findViewById(R.id.linearLayout_alarm).setOnClickListener(this);



        setBottomTabSelected(view_data,imgv_data,txtv_data);

        onRefreshName();
    }

    private void setBottomTabSelected(View v, ImageView image, TextView txtv)
    {
        view_challenge.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        view_data.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        view_myinfo.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        view_alarm.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));


        imgv_challenge.setImageResource(R.mipmap.ic_challenge);
        imgv_data.setImageResource(R.mipmap.ic_data);
        imgv_myinfo.setImageResource(R.mipmap.ic_myinfo);
        imgv_alarm.setImageResource(R.mipmap.ic_alarm);

        txtv_challenge.setTextColor(ContextCompat.getColor(context,R.color.colorGray));
        txtv_data.setTextColor(ContextCompat.getColor(context,R.color.colorGray));
        txtv_myinfo.setTextColor(ContextCompat.getColor(context,R.color.colorGray));
        txtv_alarm.setTextColor(ContextCompat.getColor(context,R.color.colorGray));





        v.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
        txtv.setTextColor(ContextCompat.getColor(context,R.color.colorBlue));

        if(image == imgv_challenge)
        {
            imgv_challenge.setImageResource(R.mipmap.ic_challenge_selected);
        }
        else if(image == imgv_data)
        {
            imgv_data.setImageResource(R.mipmap.ic_data_selected);
        }
        else if(image == imgv_myinfo)
        {
            imgv_myinfo.setImageResource(R.mipmap.ic_myinfo_selected);
        }
        else if(image == imgv_alarm)
        {
            imgv_alarm.setImageResource(R.mipmap.ic_alarm_selected);
        }


    }

    private void prepareListData()
    {
        listDataHeader = new ArrayList<>();

        listDataHeader.add("Health Data");
//        listDataHeader.add("Profile");
        listDataHeader.add("Today");
        listDataHeader.add("My Week");
        listDataHeader.add("Previous Week");
        listDataHeader.add("My Daily Goal");
        listDataHeader.add("Overall");
//        listDataHeader.add("Alarm");



        drawer_adapter = new DrawerList_Adapter(context, listDataHeader, 0);
        listv_drawer.setAdapter(drawer_adapter);

        listv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                displayView(position);


            }
        });

        displayView(0);

    }

    public void displayView(int groupPosition)
    {
        imgv_headerLogo.setVisibility(View.GONE);
        txtv_right.setVisibility(View.INVISIBLE);
        txtv_heading.setVisibility(View.VISIBLE);

        if(groupPosition == 6)
        {
            txtv_heading.setText("My Info");
        }
        else if(groupPosition == 7)
        {
            txtv_heading.setText("Alarm");
        }
        else
        {
            txtv_heading.setText(listDataHeader.get(groupPosition));
            setBottomTabSelected(view_data,imgv_data,txtv_data);

        }

        drawer_adapter.changeSelectedBackground(groupPosition);
        drawer_adapter.notifyDataSetChanged();


        switch (groupPosition)
        {


            case 0:
                //fragment = new HomeFragmentNew();
                fragment = new HealthData();
                break;


            case 1:
                txtv_right.setVisibility(View.VISIBLE);
                fragment = new Today();
                break;

            case 2:
                txtv_right.setVisibility(View.VISIBLE);
                fragment = new MyWeek();
                break;


            case 3:
                fragment = new PreviousWeek();
                break;


            case 4:
                fragment = new MyDailyGoal();
                break;


            case 5:
                fragment = new Overall();
                break;


            case 6:

                fragment = new ProfileFragment();

                break;

            case 7:

                fragment = new AlarmFragment();
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



    public void onRefreshName()
    {
        txtv_username.setText(MySharedPreference.getInstance().getName(context));
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.frame_layout_profile:

//                txt_titleTV.setText("Profile");
//                fragment = new ProfileFragment();
//                changeFragment(fragment);

                break;

            case R.id.linearLayout_challenge:


                break;

            case R.id.linearLayout_data:

                setBottomTabSelected(view_data,imgv_data,txtv_data);

                displayView(0);

                break;

            case R.id.linearLayout_myinfo:

                setBottomTabSelected(view_myinfo,imgv_myinfo,txtv_myinfo);
                displayView(6);

                break;

            case R.id.linearLayout_alarm:

                setBottomTabSelected(view_alarm,imgv_alarm,txtv_alarm);
                displayView(7);

                break;

            case R.id.txtv_right:

                displayView(5);

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

                        ((HomeFragmentNew) fragment).bleStatus(BLE_STATUS);
                    }
                    else if (fragment instanceof Today)
                    {

                        ((Today) fragment).bleStatus(BLE_STATUS);
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

                    ((HomeFragmentNew) fragment).bleStatus(BLE_STATUS);
                }
                else  if (fragment instanceof Today)
                {

                    ((Today) fragment).bleStatus(BLE_STATUS);
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
                else if (fragment instanceof Today)
                {
                    ((Today) fragment).bleStatus(BLE_STATUS);
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
                else  if (fragment instanceof Today)
                {
                    ((Today) fragment).bleStatus(BLE_STATUS);
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
                else if (fragment instanceof Today)
                {
                    ((Today) fragment).bleStatus(BLE_STATUS);
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
                    else if (fragment instanceof Today)
                    {
                        ((Today) fragment).calculate(stepsTaken=new String(txValue, "UTF-8"));
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

        MySharedPreference.getInstance().clearConnectionData(context);


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