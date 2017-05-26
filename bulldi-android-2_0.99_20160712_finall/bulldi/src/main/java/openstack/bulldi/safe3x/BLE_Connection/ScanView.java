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
 * ScanView.java: scan ble and show result
 */

package openstack.bulldi.safe3x.BLE_Connection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
// import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import openstack.bulldi.common.BleDeviceInfo;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;
import openstack.util.CustomTimer;
import openstack.util.CustomTimerCallback;

//import com.felipecsl.gifimageview.library.GifImageView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ScanView extends Fragment {
  // private static final String TAG = "ScanView";
  private final int SCAN_TIMEOUT = 4; // Seconds
  private final int CONNECT_TIMEOUT = 20; // Seconds
  private Connection mActivity = null;

  private DeviceListAdapter mDeviceAdapter = null;
  //public static PlayGifView pGif;
//  public static GifImageView pGif;
  private TextViewPlus device_list;
  private TextView mStatus;
  public static Button mBtnScan = null;
  private ListView mDeviceListView = null;
  private boolean mBusy;

  private CustomTimer mScanTimer = null;
  private CustomTimer mConnectTimer = null;
  @SuppressWarnings("unused")
  private CustomTimer mStatusTimer;
  private Context mContext;

  private int rescan=0;

  public static ImageView bv;

  public  String connetecd_MAC_addr="";
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Log.i(TAG, "onCreateView");

    // The last two arguments ensure LayoutParams are inflated properly.
    View view = inflater.inflate(R.layout.fragment_scan, container, false);

    mActivity = (Connection) getActivity();
    mContext = mActivity.getApplicationContext();
    device_list=(TextViewPlus) view.findViewById(R.id.ble_device_list);
    mStatus = (TextView) view.findViewById(R.id.status);
    mBtnScan = (Button) view.findViewById(R.id.btn_scan);
    mDeviceListView = (ListView) view.findViewById(R.id.device_list);
    mDeviceListView.setClickable(true);
    mDeviceListView.setOnItemClickListener(mDeviceClickListener);
    //mEmptyMsg = (TextView)view.findViewById(R.id.no_device);
    mBusy = false;
    
    // Alert parent activity
    mActivity.onScanViewReady(view);
    mBtnScan.callOnClick();
    return view;
  }
  @Override
  public void onResume() {
    super.onResume();
    //Get connected MAC address back
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    connetecd_MAC_addr = sharedPreferences.getString("Connected_address", "");
//    Log.i("Check action", "connected MAC 0: "+connetecd_MAC_addr);
    if(device_list!=null){
      if(Language_setting.is_english==true) {
        if(device_list.getText().toString().compareTo("검색 결과")==0) device_list.setText("Scanning Result");
      } else {
        if(device_list.getText().toString().compareTo("Scanning Result")==0) device_list.setText("검색 결과");
      }
    }
    if(mBtnScan!=null){
      if(Language_setting.is_english==true) {
        if(mBtnScan.getText().toString().compareTo("재검색")==0) mBtnScan.setText("Rescan");
        if(mBtnScan.getText().toString().compareTo("멈춤")==0) mBtnScan.setText("Stop");
      } else {
        if(mBtnScan.getText().toString().compareTo("Rescan")==0) mBtnScan.setText("재검색");
        if(mBtnScan.getText().toString().compareTo("Stop")==0) mBtnScan.setText("멈춤");
      }
    }
    if(bv!=null){
      bv.setImageResource(R.drawable.icn_connec_off);
    }
  }

  @Override
  public void onDestroy() {
    // Log.i(TAG, "onDestroy");
    super.onDestroy();
  }

  void setStatus(String txt) {
    if(mStatus!=null) {
      mStatus.setText(txt);
      mStatus.setTextAppearance(mContext, R.style.statusStyle_Success);
    }
  }

  void setStatus(String txt, int duration) {
    setStatus(txt);
    mStatusTimer = new CustomTimer(null, duration, mClearStatusCallback);
  }

  void setError(String txt) {
    setBusy(false);
    stopTimers();
    if(mStatus!=null) {
      mStatus.setText(txt);
      mStatus.setTextAppearance(mContext, R.style.statusStyle_Failure);
    }
  }
	void notifyDataSetChanged() {
		List<BleDeviceInfo> deviceList = mActivity.getDeviceInfoList();
		if (mDeviceAdapter == null) {
			mDeviceAdapter = new DeviceListAdapter(mActivity,deviceList);
		}
		mDeviceListView.setAdapter(mDeviceAdapter);
		mDeviceAdapter.notifyDataSetChanged();
	}

	void setBusy(boolean f) {
		if (f != mBusy) {
			mBusy = f;
			if (!mBusy) {
				stopTimers();
				mBtnScan.setEnabled(true);	// Enable in case of connection timeout
	      mDeviceAdapter.notifyDataSetChanged(); // Force enabling of all Connect buttons
			}
			mActivity.showBusyIndicator(f);
		}
	}

  void updateGui(boolean scanning) {
    if (mBtnScan == null)
      return; // UI not ready
    
    setBusy(scanning);

    if (scanning) {
      mScanTimer = new CustomTimer(null, SCAN_TIMEOUT, mPgScanCallback);
      mBtnScan.setText(getResources().getString(R.string.button_stop));
      mBtnScan.setTextColor(Color.BLACK);
      mBtnScan.setBackgroundResource(R.drawable.button_on1);
      mStatus.setText(getResources().getString(R.string.bluetooth_scanning));
      //mEmptyMsg.setText(R.string.nodevice);
      mActivity.updateGuiState();
      rescan=rescan+1;
    } else {
      // Indicate that scanning has stopped
      mStatus.setTextAppearance(mContext, R.style.statusStyle_Success);
      if(rescan==0)mBtnScan.setText(getResources().getString(R.string.button_scan));
      else mBtnScan.setText(getResources().getString(R.string.button_rescan));
      mBtnScan.setTextColor(Color.WHITE);
      mBtnScan.setBackgroundResource(R.drawable.button_off1);
      mActivity.setProgressBarIndeterminateVisibility(false);
    }
  }

  // Listener for device list
  private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
    public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
    	// Log.d(TAG,"item click");
      bv=(ImageView) view.findViewById(R.id.btnConnect);
      if(bv!=null) bv.setImageResource(R.drawable.icn_connec_on);
      final Handler mhandler = new Handler();
      mhandler.postDelayed(new Runnable() {
        @Override
        public void run() {
          mConnectTimer = new CustomTimer(null, CONNECT_TIMEOUT, mPgConnectCallback);
          mBtnScan.setEnabled(false);
          mDeviceAdapter.notifyDataSetChanged(); // Force disabling of all Connect buttons
          mActivity.onDeviceClick(pos);
        }
      }, 10);
    }
  };

  // Listener for progress timer expiration
  private CustomTimerCallback mPgScanCallback = new CustomTimerCallback() {
    public void onTimeout() {
      mActivity.onScanTimeout();
    }

    public void onTick(int i) {
			mActivity.refreshBusyIndicator();
    }
  };

  // Listener for connect/disconnect expiration
  private CustomTimerCallback mPgConnectCallback = new CustomTimerCallback() {
    public void onTimeout() {
      mActivity.onConnectTimeout();
      mBtnScan.setEnabled(true);
    }

    public void onTick(int i) {
			mActivity.refreshBusyIndicator();
    }
  };

  // Listener for connect/disconnect expiration
  private CustomTimerCallback mClearStatusCallback = new CustomTimerCallback() {
    public void onTimeout() {
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
          setStatus("");
        }
      });
      mStatusTimer = null;
    }

    public void onTick(int i) {
    }
  };

  private void stopTimers() {
    if (mScanTimer != null) {
      mScanTimer.stop();
      mScanTimer = null;
    }
    if (mConnectTimer != null) {
      mConnectTimer.stop();
      mConnectTimer = null;
    }
  }

  //
  // CLASS DeviceAdapter: handle device list
  //
  @SuppressLint("InflateParams") class DeviceListAdapter extends BaseAdapter {
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
      if (name == null) {
        name = new String(getResources().getString(R.string.unknown_device));
      }
      if ( name.equals("bulldi"))
      {
        String descr = "bulldi";
        String deviceAlias=null;
        try {
          Method method = device.getClass().getMethod("getAliasName");
          if(method != null) {
              deviceAlias = (String)method.invoke(device);
              Log.i("Alias","device alias: "+deviceAlias);
          }
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
        if((deviceAlias!=null) && (deviceAlias.compareTo("bulldi")!=0) && (deviceAlias.compareTo("")!=0)){
            descr=descr+" - "+deviceAlias;
        }else {
        }
        ((TextViewPlus) vg.findViewById(R.id.descr)).setText(descr);
        if(device.getAddress().toString().compareTo(connetecd_MAC_addr)==0) {
          vg.setBackgroundResource(R.color.PowderBlue);
        }

      }
      else {
        String descr = getResources().getString(R.string.unknown_device);
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

}
