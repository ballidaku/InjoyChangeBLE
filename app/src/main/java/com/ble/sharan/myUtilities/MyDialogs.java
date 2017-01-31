package com.ble.sharan.myUtilities;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

    BluetoothAdapter mBluetoothAdapter;

    List<BluetoothDevice> deviceList;
    Map<String, Integer> devRssiValues;

    DeviceAdapter deviceAdapter;

//    String deviceAddess = "";

    final long SCAN_PERIOD = 10000;

    boolean mScanning;


    ListView listv_newDevices;

    TextView txtv_close;
    TextView txtv_noDevice;

    Handler mHandler;

    AdapterView.OnItemClickListener mDeviceClickListener;


    Context context;


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
                // Log.e("Dialog","Dismiss");
            }
        });


        dialog.show();



        onStart();


    }


    public void onStart()
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
            mHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    txtv_close.setText(R.string.scan);

                    if(deviceList.size()==0)
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


    class DeviceAdapter extends BaseAdapter
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

            tvadd.setVisibility(View.GONE);
            tvrssi.setVisibility(View.VISIBLE);
            byte rssival = (byte) devRssiValues.get(device.getAddress()).intValue();
            if (rssival != 0)
            {
                tvrssi.setText("Rssi = " + String.valueOf(rssival));
            }

            tvname.setText(device.getName()== null? "No Device" : device.getName().equals("Prime") ? "InjoyHealth" : device.getName());
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







}
