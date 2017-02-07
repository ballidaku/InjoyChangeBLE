package com.ble.sharan.mainScreen.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import com.ble.sharan.adapters.DrawerListAdapter;
import com.ble.sharan.loginScreen.LoginActivity;
import com.ble.sharan.mainScreen.fragments.AlarmFragment;
import com.ble.sharan.mainScreen.fragments.Challenge;
import com.ble.sharan.mainScreen.fragments.FragmentDrawer;
import com.ble.sharan.mainScreen.fragments.HealthData;
import com.ble.sharan.mainScreen.fragments.MyDailyGoal;
import com.ble.sharan.mainScreen.fragments.MyWeek;
import com.ble.sharan.mainScreen.fragments.Overall;
import com.ble.sharan.mainScreen.fragments.ProfileFragment;
import com.ble.sharan.mainScreen.fragments.Today;
import com.ble.sharan.mainScreen.fragments.challengeFragments.CheckIn;
import com.ble.sharan.mainScreen.fragments.challengeFragments.DailyInspiration;
import com.ble.sharan.mainScreen.fragments.challengeFragments.MyPoints;
import com.ble.sharan.mainScreen.fragments.challengeFragments.ShareWin;
import com.ble.sharan.mainScreen.fragments.challengeFragments.ShoutOut;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by brst-pc93 on 12/27/16.
 */

public class MainActivityNew extends AppCompatActivity implements View.OnClickListener
{

    public static final String TAG = MainActivityNew.class.getSimpleName();


    private static final int REQUEST_SELECT_DEVICE = 1;

    private static final int REQUEST_ENABLE_BT = 2;

    public String BLE_STATUS = MyConstant.DISCONNECTED;

    public int stepsTaken = 0;

    public String deviceName = "";

    String sleepData = "";


    ImageView imgv_profile;
    TextView txtv_username;

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    FragmentDrawer drawerFragment;
    ListView listv_drawer;
    ArrayList<String> listDataHeader;

    DrawerListAdapter drawer_adapter;
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

    MyDatabase myDatabase;

    MyUtil myUtil = new MyUtil();


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

    String challengeHeadings[] = {
            "Daily Inspiration",
            "Check-In with Yourself",
            "My Points",
            "Shout Outs",
            "Tool Box",
            "Share A Win & Weekly Video"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = this;


        myDatabase = new MyDatabase(context);

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


        oneTimeDialogToConnect(context);


        // IF there is no data
        myDatabase.addDailyGoalDataFirstTime(context);

        if (myDatabase.getTodaySteps(context) == 0)
        {
            myDatabase.addStepData(context, new BeanRecords(myUtil.getTodaydate(), "0"));
        }


    }

    ImageView imgv_headerLogo;
    TextView txtv_heading;
    TextView txtv_right;

    private void setUpIds()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

//        mToolbar.findViewById(R.id.imgv_refresh).setOnClickListener(this);

        imgv_headerLogo = (ImageView) mToolbar.findViewById(R.id.imgv_headerLogo);
        txtv_heading = (TextView) mToolbar.findViewById(R.id.txtv_heading);

        (txtv_right = (TextView) mToolbar.findViewById(R.id.txtv_right)).setOnClickListener(this);

        listv_drawer = (ListView) findViewById(R.id.listv_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        imgv_profile = (ImageView) mDrawerLayout.findViewById(R.id.circularImageView_Profile);
        txtv_username = (TextView) mDrawerLayout.findViewById(R.id.txtv_username);


        frame_layout_profile = (LinearLayout) findViewById(R.id.frame_layout_profile);
        frame_layout_profile.setOnClickListener(this);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);


        view_challenge = findViewById(R.id.view_challenge);
        view_data = findViewById(R.id.view_data);
        view_myinfo = findViewById(R.id.view_myinfo);
        view_alarm = findViewById(R.id.view_alarm);


        imgv_challenge = (ImageView) findViewById(R.id.imgv_challenge);
        imgv_data = (ImageView) findViewById(R.id.imgv_data);
        imgv_myinfo = (ImageView) findViewById(R.id.imgv_myinfo);
        imgv_alarm = (ImageView) findViewById(R.id.imgv_alarm);


        txtv_challenge = (TextView) findViewById(R.id.txtv_challenge);
        txtv_data = (TextView) findViewById(R.id.txtv_data);
        txtv_myinfo = (TextView) findViewById(R.id.txtv_myinfo);
        txtv_alarm = (TextView) findViewById(R.id.txtv_alarm);


