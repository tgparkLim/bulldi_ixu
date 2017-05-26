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
 * InOut_Service.java; in/out service of bulldi
 */

package openstack.bulldi.safe3x.Device_View;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.GenericBluetoothProfile;

import java.util.List;


public class InOut_Service extends GenericBluetoothProfile {
    public  static BluetoothGattCharacteristic charac_data;
    public  static BluetoothGattCharacteristic charc_config;

    public InOut_Service(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con,device,service,controller);
        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();
        for (BluetoothGattCharacteristic c : characteristics) {
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_IO_DATA.toString())) {
                charac_data=c;
            }
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_IO_CONFIG.toString())) {
                charc_config=c;
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

}
