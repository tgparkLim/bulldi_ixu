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
 * Connection_seperate.java
 */

package openstack.bulldi.safe3x.BLE_Connection;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
// import android.util.Log;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import openstack.bulldi.common.BleDeviceInfo;
import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.HCIDefines;
import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Preference_etc.LanguageHelper;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.Preference_test.AlarmTest;
import openstack.bulldi.safe3x.R;
import openstack.util.CustomToast;

import android.view.ViewGroup.LayoutParams;

public class Connection_seperate extends ViewPagerActivity {
    // Log
    // private static final String TAG = "Connection";
    public SharedPreferences sharedPreferences;
    public static Resources res = null;
    public static boolean is_onConnection=true;

    // URLs
    private static final Uri URL_FORUM = Uri
            .parse("http://e2e.bulldi.com/support/low_power_rf/default.aspx?DCMP=hpa_hpa_community&HQS=NotApplicable+OT+lprf-forum");
    private static final Uri URL_STHOME = Uri
            .parse("http://www.bulldi.com/ww/en/wireless_connectivity/sensortag/index.shtml?INTC=SensorTagGatt&HQS=sensortag");

    // Requests to other activities
    private static final int REQ_ENABLE_BT = 0;
    private static final int REQ_DEVICE_ACT = 1;

    // GUI
    private static Connection_seperate mThis = null;
    private Scan_bluetooth mScanView;
    private Intent mDeviceIntent;
    private static final int STATUS_DURATION = 5;

    // BLE management
    private boolean mBtAdapterEnabled = false;
    private boolean mBleSupported = true;
    private boolean mScanning = false;
    private int mNumDevs = 0;
    private int mConnIndex = NO_DEVICE;
    public static List<BleDeviceInfo> mDeviceInfoList;
    private static BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBtAdapter = null;
    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothDevice re_connect = null;
    private BluetoothLeService mBluetoothLeService = null;
    private IntentFilter mFilter;
    private String[] mDeviceFilter = null;
    public static String action;
    //Save history data format
    DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
    DateFormat df2 = new SimpleDateFormat("HH:mm");
    DateFormat df1_ko = new SimpleDateFormat("yyyy.MM.dd");
    DateFormat df2_ko = new SimpleDateFormat("HH:mm");
    static Context context;
    private  TextView device_title;
    private  TextView help_title;
    Typeface font_title_light;
    Typeface font_title_regular;
    // Housekeeping
    private static final int NO_DEVICE = -1;
    private boolean mInitialised = false;
    //Store battery data
    SharedPreferences prefs = null;
    //Timer
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    //Alarm manager
/*    private AlarmManager manager;
    private PendingIntent pendingIntent;*/
    //Schedule
    private ScheduledExecutorService scheduleTaskExecutor;
    //Music
    //Music background_music=new Music();
    //Intentservice
    Intent intentMyIntentService;
    private boolean bolBroacastRegistred;
    public Connection_seperate() {
        mThis = this;
        mResourceFragmentPager = R.layout.fragment_pager_1;
        mResourceIdPager = R.id.pager_1;
        //Log.i("getAssets","value: "+getAssets());


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Start the application
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        res = this.getResources();

        font_title_light=Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        font_title_regular=Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        context=getApplication();
        // Log.i("Connection","value of context: "+context);

        // Initialize device list container and device filter
        mDeviceInfoList = new ArrayList<BleDeviceInfo>();
        Resources res = getResources();
        mDeviceFilter = res.getStringArray(R.array.device_filter);
        // Create the fragments and add them to the view pager and tabs
        int density = (int) getResources().getDisplayMetrics().density;
        int dp = 54/density;
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        device_title = new TextView(context);
        device_title.setText("BLE Device List");
        device_title.setTypeface(font_title_regular);
        //device_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
        device_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 54);
        device_title.setTextColor(Color.BLACK);
        device_title.setPadding(40, 40, 80, 0);

