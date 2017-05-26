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
 * Message_content_preference.java: Alarm sound preference show
 */

package openstack.bulldi.safe3x.Preference_sharing;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Preference_sharing.CustomAdapter;
import openstack.bulldi.safe3x.R;
import openstack.util.CheckBoxImageView;
import openstack.util.TextViewPlus;


public class Message_content_preference  extends Activity {
    ListView lv;
    Context context;
    public static String show_message="Siren";
    public SharedPreferences sharedPreferences;
    public static int [] prgmImages_1={R.drawable.icn_voiceletter,R.drawable.icn_voiceletter,R.drawable.icn_voiceletter,R.drawable.icn_voiceletter};
    public static int [] prgmImages_2={R.drawable.icn_beep,R.drawable.icn_beep,R.drawable.icn_beep,R.drawable.icn_beep};
    public static String [] prgmNameList={"Siren","Fire car","Emergency 1","Emergency 2"};
    public static boolean is_message=false;
    public static View message_setting_back;
    public TextViewPlus message_title;

    LinearLayout row1;
    LinearLayout row2;
    LinearLayout row3;
    LinearLayout row4;
    CheckBoxImageView check1;
    CheckBoxImageView check2;
    CheckBoxImageView check3;
    CheckBoxImageView check4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context=this;
        show_message=getResources().getString(R.string.message_content_item_1);
        prgmNameList=new String[]{getResources().getString(R.string.message_content_item_1),getResources().getString(R.string.message_content_item_2),getResources().getString(R.string.message_content_item_3),getResources().getString(R.string.message_content_item_4)};
//        setContentView(R.layout.message_content);
        setContentView(R.layout.audio_setting);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.message_content_titlebar);
        message_setting_back= findViewById(R.id.message_back);
        message_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Music.stop(context);
            }
        });

        message_title=(TextViewPlus) findViewById(R.id.message_title);
        message_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Music.stop(context);
            }
        });

        row1 = (LinearLayout) findViewById(R.id.audio_row1);row1.setClickable(true);
        row2 = (LinearLayout) findViewById(R.id.audio_row2);row2.setClickable(true);
        row3 = (LinearLayout) findViewById(R.id.audio_row3);row3.setClickable(true);
        row4 = (LinearLayout) findViewById(R.id.audio_row4);row4.setClickable(true);
        check1 = (CheckBoxImageView) findViewById(R.id.check_audio1);
        check2 = (CheckBoxImageView) findViewById(R.id.check_audio2);
        check3 = (CheckBoxImageView) findViewById(R.id.check_audio3);
        check4 = (CheckBoxImageView) findViewById(R.id.check_audio4);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        show_message = sharedPreferences.getString("voice_state", getResources().getString(R.string.message_content_item_1));
        if(show_message.compareTo(context.getResources().getString(R.string.message_content_item_1)) == 0){
            row1.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
        } else if(show_message.compareTo(context.getResources().getString(R.string.message_content_item_2)) == 0) {
            row2.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
        } else if(show_message.compareTo(context.getResources().getString(R.string.message_content_item_3)) == 0) {
            row3.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
        } else if(show_message.compareTo(context.getResources().getString(R.string.message_content_item_4)) == 0) {
            row4.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
        }
        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row1.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
                show_message=context.getResources().getString(R.string.message_content_item_1);
                row2.setBackgroundColor(context.getResources().getColor(R.color.White));
                row3.setBackgroundColor(context.getResources().getColor(R.color.White));
                row4.setBackgroundColor(context.getResources().getColor(R.color.White));
            }
        });
        row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row1.setBackgroundColor(context.getResources().getColor(R.color.White));
                row2.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
                show_message=context.getResources().getString(R.string.message_content_item_2);
                row3.setBackgroundColor(context.getResources().getColor(R.color.White));
                row4.setBackgroundColor(context.getResources().getColor(R.color.White));
            }
        });
        row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row1.setBackgroundColor(context.getResources().getColor(R.color.White));
                row2.setBackgroundColor(context.getResources().getColor(R.color.White));
                row3.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
                show_message=context.getResources().getString(R.string.message_content_item_3);
                row4.setBackgroundColor(context.getResources().getColor(R.color.White));
            }
        });
        row4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row1.setBackgroundColor(context.getResources().getColor(R.color.White));
                row2.setBackgroundColor(context.getResources().getColor(R.color.White));
                row3.setBackgroundColor(context.getResources().getColor(R.color.White));
                row4.setBackgroundColor(context.getResources().getColor(R.color.PowderBlue));
                show_message=context.getResources().getString(R.string.message_content_item_4);
            }
        });
        check1.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
            public void onCheckedChanged(View buttonView,
                                         boolean isChecked) {
                Music.stop(context);
                check2.setChecked(false);check3.setChecked(false);check4.setChecked(false);
                if (isChecked) Music.play(context, R.raw.siren);
                else Music.stop(context);
            }
        });
        check2.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
            public void onCheckedChanged(View buttonView,
                                         boolean isChecked) {
                Music.stop(context);
                check1.setChecked(false);check3.setChecked(false);check4.setChecked(false);
                if(isChecked) Music.play(context,R.raw.fire_car);
                else Music.stop(context);
            }
        });
        check3.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
            public void onCheckedChanged(View buttonView,
                                         boolean isChecked) {
                Music.stop(context);
                check1.setChecked(false);check2.setChecked(false);check4.setChecked(false);
                if(isChecked) Music.play(context,R.raw.emergency1);
                else Music.stop(context);
            }
        });
        check4.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
            public void onCheckedChanged(View buttonView,
                                         boolean isChecked) {
                Music.stop(context);
                check1.setChecked(false);check2.setChecked(false);check3.setChecked(false);
                if(isChecked) Music.play(context,R.raw.emergency2);
                else Music.stop(context);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        is_message=false;
        //Save voice_state
        SharedPreferences prefs_voice_mess = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_voice_mess = prefs_voice_mess.edit();
        editor_voice_mess.putString("voice_state", show_message);
        editor_voice_mess.commit(); //important, otherwise it wouldn't save.
        //Save brightness_state
    }
    @Override
    protected void onResume() {
        super.onResume();
        is_message=true;
        //if((DeviceActivity.isAlarm==true)&&(message_setting_back!=null)) message_setting_back.callOnClick();
        //Get back voice state
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        show_message = sharedPreferences.getString("voice_state", getResources().getString(R.string.message_content_item_1));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Music.stop(context);
    }
}