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
 * Connection_control.java: connection control service of bulldi
 */

package openstack.bulldi.safe3x.Device_View;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.GenericBluetoothProfile;

import java.util.List;


public class Connection_control extends GenericBluetoothProfile {
    public  static BluetoothGattCharacteristic current_configC;
    public  static BluetoothGattCharacteristic new_configC;
    private static final int CONN_INTERVAL = 0x0006;
    private static final int SUPERVISION_TIMEOUT = 0x000A;
    public Connection_control(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con,device,service,controller);
        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();
        for (BluetoothGattCharacteristic c : characteristics) {
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_CONNECT_CURRENT.toString())) {
                current_configC=c;
                int x= this.mBTLeService.readCharacteristic(current_configC);
                Log.i("Connection control","read: "+x+"value: "+getHexString(c.getValue()));
            }
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_CONNECT_NEW.toString())) {
                new_configC = c;
                byte[] value = { (byte) (CONN_INTERVAL & 0x00FF), // gets LSB of 2 byte value
                        (byte) ((CONN_INTERVAL & 0xFF00) >> 8), // gets MSB of 2 byte value
                        (byte) (CONN_INTERVAL & 0x00FF),
                        (byte) ((CONN_INTERVAL & 0xFF00) >> 8),
                        0, 0,
                        (byte) (SUPERVISION_TIMEOUT & 0x00FF),
                        (byte) ((SUPERVISION_TIMEOUT & 0xFF00) >> 8)
                };
                int error= this.mBTLeService.writeCharacteristic(new_configC,value);
                Log.i("Connection control","write: "+error+" value: "+getHexString(value));
            }
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_CONNECT_DISCONNECT.toString())) {

            }
        }
    }
    @Override
    public void enableService () {
        this.isEnabled = true;
    }

    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic c) {

    }
    public void didReadValueForCharacteristic(BluetoothGattCharacteristic c) {

    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
    public static String getHexString(byte[] b)  {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
}
