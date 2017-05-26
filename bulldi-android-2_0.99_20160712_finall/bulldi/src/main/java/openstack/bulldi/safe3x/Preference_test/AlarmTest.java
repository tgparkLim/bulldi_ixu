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
 * AlarmTest.java : Operation test->Alarm dialog show
 */

package openstack.bulldi.safe3x.Preference_test;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import openstack.bulldi.safe3x.Alarm.DialogAlarm;
import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Device_View.InOut_Service;
import openstack.bulldi.safe3x.Device_View.SensorTagCOProfile;
import openstack.bulldi.safe3x.Device_View.SensorTagIRTemperatureProfile;
import openstack.bulldi.safe3x.Device_View.SensorTagSmokeProfile;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.R;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class AlarmTest extends DialogFragment {
    public static DialogAlarmTest mActivity = null;
    int IO_data_stop = 0x80;//control the lamp
    int IO_config = 0x01;
    double change_CO = 49.5;
    //public static boolean isAlarm=true;
    public static Context context;
    private ImageView stop_alarm;
    GifImageView pGif;

    public AlarmTest() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog=getDialog();
        dialog.setCancelable(false);
        mActivity = (DialogAlarmTest) getActivity();
        if (getDialog() == null) {
            return;
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        //getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setBackgroundDrawable(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fire_alarm_test, container, false);
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
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = getActivity();
        pGif = (GifImageView) view.findViewById(R.id.viewGif_test);
        try {
            GifDrawable gifFromAssets = new GifDrawable(getActivity().getApplicationContext().getResources().getAssets(), "fire.gif");
            pGif.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stop_alarm = (ImageView) view.findViewById(R.id.stop_alarm_test);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music.stop(context);
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                    byte value_data = (byte) (IO_data_stop);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                    Lighting_preference.light_on = false;
                    if (DeviceActivity.lighting_state != null)
                        DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                    mActivity.finish();
                    Operation_test_adapter.row0.setBackgroundResource(R.color.White);
                    //Send comand to bulldi stop testing
                    Operation_test_adapter.previous_data = (byte) (0x00);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, (byte) (0x00));
                }
            }
        });
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}

