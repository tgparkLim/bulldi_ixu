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
 * Test_preference.java
 */

package openstack.bulldi.safe3x;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Device_View.InOut_Service;
import openstack.util.TextViewPlus;



public class Test_preference extends Activity {
    public static boolean is_test=false;
    public static View operation_test_back;
    public TextViewPlus operation_test_title;
    Button fireTest_button;
    Button alarmTest_button;
    Button lampTest_button;
    boolean is_fireTest=false;
    boolean is_alarmTest=false;
    boolean is_lampTest=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.operation_test);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.operation_test_titlebar);
         operation_test_back= findViewById(R.id.operation_test_back);
        operation_test_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        operation_test_title=(TextViewPlus) findViewById(R.id.operation_test_title);
        operation_test_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fireTest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_fireTest==false) is_fireTest=true;
                else is_fireTest=false;
                if(is_fireTest==true){
                    fireTest_button.setBackgroundResource(R.drawable.test_on);
                    fireTest_button.setText(getResources().getString(R.string.button_stop));
                    fireTest_button.setTextColor(getResources().getColor(R.color.White));
                    //Send comand to bulldi start testing
                    byte value_data = (byte) (0x08);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fireTest_button.setBackgroundResource(R.drawable.test_off);
                            fireTest_button.setText(getResources().getString(R.string.button_5sec));
                            fireTest_button.setTextColor(getResources().getColor(R.color.Black));
                            //Send comand to bulldi to stop testing
                            byte value_data = (byte) (0x00);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                            is_fireTest=false;
                        }
                    }, 4900);

                }else{
                    fireTest_button.setBackgroundResource(R.drawable.test_off);
                    fireTest_button.setText(getResources().getString(R.string.button_5sec));
                    fireTest_button.setTextColor(getResources().getColor(R.color.Black));
                    //Send comand to bulldi stop testing
                    byte value_data = (byte) (0x00);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                }
            }
        });
        alarmTest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_alarmTest==false) is_alarmTest=true;
                else is_alarmTest=false;
                if(is_alarmTest==true){
                    alarmTest_button.setBackgroundResource(R.drawable.test_on);
                    alarmTest_button.setText(getResources().getString(R.string.button_stop));
                    alarmTest_button.setTextColor(getResources().getColor(R.color.White));
                    //Send comand to bulldi start testing
                    byte value_data = (byte) (0x10);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alarmTest_button.setBackgroundResource(R.drawable.test_off);
                            alarmTest_button.setText(getResources().getString(R.string.button_5sec));
                            alarmTest_button.setTextColor(getResources().getColor(R.color.Black));
                            //Send comand to bulldi to stop testing
                            byte value_data = (byte) (0x00);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                            is_alarmTest=false;
                        }
                    }, 4900);
                }else{
                    alarmTest_button.setBackgroundResource(R.drawable.test_off);
                    alarmTest_button.setText(getResources().getString(R.string.button_5sec));
                    alarmTest_button.setTextColor(getResources().getColor(R.color.Black));
                    //Send comand to bulldi to stop testing
                    byte value_data = (byte) (0x00);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                }
            }
        });
        lampTest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_lampTest==false) is_lampTest=true;
                else is_lampTest=false;
                if(is_lampTest==true){
                    lampTest_button.setBackgroundResource(R.drawable.test_on);
                    lampTest_button.setText(getResources().getString(R.string.button_stop));
                    lampTest_button.setTextColor(getResources().getColor(R.color.White));
                    //Send comand to bulldi start testing
                    byte value_data = (byte) (0x40);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lampTest_button.setBackgroundResource(R.drawable.test_off);
                            lampTest_button.setText(getResources().getString(R.string.button_5sec));
                            lampTest_button.setTextColor(getResources().getColor(R.color.Black));
                            //Send comand to bulldi to stop testing
                            byte value_data = (byte) (0x00);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                            is_lampTest=false;
                        }
                    }, 4900);
                }else{
                    lampTest_button.setBackgroundResource(R.drawable.test_off);
                    lampTest_button.setText(getResources().getString(R.string.button_5sec));
                    lampTest_button.setTextColor(getResources().getColor(R.color.Black));
                    //Send comand to bulldi to stop testing
                    byte value_data = (byte) (0x00);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        is_test=true;
        //if((DeviceActivity.isAlarm==true)&&(operation_test_back!=null)) operation_test_back.callOnClick();
    }
    @Override
    public void onPause() {
        super.onPause();
        is_test=false;
    }
}
