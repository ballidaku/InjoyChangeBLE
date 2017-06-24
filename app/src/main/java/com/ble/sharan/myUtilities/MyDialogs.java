package com.ble.sharan.myUtilities;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.sharan.R;
import com.ble.sharan.mainScreen.activities.MainActivityNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brst-pc93 on 12/21/16.
 */

public class MyDialogs
{
    public Dialog dialog;

    private BluetoothAdapter mBluetoothAdapter;

    private List<BluetoothDevice> deviceList;
    private Map<String, Integer> devRssiValues;

    private DeviceAdapter deviceAdapter;

//    String deviceAddess = "";

    private boolean mScanning;


    private ListView listv_newDevices;

    private TextView txtv_close;
    private TextView txtv_noDevice;

    private Handler mHandler;

    private AdapterView.OnItemClickListener mDeviceClickListener;


    Context context;

    private MyUtil myUtil = new MyUtil();

    private static MyDialogs  instance = new MyDialogs();

    public static MyDialogs getInstance()
    {
        return instance;
    }


    public void bleDeviceAvailable(Context context, AdapterView.OnItemClickListener mDeviceClickListener)
    {


        this.context = context;
        this.mDeviceClickListener = mDeviceClickListener;

        dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        dialog.setContentView(R.layout.dialog_ble_list);


        listv_newDevices = (ListView) dialog.findViewById(R.id.listv_newDevices);
        txtv_close = (TextView) dialog.findViewById(R.id.txtv_close);
        txtv_noDevice = (TextView) dialog.findViewById(R.id.txtv_noDevice);

        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            MyUtil.showToast(context, context.getResources().getString(R.string.ble_not_supported));
            dialog.dismiss();
        }


        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null)
        {
            MyUtil.showToast(context, context.getResources().getString(R.string.ble_not_supported));
            dialog.dismiss();
            return;
        }


     /*   deviceAddess= MySharedPreference.getInstance().getDeviceAddress(context);
        if (deviceAddess.isEmpty())
        {*/
        populateList();
        txtv_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!mScanning)
                {
                    scanLeDevice(true);
                    txtv_noDevice.setText("Scanning please wait...");
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });
        /*}
        else
        {
            sharanConnect();
        }*/

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                // MyUtil.myLog("Dialog","Dismiss");
            }
        });


        dialog.show();


        onStart();


    }


    private void onStart()
    {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    }


    private void populateList()
    {
        /* Initialize device list container */
        deviceList = new ArrayList<BluetoothDevice>();


//        txtv_noDevice.setVisibility(View.VISIBLE);
//        listv_newDevices.setVisibility(View.GONE);

        txtv_noDevice.setText("Scanning please wait...");


        deviceAdapter = new DeviceAdapter(context, deviceList);
        devRssiValues = new HashMap<String, Integer>();

        //ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        listv_newDevices.setAdapter(deviceAdapter);
        listv_newDevices.setOnItemClickListener(mDeviceClickListener);


        scanLeDevice(true);

    }


//    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener()
//    {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//        {
////            BluetoothDevice device = deviceList.get(position);
//           // mBluetoothAdapter.stopLeScan(mLeScanCallback);
//
////            Bundle b = new Bundle();
////            b.putString(BluetoothDevice.EXTRA_DEVICE, deviceAddess = deviceList.get(position).getAddress());
//
//            MySharedPreference.getInstance().saveDeviceAddress(context,deviceList.get(position).getAddress());
//
////            Intent result = new Intent();
////            result.putExtras(b);
////            setResult(Activity.RESULT_OK, result);
//            dialog.dismiss();
//
//        }
//    };


    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
    {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord)
        {
            ((MainActivityNew) context).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {

                    addDevice(device, rssi);
                }
            });
        }
    };


    private void addDevice(BluetoothDevice device, int rssi)
    {
        boolean deviceFound = false;

        for (BluetoothDevice listDev : deviceList)
        {
            if (listDev.getAddress().equals(device.getAddress()))
            {
                deviceFound = true;
                break;
            }
        }


        devRssiValues.put(device.getAddress(), rssi);
        if (!deviceFound)
        {
            txtv_noDevice.setVisibility(View.GONE);
            listv_newDevices.setVisibility(View.VISIBLE);
            deviceList.add(device);
            //mEmptyList.setVisibility(View.GONE);


            deviceAdapter.notifyDataSetChanged();
        }
//        else
//        {
//            txtv_noDevice.setText("No Device Found");
//
////            txtv_noDevice.setVisibility(View.VISIBLE);
////            listv_newDevices.setVisibility(View.GONE);
//        }
    }


