package com.ble.sharan.mainScreen.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.sharan.MusicPlayer;
import com.ble.sharan.MyUartService;
import com.ble.sharan.R;
import com.ble.sharan.loginScreen.LoginActivity;
import com.ble.sharan.mainScreen.fragments.challengeFragments.CheckIn;
import com.ble.sharan.mainScreen.fragments.challengeFragments.DailyInspiration;
import com.ble.sharan.mainScreen.fragments.challengeFragments.MyPoints;
import com.ble.sharan.mainScreen.fragments.challengeFragments.ShareWin;
import com.ble.sharan.mainScreen.fragments.challengeFragments.ShoutOut;
import com.ble.sharan.mainScreen.fragments.challengeFragments.ToolBox;
import com.ble.sharan.mainScreen.fragments.mainFragments.AlarmFragment;
import com.ble.sharan.mainScreen.fragments.mainFragments.Challenge;
import com.ble.sharan.mainScreen.fragments.mainFragments.HealthData;
import com.ble.sharan.mainScreen.fragments.mainFragments.MyDailyGoal;
import com.ble.sharan.mainScreen.fragments.mainFragments.MyProfile;
import com.ble.sharan.mainScreen.fragments.mainFragments.Overall;
import com.ble.sharan.mainScreen.fragments.mainFragments.ThisWeek;
import com.ble.sharan.mainScreen.fragments.mainFragments.Today;
import com.ble.sharan.myUtilities.BeanRecords;
import com.ble.sharan.myUtilities.BleResponseInterface;
import com.ble.sharan.myUtilities.CheckUpdates;
import com.ble.sharan.myUtilities.GetStepsData;
import com.ble.sharan.myUtilities.ManupulateSleepdata;
import com.ble.sharan.myUtilities.MyConstant;
import com.ble.sharan.myUtilities.MyDatabase;
import com.ble.sharan.myUtilities.MyDialogs;
import com.ble.sharan.myUtilities.MyNotification;
import com.ble.sharan.myUtilities.MySharedPreference;
import com.ble.sharan.myUtilities.MyUtil;
import com.ble.sharan.myUtilities.ThemeChanger;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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


    Toolbar mToolbar;


    ArrayList<String> listDataHeader;

    Context context;

    // LinearLayout frame_layout_profile;
    public Fragment fragment;

    boolean isDisconnectedByRange = false;


    ReconnectTimer reconnectTimer;


    //Bluetooth
    private MyUartService mService = null;
    public BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;


   // MyDialogs myDialogs = new MyDialogs();

    MyDatabase myDatabase;

    MyUtil myUtil = new MyUtil();


    //    Bottom Tabs actuallt butto01n

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


    TextView txtv_heading;
    TextView txtv_right;


    public static boolean shownStepsNotification = false;
    public static boolean shownCaloriesNotification = false;
    public static boolean shownDistanceNotification = false;
    public static boolean shownSleepNotification = false;

    public static String todayDate = "";


    GetStepsData getStepsData;

    BleResponseInterface bleResponseInterface;


    MyNotification myNotification = new MyNotification();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ThemeChanger.getInstance().onActivityCreateSetTheme(MainActivityNew.this, ThemeChanger.THEME_BLUE);


        setContentView(R.layout.activity_main);


        context = this;



        /*Check new version*/
        new CheckUpdates(context);


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


        /*if (mBtAdapter != null && mBtAdapter.isEnabled())
        {
            oneTimeDialogToConnect(context);
        }*/


        // IF there is no data
        myDatabase.addDailyGoalDataFirstTime(context);

        if (myDatabase.getTodaySteps(context) == 0)
        {
            myDatabase.addStepData(context, new BeanRecords(myUtil.getTodaydate(), "0"));
        }


        getStepsData = new GetStepsData(context);

    }


  /*  public void connectAutoMaticallyIfAlreadyConnected()
    {
        String address = MySharedPreference.getInstance().getDeviceAddress(context).trim();
        if(!address.isEmpty()  &&  !BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
            mService.connect(address);
        }
    }*/


    private void setUpIds()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

//        mToolbar.findViewById(R.id.imgv_refresh).setOnClickListener(this);

