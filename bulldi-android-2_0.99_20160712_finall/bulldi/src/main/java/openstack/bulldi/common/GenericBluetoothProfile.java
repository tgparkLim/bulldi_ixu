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
 * GenericBluetoothProfile.java
 */

package openstack.bulldi.common;

import openstack.bulldi.safe3x.Device_View.SensorTagIRTemperatureProfile;
import openstack.bulldi.safe3x.Device_View.SensorTagSmokeProfile;
import openstack.util.GenericCharacteristicTableRow;
import openstack.util.Point3D;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TableRow;

import java.util.Map;

public class GenericBluetoothProfile {
	protected BluetoothDevice mBTDevice;
	protected BluetoothGattService mBTService;
	protected GenericCharacteristicTableRow tRow;
	protected BluetoothLeService mBTLeService;
	protected BluetoothGattCharacteristic dataC;
	protected BluetoothGattCharacteristic configC;
	protected BluetoothGattCharacteristic periodC;

	protected BluetoothGattCharacteristic dataC1;
	protected BluetoothGattCharacteristic configC1;
	protected BluetoothGattCharacteristic periodC1;

	protected BluetoothGattCharacteristic dataC2;
	protected BluetoothGattCharacteristic configC2;
	protected BluetoothGattCharacteristic periodC2;

	protected static final int GATT_TIMEOUT = 250; // milliseconds
	protected Context context;
	protected boolean isRegistered;
    public boolean isConfigured;
    public boolean isEnabled;
	public GenericBluetoothProfile(final Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
		super();
		this.mBTDevice = device;
		this.mBTService = service;
		this.mBTLeService = controller;
		this.tRow =  new GenericCharacteristicTableRow(con);
		this.dataC = null;
		this.periodC = null;
		this.configC = null;

		this.dataC1 = null;
		this.periodC1 = null;
		this.configC1 = null;

		this.dataC2 = null;
		this.periodC2 = null;
		this.configC2 = null;

		this.context = con;
		this.isRegistered = false;
	}
	public GenericBluetoothProfile(){

	}

	public void onResume() {
		if (this.isRegistered == false) {
			this.context.registerReceiver(guiReceiver, GenericBluetoothProfile.makeFilter());
			this.isRegistered = true;
		}
	}
	public void onPause() {
		if (this.isRegistered == true) {
			this.context.unregisterReceiver(guiReceiver);
			this.isRegistered = false;
		}
	}

