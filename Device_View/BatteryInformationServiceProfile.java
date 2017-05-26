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
 * BatteryInformationServiceProfile.java: battery service of bulldi
 */

package openstack.bulldi.safe3x.Device_View;

import java.nio.ByteBuffer;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.GenericBluetoothProfile;
import openstack.bulldi.safe3x.R;

public class BatteryInformationServiceProfile extends GenericBluetoothProfile {
    public static int battery_data=99;
    protected final Context context;
    public BatteryInformationServiceProfile(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con,device,service,controller);
        context=con;
        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();

        for (BluetoothGattCharacteristic c : characteristics) {
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_BATTERY_DATA.toString())) {
                this.dataC = c;
                Log.i("Battery value","value of dataC: "+this.dataC+ " value of batery: "+this.dataC.getValue());
            }

        }
    }
    @Override
    public void onResume(){
        super.onResume();
       // if(battery_data!=0) this.tRow.value.setText(battery_data);
    }
    @Override
    public void didReadValueForCharacteristic(BluetoothGattCharacteristic c) {

    }
    @Override
    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic c) {
        byte[] value = c.getValue();
        String str=byteArrayToHex(value);
        int decimal=hex2decimal(str);
//        double battery_vol=((decimal*3.273)/100);
        //int battery_data_ex=(int) (((battery_vol-2)/(3.273-2))*100);
        double battery_vol=((decimal*3.125)/100);
        int battery_data_ex=(int) (((battery_vol-2)/(3.125-2))*100);
        if(battery_data_ex>100) battery_data_ex=100;
        if(battery_data_ex<0) battery_data_ex=0;
        if((battery_data_ex<=20)&&(battery_data_ex>10)) Toast.makeText(context, context.getString(R.string.battery_status_low), Toast.LENGTH_SHORT).show();
        if(battery_data_ex<=10) Toast.makeText(context,context.getString(R.string.battery_status_replace), Toast.LENGTH_SHORT).show();
        battery_data=battery_data_ex;

    }
    public static boolean isCorrectService(BluetoothGattService service) {
        if ((service.getUuid().toString().compareTo(SensorTagGatt.UUID_BATTERY_SERV.toString())) == 0) {
            return true;
        }
        else return false;
    }
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
    @Override
    public void enableService () {
        this.isEnabled = true;
    }
    public static Integer toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
}