//        imgv_leftArrow = (ImageView) mToolbar.findViewById(R.id.imgv_leftArrow);
//        imgv_rightArrow = (ImageView) mToolbar.findViewById(R.id.imgv_rightArrow);

        txtv_heading = (TextView) mToolbar.findViewById(R.id.txtv_heading);
        txtv_heading.setTextSize(18);

        (txtv_right = (TextView) mToolbar.findViewById(R.id.txtv_right)).setOnClickListener(this);

        /*listv_drawer = (ListView) findViewById(R.id.listv_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        imgv_profile = (ImageView) mDrawerLayout.findViewById(R.id.circularImageView_Profile);
        txtv_username = (TextView) mDrawerLayout.findViewById(R.id.txtv_username);


        frame_layout_profile = (LinearLayout) findViewById(R.id.frame_layout_profile);
        frame_layout_profile.setOnClickListener(this);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);*/


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

//        onRefreshName();

    }


    private void setBottomTabSelected(View v, ImageView image, TextView txtv)
    {
        view_challenge.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        view_data.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        view_myinfo.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        view_alarm.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));


        imgv_challenge.setImageResource(R.mipmap.ic_challenge_gray);
        imgv_data.setImageResource(R.mipmap.ic_home_gray);
        imgv_myinfo.setImageResource(R.mipmap.ic_profile_gray);
        imgv_alarm.setImageResource(R.mipmap.ic_alarm_gray);

        txtv_challenge.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        txtv_data.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        txtv_myinfo.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        txtv_alarm.setTextColor(ContextCompat.getColor(context, R.color.colorGray));


        v.setBackgroundColor(ThemeChanger.getInstance().getThemePrimaryColor(context));
        txtv.setTextColor(ThemeChanger.getInstance().getThemePrimaryColor(context));


