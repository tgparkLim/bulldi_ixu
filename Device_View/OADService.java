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
 * OADService.java: over the air download service of bulldi
 */

package openstack.bulldi.safe3x.Device_View;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.GenericBluetoothProfile;
import openstack.bulldi.safe3x.Preference_etc.Version;

import java.io.FileWriter;
import java.util.List;
import java.util.zip.Inflater;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class OADService extends GenericBluetoothProfile {
    private static final String oadService_UUID = "f000ffc0-0451-4000-b000-000000000000";
    private static final String oadImageNotify_UUID = "f000ffc1-0451-4000-b000-000000000000";
    private static final String oadBlockRequest_UUID = "f000ffc2-0451-4000-b000-000000000000";

    public static final String ACTION_PREPARE_FOR_OAD = "openstack.bulldi.safe3x.Device_View.ACTION_PREPARE_FOR_OAD";
    public static final String ACTION_RESTORE_AFTER_OAD = "openstack.bulldi.safe3x.Device_View.ACTION_RESTORE_AFTER_OAD";


    private String fwRev;
    private BroadcastReceiver brRecv;
    private boolean clickReceiverRegistered = false;

    public OADService(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con,device,service,controller);

        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();

        for (BluetoothGattCharacteristic c : characteristics) {
            if (c.getUuid().toString().equals(oadImageNotify_UUID)) {
                this.dataC = c;
            }
            if (c.getUuid().toString().equals(oadBlockRequest_UUID)) {
                this.configC = c;
            }
        }

        brRecv = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Version.ACTION_VIEW_CLICKED.equals(intent.getAction())) {
                    prepareForOAD();
                }
            }
        };
        this.context.registerReceiver(brRecv,makeIntentFilter());
        this.clickReceiverRegistered = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.clickReceiverRegistered) {
            brRecv = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Version.ACTION_VIEW_CLICKED.equals(intent.getAction())) {
                        prepareForOAD();
                    }
                }
            };

            this.context.registerReceiver(brRecv, makeIntentFilter());
            this.clickReceiverRegistered = true;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (this.clickReceiverRegistered) {
            this.context.unregisterReceiver(brRecv);
            this.clickReceiverRegistered = false;
        }
    }
    public static boolean isCorrectService(BluetoothGattService service) {
        if ((service.getUuid().toString().compareTo(oadService_UUID)) == 0) {
            return true;
        }
        else return false;
    }

    public void prepareForOAD () {
        //Override click and launch the OAD screen
        Intent intent = new Intent(ACTION_PREPARE_FOR_OAD);
        context.sendBroadcast(intent);

    }

    @Override
    public void enableService() {

    }
    @Override
    public void disableService() {

    }
    @Override
    public void configureService() {

    }
    @Override
    public void deConfigureService() {

    }
    @Override
    public void periodWasUpdated(int period) {

    }
    @Override
    public void didUpdateFirmwareRevision(String firmwareRev) {

    }

    private static IntentFilter makeIntentFilter() {
        final IntentFilter fi = new IntentFilter();
        fi.addAction(Version.ACTION_VIEW_CLICKED);
        return fi;
    }

}