//    public void sharanConnect()
//    {
//        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//
//        Bundle b = new Bundle();
//
//        // b.putString(BluetoothDevice.EXTRA_DEVICE, "FB:E2:FB:DB:78:0A");
//        b.putString(BluetoothDevice.EXTRA_DEVICE, deviceAddess);
//
//        Intent result = new Intent();
//        result.putExtras(b);
//        setResult(Activity.RESULT_OK, result);
//        dialog.dismiss();
//    }


    private void scanLeDevice(final boolean enable)
    {
        // final Button cancelButton = (Button) findViewById(R.id.btn_cancel);
        if (enable)
        {
            // Stops scanning after a pre-defined scan period.
            long SCAN_PERIOD = 10000;
            mHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    txtv_close.setText(R.string.scan);

                    if (deviceList.size() == 0)
                    {
                        txtv_noDevice.setText("No Device Found");
                    }

                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            txtv_close.setText(R.string.cancel);
        }
        else
        {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            txtv_close.setText(R.string.scan);
        }

    }


    private class DeviceAdapter extends BaseAdapter
    {
        Context context;
        List<BluetoothDevice> devices;
        LayoutInflater inflater;

        public DeviceAdapter(Context context, List<BluetoothDevice> devices)
        {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.devices = devices;
        }

        @Override
        public int getCount()
        {
            return devices.size();
        }

        @Override
        public Object getItem(int position)
        {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewGroup vg;

            if (convertView != null)
            {
                vg = (ViewGroup) convertView;
            }
            else
            {
                vg = (ViewGroup) inflater.inflate(R.layout.device_element, null);
            }

            BluetoothDevice device = devices.get(position);
            final TextView tvadd = ((TextView) vg.findViewById(R.id.address));
            final TextView tvname = ((TextView) vg.findViewById(R.id.name));
            final TextView tvpaired = (TextView) vg.findViewById(R.id.paired);
            final TextView tvrssi = (TextView) vg.findViewById(R.id.rssi);

            //tvadd.setVisibility(View.GONE);
            tvrssi.setVisibility(View.VISIBLE);
            try
            {
                byte rssival = (byte) devRssiValues.get(device.getAddress()).intValue();

                tvrssi.setText("Rssi = " + String.valueOf(rssival));


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            tvname.setText(device.getName() == null ? "No Device" : device.getName().equals("Prime") ? "InjoyHealth" : device.getName());
            tvadd.setText(device.getAddress());
            if (device.getBondState() == BluetoothDevice.BOND_BONDED)
            {
                //Log.i(TAG, "device::" + device.getName());
                tvname.setTextColor(Color.BLACK);
                tvadd.setTextColor(Color.BLACK);
                tvpaired.setTextColor(Color.BLACK);
                tvpaired.setVisibility(View.VISIBLE);
                tvpaired.setText(R.string.paired);
                tvrssi.setVisibility(View.VISIBLE);
                tvrssi.setTextColor(Color.BLACK);

            }
            else
            {
                tvname.setTextColor(Color.BLACK);
                tvadd.setTextColor(Color.BLACK);
                tvpaired.setVisibility(View.GONE);
                tvrssi.setVisibility(View.VISIBLE);
                tvrssi.setTextColor(Color.BLACK);
            }
            return vg;
        }
    }


    public EditText showShareWinCheckInDialog(Context context, String fromWhere, View.OnClickListener onClickListener)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_share_win);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView txtv_title = (TextView) dialog.findViewById(R.id.txtv_title);
        TextView txtv_heading = (TextView) dialog.findViewById(R.id.txtv_heading);
        TextView txtv_btn = (TextView) dialog.findViewById(R.id.txtv_btn);

        EditText edtv_comment = (EditText) dialog.findViewById(R.id.edtv_comment);

        if (fromWhere.equals("CheckIn"))
        {
            txtv_title.setText("CHECK-IN WITH YOURSELF");
            txtv_heading.setText("How did you use one of the values today?");
            txtv_btn.setText("Submit 20 Pts");
        }
        else if (fromWhere.equals("ShareWin"))
        {
            txtv_title.setText("SHARE A WIN");
            txtv_heading.setText("What's one win you've had during this Challenge?");
            txtv_btn.setText("Share a Win 10 Pts");
        }
        else if (fromWhere.equals("WeeklyVideo"))
        {
            txtv_title.setText("WEEKLY VIDEO");
            txtv_heading.setText("What's one thing that you took away from this video?");
            txtv_btn.setText("Submit 40 Pts");
        }


        CardView cardViewBtn = (CardView) dialog.findViewById(R.id.cardViewBtn);

        cardViewBtn.setOnClickListener(onClickListener);


        return edtv_comment;

    }


    public void showToolBoxDialog(Context context, String heading, String image, String description, View.OnClickListener onClickListener, String day, int count)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tool_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView txtv_title = (TextView) dialog.findViewById(R.id.txtv_title);
        TextView txtv_heading = (TextView) dialog.findViewById(R.id.txtv_heading);

        ImageView imgv_toolbox = (ImageView) dialog.findViewById(R.id.imgv_toolbox);

        TextView txtv_description = (TextView) dialog.findViewById(R.id.txtv_description);
        TextView txtv_btn = (TextView) dialog.findViewById(R.id.txtv_btn);


        /*if(fromWhere.equals("ToolBoxReadNow"))
        {*/
        txtv_title.setText("TOOLBOX");

        //}
        txtv_heading.setText(heading);

        myUtil.showImageWithPicasso(context, imgv_toolbox, image);

        txtv_description.setText(description);

        txtv_btn.setText("Submit 15 Pts");


        CardView cardViewBtn = (CardView) dialog.findViewById(R.id.cardViewBtn);

        cardViewBtn.setOnClickListener(onClickListener);


        if (day.equals(myUtil.getCurrentDay()) && count == 0)
        {
            cardViewBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            cardViewBtn.setVisibility(View.GONE);
        }


    }


    public void showDailyInspirationDialog(Context context, String fromWhere, String image, int count, View.OnClickListener onClickListener)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_daily_inspiration);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView txtv_title = (TextView) dialog.findViewById(R.id.txtv_title);

        ImageView imgvCenter = (ImageView) dialog.findViewById(R.id.imgvCenter);

        TextView txtv_btn = (TextView) dialog.findViewById(R.id.txtv_btn);


        if (fromWhere.equals("DailyInspiration"))
        {
            txtv_title.setText("DAILY INSPIRATION");
            txtv_btn.setText("Submit 10 Pts");

        }

        myUtil.showImageWithPicasso(context, imgvCenter, image);


        CardView cardViewBtn = (CardView) dialog.findViewById(R.id.cardViewBtn);

        cardViewBtn.setOnClickListener(onClickListener);


        if (count == 0)
        {
            cardViewBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            cardViewBtn.setVisibility(View.GONE);
        }


    }

    AlertDialog alertDialog;

    public void showExitDialog(Context context, DialogInterface.OnClickListener onClickListener)
    {
        alertDialog = new AlertDialog.Builder(context)
                  .setTitle("Close Confirmation")
                  .setMessage("Are you sure you want to close app?")
                  .setPositiveButton("CLOSE", onClickListener)
                  .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                  {
                      public void onClick(DialogInterface dialog, int which)
                      {
                          dialog.dismiss();
                      }
                  })
                  .setIcon(R.mipmap.ic_alert)
                  .show();
    }

    public void oneTimeDialogToConnect(final Context context)
    {
        alertDialog = new AlertDialog.Builder(context)
                  .setTitle("Connection Alert")
                  .setMessage("You have to connect mobile to band to get data.")
                  .setIcon(R.mipmap.ic_alert)
                  .setPositiveButton("OK", new DialogInterface.OnClickListener()
                  {
                      public void onClick(DialogInterface dialog, int which)
                      {
                          alertDialog.dismiss();


                          if(MySharedPreference.getInstance().getIsNewVersionAvailable(context))
                          {
                              showUpdateDialog(context);
                          }
                          //If sleep time is not set under My Profile
                /*if (MySharedPreference.getInstance().getSleepStartTime(context).isEmpty()  && MySharedPreference.getInstance().getSleepEndTime(context).isEmpty())
                {
                    oneTimeDialogToSetSleepTime(context);
                }*/
                      }
                  })
                  .show();
    }

    public void showUpdateDialog(final Context context)
    {
        alertDialog =  new AlertDialog.Builder(context)
                  .setTitle("App Update!")
                  .setMessage("App new version is avaliable now. Do you want to update?")
                  .setPositiveButton("UPDATE", new DialogInterface.OnClickListener()
                  {
                      public void onClick(DialogInterface dialog, int which)
                      {
                          context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                      }
                  })
                  .setNegativeButton("DISMISS", new DialogInterface.OnClickListener()
                  {
                      public void onClick(DialogInterface dialog, int which)
                      {
                          dialog.dismiss();
                      }
                  })
                  .setIcon(R.mipmap.ic_alert)
                  .show();
    }


    // This dialog functionality is stopped by client
    public void oneTimeDialogToSetSleepTime(Context context)
    {

        alertDialog = new AlertDialog.Builder(context)
                  .setTitle("Set Sleep Time Alert")
                  .setMessage("You have to set sleep time under My Profile to get proper sleep time.")
                  .setIcon(R.mipmap.ic_alert)
                  .setPositiveButton("OK", new DialogInterface.OnClickListener()
                  {
                      public void onClick(DialogInterface dialog, int which)
                      {
                          alertDialog.dismiss();
                      }
                  })
                  .show();
    }


}
