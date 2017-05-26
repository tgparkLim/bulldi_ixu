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
 * Connection.java; control ble connection of bulldi
 */

package openstack.bulldi.safe3x.BLE_Connection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
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
import openstack.bulldi.safe3x.Device_View.Device_CO;
import openstack.bulldi.safe3x.Device_View.Device_Smoke;
import openstack.bulldi.safe3x.Device_View.Device_Temp;
import openstack.bulldi.safe3x.Device_View.SensorTagCOProfile;
import openstack.bulldi.safe3x.Device_View.SensorTagIRTemperatureProfile;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.Preference_history.History_bean;
import openstack.bulldi.safe3x.Preference_history.History_preference;
import openstack.bulldi.safe3x.R;
import openstack.util.CustomToast;

import android.view.ViewGroup.LayoutParams;

public class Connection extends ViewPagerActivity {
    // Log
    // private static final String TAG = "Connection";
    public static boolean is_onConnection=true;
    public static Resources res = null;
    // URLs
    private static final Uri URL_FORUM = Uri
            .parse("http://e2e.bulldi.com/support/low_power_rf/default.aspx?DCMP=hpa_hpa_community&HQS=NotApplicable+OT+lprf-forum");
    private static final Uri URL_STHOME = Uri
            .parse("http://www.bulldi.com/ww/en/wireless_connectivity/safe3x/index.shtml?INTC=SensorTagGatt&HQS=safe3x");

    // Requests to other activities
    private static final int REQ_ENABLE_BT = 0;
    private static final int REQ_DEVICE_ACT = 1;

    // GUI
    private static Connection mThis = null;
    private ScanView mScanView;
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
    private BluetoothDevice re_connect = null;
    private BluetoothLeService mBluetoothLeService = null;
    private IntentFilter mFilter;
    private String[] mDeviceFilter = null;
    public static String action;

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
    public SharedPreferences sharedPreferences;
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

    public Connection() {
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

        mScanView = new ScanView();
        mSectionsPagerAdapter.addSection(mScanView, "BLE Device List");
        //mSectionsPagerAdapter.addSection(mScanView, device_title);
        // Register the BroadcastReceiver
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

        //Music.play(this, R.raw.kiss_the_rain);
        //Alarm
/*        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);*/
        //Schedule
        //scheduleTaskExecutor= Executors.newScheduledThreadPool(5);5

    }



    @Override
    public void onDestroy() {
        // Log.e(TAG,"onDestroy");
        super.onDestroy();
        is_onConnection=false;
        Log.i("Check onPause","onDestroy Connection");
        DeviceActivity.auto_scan=false;
        //Music.stop(this);

        mBtAdapter = null;
        //Clear Device
        //DeviceActivity.mBtLeService=null;
//        unregisterReceiver(mReceiver);
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
        //stop_service();

        //Music.stop(this);
        //stoptimertask();
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
        //stop_service();
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
                    if(ScanView.bv!=null) ScanView.bv.setImageResource(R.drawable.icn_connec_on);
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
    void auto_connect(BluetoothDevice x){
        int connState = mBluetoothManager.getConnectionState(x,
                BluetoothGatt.GATT);

        switch (connState) {
            case BluetoothGatt.STATE_CONNECTED:
                //mBluetoothLeService.disconnect(null);
                break;
            case BluetoothGatt.STATE_DISCONNECTED:
                boolean ok = mBluetoothLeService.connect(x.getAddress());
                if (!ok) {
                    setError("Connect failed");
                }
                break;
            default:
                setError("Device busy (connecting/disconnecting)");
                break;
        }
    }

    private void startScan() {
        // Start device discovery
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
                    if (mNumDevs > 1) mScanView.setStatus(mNumDevs + " "+getResources().getString(R.string.num_devices));
                    else mScanView.setStatus(mNumDevs + " "+getResources().getString(R.string.num_device));
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
                    // User did not enable Bluetooth or an error occurred
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
                    //Add history
                    History_bean objHistory = new History_bean();
                    objHistory.setImg1(History_preference.connect_icon);
                    objHistory.setTitle(History_preference.ble_connect);
                    objHistory.setValue(History_preference.connect_value);
                    objHistory.setTime1(History_preference.df1.format(Calendar.getInstance().getTime()));
                    objHistory.setTime2(History_preference.df2.format(Calendar.getInstance().getTime()));
                    History_preference.HistoryList.add(0, objHistory);
                    saveHistory();
//                    Log.i("Check action", "add connect:" + History_preference.history_size + ";" + History_preference.HistoryList);
                } else
                    setError(getResources().getString(R.string.status_connected_fail) + status);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                // GATT disconnect

                int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
                        BluetoothGatt.GATT_FAILURE);
                stopDeviceActivity();
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    setBusy(false);
                    mScanView.setStatus(getResources().getString(R.string.status_disconected), STATUS_DURATION);
                } else {
                    setError(getResources().getString(R.string.status_error_generation) + HCIDefines.hciErrorCodeStrings.get(status));
                }
                //Add history
                History_bean objHistory = new History_bean();
                objHistory.setImg1(History_preference.disconnect_icon);
                objHistory.setTitle(History_preference.ble_disconnect);
                objHistory.setValue(History_preference.disconnect_value);
                objHistory.setTime1(History_preference.df1.format(Calendar.getInstance().getTime()));
                objHistory.setTime2(History_preference.df2.format(Calendar.getInstance().getTime()));
                History_preference.HistoryList.add(0, objHistory);
                saveHistory();