        findViewById(R.id.linearLayout_challenge).setOnClickListener(this);
        findViewById(R.id.linearLayout_data).setOnClickListener(this);
        findViewById(R.id.linearLayout_myinfo).setOnClickListener(this);
        findViewById(R.id.linearLayout_alarm).setOnClickListener(this);


        setBottomTabSelected(view_data, imgv_data, txtv_data);

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

        txtv_challenge.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        txtv_data.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        txtv_myinfo.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        txtv_alarm.setTextColor(ContextCompat.getColor(context, R.color.colorGray));


        v.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
        txtv.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));

        if (image == imgv_challenge)
        {
            imgv_challenge.setImageResource(R.mipmap.ic_challenge_selected);
        }
        else if (image == imgv_data)
        {
            imgv_data.setImageResource(R.mipmap.ic_data_selected);
        }
        else if (image == imgv_myinfo)
        {
            imgv_myinfo.setImageResource(R.mipmap.ic_myinfo_selected);
        }
        else if (image == imgv_alarm)
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
//        listDataHeader.add("Previous Week");
        listDataHeader.add("My Daily Goal");
        listDataHeader.add("Overall");
        listDataHeader.add("Sign Out");


        drawer_adapter = new DrawerListAdapter(context, listDataHeader, 0);
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


        drawer_adapter.changeSelectedBackground(groupPosition);
        drawer_adapter.notifyDataSetChanged();


        if (groupPosition == 5)// SIGN OUT
        {
//            mDrawerLayout.closeDrawers();

            MySharedPreference.getInstance().saveAccessToken(context, "");
            MySharedPreference.getInstance().removeGoalKeys(context);


            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();


            return;
        }
        else if (groupPosition == 6)
        {
            txtv_heading.setText("My Info");
        }
        else if (groupPosition == 7)
        {
            txtv_heading.setText("Alarm");
        }
        else if (groupPosition == 8)
        {
            txtv_heading.setText("Challenge");
        }
        else
        {
            txtv_heading.setText(listDataHeader.get(groupPosition));
            setBottomTabSelected(view_data, imgv_data, txtv_data);

        }

        switch (groupPosition)
        {
            case 0:
                fragment = new HealthData();
                break;


            case 1:
                fragment = new Today();
                break;

            case 2:
                txtv_right.setVisibility(View.VISIBLE);
                fragment = new MyWeek();
                break;


            case 3:
                fragment = new MyDailyGoal();
                break;


            case 4:
                fragment = new Overall();
                break;


            case 6:

                fragment = new ProfileFragment();

                break;

            case 7:

                fragment = new AlarmFragment();
                break;


            case 8:

                fragment = new Challenge();
                break;


            default:
                break;
        }


        changeFragment(fragment);


    }


    // For Challenge Fragments
    public void displayView2(int position)
    {
        txtv_heading.setText(challengeHeadings[position]);

        Fragment myFragment = null;

        switch (position)
        {
            case 0:
                myFragment = new DailyInspiration();
                break;


            case 1:
                myFragment = new CheckIn();
                break;

            case 2:
                myFragment = new MyPoints();
                break;


            case 3:
                myFragment = new ShoutOut();
                break;


            case 4:
                //  fragment = new Overall();
                break;


            case 5:

                myFragment = new ShareWin();
                break;


            default:
                break;
        }


        changeFragment2(myFragment);


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

    private void changeFragment2(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();


        if (fragment instanceof Challenge)
        {
            int count = getSupportFragmentManager().getBackStackEntryCount();

            Log.e("Count", "" + count);

            if (count == 0)
            {
                txtv_heading.setText("Challenge");
            }
        }


    }

    public void onRefreshName()
    {
        myUtil.showImageWithPicasso(context, imgv_profile, MySharedPreference.getInstance().getPhoto(context));

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


                /*String url = MyConstant.CHALLENGE_API;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/

                setBottomTabSelected(view_challenge, imgv_challenge, txtv_challenge);

                displayView(8);


                break;

            case R.id.linearLayout_data:

                setBottomTabSelected(view_data, imgv_data, txtv_data);

                displayView(0);

                break;

            case R.id.linearLayout_myinfo:

                setBottomTabSelected(view_myinfo, imgv_myinfo, txtv_myinfo);
                displayView(6);

                break;

            case R.id.linearLayout_alarm:

                setBottomTabSelected(view_alarm, imgv_alarm, txtv_alarm);
                displayView(7);

                break;

            case R.id.txtv_right:

                displayView(4);

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
                finish();
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

                    if (fragment instanceof Today)
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

                if (fragment instanceof Today)
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

                if (fragment instanceof Today)
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


                if (fragment instanceof Today)
                {
                    try
                    {
                        ((Today) fragment).bleStatus(BLE_STATUS);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }


                if (mService != null)
                {
                    mService.close();
                }

                // If Connection Lost
                MySharedPreference.getInstance().saveIsConnectedNow(context, false);

                reconnectTimer.start();
            }
            else if (action.equals(MyUartService.ACTION_GATT_DISCONNECTED))
            {
                isDisconnectedByRange = false;

                Log.e(TAG, "Disconnected");

                BLE_STATUS = MyConstant.DISCONNECTED;

                if (fragment instanceof Today)
                {
                    ((Today) fragment).bleStatus(BLE_STATUS);
                }

                MyUtil.showToast(context, "Device Disconnected");

                mService.close();
            }
            else if (action.equals(MyUartService.ACTION_GATT_SERVICES_DISCOVERED))
            {

                if (mService != null)
                {
                    mService.enableTXNotification();
                }

                SetDateCountDownTimer setDateCountDownTimer = new SetDateCountDownTimer(2000, 1000);
                setDateCountDownTimer.start();

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

                    if (COMMAND.contains("dt"))
                    {
                        //TODO
                        setHeightWeightStrideDataToBLE();
                    }
                    else if (COMMAND.contains("b"))
                    {
                        //TODO
                        commandToBLE(MyConstant.GET_STEPS);
                    }
                    else if (COMMAND.equals(MyConstant.GET_SLEEP))
                    {
                        String hex = MyUtil.bytesToHex(txValue);
                        sleepData += hex.substring(8, hex.length());

                        Log.e("remaining", "" + sleepData);

                        if (new String(txValue, "UTF-8").contains("Done"))
                        {
                            TestingSleep2(sleepData);
                        }
                    }
                    else if (COMMAND.equals(MyConstant.GET_STEPS))
                    {

                        if (fragment instanceof Today)
                        {

                            String stepsString = new String(txValue, "UTF-8");


                            try
                            {
                                if (stepsString.length() == 6)
                                {
                                    stepsTaken = Integer.parseInt(stepsString);


                                    ((Today) fragment).calculate(stepsTaken);

                                    Log.e("Steps", "" + new String(txValue, "UTF-8"));


                                    // To get Sleep data********************************************************
                                    //TODO
                                    commandToBLE(MyConstant.GET_SLEEP);
                                    //**************************************************************************

                                }

                            } catch (NumberFormatException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    Log.e("Response of Command", "" + new String(txValue, "UTF-8"));


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
//                Intent newIntent = new Intent(MainActivityNew.this, DeviceListActivity.class);
//                startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
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


    public class SetDateCountDownTimer extends CountDownTimer
    {
        public SetDateCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            // TODO
            setDateToBLE();
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
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
                reconnectTimer.cancel();
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
    protected void onPause()
    {
        super.onPause();

        // sharan Work for reconnection
        reconnectTimer.cancel();
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


            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null)
                {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
                    ((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName() + " - connecting");
                    mService.connect(deviceAddress);


                }
                break;


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


    public String hTD(String hex)
    {
        return String.valueOf(Integer.parseInt(hex, 16));
    }


    public void TestingSleep2(String remaining)
    {


//   String remaining = "0000000000110105001800A00023000F002500080101000901020004010F000C0110001D01120018011300140136000F0137000C0200000B0204000902050011020B00070212000902190007021A000A0220000F02270018022A0005022B0007022D000B0305000103080001030D000B0318000C03190045031A0001031E0014033300180339001C04030002040400020405000B040800100411000B0418000B041B000F041C002D041D000B0000000000110105051D002C0523001A05250004053000060533000B0604001B06070019060D001C061C000F061D0008061F000106320050FFFFFFFFFFFFFFFFFFFFFFFF";
//        String remaining = "0000000000110105161D000C161E001A161F0040162000220000000000110106011C000C01330011013400100135000D0000000000110106020B001002160011031A00010336000A04070050000000000011010605070044052C001205320023053700190601000606080007060E000506110001061A0002061B001106220008062800020708002D0714000A071F002B073700080738000B07390017000000000011010B1611000416370014000000000011010C16090008160F000117170008000000000011010D1108000411110050000000000011010D1203007412040001120500021206000212080002120A0002120B0003120C0001120E0001120F000212100002122800141229004E122C0002123000091235000112360001130000011302000213030001130500071311000113140001131A0001132B0001132C0001132E000113300001133A0002140A0050000000000011010E093400040A080050000000000011010F0410007C04260008042F000104390011051E0018051F001E060A0022060B0004060D0031060E000C061800150619004D061A0001062E0001070700180708000F072200140726000E080D0010080F00070810000108110002081200100813000608170006081800240819000108250015082700020828001408290012082A004000000000001101100217000C022E002E022F00120230003100000000001101100312000C03290034032A001E032B000A0000000000110110052A0040062A00170630001506380014071D0010072200050723001E072B000207390005080B0006081A0002081B001F081C000309080019092C00180930000A09310050FFFFFFFFFFFFFFFFFFFFFFFF2130783030303030303237";

//        String remaining ="000000000011010D1203007412040001120500021206000212080002120A0002120B0003120C0001120E0001120F000212100002122800141229004E122C0002123000091235000112360001130000011302000213030001130500071311000113140001131A0001132B0001132C0001132E000113300001133A0002140A0050";


        ArrayList<HashMap<String, Long>> list = new ArrayList<>();

        while (remaining.startsWith("0000000000"))
        {
            if (remaining.substring(0, 10).equals("0000000000"))
            {
                String realData = remaining.substring(10, remaining.length());

                //Log.e("realData", realData);


                String date = "";
                String startTime = "";
                String endTime = "";

                int totalBytes;

                date = hTD(realData.substring(0, 2)) + "-" + hTD(realData.substring(2, 4)) + "-" + hTD(realData.substring(4, 6));
                startTime = hTD(realData.substring(6, 8)) + ":" + hTD(realData.substring(8, 10));
                totalBytes = Integer.parseInt(realData.substring(10, 12), 16) + Integer.parseInt(realData.substring(12, 14), 16);

                // Log.e("date", "---"+date);
                // Log.e("startTime", "---"+startTime);
//                Log.e("totalBytes", "---" + totalBytes);


                int last = totalBytes * 2;
                String remainingLast = realData.substring(14, 14 + last);

                //Log.e("remainingLast", "" + remainingLast);

                int f = last + 24;

//                Log.e("fffffffffffff", "" + f + "--------" + remaining.substring(0, f));

                int count = 0;

//                Log.e("Last",""+last);

                for (int i = 0; i < last; i += 2)
                {
                    count++;

                    String str = remainingLast.substring(i, i + 2);

//                    Log.e("Inside", "" + count + "---" + str);

                    int value = Integer.parseInt(str, 16);


                    if (count == totalBytes - 3)
                    {
                        endTime = String.valueOf(value);
                    }
                    else if (count == totalBytes - 2)
                    {
                        endTime = endTime + ":" + String.valueOf(value);
                    }
                }

                //Log.e("endTime", endTime);

//                Log.e("remaining", "" + remaining);
//                Log.e("String to be cut", remaining.substring(0, f));


                remaining = remaining.replaceFirst(remaining.substring(0, f), "");

//                Log.e("After cut", remaining);


                try
                {

                    SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");
                    Date Date1 = myFormat.parse(startTime);
                    Date Date2 = myFormat.parse(endTime);
                    long mills = Date2.getTime() - Date1.getTime();

//                    totalTime += mills;
//                    Log.e("Date1", "" + Date1.getTime());
//                    Log.e("Date2", "" + Date2.getTime());
//                    int Hours = (int) (mills / (1000 * 60 * 60));
//                    int Mins = (int) (mills / (1000 * 60)) % 60;

//                    String diff = Hours + ":" + Mins; // updated value every1 second


                    Log.e("Final", "" + parseDateToddMMyyyy(date) + "--------------" + mills + "-----" + myUtil.convertMillisToHrMins(mills));


                    boolean isStored = false;

                    for (int k = 0; k < list.size(); k++)
                    {


                        if (list.get(k).containsKey(parseDateToddMMyyyy(date)))
                        {
                            isStored = true;
                            long totalMillis = list.get(k).get(parseDateToddMMyyyy(date)) + mills;

                            HashMap<String, Long> map = new HashMap<>();
                            map.put(parseDateToddMMyyyy(date), totalMillis);

                            list.set(k, map);

                            break;
                        }
                    }


                    if (!isStored)
                    {
                        HashMap<String, Long> map = new HashMap<>();
                        map.put(parseDateToddMMyyyy(date), mills);
                        list.add(map);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        Log.e("List-----", "" + list);

        if (list.size() > 0)
        {
            myDatabase.addSleepData(context, list);


            //To set sleep time to band
            String todayDate = myUtil.getTodaydate();

            boolean isSleepRecordAvailable = false;

            for (int i = 0; i < list.size(); i++)
            {
                for (String listDate : list.get(i).keySet())
                {
                    if (listDate.equals(todayDate))
                    {
                        isSleepRecordAvailable = true;
//                        Log.e(TAG,"----Date----"+todayDate+"-----"+list.get(i).get(listDate));
                        processingSleeptimeForSetting(list.get(i).get(listDate));

                        break;
                    }
                }
            }

            if (!isSleepRecordAvailable)
            {
                processingSleeptimeForSetting((long) 0);
            }

        }

        if (fragment instanceof Today)
        {
            ((Today) fragment).sleepTime();
        }

    }

    public void processingSleeptimeForSetting(Long millis)
    {
        String time = myUtil.convertMillisToHrMins(millis).replace(":", "");
//        Log.e(TAG,"----Time----"+time);
        String commandSleepTime = "setslptm" + time;

        commandToBLE(commandSleepTime);

    }


    public String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try
        {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return str;
    }


    public void oneTimeDialogToConnect(Context context)
    {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle("Connection Alert");

        // Setting Dialog Message
        alertDialog.setMessage("You have to connect mobile to band to get data.");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_alert);

        // Setting Positive "Yes" Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    //*********************************************************************************************
    //*********************************************************************************************
    //  Commands To Device
    //*********************************************************************************************
    //*********************************************************************************************


    String COMMAND;

    public void commandToBLE(String command)
    {
        COMMAND = command;


        Log.e(TAG, "COMMANDToBLE----" + command);

        if (COMMAND.equals(MyConstant.GET_SLEEP))
        {
            // Clear Sleep Data
            sleepData = "";
        }

        try
        {
            byte[] value = command.getBytes("UTF-8");

            mService.writeRXCharacteristic(value);

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }


    MyUtil.HeightWeightHelper heightWeightHelper = new MyUtil.HeightWeightHelper();

    public void setHeightWeightStrideDataToBLE()
    {
        // Weight

        double weightInDouble = Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg", "").replace("Lbs", "").trim());
        String weightUnit = MySharedPreference.getInstance().getWeightUnit(context);

//        Log.e(TAG, "Weight-----" + weightInDouble + "-----Unit-----" + weightUnit);


        if (!weightUnit.equals(MyConstant.LBS))
        {
            weightInDouble = heightWeightHelper.kgToLbConverter(weightInDouble);

//            Log.e(TAG, "WeightInLBS-----" + weightInDouble);
        }

        String finalWeight = String.format("%03d", Math.round(weightInDouble));

//        Log.e(TAG, "FinalWeightInLBS---" + finalWeight);


        // Stride

        double strideInDouble = Double.parseDouble(MySharedPreference.getInstance().getStride(context).replace("In", "").replace("Cm", "").trim());
        String strideUnit = MySharedPreference.getInstance().getStrideUnit(context);

//        Log.e(TAG, "Stride-----" + strideInDouble + "-----Unit-----" + strideUnit);

        if (!strideUnit.equals(MyConstant.IN))
        {
            strideInDouble = heightWeightHelper.cmToInches(strideInDouble);

//            Log.e(TAG, "StrideInINCHES-----" + strideInDouble);
        }

        String finalStride = String.format("%03d", Math.round(strideInDouble));

//        Log.e(TAG, "FinalStideInINCHES---" + finalStride);


        //Height

        double heightInDouble = Double.parseDouble(MySharedPreference.getInstance().getHeight(context).replace("In", "").trim());

        String finalHeight = String.format("%03d", Math.round(heightInDouble));

        Log.e(TAG, "FinalHeight---" + finalHeight);


        String commandToSetHeightWeightStride = "b" + finalHeight + finalWeight + finalStride + "1" + "0";
//        Log.e(TAG, "commandToSetHeightWeightStride---" + commandToSetHeightWeightStride);


        if (BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            commandToBLE(commandToSetHeightWeightStride);
        }
    }


    public void setDateToBLE()
    {
        SimpleDateFormat mSDF1 = new SimpleDateFormat("yyMMddHHmmss");
        String currentDateTime = mSDF1.format(new Date());

//        Log.e("currentDateTime", currentDateTime);

        String commandToSetDateTime = "dt" + currentDateTime;


        if (BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            commandToBLE(commandToSetDateTime);
        }
    }


}