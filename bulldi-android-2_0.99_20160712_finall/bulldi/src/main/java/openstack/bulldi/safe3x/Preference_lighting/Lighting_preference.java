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
 * Lighting_preference.java; lamp control
 */

package openstack.bulldi.safe3x.Preference_lighting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.safe3x.Device_View.BatteryInformationServiceProfile;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.Preference_history.History_bean;
import openstack.bulldi.safe3x.Preference_history.History_preference;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Device_View.InOut_Service;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class Lighting_preference extends Activity{
    ListView lv;
    public static int [] prgmImages={R.drawable.switch_on};
    public static String [] prgmNameList={"Light"};
    public SharedPreferences sharedPreferences;
    public static boolean light_on=false;
    //public static boolean[] brightness_on={true,false,false,false,false};
    public static boolean[] sleep_on={true,false,false,false,false};
    public static DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
    public static DateFormat df2 = new SimpleDateFormat("HH:mm");
    public static DateFormat df1_ko = new SimpleDateFormat("yyyy.MM.dd");
    public static DateFormat df2_ko = new SimpleDateFormat("HH:mm");
    public static boolean is_lighting=false;
    public static View lighting_back;
    public TextViewPlus lighting_title;
    public static BluetoothLeService mBtLeService = null;
    public static Handler handler_1=null;public static Runnable runnable_1=null;
    public static Handler handler_2=null;public static Runnable runnable_2=null;
    public static Handler handler_3=null;public static Runnable runnable_3=null;
    public static Handler handler_4=null;public static Runnable runnable_4=null;
    public static Resources res=null;
    private boolean is_none=true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lighting);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.lighting_title_bar);
        mBtLeService = BluetoothLeService.getInstance();
        lighting_back= findViewById(R.id.lighting_back);
        lighting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lighting_title=(TextViewPlus) findViewById(R.id.lighting_title);
        lighting_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv=(ListView) findViewById(R.id.lighting_list);
        lv.setAdapter(new Listview_light(this, prgmNameList, prgmImages));
        res=getResources();
        if(BatteryInformationServiceProfile.battery_data<=30){
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.customer_light_dialog);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            window.setAttributes(wlp);
            final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_custom_light_close);
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
            dialog.show();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
//        Log.i("Check light", "onPause");
        is_lighting=false;

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Check light", "onResume");
        is_lighting=true;

    }
}
