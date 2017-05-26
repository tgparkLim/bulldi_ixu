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
 * Alias.java: assign bulldi alias
 */
package openstack.bulldi.safe3x.Preference_etc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.bluetooth.BluetoothDevice;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.EditTextPlus;
import openstack.util.TextViewPlus;

public class Alias extends Activity {
    public static ImageView alias_setting_back;
    public static boolean is_alias=false;
    TextViewPlus alias_title;
    EditTextPlus alias_text;
    Button set_alias;
    public static String alias_name;

//    private  BluetoothDevice mBluetoothDevice = null;
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_alias);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.alias_title);
        alias_setting_back=(ImageView) findViewById(R.id.alias_back);
        alias_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alias_title=(TextViewPlus) findViewById(R.id.alias_title);
        alias_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        alias_text=(EditTextPlus) findViewById(R.id.text_alias);

        String deviceAlias=null;
        try {
            Method method = DeviceActivity.mBluetoothDevice.getClass().getMethod("getAliasName");
            Log.i("Alias","method get: "+method);
            if(method != null) {
                deviceAlias = (String)method.invoke(DeviceActivity.mBluetoothDevice);
                Log.i("Alias","device alias: "+deviceAlias);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if((alias_text!=null)&&(deviceAlias.compareTo("bulldi")!=0))alias_text.setText(deviceAlias);
        set_alias=(Button) findViewById(R.id.but_alias);
        set_alias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_alias.setBackgroundResource(R.drawable.button_on);
                set_alias.setTextColor(Color.BLACK);
                alias_name=alias_text.getText().toString();
                Log.i("Alias", "new alias: " + alias_name);
                try {
                    Method method = DeviceActivity.mBluetoothDevice.getClass().getMethod("setAlias", String.class);
                    Log.i("Alias","method set: "+method);
                    if(method != null) {
                        method.invoke(DeviceActivity.mBluetoothDevice, alias_name);
                        Log.i("Alias", "inside method: " + method.invoke(DeviceActivity.mBluetoothDevice, alias_name));
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                alias_setting_back.callOnClick();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        is_alias=true;
    }
    @Override
    public void onPause(){
        super.onPause();
        is_alias=false;
    }
}
