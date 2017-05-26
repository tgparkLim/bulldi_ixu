/******************************************************************************
 * Copyright (C) Open Stack, Inc.  All Rights Reserved.
 *
 * This software is unpublished and contains the trade secrets and
 * confidential proprietary information of Open Stack, Inc..
 *
 * No part of this publication may be reproduced in any form whatsoever without
 * written prior approval by Open Stack, Inc..
 *
 * Open Stack, Inc. reserves the right to revise this publication
 * and make changes without obligation to notify any person of such revisions
 * or changes.
 *****************************************************************************/

/*
 * Autoconnect_service.java: control rescanning and re-connecting of disconnection
 */

package openstack.bulldi.safe3x.BLE_Connection;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;

import openstack.bulldi.common.BleDeviceInfo;
import openstack.bulldi.common.BluetoothLeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.MainActivity;
import openstack.bulldi.safe3x.R;
import pl.droidsonroids.gif.GifDrawable;


public class Autoconnect_service extends IntentService {
    // Requests to other activities
    private static final int REQ_ENABLE_BT = 0;
    private static final int REQ_DEVICE_ACT = 1;

    // GUI
    private static MainActivity mThis = null;
    private Intent mDeviceIntent;
    private static final int STATUS_DURATION = 5;

    // BLE management
    private boolean mBtAdapterEnabled = false;
    private boolean mBleSupported = true;
    private boolean mScanning = false;
    private int mNumDevs = 0;
    private int mConnIndex = NO_DEVICE;
    private List<BleDeviceInfo> mDeviceInfoList;
    private static BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBtAdapter = null;
    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothLeService mBluetoothLeService = null;
    private BluetoothDevice re_connect = null;
    private IntentFilter mFilter;
    private String[] mDeviceFilter = null;

    // Housekeeping
    private static final int NO_DEVICE = -1;
    private boolean mInitialised = false;

    private ScheduledExecutorService scheduleTaskExecutor;
    final Handler handler = new Handler();
    Intent intent_check;
    public Autoconnect_service() {
        super("Autoscan Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //startTimer();
        mDeviceInfoList = new ArrayList<BleDeviceInfo>();
        Resources res = getResources();
        mDeviceFilter = res.getStringArray(R.array.device_filter);


        mFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

        intent_check=intent;
            scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
            scheduleTaskExecutor.scheduleAtFixedRate(new run_schedule(), 0, 2000, TimeUnit.MILLISECONDS);

    }
    class run_schedule implements Runnable {

        @Override
        public void run() {
            if(BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(Connection.action)) {
                scan_repeated();
                Log.i("Check connection status", "onScan");
            }
            else {

            }
        }
    }
    public void scan_repeated(){
        if ( DeviceActivity.auto_scan==false) {
            stopSelf();
            cancelSchedule();
        }
        else if(MainActivity.is_MainActivity==true){
            stopSelf();
            cancelSchedule();
        }
        else {
            handler.post(new Runnable() {
                public void run() {
                    if (mScanning) {
                        stopScan();
                    } else {

                        startScan();
                        if(ScanView.mBtnScan!=null){
                            ScanView.mBtnScan.callOnClick();
                        }
                        final Handler handler_reconnect = new Handler();
                        handler_reconnect.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                check_autoconnect();
                            }
                        }, 1000);
                    }
                }
            });
        }
    }
    public void check_autoconnect(){
        //Do  after 1000ms
        if (DeviceActivity.MAC_addr != null) {

            for (int i = 0; i < mDeviceInfoList.size(); i++) {
                re_connect = mDeviceInfoList.get(i).getBluetoothDevice();
                String old_addr = re_connect.getAddress();
                if (DeviceActivity.MAC_addr.equals(old_addr)) {
                    auto_connect(re_connect);
                }
            }
        }
    }

    //Old connection


    void auto_connect(final BluetoothDevice x){
        int connState = mBluetoothManager.getConnectionState(x,
                BluetoothGatt.GATT);

        switch (connState) {
            case BluetoothGatt.STATE_CONNECTED:
                break;
            case BluetoothGatt.STATE_DISCONNECTED:
                if(ScanView.bv!=null) ScanView.bv.setImageResource(R.drawable.icn_connec_on);
                        boolean ok = mBluetoothLeService.connect(x.getAddress());
                        if (!ok) {
                        }
                break;
            default:

                break;
        }
    }

    private void startScan() {
        if (mBleSupported) {
            mNumDevs = 0;
            mDeviceInfoList.clear();
            scanLeDevice(true);

        }
    }

    private void stopScan() {
        mScanning = false;
        scanLeDevice(false);
    }


    private boolean scanLeDevice(boolean enable) {
        mBluetoothLeService = BluetoothLeService.getInstance();
        mBluetoothManager = mBluetoothLeService.getBtManager();
        mBtAdapter = mBluetoothManager.getAdapter();
        if (enable) {
            mScanning = mBtAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBtAdapter.stopLeScan(mLeScanCallback);
        }
        return mScanning;
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            Handler handler2=new Handler();
            handler2.post(new Runnable() {
                public void run() {
                    // Filter devices
                    if (checkDeviceFilter(device.getName())) {
                        if (!deviceInfoExists(device.getAddress())) {
                            // New device
                            BleDeviceInfo deviceInfo = createDeviceInfo(device, rssi);
                            addDevice(deviceInfo);
                        } else {
                            // Already in list, update RSSI info
                            BleDeviceInfo deviceInfo = findDeviceInfo(device);
                            deviceInfo.updateRssi(rssi);
                        }
                    }
                }

            });
        }
    };
    private void addDevice(BleDeviceInfo device) {
        mNumDevs++;
        mDeviceInfoList.add(device);
    }
    boolean checkDeviceFilter(String deviceName) {
        if (deviceName == null)
            return false;

        int n = mDeviceFilter.length;
        if (n > 0) {
            boolean found = false;
            for (int i = 0; i < n && !found; i++) {
                found = deviceName.equals(mDeviceFilter[i]);
            }
            return found;
        } else
            // Allow all devices if the device filter is empty
            return true;
    }
    private boolean deviceInfoExists(String address) {
        for (int i = 0; i < mDeviceInfoList.size(); i++) {
            if (mDeviceInfoList.get(i).getBluetoothDevice().getAddress()
                    .equals(address)) {
                return true;
            }
        }
        return false;
    }
    private BleDeviceInfo createDeviceInfo(BluetoothDevice device, int rssi) {
        BleDeviceInfo deviceInfo = new BleDeviceInfo(device, rssi);

        return deviceInfo;
    }
    private BleDeviceInfo findDeviceInfo(BluetoothDevice device) {
        for (int i = 0; i < mDeviceInfoList.size(); i++) {
            if (mDeviceInfoList.get(i).getBluetoothDevice().getAddress()
                    .equals(device.getAddress())) {
                return mDeviceInfoList.get(i);
            }
        }
        return null;
    }
    public void cancelSchedule() {
        if (scheduleTaskExecutor != null) {
            scheduleTaskExecutor.shutdownNow();
        }
    }

}