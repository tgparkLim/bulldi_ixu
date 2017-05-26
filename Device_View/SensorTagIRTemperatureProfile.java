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
 * SensorTagIRTemperatureProfile.java: temperature service of bulldi
 */

package openstack.bulldi.safe3x.Device_View;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableString;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.Data_store;
import openstack.bulldi.common.GenericBluetoothProfile;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.Preference_etc.Unit_setting;
import openstack.bulldi.safe3x.R;
import openstack.util.GenericCharacteristicTableRow;
import openstack.util.Point3D;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Intent.getIntent;
import static com.facebook.FacebookSdk.getApplicationContext;

public class SensorTagIRTemperatureProfile extends GenericBluetoothProfile {
	protected final Context context;
	public final static String ACTION_PERIOD_UPDATE = "openstack.util.ACTION_PERIOD_UPDATE";
	public final static String EXTRA_SERVICE_UUID = "openstack.util.EXTRA_SERVICE_UUID";
	public final static String EXTRA_PERIOD = "openstack.util.EXTRA_PERIOD";
	public static int temp_period=1000;
	public static boolean temp_alarm=false;

	public static List<Double> myList = new ArrayList<Double>();
	//co transfer
	//public  List<Double> myList_co = new ArrayList<Double>();
	//public static List<Double> myList_co_check = new ArrayList<Double>();

	public static double temp_max;
	public static double temp_min;
	public static double temp_average;

	public static boolean temp_alarm_fix=false;
	public static boolean temp_alarm_rate=false;
	public static double nomal_temp=25;

