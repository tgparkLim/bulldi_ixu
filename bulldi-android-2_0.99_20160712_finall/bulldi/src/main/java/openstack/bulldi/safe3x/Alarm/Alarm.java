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
 * Alarm.java : Dialog interface show when alarm happens
 */

package openstack.bulldi.safe3x.Alarm;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Device_View.InOut_Service;
import openstack.bulldi.safe3x.Device_View.Sensor;
import openstack.bulldi.safe3x.Device_View.SensorTagCOProfile;
import openstack.bulldi.safe3x.Device_View.SensorTagIRTemperatureProfile;
import openstack.bulldi.safe3x.Device_View.SensorTagSmokeProfile;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.Preference_lighting.Listview_light;
import openstack.bulldi.safe3x.R;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class Alarm extends DialogFragment {
    public static DialogAlarm mActivity = null;
     int IO_data_stop = 0x80;//control the lamp
     int IO_config = 0x01;
    double change_CO=49.5;
    //public static boolean isAlarm=true;
    Context context;
    private  ImageView stop_alarm;
    private ImageView call_119;
    private ImageView dial_phone;
    GifImageView pGif;
    public Alarm() {
        super();
    }
    @Override
    public void onStart() {
        super.onStart();
        DeviceActivity.isAlarm=true;
        Log.i("Check alarm", "onStart DialogFragment");
        Dialog dialog=getDialog();
        dialog.setCancelable(false);
        mActivity=(DialogAlarm) getActivity();
        if (getDialog() == null) {
            return;
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth =  metrics.widthPixels;
        int screenHeight =  metrics.heightPixels;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        //getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setBackgroundDrawable(null);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fire_alarm, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity=null;
    }

    @Override
    public void onViewCreated(final View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = getActivity();
        pGif = (GifImageView) view.findViewById(R.id.viewGif);
        try {
            GifDrawable gifFromAssets = new GifDrawable( getActivity().getApplicationContext().getResources().getAssets(), "fire.gif" );
            pGif.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stop_alarm=(ImageView) view.findViewById(R.id.stop_alarm);
        //stop_alarm.setImageResource(R.drawable.but_beep_on);
        call_119=(ImageView) view.findViewById(R.id.call_119);
        dial_phone=(ImageView) view.findViewById(R.id.dial_phone);
        stop_alarm.setOnTouchListener(new View.OnTouchListener() {
            //Timer timer = new Timer();
            Handler mHandler_alarm = new Handler();
            Runnable mRunnable_alarm=null;
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch ( arg1.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        stop_alarm.setImageResource(R.drawable.fire_stop_on);
                        try {
                            GifDrawable gifFromAssets = new GifDrawable( getActivity().getApplicationContext().getResources().getAssets(), "number.gif" );
                            pGif.setImageDrawable(gifFromAssets);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mRunnable_alarm=new  Runnable() {
                            @Override
                            public void run() {

                                Music.stop(context);
                                Dialog dialog=getDialog();
                                if(dialog!=null){
                                    dialog.dismiss();
                                    byte value_config =  (byte) (IO_config);
                                    byte value_data =  (byte) (IO_data_stop);
                                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                    Lighting_preference.light_on = false;
                                    //Update alarm status
                                    if(SensorTagIRTemperatureProfile.temp_alarm==true) {
                                        SensorTagIRTemperatureProfile.temp_alarm=false;
                                        SensorTagIRTemperatureProfile.temp_alarm_fix=false;
                                        SensorTagIRTemperatureProfile.temp_alarm_rate=false;
                                        SensorTagIRTemperatureProfile.nomal_temp=25;
                                    }
                                    if(SensorTagCOProfile.co_alarm==true) {
                                        SensorTagCOProfile.co_alarm=false;
                                        SensorTagCOProfile.myList_co_check=new ArrayList<Double>();
                                    }
                                    if(SensorTagSmokeProfile.smoke_alarm==true) SensorTagSmokeProfile.smoke_alarm=false;
                                    if (DeviceActivity.lighting_state != null)
                                        DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                                    DeviceActivity.isAlarm = false;
                                    mActivity.finish();
                                }
                            }
                                //getDialog().dismiss();

                        };
                        mHandler_alarm.postDelayed(mRunnable_alarm, 3000); //time out 3s
                        return true;
                    case MotionEvent.ACTION_UP:
                        //stop timer
                        stop_alarm.setImageResource(R.drawable.fire_stop_off);
                        mHandler_alarm.removeCallbacks(mRunnable_alarm);
                        mHandler_alarm=new Handler();
                        try {
                            GifDrawable gifFromAssets = new GifDrawable( getActivity().getApplicationContext().getResources().getAssets(), "fire.gif" );
                            pGif.setImageDrawable(gifFromAssets);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });
        call_119.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_119.setImageResource(R.drawable.fire_119_on);
                final Handler handler_reconnect = new Handler();
                handler_reconnect.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Music.stop(context);
                        DeviceActivity.call_num.call(context);
                        getDialog().dismiss();
                        mActivity.finish();
                    }
                }, 10);
            }
        });
        dial_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial_phone.setImageResource(R.drawable.fire_keypad_on);
                final Handler handler_reconnect = new Handler();
                handler_reconnect.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Music.stop(context);
                        DeviceActivity.call_num.dial(context);
                        getDialog().dismiss();
                        mActivity.finish();
                    }
                }, 10);

            }
        });
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}