        mScanView = new Scan_bluetooth();
        mSectionsPagerAdapter.addSection(mScanView, "BLE Device List");
        //mSectionsPagerAdapter.addSection(mScanView, device_title);
        // Register the BroadcastReceiver
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

    }



    @Override
    public void onDestroy() {
        // Log.e(TAG,"onDestroy");
        super.onDestroy();
        is_onConnection=false;
        Log.i("Check connection","onDestroy Connection");
        DeviceActivity.auto_scan=false;
        //Music.stop(this);

        mBtAdapter = null;
        //Clear Device
        //DeviceActivity.mBtLeService=null;
        // unregisterReceiver(mReceiver);
        if (bolBroacastRegistred) {
            unregisterReceiver(mReceiver);
            bolBroacastRegistred = false;
        }
        // Clear cache
        File cache = getCacheDir();
        String path = cache.getPath();
        try {
            Runtime.getRuntime().exec(String.format("rm -rf %s", path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                return true;
            case R.id.opt_bt:
                onBluetooth();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DeviceActivity.auto_scan=false;
        System.exit(0);
        //finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void onUrl(final Uri uri) {
        Intent web = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(web);
    }

    private void onBluetooth() {
        Intent settingsIntent = new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(settingsIntent);
    }

    void onScanViewReady(View view) {


        if (!mInitialised) {
            // Broadcast receiver
            mBluetoothLeService = BluetoothLeService.getInstance();
            mBluetoothManager = mBluetoothLeService.getBtManager();
            mBtAdapter = mBluetoothManager.getAdapter();
            registerReceiver(mReceiver, mFilter);
            bolBroacastRegistred = true;
            mBtAdapterEnabled = mBtAdapter.isEnabled();
            if (mBtAdapterEnabled) {
                // Start straight away
                //startBluetoothLeService();
            } else {
                // Request BT adapter to be turned on
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQ_ENABLE_BT);
            }
            mInitialised = true;
        } else {
            mScanView.notifyDataSetChanged();
        }
        // Initial state of widgets
        updateGuiState();
    }

    public void onBtnScan(View view) {
        if (mScanning) {
            stopScan();
        } else {
            startScan();
        }
    }

    void onConnect() {
        if (mNumDevs > 0) {

            int connState = mBluetoothManager.getConnectionState(mBluetoothDevice,
                    BluetoothGatt.GATT);

            switch (connState) {
                case BluetoothGatt.STATE_CONNECTED:
                    mBluetoothLeService.disconnect(null);
                    break;
                case BluetoothGatt.STATE_DISCONNECTED:
                    //if(Scanning_result.bv!=null) Scanning_result.bv.setImageResource(R.drawable.icn_connec_on);
                    final Handler handler_reconnect = new Handler();
                    handler_reconnect.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            boolean ok = mBluetoothLeService.connect(mBluetoothDevice.getAddress());
                            if (!ok) {
                                setError("Connect failed");
                            }
                        }
                    }, 10);
                    break;
                default:
                    setError("Device busy (connecting/disconnecting)");
                    break;
            }
        }
    }

    private void startScan() {
        // Start device discovery
        //
        //Log.i("Check onResume","onStartScan");
        if (mBleSupported) {

            mNumDevs = 0;
            mDeviceInfoList.clear();
            mScanView.notifyDataSetChanged();
            scanLeDevice(true);
            mScanView.updateGui(mScanning);
            if (!mScanning) {
                setError("Device discovery start failed");
                setBusy(false);
            }
        } else {
            setError("BLE not supported on this device");
        }

    }

    private void stopScan() {
        mScanning = false;
        mScanView.updateGui(false);
        scanLeDevice(false);
    }

    private void startDeviceActivity() {
        mDeviceIntent = new Intent(this, DeviceActivity.class);
        mDeviceIntent.putExtra(DeviceActivity.EXTRA_DEVICE, mBluetoothDevice);
        startActivityForResult(mDeviceIntent, REQ_DEVICE_ACT);
    }

    private void stopDeviceActivity() {
        finishActivity(REQ_DEVICE_ACT);
    }

    public void onDeviceClick(final int pos) {

        if (mScanning)
            stopScan();

        setBusy(true);
        mBluetoothDevice = mDeviceInfoList.get(pos).getBluetoothDevice();
        if (mConnIndex == NO_DEVICE) {
            mScanView.setStatus(getResources().getString(R.string.connecting_2));
            mConnIndex = pos;
            onConnect();
        } else {
            mScanView.setStatus("Disconnecting");
            if (mConnIndex != NO_DEVICE) {
                mBluetoothLeService.disconnect(mBluetoothDevice.getAddress());
                Log.i("Check disconnect", "on DeviceClick");
            }
        }
    }

    public void onScanTimeout() {
        runOnUiThread(new Runnable() {
            public void run() {
                stopScan();
            }
        });
    }

    public void onConnectTimeout() {
        runOnUiThread(new Runnable() {
            public void run() {
                setError("Connection timed out");
            }
        });
        if (mConnIndex != NO_DEVICE) {
            mBluetoothLeService.disconnect(mBluetoothDevice.getAddress());
            mConnIndex = NO_DEVICE;
            Log.i("Check disconnect", "on ConnectTimeout");
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // GUI methods
    //
    public void updateGuiState() {
        boolean mBtEnabled = mBtAdapter.isEnabled();

        if (mBtEnabled) {
            if (mScanning) {
                // BLE Host connected
                if (mConnIndex != NO_DEVICE) {
                    String txt = mBluetoothDevice.getName() + " connected";
                    mScanView.setStatus(txt);
                } else {
                    mScanView.setStatus(mNumDevs + " "+getResources().getString(R.string.num_device));
                }
            }
        } else {
            mDeviceInfoList.clear();
            mScanView.notifyDataSetChanged();
        }
    }

    private void setBusy(boolean f) {
        mScanView.setBusy(f);
    }

    void setError(String txt) {
        mScanView.setError(txt);
        //CustomToast.middleBottom(this, "Turning BT adapter off and on again may fix Android BLE stack problems");
    }

    private BleDeviceInfo createDeviceInfo(BluetoothDevice device, int rssi) {
        BleDeviceInfo deviceInfo = new BleDeviceInfo(device, rssi);

        return deviceInfo;
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

    private void addDevice(BleDeviceInfo device) {
        mNumDevs++;
        mDeviceInfoList.add(device);
        mScanView.notifyDataSetChanged();
        if (mNumDevs > 1)
            mScanView.setStatus(mNumDevs + " "+getResources().getString(R.string.num_found_devices));
        else
            mScanView.setStatus("1 "+ getResources().getString(R.string.num_found_device));
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

    private BleDeviceInfo findDeviceInfo(BluetoothDevice device) {
        for (int i = 0; i < mDeviceInfoList.size(); i++) {
            if (mDeviceInfoList.get(i).getBluetoothDevice().getAddress()
                    .equals(device.getAddress())) {
                return mDeviceInfoList.get(i);
            }
        }
        return null;
    }

    private boolean scanLeDevice(boolean enable) {
        if (enable) {
            mScanning = mBtAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBtAdapter.stopLeScan(mLeScanCallback);
        }
        return mScanning;
    }

    List<BleDeviceInfo> getDeviceInfoList() {
        return mDeviceInfoList;
    }



    // Activity result handling
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_DEVICE_ACT:
                // When the device activity has finished: disconnect the device
                if (mConnIndex != NO_DEVICE) {
                    mBluetoothLeService.disconnect(mBluetoothDevice.getAddress());
                    Log.i("Check disconnect", "on ActivityResult");
                }
                break;

            case REQ_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();  // deprecated
                    int height = display.getHeight();  // deprecated
                    Toast toast= Toast.makeText(this, R.string.bt_on, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, height*2/3);
                    toast.show();
                } else {
                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();  // deprecated
                    int height = display.getHeight();  // deprecated
                    Toast toast= Toast.makeText(this, R.string.bt_not_on, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, height*2/3);
                    finish();
                }
                break;
            default:
                CustomToast.middleBottom(this, "Unknown request code: " + requestCode);

                // Log.e(TAG, "Unknown request code");
                break;
        }
    }
    private void displayToastAboveButton(View v, int messageId)
    {
        int xOffset = 0;
        int yOffset = 0;
        Rect gvr = new Rect();

        View parent = (View) v.getParent();
        int parentHeight = parent.getHeight();

        if (v.getGlobalVisibleRect(gvr))
        {
            View root = v.getRootView();

            int halfWidth = root.getRight() / 2;
            int halfHeight = root.getBottom() / 2;

            int parentCenterX = ((gvr.right - gvr.left) / 2) + gvr.left;

            int parentCenterY = ((gvr.bottom - gvr.top) / 2) + gvr.top;

            if (parentCenterY <= halfHeight)
            {
                yOffset = -(halfHeight - parentCenterY) - parentHeight;
            }
            else
            {
                yOffset = (parentCenterY - halfHeight) - parentHeight;
            }

            if (parentCenterX < halfWidth)
            {
                xOffset = -(halfWidth - parentCenterX);
            }

            if (parentCenterX >= halfWidth)
            {
                xOffset = parentCenterX - halfWidth;
            }
        }

        Toast toast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, xOffset, yOffset);
        toast.show();
    }
    // ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Broadcasted actions from Bluetooth adapter and BluetoothLeService
    //
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             action = intent.getAction();
            Log.i("Check action","value: "+action);
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                // Bluetooth adapter state change
                switch (mBtAdapter.getState()) {
                    case BluetoothAdapter.STATE_ON:
                        mConnIndex = NO_DEVICE;
                        //startBluetoothLeService();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, R.string.app_closing, Toast.LENGTH_LONG)
                                .show();
                        finish();
                        break;
                    default:
                        // Log.w(TAG, "Action STATE CHANGED not processed ");
                        break;
                }

                updateGuiState();
            } else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                // GATT connect
                int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
                        BluetoothGatt.GATT_FAILURE);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    setBusy(false);
                    startDeviceActivity();
                    //add history

                } else
                    setError(getResources().getString(R.string.status_connected_fail) + status);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                // GATT disconnect
                //add history

                int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
                        BluetoothGatt.GATT_FAILURE);
                stopDeviceActivity();
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    setBusy(false);
                    mScanView.setStatus(getResources().getString(R.string.status_disconected), STATUS_DURATION);
                } else {
                    setError(getResources().getString(R.string.status_error_generation) + HCIDefines.hciErrorCodeStrings.get(status));
                }
                mConnIndex = NO_DEVICE;
                mBluetoothLeService.close();
            } else {
                // Log.w(TAG,"Unknown action: " + action);
            }

        }
    };


    // Device scan callback.
    // NB! Nexus 4 and Nexus 7 (2012) only provide one scan result per scan
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Filter devices
                    if (checkDeviceFilter(device.getName())) {
                        if (!deviceInfoExists(device.getAddress())) {
                            // New device
                            BleDeviceInfo deviceInfo = createDeviceInfo(device, rssi);
                            addDevice(deviceInfo);
                            //Log.i("MAC address", "Device List size: " + mDeviceInfoList.size());
                        } else {
                            // Already in list, update RSSI info
                            BleDeviceInfo deviceInfo = findDeviceInfo(device);
                            deviceInfo.updateRssi(rssi);
                            mScanView.notifyDataSetChanged();
                        }
                    }
                }

            });
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Check connection", "onResume Connection"+AlarmTest.context);
        is_onConnection=true;
        Music.stop(getApplicationContext());
       // if(AlarmTest.context!=null) Music.stop(AlarmTest.context);
        if(Language_setting.is_english!=true){
            if(Scan_bluetooth.device_scan!=null) Scan_bluetooth.device_scan.setText("블루투스 검색 중...");
            if(Scan_bluetooth.mBtnScan!=null) Scan_bluetooth.mBtnScan.setText("검색");
        }else{
            if(Scan_bluetooth.device_scan!=null) Scan_bluetooth.device_scan.setText("Bluetooth Scanning...");
            if(Scan_bluetooth.mBtnScan!=null) Scan_bluetooth.mBtnScan.setText("Scan");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Check connection", "onPause Connection");
//        is_onConnection=false;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("Check connection", "onStop Connection");
    }

}