	public static BluetoothGattCharacteristic co1;
	public static BluetoothGattCharacteristic co2;
	public static BluetoothGattCharacteristic co3;
	//History
		/*public static double temp_emergency;
		public static String temp_t1;
		public static String temp_t2;*/
	//Draw graph
	static Calendar current_time;
	public static DateFormat df;
	public static DateFormat df_1;
	DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
	DateFormat df2 = new SimpleDateFormat("HH:mm");
	DateFormat df1_ko = new SimpleDateFormat("yyyy.MM.dd");
	DateFormat df2_ko = new SimpleDateFormat("HH:mm");
	public static String alarm_value;
	public static String time1;
	public static String time2;
	public static String cur_time_1;
	public static String cur_time_2;
	static public Data_store data_temp[]={new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),
			new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),
			new Data_store(),new Data_store(),new Data_store(),new Data_store()};
	public Data_store data_current=new Data_store();
	static public Data_store data_max=new Data_store();
	static public Data_store data_min=new Data_store();

	public SensorTagIRTemperatureProfile(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
		super(con,device,service,controller);
		context=con;
		this.tRow =  new GenericCharacteristicTableRow(con);
		List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();

		for (BluetoothGattCharacteristic c : characteristics) {
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_IRT_DATA.toString())) {
				this.dataC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_IRT_CONF.toString())) {
				this.configC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_IRT_PERI.toString())) {
				this.periodC = c;
			}
		}

		this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());
		Handler handler_reconnect = new Handler(Looper.getMainLooper());
		handler_reconnect.postDelayed(new Runnable() {
			@Override
			public void run() {
				temp_period=500;
				send_period(temp_period);
			}
		}, 1000);
	}


	@Override
	public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic c) {
		byte[] value = c.getValue();
		if (c.equals(this.dataC)) {
			Point3D v = Sensor.IR_TEMPERATURE.convert(value);
			if (myList.size() == 2147483647) myList.remove(0);
			myList.add(v.z);
			Log.i("Check list size", "Temperature list size: " + myList.size() + " with value: " + v.z);
			temp_max = Collections.max(myList);
			//temp_max =Double.parseDouble(new DecimalFormat("##.#").format(temp_max));
			temp_min = Collections.min(myList);
			//temp_min =Double.parseDouble(new DecimalFormat("##.#").format(temp_min));
			temp_average = calculateAverage(myList);
			//temp_average =Double.parseDouble(new DecimalFormat("##.#").format(temp_average));
			double temperature_show = v.z;

			if (Unit_setting.is_celsius == false) temperature_show = (v.z * 1.8) + 32;
			//String s = String.format("%.2f", v.z);
			final String tempNow = String.format("%.1f", temperature_show);

			Spannable span = new SpannableString(tempNow);
			span.setSpan(new RelativeSizeSpan(1.5f), 0, 3,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			//String span=String.format("%.0f", temperature_show);
			Device_Temp.temp_text.setText(span);
				/*int temperature_hex=(int) v.y;
				String span=Integer.toHexString(temperature_hex);
				Device_Temp.temp_text.setText("Ox"+span);*/
			if (Unit_setting.is_celsius == true) Device_Temp.temp_unit.setText("C");
			else Device_Temp.temp_unit.setText("F");
			if (v.z >= 57)
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_57up));
			else if ((v.z >= 42) && (v.z < 57))
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_4157));
			else if ((v.z >= 33) && (v.z < 42))
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_3340));
			else if ((v.z >= 27) && (v.z < 33))
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_2732));
			else if ((v.z >= 18) && (v.z < 27))
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_1826));
			else if ((v.z >= 11) && (v.z < 18))
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_1118));
			else if ((v.z >= -5) && (v.z < 11))
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_510));
			else if (v.z < -5)
				Device_Temp.temp_status.setText(context.getString(R.string.sensor_temperature_status_minus));
			Log.i("Check temperature", "size: " + myList.size());
			Log.i("Check temperature value", "value: " + v.z);
			//Graph
			current_time = Calendar.getInstance();
			df = new SimpleDateFormat("HH:mm a");
			cur_time_1 = df.format(Calendar.getInstance().getTime());
			df_1 = new SimpleDateFormat("d/MM");
			cur_time_2 = df_1.format(Calendar.getInstance().getTime());
			data_current.set_time(current_time.get(Calendar.YEAR), current_time.get(Calendar.MONTH), current_time.get(Calendar.DAY_OF_MONTH), current_time.get(Calendar.HOUR_OF_DAY), current_time.get(Calendar.MINUTE));
			data_current.set_data(v.z);
			if (data_current.data == temp_max) {
				data_max.set_time(current_time.get(Calendar.YEAR), current_time.get(Calendar.MONTH), current_time.get(Calendar.DAY_OF_MONTH), current_time.get(Calendar.HOUR_OF_DAY), current_time.get(Calendar.MINUTE));
				data_max.set_data(v.z);
			}

			if (data_current.data == temp_min) {
				data_min.set_time(current_time.get(Calendar.YEAR), current_time.get(Calendar.MONTH), current_time.get(Calendar.DAY_OF_MONTH), current_time.get(Calendar.HOUR_OF_DAY), current_time.get(Calendar.MINUTE));
				data_min.set_data(v.z);
			}
			if (current_time.get(Calendar.MINUTE) == 0) {
				data_temp[current_time.get(Calendar.HOUR_OF_DAY)].set_time(current_time.get(Calendar.YEAR), current_time.get(Calendar.MONTH), current_time.get(Calendar.DAY_OF_MONTH), current_time.get(Calendar.HOUR_OF_DAY), current_time.get(Calendar.MINUTE));
				data_temp[current_time.get(Calendar.HOUR_OF_DAY)].set_data(v.z);
			}
			//Alarm check

			if (v.z <= 57) {
				if (temp_alarm == true) {
					if (temp_alarm_rate == true) {
						if (v.z >= (nomal_temp + nomal_temp / 10)) {
							//Do nothing
						} else {
							if (temp_period != 1000) {
								temp_period = 1000;
								send_period(temp_period);
							}
							temp_alarm = false;
							temp_alarm_rate = false;
							nomal_temp = 25;
						}
					} else {
						if (v.z >= 42) {
							//Do nothing
						} else {
							if (temp_period != 1000) {
								temp_period = 1000;
								send_period(temp_period);
							}
							temp_alarm = false;
							temp_alarm_fix = false;
						}
					}
				} else {
					if (temp_period != 1000) {
						temp_period = 1000;
						send_period(temp_period);
						temp_alarm = false;
					} else {
						//Get 7 last variable in array
						if (myList.size() > 6) {
							List<Double> array = new ArrayList<Double>();
							for (int i = 1; i < 8; i++) {
								array.add(myList.get(myList.size() - i));
							}
							if (((array.get(0) - array.get(6)) >= 8)) {
								temp_alarm = true;
								temp_alarm_rate = true;
								nomal_temp = array.get(6);
								if (Unit_setting.is_celsius == true)
									alarm_value = String.format("%.2f", v.z) + "'C";
								else
									alarm_value = String.format("%.2f", (v.z * 1.8) + 32) + "'F";
								time1 = df1.format(Calendar.getInstance().getTime());
								time2 = df2.format(Calendar.getInstance().getTime());

							} else temp_alarm = false;
						} else temp_alarm = false;
					}
				}
			} else {
				if (temp_period == 1000) {
					//Get 7 last values
					if (myList.size() > 6) {
						List<Double> array = new ArrayList<Double>();
						for (int i = 1; i < 8; i++) {
							array.add(myList.get(myList.size() - i));
						}
						if (((array.get(0) - array.get(6)) >= 8)) {
							temp_alarm = true;
							temp_alarm_rate = true;
							nomal_temp = array.get(6);
							if (Unit_setting.is_celsius == true)
								alarm_value = String.format("%.2f", v.z) + "'C";
							else alarm_value = String.format("%.2f", (v.z * 1.8) + 32) + "'F";
							time1 = df1.format(Calendar.getInstance().getTime());
							time2 = df2.format(Calendar.getInstance().getTime());
							temp_period = 100;
							send_period(temp_period);
						} else {
							temp_alarm = false;
							temp_period = 500;
							send_period(temp_period);
						}
					} else {
						temp_period = 500;
						send_period(temp_period);
						temp_alarm = false;
					}
				} else if (temp_period == 500) {
					temp_period = 100;
					send_period(temp_period);
					temp_alarm = false;
				} else if (temp_period == 100) {
					temp_alarm = true;
					temp_alarm_fix = true;
					current_time = Calendar.getInstance();
					if (Unit_setting.is_celsius == true)
						alarm_value = String.format("%.2f", v.z) + "'C";
					else alarm_value = String.format("%.2f", (v.z * 1.8) + 32) + "'F";
					time1 = df1.format(Calendar.getInstance().getTime());
					time2 = df2.format(Calendar.getInstance().getTime());

				}
			}
		}

	}

	public static boolean isCorrectService(BluetoothGattService service) {
		if ((service.getUuid().toString().compareTo(SensorTagGatt.UUID_IRT_SERV.toString())) == 0) {
			return true;
		}
		else return false;
	}
	private double calculateAverage(List <Double> marks) {
		Double sum = 0.0;
		if(!marks.isEmpty()) {
			for (Double mark : marks) {
				sum += mark;
			}
			return sum / marks.size();
		}
		return sum;
	}
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte b: a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
	private void send_period(int period)
	{
		Log.i("Check period","period sent: "+period);
		final Intent intent = new Intent(ACTION_PERIOD_UPDATE);
		intent.putExtra(EXTRA_SERVICE_UUID, SensorTagGatt.UUID_IRT_DATA.toString());
		intent.putExtra(EXTRA_PERIOD,period);
		context.sendBroadcast(intent);
	}
	public static Data_store[] get_temp()
	{
		return data_temp;
	}



}
