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
 * SensorTagSmokeProfile.java: smoke service of bulldi
 */

package openstack.bulldi.safe3x.Device_View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
import openstack.bulldi.safe3x.R;
import openstack.util.GenericCharacteristicTableRow;
import openstack.util.Point3D;

public class SensorTagSmokeProfile extends GenericBluetoothProfile {
    protected final Context context;
    public final static String ACTION_PERIOD_UPDATE = "openstack.util.ACTION_PERIOD_UPDATE";
    public final static String EXTRA_SERVICE_UUID = "openstack.util.EXTRA_SERVICE_UUID";
    public final static String EXTRA_PERIOD = "openstack.util.EXTRA_PERIOD";
    public static int smoke_period=1000;
    public static boolean smoke_alarm=false;

    public static List<Double> myList_smoke = new ArrayList<Double>();

    public static double smoke_max;
    public static double smoke_min;
    public static double smoke_average;

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
    static public Data_store data_smoke[]={new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),
            new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),new Data_store(),
            new Data_store(),new Data_store(),new Data_store(),new Data_store()};

    public SensorTagSmokeProfile(Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
        super(con,device,service,controller);
        context=con;
        this.tRow =  new GenericCharacteristicTableRow(con);

        List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();

        for (BluetoothGattCharacteristic c : characteristics) {
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_SMO_DATA.toString())) {
                this.dataC = c;
            }
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_SMO_CONF.toString())) {
                this.configC = c;
            }
            if (c.getUuid().toString().equals(SensorTagGatt.UUID_SMO_PERI.toString())) {
                this.periodC = c;
            }
        }
        this.tRow.uuidLabel.setText(this.dataC.getUuid().toString());

    }

    public static boolean isCorrectService(BluetoothGattService service) {
        if ((service.getUuid().toString().compareTo(SensorTagGatt.UUID_SMO_SERV.toString())) == 0) {//service.getUuid().toString().compareTo(SensorTagGatt.UUID_HUM_DATA.toString())) {
            Log.d("Test", "Match !");
            return true;
        }
        else return false;
    }
    public void didWriteValueForCharacteristic(BluetoothGattCharacteristic c) {

    }
    public void didReadValueForCharacteristic(BluetoothGattCharacteristic c) {

    }
    @Override
    public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic c) {
        byte[] value = c.getValue();
        if (c.equals(this.dataC)){
            Point3D v = Sensor.SMOKE.convert(value);
            if(myList_smoke.size()==2147483647) myList_smoke.remove(0);
            myList_smoke.add(v.x);
            //Log.i("Check list size", "Smoke list size: " + myList_smoke.size() + " with value: " + v.x);
            smoke_max = Collections.max(myList_smoke);
            smoke_min=Collections.min(myList_smoke);
            smoke_average=calculateAverage(myList_smoke);
            /*int smoke_hex=(int) v.y;
            String span=Integer.toHexString(smoke_hex);
            Device_Smoke.smoke_text.setText("0x"+span);*/
            //Device_Smoke.smoke_text.setText(String.format("%.0f ", v.x));

            //transfer smoke
            final double smoke_show = v.x;
            final String s = String.format("%.0f ", smoke_show);

            if(v.x==0)  {
                Device_Smoke.smoke_status.setText(context.getString(R.string.sensor_status_zeros));
                Device_Smoke.smoke_image.setImageResource(R.drawable.smoke_stable);
            }
            else {
                Device_Smoke.smoke_status.setText(context.getString(R.string.sensor_status_suspect));
                Device_Smoke.smoke_image.setImageResource(R.drawable.smoke_warn);
            }
            //Graph
            current_time= Calendar.getInstance();
            df = new SimpleDateFormat("HH:mm a");
            cur_time_1 = df.format(Calendar.getInstance().getTime());
            df_1 = new SimpleDateFormat("d/MM");
            cur_time_2 = df_1.format(Calendar.getInstance().getTime());

            if(current_time.get(Calendar.MINUTE)==0 ) {
                data_smoke[current_time.get(Calendar.HOUR_OF_DAY)].set_time(current_time.get(Calendar.YEAR), current_time.get(Calendar.MONTH), current_time.get(Calendar.DAY_OF_MONTH),current_time.get(Calendar.HOUR_OF_DAY),current_time.get(Calendar.MINUTE));
                data_smoke[current_time.get(Calendar.HOUR_OF_DAY)].set_data(v.x);
            }
            //Alarm check

            if(v.x<10)
            {
                if(smoke_alarm==true){
                    if(v.x>=3) {
                        //Do nothing
                    }
                    else{
                        if(smoke_period!=1000) {
                            smoke_period=1000;
                            send_period(smoke_period);
                            smoke_alarm=false;
                        }
                    }
                }
                else {
                    if (smoke_period != 1000) {
                        smoke_period = 1000;
                        send_period(smoke_period);
                        smoke_alarm = false;
                    }
                }
            }
            else
            {
                if(smoke_period==1000)
                {
                    smoke_period=500;
                    send_period(smoke_period);
                    smoke_alarm = false;
                }
                else if(smoke_period==500)
                {

                    smoke_period=100;
                    send_period(smoke_period);
                    smoke_alarm = false;
                }
                else  if(smoke_period==100) {
                    smoke_alarm = true;
                    alarm_value=String.format("%.0f", v.x);
                    time1 = df1.format(Calendar.getInstance().getTime());
                    time2 = df2.format(Calendar.getInstance().getTime());
                }
            }
        }
    }
    @Override
    public Map<String,String> getMQTTMap() {
        Point3D v = Sensor.SMOKE.convert(this.dataC.getValue());
        Map<String,String> map = new HashMap<String, String>();
        map.put("smoke",String.format("%.2f",v.x));
        return map;
    }
    public Point3D get_data()
    {
        Point3D smo = Sensor.SMOKE.convert(this.dataC.getValue());
        return smo;
    }
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
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
    private void send_period(int period)
    {
        Log.i("Check period","period sent: "+period);
        final Intent intent = new Intent(ACTION_PERIOD_UPDATE);
        intent.putExtra(EXTRA_SERVICE_UUID, SensorTagGatt.UUID_SMO_DATA.toString());
        intent.putExtra(EXTRA_PERIOD,period);
        context.sendBroadcast(intent);
    }
    public static Data_store[] get_smoke()
    {
        return data_smoke;
    }
}
