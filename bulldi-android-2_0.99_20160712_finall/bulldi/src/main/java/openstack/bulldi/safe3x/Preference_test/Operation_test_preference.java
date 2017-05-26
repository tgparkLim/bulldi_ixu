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
 * Operation_test_preference.java: Operation test control
 */

package openstack.bulldi.safe3x.Preference_test;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.BLE_Connection.Connection;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Device_View.InOut_Service;
import openstack.bulldi.safe3x.Preference_sharing.CustomAdapter;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;

public class Operation_test_preference extends Activity {
    ListView lv;
    Context context;
    public static int [] prgmImages={R.drawable.set_warning_history,R.drawable.play_emergency_off,R.drawable.ot_demo_lamp};
    public static String [] prgmNameList={"Fire demo test","Alarm test","Lamp test"};
    public static boolean is_test=false;
    public static View operation_test_back;
    public TextViewPlus operation_test_title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context=this;
        prgmNameList=new String[]{getResources().getString(R.string.operation_test_fire),getResources().getString(R.string.operation_test_alarm),getResources().getString(R.string.operation_test_lamp)};
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


        lv=(ListView) findViewById(R.id.operationTest_list);
        lv.setAdapter(new Operation_test_adapter(this, prgmNameList, prgmImages));
    }
    private void showToast(String msg) {
        Toast toast= Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        centerText(toast.getView());
        toast.show();
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
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Check test","onPause");
        is_test=false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Check test", "onResume");
        is_test=true;
        if(Operation_test_adapter.is_fireTest ==true){
            Operation_test_adapter.is_fireTest = false;
            if (Operation_test_adapter.row0 != null) Operation_test_adapter.row0.setBackgroundResource(R.color.White);
            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, (byte) (0x00));
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("Check test", "onBackPressed");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Operation_test_adapter.previous_data = (byte) (0x00);
        byte value_data = (byte) (0x00);
        if(Connection.action!=null && Connection.action.compareTo(BluetoothLeService.ACTION_GATT_DISCONNECTED)!=0)
        DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
        Log.i("Check test", "onDestroy: "+DeviceActivity.mBtLeService);
    }
}