//                Log.i("Check action", "add disconnect:" + History_preference.history_size + ";" + History_preference.HistoryList.get(0).getTitle());
                if(ScanView.bv!=null){
                    ScanView.bv.setImageResource(R.drawable.icn_connec_off);
                }
                if(SensorTagIRTemperatureProfile.myList!=null) SensorTagIRTemperatureProfile.myList= new ArrayList<Double>();
                if(SensorTagCOProfile.myList_co_check!=null) SensorTagCOProfile.myList_co_check= new ArrayList<Double>();
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
        is_onConnection=true;
        if(ScanView.bv!=null) ScanView.bv.setImageResource(R.drawable.icn_connec_off);
        Music.stop(getApplicationContext());
//        Log.i("Check action","onResume");
        //Warning history

        //Get history_size back
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        History_preference.history_size = sharedPreferences.getInt("history_size", 0);
//        Log.i("Check history_size","value connection: "+History_preference.history_size);
        if ((History_preference.history_size == 0)&&(History_preference.is_clear_all==false)) {
            History_preference.HistoryList = new ArrayList<History_bean>();
            try {
                History_preference.packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
                History_preference.installTime1 = History_preference.df1.format(new Date(History_preference.packageInfo.firstInstallTime));
                History_preference.installTime2 = History_preference.df2.format(new Date(History_preference.packageInfo.firstInstallTime));
//                Log.i("Check install","value: "+History_preference.installTime1+" "+History_preference.installTime2);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            History_bean objHistory = new History_bean();
            History_preference.install=getResources().getString(R.string.warning_history_install);
            History_preference.install_value=getResources().getString(R.string.warning_history_install_value);
            objHistory.setImg1(History_preference.install_icon);objHistory.setTitle(History_preference.install);objHistory.setValue(History_preference.install_value);objHistory.setTime1(History_preference.installTime1);objHistory.setTime2(History_preference.installTime2);
            History_preference.HistoryList.add(objHistory);
            History_preference.history_size=History_preference.HistoryList.size();
            History_preference.his_image=new int[History_preference.history_size];History_preference.his_image[0]=History_preference.install_icon;
            History_preference.his_title=new String[History_preference.history_size];History_preference.his_title[0]=History_preference.install;
            History_preference.his_value=new String[History_preference.history_size];History_preference.his_value[0]=History_preference.install_value;
            History_preference.his_time1=new String[History_preference.history_size];History_preference.his_time1[0]=History_preference.installTime1;
            History_preference.his_time2=new String[History_preference.history_size];History_preference.his_time2[0]=History_preference.installTime2;
        }
        else {
            History_preference.HistoryList = new ArrayList<History_bean>();
            //Get history image back
            History_preference.his_image=new int[History_preference.history_size];
            for (int i = 0; i < History_preference.history_size; i++) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int img = sharedPreferences.getInt("history_img_" + Integer.toString(i), 0);
                History_preference.his_image[i]=img;
            }
            //Get history title back
            History_preference.his_title=new String[History_preference.history_size];
            for (int i = 0; i < History_preference.history_size; i++) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String title = sharedPreferences.getString("history_title_" + Integer.toString(i), "");
                History_preference.his_title[i]=title;
            }
            //Get history value back
            History_preference.his_value=new String[History_preference.history_size];
            for (int i = 0; i < History_preference.history_size; i++) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String value = sharedPreferences.getString("history_value_" + Integer.toString(i), "");
                History_preference.his_value[i]=value;
            }
            //Get history time1 back
            History_preference.his_time1=new String[History_preference.history_size];
            for (int i = 0; i < History_preference.history_size; i++) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String time1 = sharedPreferences.getString("history_time1_" + Integer.toString(i), "");
                History_preference.his_time1[i]=time1;
            }
            //Get history time2 back
            History_preference.his_time2=new String[History_preference.history_size];
            for (int i = 0; i < History_preference.history_size; i++) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String time2 = sharedPreferences.getString("history_time2_" + Integer.toString(i), "");
                History_preference.his_time2[i]=time2;
            }
            //Change the language
            try {
                History_preference.packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(Language_setting.is_english==true){
                for (int i = 0; i < History_preference.history_size; i++) {
                    if(History_preference.his_title[i].compareTo("환영합니다, 만나서 반갑습니다")==0){
                        History_preference.his_title[i]="Hi, nice to meet you";
                        History_preference.his_value[i]="installation";
                        History_preference.his_time1[i] = History_preference.df1.format(new Date(History_preference.packageInfo.firstInstallTime));
                        History_preference.his_time2[i] = History_preference.df2.format(new Date(History_preference.packageInfo.firstInstallTime));
                    }
                    if(History_preference.his_title[i].compareTo("잠자기 설정")==0){
                        History_preference.his_title[i]="Sleeping mode";
                        if(History_preference.his_value[i].compareTo("없음")==0)History_preference.his_value[i]="None";
                        else if(History_preference.his_value[i].compareTo("5분")==0)History_preference.his_value[i]="5m";
                        else if(History_preference.his_value[i].compareTo("10분")==0)History_preference.his_value[i]="10m";
                        else if(History_preference.his_value[i].compareTo("20분")==0)History_preference.his_value[i]="20m";
                        else History_preference.his_value[i]="30m";}
                    if(History_preference.his_title[i].compareTo("일산화탄소 감지")==0){
                        History_preference.his_title[i]="Carbon monoxide Emergency";}
                    if(History_preference.his_title[i].compareTo("온도이상 감지")==0){
                        History_preference.his_title[i]="Temperature Emergency";}
                    if(History_preference.his_title[i].compareTo("연기 감지")==0){
                        History_preference.his_title[i]="Smoke Emergency";}
                    if(History_preference.his_title[i].compareTo("블루투스 연결")==0){
                        History_preference.his_title[i]="Connected";}
                    if(History_preference.his_title[i].compareTo("블루투스 끊어짐")==0){
                        History_preference.his_title[i]="Disconnected";}
                }
            }
            else {
                for (int i = 0; i < History_preference.history_size; i++) {
                    if(History_preference.his_title[i].compareTo("Hi, nice to meet you")==0){
                        History_preference.his_title[i]="환영합니다, 만나서 반갑습니다";
                        History_preference.his_value[i]="앱 설치";
                        History_preference.his_time1[i] = History_preference.df1.format(new Date(History_preference.packageInfo.firstInstallTime));
                        History_preference.his_time2[i] = History_preference.df2.format(new Date(History_preference.packageInfo.firstInstallTime));}
                    if(History_preference.his_title[i].compareTo("Sleeping mode")==0){
                        History_preference.his_title[i]="잠자기 설정";
                        if(History_preference.his_value[i].compareTo("None")==0)History_preference.his_value[i]="없음";
                        else if(History_preference.his_value[i].compareTo("5m")==0)History_preference.his_value[i]="5분";
                        else if(History_preference.his_value[i].compareTo("10m")==0)History_preference.his_value[i]="10분";
                        else if(History_preference.his_value[i].compareTo("20m")==0)History_preference.his_value[i]="20분";
                        else History_preference.his_value[i]="30분";}
                    if(History_preference.his_title[i].compareTo("Carbon monoxide Emergency")==0){
                        History_preference.his_title[i]="일산화탄소 감지";}
                    if(History_preference.his_title[i].compareTo("Temperature Emergency")==0){
                        History_preference.his_title[i]="온도이상 감지";}
                    if(History_preference.his_title[i].compareTo("Smoke Emergency")==0){
                        History_preference.his_title[i]="연기 감지";}
                    if(History_preference.his_title[i].compareTo("Connected")==0){
                        History_preference.his_title[i]="블루투스 연결";}
                    if(History_preference.his_title[i].compareTo("Disconnected")==0){
                        History_preference.his_title[i]="블루투스 끊어짐";}
                }
            }
            //Create History bean
            for (int i = 0; i < History_preference.history_size; i++) {
                History_bean objHistory = new History_bean();
                objHistory.setImg1(History_preference.his_image[i]);objHistory.setTitle(History_preference.his_title[i]);objHistory.setValue(History_preference.his_value[i]);objHistory.setTime1(History_preference.his_time1[i]);objHistory.setTime2(History_preference.his_time2[i]);
                History_preference.HistoryList.add(objHistory);
            }
            History_preference.history_size=History_preference.HistoryList.size();
        }
    }
    protected void onPause() {
        super.onPause();
        is_onConnection=false;
//        Log.i("Check action","onPause");
        saveHistory();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
    }
