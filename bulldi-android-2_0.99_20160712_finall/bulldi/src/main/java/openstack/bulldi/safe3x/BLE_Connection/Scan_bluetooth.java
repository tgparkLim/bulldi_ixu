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
 * Scan_bluetooth.c: control scanning ble
 */

package openstack.bulldi.safe3x.BLE_Connection;

import java.io.IOException;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
// import android.util.Log;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;
import openstack.util.CustomTimer;
import openstack.util.CustomTimerCallback;

//import com.felipecsl.gifimageview.library.GifImageView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class Scan_bluetooth extends Fragment {
    // private static final String TAG = "ScanView";
    private final int SCAN_TIMEOUT = 4; // Seconds
    private final int CONNECT_TIMEOUT = 20; // Seconds
    private Connection_seperate mActivity = null;


    public static TextViewPlus device_scan;
    public static GifImageView pGif;
    private TextViewPlus mStatus;
    public static Button mBtnScan = null;
    private boolean mBusy;
    public static View result;

    private CustomTimer mScanTimer = null;
    //private CustomTimer mConnectTimer = null;
    @SuppressWarnings("unused")
    private CustomTimer mStatusTimer;
    private Context mContext;

    private int rescan=0;

    //public static ImageView bv;
    public static FragmentManager fm_1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Log.i(TAG, "onCreateView");

        // The last two arguments ensure LayoutParams are inflated properly.
        View view = inflater.inflate(R.layout.scan, container, false);

        mActivity = (Connection_seperate) getActivity();
        mContext = mActivity.getApplicationContext();
        fm_1=getActivity().getSupportFragmentManager();
        // Initialize widgets
        pGif = (GifImageView) view.findViewById(R.id.blueGif);
        try {
            GifDrawable gifFromAssets = new GifDrawable( mContext.getResources().getAssets(), "bluetooth_constant.gif" );
            pGif.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
        }
        device_scan=(TextViewPlus) view.findViewById(R.id.blue_scan);
        mStatus = (TextViewPlus) view.findViewById(R.id.no_device);
        mBtnScan = (Button) view.findViewById(R.id.btn_scan_seperate);
        mBusy = false;
        // Alert parent activity
        mActivity.onScanViewReady(view);
        mBtnScan.callOnClick();
        return view;
    }

    @Override
    public void onDestroy() {
        // Log.i(TAG, "onDestroy");
        super.onDestroy();

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
    }

    void setBusy(boolean f) {
        if (f != mBusy) {
            mBusy = f;
            if (!mBusy) {
                stopTimers();
                mBtnScan.setEnabled(true);	// Enable in case of connection timeout
                //Scanning_result.mDeviceAdapter.notifyDataSetChanged(); // Force enabling of all Connect buttons
            }
            mActivity.showBusyIndicator(f);
        }
    }

    void updateGui(boolean scanning) {
        if (mBtnScan == null)
            return; // UI not ready

        setBusy(scanning);

        if (scanning) {

            // Indicate that scanning has started
            //pGif.setImageResource(R.drawable.bluetooth_scan);
            try {
                GifDrawable gifFromAssets = new GifDrawable( mContext.getResources().getAssets(), "bluetooth_scan.gif" );
                pGif.setImageDrawable(gifFromAssets);
            } catch (IOException e) {
                e.printStackTrace();
            }
            device_scan.setText(getResources().getString(R.string.bluetooth_scan));
            mScanTimer = new CustomTimer(null, SCAN_TIMEOUT, mPgScanCallback);
            mBtnScan.setTextColor(Color.BLACK);
            mBtnScan.setText(getResources().getString(R.string.button_stop));
            mBtnScan.setBackgroundResource(R.drawable.button_on);

            mStatus.setText(getResources().getString(R.string.bluetooth_scanning));
            //mEmptyMsg.setText(R.string.nodevice);
            mActivity.updateGuiState();

        } else {
            try {
                GifDrawable gifFromAssets = new GifDrawable( mContext.getResources().getAssets(), "bluetooth_constant.gif" );
                pGif.setImageDrawable(gifFromAssets);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Indicate that scanning has stopped
            mStatus.setTextAppearance(mContext, R.style.statusStyle_Success);
            mBtnScan.setTextColor(Color.WHITE);
            if(rescan==0)mBtnScan.setText(getResources().getString(R.string.button_scan));
            else mBtnScan.setText(getResources().getString(R.string.button_rescan));
            mBtnScan.setBackgroundResource(R.drawable.button_off);
            //mBtnScan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_refresh, 0);
            //mEmptyMsg.setText(R.string.scan_advice);
            mActivity.setProgressBarIndeterminateVisibility(false);
            //mDeviceAdapter.notifyDataSetChanged();
            Scanning_result frag = new Scanning_result();
            FragmentTransaction ft = fm_1.beginTransaction();
            ft.add(frag, "fragment_scan_result");
            ft.commitAllowingStateLoss();
//            frag.show(fm_1, "fragment_scan_result");

        }
    }



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
        if (Scanning_result.mConnectTimer != null) {
            Scanning_result.mConnectTimer.stop();
            Scanning_result.mConnectTimer = null;
        }
    }
    void setStatus(String txt) {
        mStatus.setText(txt);
        mStatus.setTextAppearance(mContext, R.style.statusStyle_Success);
    }
    void setStatus(String txt, int duration) {
        setStatus(txt);
        mStatusTimer = new CustomTimer(null, duration, mClearStatusCallback);
    }

}
