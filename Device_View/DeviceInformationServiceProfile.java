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
 * DeviceInformationServiceProfile.java: device information service of bulldi
 */

package openstack.bulldi.safe3x.Device_View;


import java.io.UnsupportedEncodingException;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.GenericBluetoothProfile;
import openstack.bulldi.safe3x.Preference_etc.Version;


public class DeviceInformationServiceProfile extends GenericBluetoothProfile {
    private static final String dISService_UUID = "0000180a-0000-1000-8000-00805f9b34fb";
    private static final String dISFirmwareREV_UUID = "00002a26-0000-1000-8000-00805f9b34fb";

    BluetoothGattCharacteristic firmwareREVc;

    public DeviceInformationServiceProfile(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con,device,service,controller);

        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();

        for (BluetoothGattCharacteristic c : characteristics) {

            if (c.getUuid().toString().equals(dISFirmwareREV_UUID)) {
                this.firmwareREVc = c;
                Log.i("Check infor","value: "+this.firmwareREVc.getUuid().toString());
            }

        }

    }
    public static boolean isCorrectService(BluetoothGattService service) {
        if ((service.getUuid().toString().compareTo(dISService_UUID)) == 0) {
            return true;
        }
        else return false;
    }
    @Override
    public void configureService() {
        // Nothing to do here

    }
    @Override
    public void deConfigureService() {
        // Nothing to do here
    }
    @Override
    public void enableService () {
        // Read all values

        this.mBTLeService.readCharacteristic(this.firmwareREVc);
        mBTLeService.waitIdle(GATT_TIMEOUT);

    }
    @Override
    public void disableService () {
        // Nothing to do here
    }
    @Override
    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic c) {

    }
    @Override
    public void didReadValueForCharacteristic(BluetoothGattCharacteristic c) {

        if (this.firmwareREVc != null) {
            if (c.equals(this.firmwareREVc)) {
                try {
                    String s = new String(c.getValue(),"UTF-8");
                    Version.f_version=s;
                    Log.i("Check infor","value: "+s);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic c) {

    }


}