void saveHistory(){
    //Save history_size
    History_preference.history_size=History_preference.HistoryList.size();
    SharedPreferences prefs_history_size = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    SharedPreferences.Editor editor_history_size = prefs_history_size.edit();
    editor_history_size.putInt("history_size", History_preference.history_size);
    editor_history_size.commit(); //important, otherwise it wouldn't save.
    //Save history image
    SharedPreferences[] prefs_history_img=new SharedPreferences[History_preference.history_size];
    SharedPreferences.Editor[] editor_history_img= new SharedPreferences.Editor[History_preference.history_size];
    for(int i=0;i<History_preference.history_size;i++) {
        prefs_history_img[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor_history_img[i] = prefs_history_img[i].edit();
        editor_history_img[i].putInt("history_img_" + Integer.toString(i), History_preference.HistoryList.get(i).getImg1());
        editor_history_img[i].commit(); //important, otherwise it wouldn't save.
    }
    //Save history title
    SharedPreferences[] prefs_history_title=new SharedPreferences[History_preference.history_size];
    SharedPreferences.Editor[] editor_history_title= new SharedPreferences.Editor[History_preference.history_size];
    for(int i=0;i<History_preference.history_size;i++) {
        prefs_history_title[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor_history_title[i] = prefs_history_title[i].edit();
        editor_history_title[i].putString("history_title_" + Integer.toString(i), History_preference.HistoryList.get(i).getTitle());
        editor_history_title[i].commit(); //important, otherwise it wouldn't save.
    }
    //Save history value
    SharedPreferences[] prefs_history_value=new SharedPreferences[History_preference.history_size];
    SharedPreferences.Editor[] editor_history_value= new SharedPreferences.Editor[History_preference.history_size];
    for(int i=0;i<History_preference.history_size;i++) {
        prefs_history_value[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor_history_value[i] = prefs_history_value[i].edit();
        editor_history_value[i].putString("history_value_" + Integer.toString(i), History_preference.HistoryList.get(i).getValue());
        editor_history_value[i].commit(); //important, otherwise it wouldn't save.
    }
    //Save history time1
    SharedPreferences[] prefs_history_time1=new SharedPreferences[History_preference.history_size];
    SharedPreferences.Editor[] editor_history_time1= new SharedPreferences.Editor[History_preference.history_size];
    for(int i=0;i<History_preference.history_size;i++) {
        prefs_history_time1[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor_history_time1[i] = prefs_history_time1[i].edit();
        editor_history_time1[i].putString("history_time1_" + Integer.toString(i), History_preference.HistoryList.get(i).getTime1());
        editor_history_time1[i].commit(); //important, otherwise it wouldn't save.
    }
    //Save history time2
    SharedPreferences[] prefs_history_time2=new SharedPreferences[History_preference.history_size];
    SharedPreferences.Editor[] editor_history_time2= new SharedPreferences.Editor[History_preference.history_size];
    for(int i=0;i<History_preference.history_size;i++) {
        prefs_history_time2[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor_history_time2[i] = prefs_history_time2[i].edit();
        editor_history_time2[i].putString("history_time2_" + Integer.toString(i), History_preference.HistoryList.get(i).getTime2());
        editor_history_time2[i].commit(); //important, otherwise it wouldn't save.
    }
}

}
