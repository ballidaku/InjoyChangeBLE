
/*

 * Copyright (c) 2015, Nor0dic Semiconductor
 * All rights reserved.
 *
 * Redistribution and0 use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ble.sharan;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 0
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class MyUartService extends Service
{
    private final static String TAG = MyUartService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    // private int mConnectionState = STATE_DISCONNECTED;

//    private static final int STATE_DISCONNECTING = 3;
//    private static final int STATE_DISCONNECTED = 0;
//    private static final int STATE_CONNECTING = 1;
//    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTING = "com.nordicsemi.nrfUART.ACTION_GATT_CONNECTING";
    public final static String ACTION_GATT_CONNECTED = "com.nordicsemi.nrfUART.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTING = "com.nordicsemi.nrfUART.ACTION_GATT_DISCONNECTING";
    public final static String ACTION_GATT_DISCONNECTED = "com.nordicsemi.nrfUART.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_DISCONNECTED_DUE_TO_RANGE = "com.nordicsemi.nrfUART.ACTION_GATT_DISCONNECTED_DUE_TO_RANGE";

    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.nordicsemi.nrfUART.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.nordicsemi.nrfUART.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.nordicsemi.nrfUART.EXTRA_DATA";
    public final static String DEVICE_DOES_NOT_SUPPORT_UART = "com.nordicsemi.nrfUART.DEVICE_DOES_NOT_SUPPORT_UART";

    public static final UUID TX_POWER_UUID = UUID.fromString("00001804-0000-1000-8000-00805f9b34fb");
    public static final UUID TX_POWER_LEVEL_UUID = UUID.fromString("00002a07-0000-1000-8000-00805f9b34fb");
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID FIRMWARE_REVISON_UUID = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    public static final UUID DIS_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");


    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {

            Log.w(TAG, "new state -- " + newState + "---status-- " + status);

            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                broadcastUpdate(ACTION_GATT_CONNECTED);
                Log.w(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.w(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());

            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED && status == 0)
            {
                Log.w(TAG, "Disconnected from GATT server.");
                broadcastUpdate(ACTION_GATT_DISCONNECTED);

            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED)
            {
                broadcastUpdate(ACTION_GATT_DISCONNECTED_DUE_TO_RANGE);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                Log.w(TAG, "mBluetoothGatt = " + mBluetoothGatt);

                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            }
            else
            {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status)
        {

            Log.w(TAG, "onCharacteristicRead" + characteristic + "--status--" + status);
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic)
        {
            Log.w(TAG, "onCharacteristicChanged-----" + characteristic);
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action)
    {
        final Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic)
    {
        final Intent intent = new Intent(action);

        // This is handling for the notification on TX Character of NUS service
        if (TX_CHAR_UUID.equals(characteristic.getUuid()))
        {

            // Log.d(TAG, String.format("Received TX: %d",characteristic.getValue() ));
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public class LocalBinder extends Binder
    {
        public MyUartService getService()
        {
            return MyUartService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize()
    {

        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null)
        {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null)
            {
                Log.w(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null)
        {
            Log.w(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     * <p>
     * 79695     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address)
    {

        try
        {
            if (mBluetoothAdapter == null || address == null)
            {
                Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
                return false;
            }

            // Previously connected device.  Try to reconnect.
            if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null)
            {
                Log.w(TAG, "Trying to use an existing mBluetoothGatt for connection.");
                if (mBluetoothGatt.connect())
                {
                    // mConnectionState = STATE_CONNECTING;
                    broadcastUpdate(ACTION_GATT_CONNECTING);
                    return true;
                }
                else
                {
                    return false;
                }
            }


            // MyUtil.myLog(TAG," previous address "+mBluetoothDeviceAddress+"  current address "+address );

            final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);





            if (device == null)
            {
                Log.w(TAG, "Device not found.  Unable to connect.");
                return false;
            }


            //**************************************************************************************
            //**************************************************************************************
            // If device is paired remove the bonded state : 12 Bonded,  10 unbounded
           // Log.e("getBondState",""+ device.getBondState());
            if(device.getBondState() == 12)
            {
                unpairDevice(device);
            }

            //**************************************************************************************
            //**************************************************************************************



            // We want to directly connect to the device, so we are setting the autoConnect
            // parameter to false.
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
            Log.w(TAG, "Trying to create a new connection.");
            mBluetoothDeviceAddress = address;
            //  mConnectionState = STATE_CONNECTING;

            broadcastUpdate(ACTION_GATT_CONNECTING);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }


    public void unpairDevice(BluetoothDevice device)
    {
       //Log.e("getBondState",""+ mBluetoothAdapter.getRemoteDevice(address).getBondState());
        try
        {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }


   /* public boolean createBond(BluetoothDevice btDevice)
              throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }*/


    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect()
    {

        broadcastUpdate(ACTION_GATT_DISCONNECTING);

        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close()
    {
        try
        {
            if (mBluetoothGatt == null)
            {
                return;
            }
            Log.w(TAG, "mBluetoothGatt closed");
            mBluetoothDeviceAddress = null;
            mBluetoothGatt.close();
            mBluetoothGatt = null;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }


    /**
     * Enables or disables notification on a give characteristic.
     *

     */

    /**
     * Enable Notification on TX characteristic
     *
     * @return
     */
    public void enableTXNotification()
    { 
        /*if (mBluetoothGatt == null) {
            showMessage("mBluetoothGatt null" + mBluetoothGatt);
    		broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
    		return;
    	}*/

        BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
        if (RxService == null)
        {
            showMessage("Rx service not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_UUID);
        if (TxChar == null)
        {
            showMessage("Tx charateristic not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(TxChar, true);


        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        //descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

         /*After error in descriptor, Ballidaku changed the code and commented the previous upper line*/

        //***************************************************************************
        // Added extra by BALLIDAKU : this is bcz  descriptor.setValue() giving error in 6.0.1
        //***************************************************************************
        byte[] ENABLE_NOTIFICATION_VALUE = {0x01, 0x00};
        descriptor.setValue(ENABLE_NOTIFICATION_VALUE);



        /*End Here*/

        mBluetoothGatt.writeDescriptor(descriptor);
    }


    public void writeRXCharacteristic(byte[] value)
    {
        try
        {
            BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
            showMessage("mBluetoothGatt " + mBluetoothGatt);
            if (RxService == null)
            {
                showMessage("Rx service not found!");
                broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
                return;
            }
            BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
            if (RxChar == null)
            {
                showMessage("Rx charateristic not found!");
                broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
                return;
            }
            RxChar.setValue(value);
            boolean status = mBluetoothGatt.writeCharacteristic(RxChar);

            Log.d(TAG, "write TXchar - status=" + status);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showMessage(String msg)
    {
        Log.w(TAG, msg);
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
  /*  public List<BluetoothGattService> getSupportedGattServices()
    {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }*/
}

