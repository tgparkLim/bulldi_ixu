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
 * Sensor.java: convert and calculate data format from bulldi
 */

package openstack.bulldi.safe3x.Device_View;

//import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

import java.nio.ByteBuffer;
import java.util.UUID;

import openstack.util.Point3D;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;


/**
 * This enum encapsulates the differences amongst the sensors. The differences include UUID values and how to interpret the
 * characteristic-containing-measurement.
 */
public enum Sensor {
	IR_TEMPERATURE(SensorTagGatt.UUID_IRT_SERV, SensorTagGatt.UUID_IRT_DATA, SensorTagGatt.UUID_IRT_CONF) {
		@Override
		public Point3D convert(final byte [] value) {

			if(value!=null) {
				String temp_string=byteArrayToHex(value);
				Log.i("Check temperature value", "value: " + temp_string);
				int decimal=hex2decimal(temp_string);
				double temp_double=(double) decimal;
				//double final_temp=(temp_double-1558)/(-8.54);
				double final_temp=(temp_double-1557)/(-7.77);
				double ambient=final_temp;
				double target=final_temp;
				double targetNewSensor=final_temp;
				return new Point3D(ambient, temp_double, targetNewSensor);
			}
			else return (new Point3D(0,0,0));
		}
	},


	SMOKE(SensorTagGatt.UUID_SMO_SERV, SensorTagGatt.UUID_SMO_DATA, SensorTagGatt.UUID_SMO_CONF) {
		@Override
		public Point3D convert(final byte [] value) {
			if(value!=null)
			{
				String smoke_string=byteArrayToHex(value);
				String newText = smoke_string.substring(0, 4) ;
				int decimal=hex2decimal(newText);
				double smoke_double=(double) decimal;
				double final_smoke=smoke_double;
				return new Point3D(final_smoke, smoke_double, 0);
			}
			else return new Point3D(0,0,0);
		}
	},
	CO(SensorTagGatt.UUID_CO_SERV, SensorTagGatt.UUID_CO_DATA, SensorTagGatt.UUID_CO_CONF) {
		@Override
		public Point3D convert(final byte [] value) {
			if(value!=null)
			{
				String co_string=byteArrayToHex(value);
				String newText = co_string.substring(0, 4) ;
				int decimal=hex2decimal(newText);
				double co_double=(double) decimal;
				//double final_co=co_double;
				//double final_co=(co_double-7.555)/0.3264;
				double final_co=(co_double-2)/0.48;
				//Limitation of CO value
				if(final_co<=9) final_co=0;
				if(final_co>1000) final_co=1000;
				return new Point3D(final_co, co_double, 0);
			}
			else return new Point3D(0,0,0);
		}
	};

	/**
	 * Gyroscope, Magnetometer, Barometer, IR temperature all store 16 bit two's complement values as LSB MSB, which cannot be directly parsed
	 * as getIntValue(FORMAT_SINT16, offset) because the bytes are stored as little-endian.
	 * 
	 * This function extracts these 16 bit two's complement values.
	 * */
	private static Integer shortSignedAtOffset(byte[] c, int offset) {
		Integer lowerByte = (int) c[offset] & 0xFF; 
		Integer upperByte = (int) c[offset+1]; // // Interpret MSB as signed
		return (upperByte << 8) + lowerByte;
	}

	private static Integer shortUnsignedAtOffset(byte[] c, int offset) {
		Integer lowerByte = (int) c[offset] & 0xFF;
		Integer upperByte = (int) c[offset+1] & 0xFF;
		return (upperByte << 8) + lowerByte;
	}
    private static Integer twentyFourBitUnsignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = (int) c[offset] & 0xFF;
        Integer mediumByte = (int) c[offset+1] & 0xFF;
        Integer upperByte = (int) c[offset + 2] & 0xFF;
        return (upperByte << 16) + (mediumByte << 8) + lowerByte;
    }

	public void onCharacteristicChanged(BluetoothGattCharacteristic c) {
		throw new UnsupportedOperationException("Error: the individual enum classes are supposed to override this method.");
	}


	public Point3D convert(byte[] value) {
		throw new UnsupportedOperationException("Error: the individual enum classes are supposed to override this method.");
	}

	private final UUID service, data, config;
	private byte enableCode; // See getEnableSensorCode for explanation.
	public static final byte DISABLE_SENSOR_CODE = 0;
	public static final byte ENABLE_SENSOR_CODE = 1;
	public static final byte CALIBRATE_SENSOR_CODE = 2;

	/**
	 * Constructor called by the Gyroscope and Accelerometer because it more than a boolean enable
	 * code.
	 */
	private Sensor(UUID service, UUID data, UUID config, byte enableCode) {
		this.service = service;
		this.data = data;
		this.config = config;
		this.enableCode = enableCode;
	}

	/**
	 * Constructor called by all the sensors except Gyroscope
	 * */
	private Sensor(UUID service, UUID data, UUID config) {
		this.service = service;
		this.data = data;
		this.config = config;
		this.enableCode = ENABLE_SENSOR_CODE; // This is the sensor enable code for all sensors except the gyroscope
	}

	/**
	 * @return the code which, when written to the configuration characteristic, turns on the sensor.
	 * */
	public byte getEnableSensorCode() {
		return enableCode;
	}

	public UUID getService() {
		return service;
	}

	public UUID getData() {
		return data;
	}

	public UUID getConfig() {
		return config;
	}

	public static Sensor getFromDataUuid(UUID uuid) {
		for (Sensor s : Sensor.values()) {
			if (s.getData().equals(uuid)) {
				return s;
			}
		}
		throw new RuntimeException("unable to find UUID.");
	}

	public static final Sensor[] SENSOR_LIST = {IR_TEMPERATURE,SMOKE,CO};

	public static double toDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getDouble();
	}
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte b: a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
	public double convertHexStrToDouble(String input) {
		// convert the input to positive, as needed
		String s2 = preprocess(input);
		boolean negative = true;
		// if the original equals the new string, then it is not negative
		if (input.equalsIgnoreCase(s2))
			negative = false;

		// convert the hex string to long
		long doubleAsLongReverse = Long.parseLong(s2, 16);

		// Convert the long back into the original double
		double doubleOutput = Double.longBitsToDouble(doubleAsLongReverse);

		// return as a negative value, as needed
		if (negative)
			return -doubleOutput;

		return doubleOutput;
	}
	private String preprocess(String doubleAsHexString) {
		// get the first char and convert it to an int
		String s0 = doubleAsHexString.substring(0, 1);
		int int1 = Integer.parseInt(s0, 16);

		// if the int is < 8, then the string is not negative
		// and is returned without further processing
		if (int1 < 8)
			return doubleAsHexString;

		// otherwise subtract 8
		int1 = int1 - 8;
		s0 = Integer.toString(int1);

		// don't prepend a "0"
		if (int1 == 0)
			s0 = "";

		// return the string with a new inital char
		return s0 + doubleAsHexString.substring(1);
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