	public static boolean isCorrectService(BluetoothGattService service) {
		//Always return false in parent class
		return false;
	}
    public boolean isDataC(BluetoothGattCharacteristic c) {
        if (this.dataC == null) return false;
        if (c.equals(this.dataC)) return true;
        else return false;
    }
	public void configureService() {
        int error = this.mBTLeService.setCharacteristicNotification(this.dataC, true);
        if (error != 0) {
            if (this.dataC != null)
                printError("Sensor notification enable failed: ",this.dataC,error);
        }
		this.isConfigured = true;
	}
	public void deConfigureService() {
        int error = this.mBTLeService.setCharacteristicNotification(this.dataC, false);
        if (error != 0) {
            if (this.dataC != null)
                printError("Sensor notification disable failed: ",this.dataC,error);
        }
        this.isConfigured = false;
	}
	public void enableService () {
        int error = mBTLeService.writeCharacteristic(this.configC, (byte) 0x01);
		Log.i("enableService", "value of error: " + error);
        if (error != 0) {
            if (this.configC != null)
                printError("Sensor enable failed: ",this.configC,error);
        }
        //this.periodWasUpdated(1000);
        this.isEnabled = true;
	}
	public void disableService () {
        int error = mBTLeService.writeCharacteristic(this.configC, (byte)0x00);
        if (error != 0) {
            if (this.configC != null)
                printError("Sensor disable failed: ",this.configC,error);
        }
        this.isConfigured = false;
	}
	public void didWriteValueForCharacteristic(BluetoothGattCharacteristic c) {
		
	}
	public void didReadValueForCharacteristic(BluetoothGattCharacteristic c) {
		if (this.periodC != null) {
			if (c.equals(this.periodC)) {
				byte[] value = c.getValue();
				this.periodWasUpdated(value[0] * 10);
			}
		}
	}
	public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic c) {
        /*
		if (c.equals(this.dataC)) {
			byte[] value = c.getValue();
			this.didUpdateValueForCharacteristic(this.dataC.getUuid().toString(), value);
		}
		*/
	}
	public boolean alarm(BluetoothGattCharacteristic c) {
        /*
		if (c.equals(this.dataC)) {
			byte[] value = c.getValue();
			this.didUpdateValueForCharacteristic(this.dataC.getUuid().toString(), value);
		}
		*/
		return false;
	}
	public TableRow getTableRow() {
		return this.tRow;
	}
	public String getIconPrefix() {
		String iconPrefix;
		if (this.mBTDevice.getName().equals("bulldi")) {
			iconPrefix = "sensortag2";
		}
		else iconPrefix = "";
		return iconPrefix;
	}
	public boolean isEnabledByPrefs(String prefName) {
		String preferenceKeyString = "pref_"
		    + prefName;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mBTLeService);

		Boolean defaultValue = true;
		return prefs.getBoolean(preferenceKeyString, defaultValue);
	}
	public void periodWasUpdated(int period) {
		if (period > 2450) period = 2450; 
		if (period < 100) period = 100;
		byte p = (byte)((period / 10));
		Log.d("GenericBluetoothProfile","Period characteristic set to :" + period);
        /*
		if (this.mBTLeService.writeCharacteristic(this.periodC, p)) {
			mBTLeService.waitIdle(GATT_TIMEOUT);
		} else {
			Log.d("GenericBluetoothProfile","Sensor period failed: " + this.periodC.getUuid().toString());
		}
		*/
        int error = mBTLeService.writeCharacteristic(this.periodC, p);
        if (error != 0) {
            if (this.periodC != null)
                printError("Sensor period failed: ",this.periodC,error);
        }
		this.tRow.periodLegend.setText("Sensor period (currently : " + period + "ms)");
	}
    public Map<String,String> getMQTTMap() {
        return null;
    }
	public void onOffWasUpdated(boolean on) {
		Log.d("GenericBluetoothProfile","Config characteristic set to :" + on);
		if (on) {
			this.configureService();
			this.enableService();
			this.tRow.grayedOut(false);
		}
		else {
			this.deConfigureService();
			this.disableService();
			this.tRow.grayedOut(true);
		}
		
	}
	public void grayOutCell(boolean grayedOut) {
		
		if (grayedOut == true){ 
			this.tRow.setAlpha(0.4f);
			this.tRow.onOff.setChecked(false);
		}
		else {
			this.tRow.setAlpha(1.0f);
			this.tRow.onOff.setChecked(true);
		}
	}
	private static IntentFilter makeFilter() {
		final IntentFilter fi = new IntentFilter();
		fi.addAction(GenericCharacteristicTableRow.ACTION_PERIOD_UPDATE);
		fi.addAction(SensorTagIRTemperatureProfile.ACTION_PERIOD_UPDATE);
		fi.addAction(SensorTagSmokeProfile.ACTION_PERIOD_UPDATE);
		fi.addAction(GenericCharacteristicTableRow.ACTION_ONOFF_UPDATE);
		fi.addAction(GenericCharacteristicTableRow.ACTION_CALIBRATE);
		return fi;
	}
	private final BroadcastReceiver guiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			final String uuid = intent.getStringExtra(GenericCharacteristicTableRow.EXTRA_SERVICE_UUID);
			Log.d("Test", uuid + "!=" + tRow.uuidLabel.getText().toString());
			if ((tRow.uuidLabel.getText().toString().compareTo(uuid)) == 0) { 
				if ((action.compareTo(SensorTagIRTemperatureProfile.ACTION_PERIOD_UPDATE) == 0)) {

					final int period = intent.getIntExtra(SensorTagIRTemperatureProfile.EXTRA_PERIOD, 100);
//					Log.i("Check action","value: "+ period);
					periodWasUpdated(period);
				}
				else if ((action.compareTo(SensorTagSmokeProfile.ACTION_PERIOD_UPDATE) == 0)) {
					final int period = intent.getIntExtra(SensorTagSmokeProfile.EXTRA_PERIOD, 100);
//					Log.i("Check action","value: "+ period);
					periodWasUpdated(period);
				}
				else if ((action.compareTo(GenericCharacteristicTableRow.ACTION_ONOFF_UPDATE) == 0)) {
					final boolean on = intent.getBooleanExtra(GenericCharacteristicTableRow.EXTRA_ONOFF, false);
					onOffWasUpdated(on);
				}
				else if ((action.compareTo(GenericCharacteristicTableRow.ACTION_CALIBRATE) == 0)) {
					calibrationButtonTouched();
				}
			}
		}
	};
	protected void calibrationButtonTouched() {
		
	}
	public void didUpdateFirmwareRevision(String fwRev) {
		
	}
    public void printError (String msg, BluetoothGattCharacteristic c, int error) {
        try {
            Log.d("GenericBluetoothProfile", msg + c.getUuid().toString() + " Error: " + error);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Point3D get_data()
	{
		return new Point3D(0,0,0);
	}



}