//        v.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
//        txtv.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

        if (image == imgv_challenge)
        {
            //imgv_challenge.setImageResource(R.mipmap.ic_challenge_blue);
            imgv_challenge.setImageResource((int) ThemeChanger.getInstance().getBackground(context, MyConstant.CHALLENGE));
        }
        else if (image == imgv_data)
        {
            //imgv_data.setImageResource(R.mipmap.ic_home_blue);
            imgv_data.setImageResource((int) ThemeChanger.getInstance().getBackground(context, MyConstant.HOME));
        }
        else if (image == imgv_myinfo)
        {
            //imgv_myinfo.setImageResource(R.mipmap.ic_profile_blue);
            imgv_myinfo.setImageResource((int) ThemeChanger.getInstance().getBackground(context, MyConstant.PROFILE));
        }
        else if (image == imgv_alarm)
        {
            //imgv_alarm.setImageResource(R.mipmap.ic_alarm_blue);
            imgv_alarm.setImageResource((int) ThemeChanger.getInstance().getBackground(context, MyConstant.ALARM));
        }


    }

    private void prepareListData()
    {
        listDataHeader = new ArrayList<>();

        listDataHeader.add("Health Data");
        listDataHeader.add("Today");
        listDataHeader.add("This Week");
        listDataHeader.add("My Daily Goals");
        listDataHeader.add("Overall");
        listDataHeader.add("Sign Out");


      /*  drawer_adapter = new DrawerListAdapter(context, listDataHeader, 0);
        listv_drawer.setAdapter(drawer_adapter);

        listv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                displayView(position);


            }
        });*/

        displayView(0);

    }

    public void displayView(int groupPosition)
    {
//        imgv_headerLogo.setVisibility(View.GONE);
        txtv_right.setVisibility(View.INVISIBLE);
        txtv_heading.setVisibility(View.VISIBLE);


       /* drawer_adapter.changeSelectedBackground(groupPosition);
        drawer_adapter.notifyDataSetChanged();*/


        if (groupPosition == 5)// SIGN OUT
        {
//            mDrawerLayout.closeDrawers();

            MySharedPreference.getInstance().saveUID(context, "");
            MySharedPreference.getInstance().removeGoalKeys(context);


            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();


            return;
        }
        else if (groupPosition == 6)
        {
            txtv_heading.setText("My Profile");
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

                fragment = new ThisWeek();
                break;


            case 3:
                fragment = new MyDailyGoal();
                break;


            case 4:
                fragment = new Overall();
                break;


            case 6:

                fragment = new MyProfile();

                break;

            case 7:

                fragment = new AlarmFragment();
                break;


            case 8:

                fragment = new Challenge();

                removeFragmentsOfChallenge();
                break;


            default:
                break;
        }


        changeFragment(fragment);


    }


    // For Challenge Fragments
    public void displayView2(int position)
    {
//        txtv_heading.setText(challengeHeadings[position]);

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
                myFragment = new ToolBox();
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
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
//            mDrawerLayout.closeDrawers();
        }
    }

    public void changeFragment2(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    public void setTitleHeader(String text)
    {
        txtv_heading.setText(text);
    }


    @Override
    public void onBackPressed()
    {
        // super.onBackPressed();


        int count = getSupportFragmentManager().getBackStackEntryCount();

        // Log.e("onBackPressedCount", "" + count);


        if (count == 1)
        {
            MyDialogs.getInstance().showExitDialog(context, onBackPressedClickListener);
        }
        else
        {
            super.onBackPressed();
        }


    }

    DialogInterface.OnClickListener onBackPressedClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
            finish();
        }
    };


    public void removeFragmentsOfChallenge()
    {

        int count = getSupportFragmentManager().getBackStackEntryCount();
//        Log.e("Count", "" + count);


        if (count != 0)
        {
            while (count != 0)
            {
                getSupportFragmentManager().popBackStackImmediate();
                count = getSupportFragmentManager().getBackStackEntryCount();

//                Log.e("InSide Count", "" + count);
            }
        }


    }


  /*  public void onRefreshName()
    {
        myUtil.showCircularImageWithPicasso(context, imgv_profile, MySharedPreference.getInstance().getPhoto(context));

        txtv_username.setText(MySharedPreference.getInstance().getName(context));
    }*/


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            /*case R.id.frame_layout_profile:

//                txt_titleTV.setText("Profile");
//                fragment = new MyProfile();
//                changeFragment(fragment);

                break;*/

            case R.id.linearLayout_challenge:


                String url = MyConstant.CHALLENGE_API;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                /*setBottomTabSelected(view_challenge, imgv_challenge, txtv_challenge);

                displayView(8);*/


                break;

            case R.id.linearLayout_data:


                if (!(fragment instanceof HealthData))
                {

                    removeFragmentsOfChallenge();
                    setBottomTabSelected(view_data, imgv_data, txtv_data);
                    displayView(0);

                }

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
            MyUtil.myLog(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize())
            {
                MyUtil.myLog(TAG, "Unable to initialize Bluetooth");
                //finish();
            }
        }

        public void onServiceDisconnected(ComponentName classname)
        {
            ////     mService.disconnect(mDevice);
            mService = null;
        }
    };


    int dateCommandresposeCount = 0;
    int fiemwareCommandResposeCount = 0;
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver()
    {

        public void onReceive(final Context mcontext, Intent intent)
        {
            String action = intent.getAction();


            MyUtil.myLog(TAG, "Action : " + action);


            switch (action)
            {
                case MyUartService.ACTION_GATT_CONNECTING:

                    if (!isDisconnectedByRange)
                    {
//                    txtv_connect_disconnect.setText("Connecting...");
//                    txtv_deviceName.setText(deviceName + " : Connecting...");
//
                        MyUtil.myLog(TAG, "Connecting");

                        BLE_STATUS = MyConstant.CONNECTING;

                        if (fragment instanceof Today)
                        {

                            ((Today) fragment).bleStatus(BLE_STATUS);
                        }
                    }

                    break;

                case MyUartService.ACTION_GATT_DISCONNECTING:

//                txtv_connect_disconnect.setText("Disconnecting...");
//                txtv_deviceName.setText(deviceName + " : Disconnecting...");

                    MyUtil.myLog(TAG, "Disconnecting");

                    BLE_STATUS = MyConstant.DISCONNECTING;

                    if (fragment instanceof Today)
                    {

                        ((Today) fragment).bleStatus(BLE_STATUS);
                    }

                    break;

                case MyUartService.ACTION_GATT_CONNECTED:

              /*  getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {*/
                    MyUtil.myLog(TAG, "Connected");

                    BLE_STATUS = MyConstant.CONNECTED;

                    //**********************************************************************************
                    // In case if band is connected in background
                    reconnectTimer.cancel();


                    // Client is getting error of " java.lang.NullPointerException: Attempt to invoke virtual method 'boolean android.app.Dialog.isShowing()' on a null object reference"
                    // After this i add : myDialogs != null && myDialogs.dialog != null


                    try
                    {
                        if ( MyDialogs.getInstance().dialog != null && MyDialogs.getInstance().dialog.isShowing())
                        {
                            MyDialogs.getInstance().dialog.dismiss();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    //**********************************************************************************

                    if (fragment instanceof Today)
                    {
                        ((Today) fragment).bleStatus(BLE_STATUS);
                    }


                    MyUtil.showToast(context, "Device Connected");

                    MySharedPreference.getInstance().saveIsConnectedNow(context, true);

                    break;

                case MyUartService.ACTION_GATT_DISCONNECTED_DUE_TO_RANGE:

                    MyUtil.myLog(TAG, "Disconnected due to range");

                    isDisconnectedByRange = true;

                    BLE_STATUS = MyConstant.DISCONNECTED;


                    if (fragment instanceof Today)
                    {
                        try
                        {
                            ((Today) fragment).bleStatus(BLE_STATUS);
                        }
                        catch (Exception e)
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

                    reconnectTimer.cancel();
                    reconnectTimer.start();
                    break;

                case MyUartService.ACTION_GATT_DISCONNECTED:

                    isDisconnectedByRange = false;

                    MyUtil.myLog(TAG, "Disconnected");

                    BLE_STATUS = MyConstant.DISCONNECTED;

                    if (fragment instanceof Today)
                    {
                        ((Today) fragment).bleStatus(BLE_STATUS);
                    }

                    MyUtil.showToast(context, "Device Disconnected");

                    if (mService != null)
                    {
                        mService.close();
                    }

                    break;

                case MyUartService.ACTION_GATT_SERVICES_DISCOVERED:


                    if (mService != null)
                    {
                        mService.enableTXNotification();
                    }

                    SetDateCountDownTimer setDateCountDownTimer = new SetDateCountDownTimer(2000, 1000);
                    setDateCountDownTimer.start();

                    break;

                case MyUartService.ACTION_DATA_AVAILABLE:


                    final byte[] txValue = intent.getByteArrayExtra(MyUartService.EXTRA_DATA);
         /*       getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {*/

                    try
                    {
                        MyUtil.myLog(TAG, "CR---Response of Command   " + new String(txValue, "UTF-8"));


                        // IN MUSIC CASE, TO PLAY MUSIC
                        if (new String(txValue, "UTF-8").equals(MyConstant.MUSIC_PLAY) || new String(txValue, "UTF-8").equals(MyConstant.MUSIC_PAUSE))
                        {
                            MusicPlayer.getInstance().playPause(context);
                        }
                        else if (new String(txValue, "UTF-8").equals(MyConstant.MUSIC_PREVIOUS))
                        {
                            MusicPlayer.getInstance().previous(context);
                        }
                        else if (new String(txValue, "UTF-8").equals(MyConstant.MUSIC_NEXT))
                        {
                            MusicPlayer.getInstance().next(context);
                        }
                        else if (COMMAND.contains("dt"))
                        {
                            dateCommandresposeCount++;
                            if (dateCommandresposeCount == 4)
                            {
                                //TODO
                                setHeightWeightStrideDataToBLE();
                                dateCommandresposeCount = 0;
                            }
                        }
                        else if (COMMAND.contains("b") && COMMAND.length() != 2)
                        {
                            //TODO
                            commandToBLE(MyConstant.GET_STEPS);
                        }
                        else if (COMMAND.equals(MyConstant.GET_SLEEP)) //After getting all the sleep data
                        {
                            String hex = MyUtil.bytesToHex(txValue);
                            sleepData += hex.substring(8, hex.length());

//                        MyUtil.myLog("remaining", "" + sleepData);

                            if (new String(txValue, "UTF-8").contains("Done")) // After getting all the sleep data
                            {
                                MyUtil.myLog("Total Sleep String", "" + sleepData);
                                // TestingSleep2(sleepData);

                                ManupulateSleepdata manupulateSleepdata = new ManupulateSleepdata();

                                manupulateSleepdata.gettingSleepData(context, sleepData, myDatabase);

                                // This functionality is stopped by client

                           /* ManupulateSleepDataNew manupulateSleepdata = new ManupulateSleepDataNew();

                            manupulateSleepdata.gettingSleepData(context, sleepData, myDatabase);*/
                            }
                        }
                        else if (COMMAND.equals(MyConstant.GET_STEPS))// After getting the steps
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

                                        MyUtil.myLog("Steps", "" + new String(txValue, "UTF-8"));


                                        //**************************************************************************
                                        // Notification
                                        //**************************************************************************

                                        int remainingSteps = Integer.parseInt(MySharedPreference.getInstance().getDailySteps(context)) - stepsTaken;

                                        if (remainingSteps <= 0 && shownStepsNotification)
                                        {
                                            shownStepsNotification = false;
                                            myUtil.vibrate(context);
                                            myNotification.showNotification(context, "CONGRATULATIONS!!\n" +
                                                      "You Hit Your Steps Goal for Today : )\n" +
                                                      "You Totally Rock!", 1);
                                        }

                                        double Calories = myUtil.stepsToCaloriesFormula(context, stepsTaken);

                                        double remainingCalories = Double.parseDouble(MySharedPreference.getInstance().getDailyCalories(context).trim()) - (int) Calories;


                                        if (remainingCalories <= 0 && shownCaloriesNotification)
                                        {
                                            shownCaloriesNotification = false;
                                            myUtil.vibrate(context);
                                            myNotification.showNotification(context, "CONGRATULATIONS!!\n" +
                                                      "You Hit Your Calories Goal for Today : )\n" +
                                                      "You're Amazing!", 2);
                                        }


                                        double distance = myUtil.stepsToDistanceFormula(context, stepsTaken);

                                        double remainingKm = Double.parseDouble(MySharedPreference.getInstance().getDailyMiles(context).trim()) - distance;

                                        if (remainingKm < 0 && shownDistanceNotification)
                                        {
                                            shownDistanceNotification = false;
                                            myUtil.vibrate(context);
                                            myNotification.showNotification(context, "CONGRATULATIONS!!\n" +
                                                      "You Hit Your Miles Goal for Today : )\n" +
                                                      "You're Incredible!", 3);
                                        }


                                        //**************************************************************************
                                        //**************************************************************************
                                        //**************************************************************************
                                        // To get Sleep data********************************************************
                                        //TODO
                                        commandToBLE(MyConstant.GET_SLEEP);
                                        //**************************************************************************

                                    }

                                }
                                catch (NumberFormatException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if (COMMAND.contains("setslptm")) //After setting the sleep time to band
                        {

                            commandToBLE(MyConstant.CLEAR_SLEEP);
                        }
                        else if (COMMAND.contains(MyConstant.CLEAR_SLEEP)) // After clearing the sleep data
                        {
                            //To get the fiemware version
                            commandToBLE("fw");
                        }
                        else if (COMMAND.contains("fw")) // After gettting the band firmware version
                        {
                            fiemwareCommandResposeCount++;
                            //TODO
                            if (fiemwareCommandResposeCount == 1 && !new String(txValue, "UTF-8").equals("OK!"))
                            {
                                MyUtil.myLog("Firmware Version", "" + new String(txValue, "UTF-8"));
                                MySharedPreference.getInstance().saveFirmwareVersion(context, new String(txValue, "UTF-8").replace("FW:", ""));
                            }
                            if (fiemwareCommandResposeCount == 2)
                            {
                                // To get previous days steps from memory
                                getStepsData.count = 0;
                                getStepsData.st();
                                fiemwareCommandResposeCount = 0;
                            }
                        }
                        else if (COMMAND.contains("d") && COMMAND.length() == 2)  // To get previous steps
                        {
                            bleResponseInterface.onResponse(new String(txValue, "UTF-8"));
                        }


                    }
                    catch (Exception e)
                    {
                        MyUtil.myLog(TAG, e.toString());
                    }
                    break;

                case MyUartService.DEVICE_DOES_NOT_SUPPORT_UART:

                    MyUtil.showToast(context, "Device doesn't support UART. Disconnecting");
                    mService.disconnect();

                    break;

            }

        }
    };


    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            String deviceAddress = ((TextView) view.findViewById(R.id.address)).getText().toString().trim();
            deviceName = ((TextView) view.findViewById(R.id.name)).getText().toString();

            mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);


            MyUtil.myLog("select address", "--" + deviceAddress);
            if (!deviceAddress.isEmpty() && !deviceName.isEmpty())
            {

                // If any connection serching for band in background
                reconnectTimer.cancel();


                MySharedPreference.getInstance().saveDeviceAddress(context, deviceAddress);
                mService.connect(deviceAddress);

            }
            MyDialogs.getInstance().dialog.dismiss();

        }
    };


    public void connectDisconnect()
    {
        if (mBtAdapter != null && !mBtAdapter.isEnabled())
        {
            //Log.i(TAG, "onClick - BT not enabled yet");
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
                MyDialogs.getInstance().bleDeviceAvailable(context, mDeviceClickListener);
            }
            else
            {
                //Disconnect button pressed
                if (mDevice != null)
                {
                    dateCommandresposeCount = 0;
                    mService.disconnect();

                }
            }
        }
    }


    private class SetDateCountDownTimer extends CountDownTimer
    {
        private SetDateCountDownTimer(long startTime, long interval)
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


    private class ReconnectTimer extends CountDownTimer
    {
        private ReconnectTimer(long startTime, long interval)
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
            String address = MySharedPreference.getInstance().getDeviceAddress(context).trim();

            //  MyUtil.myLog(TAG, "Searching..." + millisUntilFinished+"  address  "+address);

            // this try catch is due to crash in some devices showed in fabric
            try
            {
                if (!address.isEmpty())
                {
                    mService.connect(address);
                }
                //  MyUtil.showToast(context, "Searching...");

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }


    //**********************************************************************************************
    //**********************************************************************************************


    @Override
    protected void onResume()
    {
        super.onResume();

        if (mBtAdapter != null && !mBtAdapter.isEnabled())
        {
            //Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }


        if (todayDate.isEmpty() || !todayDate.equals(myUtil.getTodaydate()))
        {
            todayDate = myUtil.getTodaydate();

            shownStepsNotification = true;
            shownCaloriesNotification = true;
            shownDistanceNotification = true;
            shownSleepNotification = true;

            // MyUtil.myLog(TAG, "shownStepsNotification----" + shownStepsNotification);
        }
//
//
//        MyUtil.myLog("BalliDaku onResume", "" + MySharedPreference.getInstance().getIsConnectedNow(context) + "---" + isConnectedEarlier);
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

        BLE_STATUS = MyConstant.DISCONNECTED;
        MySharedPreference.getInstance().clearConnectionData(context);


        try
        {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(UARTStatusChangeReceiver);
        }
        catch (Exception ignore)
        {
            MyUtil.myLog(TAG, ignore.toString());
        }
        context.unbindService(mServiceConnection);
        mService.stopSelf();
        mService = null;


        if (mBtAdapter!=null && mBtAdapter.isEnabled())
        {
            mBtAdapter.disable();
        }


    }


    //**********************************************************************************************
    //**********************************************************************************************


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        // MyUtil.myLog(TAG, "RequestCode   " + requestCode);

        switch (requestCode)
        {


            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null)
                {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE).trim();
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                    //Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
//                    ((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName() + " - connecting");
//                    mService.connect(deviceAddress);

                    if (!deviceAddress.isEmpty())
                    {
                        mService.connect(deviceAddress);
                    }


                }
                break;


            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK)
                {
                    MyUtil.showToast(context, "Bluetooth has turned on ");
                    MyDialogs.getInstance().oneTimeDialogToConnect(context);
                }
                else
                {
                    MyUtil.showToast(context, "Problem in BT Turning ON ");
                }
                break;

            default:
                MyUtil.myLog(TAG, "wrong request code");
                break;
        }
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

        MyUtil.myLog(TAG, "CR---COMMANDToBLE----" + command);

        if (COMMAND.equals(MyConstant.GET_SLEEP))
        {
            // Clear Sleep Data
            sleepData = "";
        }

        try
        {
            if (command != null && !command.isEmpty())
            {
                byte[] value = command.getBytes("UTF-8");

                if(value.length != 0)
                {
                    mService.writeRXCharacteristic(value);
                }
            }

        }
        catch (NullPointerException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

    }

    public void commandToBLE(String command, BleResponseInterface bleResponseInterface)
    {
        COMMAND = command;
        this.bleResponseInterface = bleResponseInterface;

        MyUtil.myLog(TAG, "CR---COMMANDToBLE----" + command);

        try
        {
            if (command != null && !command.isEmpty())
            {
                byte[] value = command.getBytes("UTF-8");

                if(value.length != 0)
                {
                    mService.writeRXCharacteristic(value);
                }
            }

        }
        catch (NullPointerException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }


    MyUtil.HeightWeightHelper heightWeightHelper = new MyUtil.HeightWeightHelper();

    public void setHeightWeightStrideDataToBLE()
    {
        // Weight

        double weightInDouble = Double.parseDouble(MySharedPreference.getInstance().getWeight(context).replace("Kg", "").replace("Lbs", "").trim());
        String weightUnit = MySharedPreference.getInstance().getUnit(context, MyConstant.WEIGHT);

//        MyUtil.myLog(TAG, "Weight-----" + weightInDouble + "-----Unit-----" + weightUnit);


        if (!weightUnit.equals(MyConstant.LBS))
        {
            weightInDouble = heightWeightHelper.kgToLbConverter(weightInDouble);

//            MyUtil.myLog(TAG, "WeightInLBS-----" + weightInDouble);
        }

        String finalWeight = String.format(Locale.US, "%03d", Math.round(weightInDouble));

//        MyUtil.myLog(TAG, "FinalWeightInLBS---" + finalWeight);


        // Stride

        double strideInDouble = Double.parseDouble(MySharedPreference.getInstance().getStride(context).replace("In", "").replace("Cm", "").trim());
        String strideUnit = MySharedPreference.getInstance().getUnit(context, MyConstant.STRIDE);

//        MyUtil.myLog(TAG, "Stride-----" + strideInDouble + "-----Unit-----" + strideUnit);

        if (!strideUnit.equals(MyConstant.IN))
        {
            strideInDouble = heightWeightHelper.cmToInches(strideInDouble);

//            MyUtil.myLog(TAG, "StrideInINCHES-----" + strideInDouble);
        }

        String finalStride = String.format(Locale.US, "%03d", Math.round(strideInDouble));

//        MyUtil.myLo1g(TAG, "FinalStideInINCHES---" + finalStride);


        //Height

        double heightInDouble = Double.parseDouble(MySharedPreference.getInstance().getHeight(context).replace("Inches", "").trim());

        String finalHeight = String.format(Locale.US, "%03d", Math.round(heightInDouble));

        //MyUtil.myLog(TAG, "FinalHeight---" + finalHeight);


        String commandToSetHeightWeightStride = "b" + finalHeight + finalWeight + finalStride + "1" + "1";
//        MyUtil.myLog(TAG, "commandToSetHeightWeightStride---" + commandToSetHeightWeightStride);


        if (BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            commandToBLE(commandToSetHeightWeightStride);
//            commandToBLE("b15090020010");
        }
    }


    public void setDateToBLE()
    {
        SimpleDateFormat mSDF1 = new SimpleDateFormat("yyMMddHHmmss", Locale.US);
        String currentDateTime = mSDF1.format(new Date());

//        MyUtil.myLog("currentDateTime", currentDateTime);

        String commandToSetDateTime = "dt" + currentDateTime;


        if (BLE_STATUS.equals(MyConstant.CONNECTED))
        {
            commandToBLE(commandToSetDateTime);
        }
    }




    /*public void stopPlaying()
    {
        AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        if (mAudioManager.isMusicActive()) {

            *//*Intent i = new Intent("com.android.music.musicservicecommand");

            i.putExtra("command", "pause");

//            Intent i = new Intent();
//            i.setAction("com.android.music.musicservicecommand.pause");
//            i.putExtra("command", "pause");

            MainActivityNew.this.sendBroadcast(i);*//*
//            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            sendMediaButton(getApplicationContext(), KeyEvent.KEYCODE_MEDIA_PAUSE);

        }

       // long eventtime = SystemClock.uptimeMillis();

        *//*Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_MEDIA_NEXT, 0);
        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
        sendOrderedBroadcast(downIntent, null);*//*

        *//*Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
        KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
        sendOrderedBroadcast(upIntent, null);*//*


    }

    private static void sendMediaButton(Context context, int keyCode) {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent,null);

        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent,null);
    }*/


}

