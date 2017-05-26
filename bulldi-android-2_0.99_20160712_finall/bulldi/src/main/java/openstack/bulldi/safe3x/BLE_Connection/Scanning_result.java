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
 * Scanning_result.java: show result of ble scan
 */

package openstack.bulldi.safe3x.BLE_Connection;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import openstack.bulldi.common.BleDeviceInfo;
import openstack.bulldi.safe3x.Device_View.BatteryInformationServiceProfile;
import openstack.bulldi.safe3x.Preference_etc.Alias;
import openstack.bulldi.safe3x.Preference_sharing.Notify_friend_preference;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;
import openstack.util.CustomTimer;
import openstack.util.CustomTimerCallback;



public class Scanning_result extends DialogFragment {
    private final int SCAN_TIMEOUT = 10; // Seconds
    private final int CONNECT_TIMEOUT = 20; // Seconds
    public static ArrayList<String> device_MAC=new ArrayList<String>();
    public static ArrayList<String> device_name=new ArrayList<String>();
    private Connection_seperate mActivity = null;
    public static CustomTimer mConnectTimer = null;
    @SuppressWarnings("unused")
    private CustomTimer mStatusTimer;
    Context context;
    public  DeviceListAdapter mDeviceAdapter;
    public  ImageView bv;
    public ListView mDeviceListView = null;
    public TextViewPlus mStatus;
    public Button mBtnReScan = null;
    public Scanning_result() {
        super();
    }
    public  String connetecd_MAC_addr="";
    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) {
            return;
        }

        //WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth =  metrics.widthPixels;
        int screenHeight =  metrics.heightPixels;
        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.full_screen_dialog);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.scan_result, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Get connected MAC address back
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        connetecd_MAC_addr = sharedPreferences.getString("Connected_address", "");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity = (Connection_seperate) getActivity();
        context = getActivity();
        List<BleDeviceInfo> deviceList = mActivity.getDeviceInfoList();
        mDeviceListView = (ListView) view.findViewById(R.id.device_list_seperate);
        mDeviceAdapter = new DeviceListAdapter(mActivity,deviceList);
        mDeviceListView.setAdapter(mDeviceAdapter);
        mDeviceListView.setClickable(true);
        mDeviceListView.setOnItemClickListener(mDeviceClickListener);
        mStatus = (TextViewPlus) view.findViewById(R.id.num_device);
        int num_device=deviceList.size();
        if((num_device==0)||(num_device==1)) mStatus.setText(Integer.toString(num_device)+ " "+getResources().getString(R.string.num_found_device));
        if(num_device==0) {

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.no_device_dialog);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            window.setAttributes(wlp);
            final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_noDevice_close);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogButton.setBackgroundResource(R.drawable.button_on);
                    dialogButton.setTextColor(Color.BLACK);
                    final Handler handler_reconnect = new Handler();
                    handler_reconnect.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                        }
                    }, 10);
                }
            });
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final Handler mhandler = new Handler();
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                }
            }, 500);

        }
        else mStatus.setText(Integer.toString(num_device)+ " "+getResources().getString(R.string.num_found_devices));
        mStatus.setTextAppearance(context, R.style.statusStyle_Success);
        mBtnReScan = (Button) view.findViewById(R.id.btn_rescan);
        mBtnReScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnReScan.setBackgroundResource(R.drawable.button_on);
                mBtnReScan.setTextColor(Color.BLACK);
                final Handler handler_reconnect = new Handler();
                handler_reconnect.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDialog().dismiss();
                        Scan_bluetooth.mBtnScan.callOnClick();
                    }
                }, 10);

            }
        });
    }
    void centerText(View view) {
        if( view instanceof TextView){
            ((TextView) view).setGravity(Gravity.CENTER);
        }else if( view instanceof ViewGroup){
            ViewGroup group = (ViewGroup) view;
            int n = group.getChildCount();
            for( int i = 0; i<n; i++ ){
                centerText(group.getChildAt(i));
            }
        }
    }
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
            Log.d("Check bv", "value: "+bv);
            bv=(ImageView) view.findViewById(R.id.btnConnect);
            if(bv!=null) bv.setImageResource(R.drawable.icn_connec_on);
            final Handler handler_reconnect = new Handler();
            handler_reconnect.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mConnectTimer = new CustomTimer(null, CONNECT_TIMEOUT, mPgConnectCallback);
                    mBtnReScan.setEnabled(false);
                    mDeviceAdapter.notifyDataSetChanged(); // Force disabling of all Connect buttons
                    mActivity.onDeviceClick(pos);
                    getDialog().dismiss();
                }
            }, 10);

        }
    };

    // Listener for connect/disconnect expiration
    private CustomTimerCallback mPgConnectCallback = new CustomTimerCallback() {
        public void onTimeout() {
            mActivity.onConnectTimeout();
            Scan_bluetooth.mBtnScan.setEnabled(true);
            mBtnReScan.setEnabled(true);
        }

        public void onTick(int i) {
            mActivity.refreshBusyIndicator();
        }
    };

    // Listener for connect/disconnect expiration

    //
    // CLASS DeviceAdapter: handle device list
    //
    @SuppressLint("InflateParams") public  class DeviceListAdapter extends BaseAdapter {
        private List<BleDeviceInfo> mDevices;
        private LayoutInflater mInflater;

        public DeviceListAdapter(Context context, List<BleDeviceInfo> devices) {
            mInflater = LayoutInflater.from(context);
            mDevices = devices;
        }

        public int getCount() {
            return mDevices.size();
        }

        public Object getItem(int position) {
            return mDevices.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup vg;

            if (convertView != null) {
                vg = (ViewGroup) convertView;
            } else {
                vg = (ViewGroup) mInflater.inflate(R.layout.element_device, null);

            }

            BleDeviceInfo deviceInfo = mDevices.get(position);
            BluetoothDevice device = deviceInfo.getBluetoothDevice();

            int rssi = deviceInfo.getRssi();
            String name;
            name = device.getName();
            Log.i("Check name","value: "+name);
            if (name == null) {
                name = new String(getResources().getString(R.string.unknown_device));
            }
            if ( name.equals("bulldi"))
            {
                //String descr = "bulldi"+ "\n" + device.getAddress();
                String descr = "bulldi";
                String deviceAlias=null;
                try {
                    Method method = device.getClass().getMethod("getAliasName");
                    if(method != null) {
                        deviceAlias = (String)method.invoke(device);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if((deviceAlias!=null) && (deviceAlias.compareTo("bulldi")!=0) && (deviceAlias.compareTo("")!=0)){
                    descr=descr+" - "+deviceAlias+ "\n" + device.getAddress();
                }else {
                    descr=descr+ "\n" + device.getAddress();
                }
                ((TextViewPlus) vg.findViewById(R.id.descr)).setText(descr);
                if(device.getAddress().toString().compareTo(connetecd_MAC_addr)==0) {
                    vg.setBackgroundResource(R.color.PowderBlue);
                }

            }
            else {
                String descr = getResources().getString(R.string.unknown_device)+ "\n" + device.getAddress();
                ((TextViewPlus) vg.findViewById(R.id.descr)).setText(descr);
            }


            ImageView iv = (ImageView)vg.findViewById(R.id.devImage);
            if ( name.equals("bulldi"))
                iv.setImageResource(R.drawable.icn_ble_03);
            else {
                iv.setImageResource(R.drawable.icn_ble_02);
            }

            return vg;
        }
    }
    public boolean is_existing(ArrayList<String>array,String s)
    {
        boolean existing=false;
        for(int i=0;i<array.size();i++)
        {
            if(array.get(i)==s) existing=true;
        }
        return existing;
    }

}
