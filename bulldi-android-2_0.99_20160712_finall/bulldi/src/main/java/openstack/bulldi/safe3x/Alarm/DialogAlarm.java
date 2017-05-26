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
 * DialogAlarm.java: control alarm situation  (music, sms, vibration, ...)
 */

package openstack.bulldi.safe3x.Alarm;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import openstack.bulldi.safe3x.BLE_Connection.Connection_seperate;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.Preference_lighting.Listview_light;
import openstack.bulldi.safe3x.Preference_sharing.Message_content_preference;
import openstack.bulldi.safe3x.R;



public class DialogAlarm extends FragmentActivity {
    Context context;

    public  FragmentManager fm;
    SendSMS send_num;
//    public static boolean having_AlarmDialog=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        context=getApplication();
        fm = getSupportFragmentManager();
        send_num = new SendSMS();
        alarm_status();
    }
    @Override
    public void onBackPressed() {
      super.onBackPressed();
    }
    public void alarm_status() {
        //isAlarm = true;
        if (Message_content_preference.show_message.compareTo(getResources().getString(R.string.message_content_item_1)) == 0) {
            AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            Music.play(context, R.raw.siren);
        } else if (Message_content_preference.show_message.compareTo(getResources().getString(R.string.message_content_item_2)) == 0) {
            AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            Music.play(context, R.raw.fire_car);
        } else if (Message_content_preference.show_message.compareTo(getResources().getString(R.string.message_content_item_3)) == 0) {
            AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            Music.play(context, R.raw.emergency1);
        } else {
            AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            Music.play(context, R.raw.emergency2);
        }
        Vibrator phone_vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        phone_vib.vibrate(3000);
        send_num.send_sms(context);
        Alarm frag = new Alarm();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(frag, "fragment_alarm");
        ft.commitAllowingStateLoss();
//        having_AlarmDialog=true;
        Log.i("Check alarm", "onCreate dialog");
    }
    @Override
    public void onResume() {
        unlockScreen();
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

}
