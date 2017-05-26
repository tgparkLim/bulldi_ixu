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
 * Operation_test_adapter.java: Operation test list control
 */

package openstack.bulldi.safe3x.Preference_test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import openstack.bulldi.safe3x.Alarm.Alarm;
import openstack.bulldi.safe3x.Alarm.DialogAlarm;
import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Device_View.BatteryInformationServiceProfile;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Device_View.InOut_Service;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.Preference_lighting.Listview_light;
import openstack.bulldi.safe3x.Preference_sharing.Message_content_preference;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;

public class Operation_test_adapter extends BaseAdapter {
    String [] result;
    Context context;
    int [] imageId_1;
    public static  boolean is_fireTest=false;
    boolean is_alarmTest=false;
    boolean is_lampTest=false;
    public static byte previous_data = (byte) (0x00);
/*    Handler handler=new Handler();
    Runnable runnable;*/
    private  LayoutInflater inflater=null;
    public static View row0;
    public static View row1;
    public static View row2;
    public static Holder holder0;
    public static Holder holder1;
    public static Holder holder2;
    public Operation_test_adapter(Operation_test_preference mainActivity, String[] prgmNameList, int[] prgmImages_1) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId_1=prgmImages_1;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        ImageView img_1;
        TextViewPlus tv;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.operation_test_element, null);
        holder.tv=(TextViewPlus) rowView.findViewById(R.id.operation_test_tv);
        holder.img_1=(ImageView) rowView.findViewById(R.id.operation_test_img);
        holder.tv.setText(result[position]);
        holder.img_1.setImageResource(imageId_1[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (BatteryInformationServiceProfile.battery_data > 30) {
                    if (position == 0) {
                        row0 = rowView;
                        holder0 = holder;
                        is_lampTest = false;
                        is_lampTest = false;
                        if (is_fireTest == false) is_fireTest = true;
                        else is_fireTest = false;
                        if (is_fireTest == true) {
                            if (row1 != null) row1.setBackgroundResource(R.color.White);
                            if (row2 != null) row2.setBackgroundResource(R.color.White);
                            if (holder1 != null) holder1.tv.setTextColor(Color.BLACK);
                            if (holder2 != null) holder2.tv.setTextColor(Color.BLACK);
                            rowView.setBackgroundResource(R.color.DarkTurquoise);
                            //holder.tv.setTextColor(Color.WHITE);
                            //Send comand to bulldi start testing
                            byte value_data = (byte) (0x08);
                            previous_data = (byte) (0x08);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                            Intent i = new Intent(context, DialogAlarmTest.class);
                            context.startActivity(i);
                        } else {
                            rowView.setBackgroundResource(R.color.White);
                            //holder.tv.setTextColor(Color.BLACK);
                            //Send comand to bulldi stop testing
                            byte value_data = (byte) (0x00);
                            previous_data = (byte) (0x00);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                        }
                    } else if (position == 1) {
                        row1 = rowView;
                        holder1 = holder;
                        is_fireTest = false;
                        is_lampTest = false;
                        if (is_alarmTest == false) is_alarmTest = true;
                        else is_alarmTest = false;
                        if (is_alarmTest == true) {
                            if (row0 != null) row0.setBackgroundResource(R.color.White);
                            if (row2 != null) row2.setBackgroundResource(R.color.White);
                            if (holder0 != null) holder0.tv.setTextColor(Color.BLACK);
                            if (holder2 != null) holder2.tv.setTextColor(Color.BLACK);
                            rowView.setBackgroundResource(R.color.DarkTurquoise);
                            holder.tv.setTextColor(Color.WHITE);
                            //Send comand to bulldi start testing
                            byte value_data = (byte) (0x10);
                            previous_data = (byte) (0x10);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                        } else {
                            rowView.setBackgroundResource(R.color.White);
                            holder.tv.setTextColor(Color.BLACK);
                            //Send comand to bulldi to stop testing
                            byte value_data = (byte) (0x00);
                            previous_data = (byte) (0x00);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                        }
                    } else if (position == 2) {
                        row2 = rowView;
                        holder2 = holder;
                        is_fireTest = false;
                        is_alarmTest = false;
                        if (previous_data != (byte) (0x00)) {
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, (byte) (0x00));
                            final Handler handler_reconnect = new Handler();
                            handler_reconnect.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (is_lampTest == false) is_lampTest = true;
                                    else is_lampTest = false;
                                    if (is_lampTest == true) {
                                        if (row0 != null) row0.setBackgroundResource(R.color.White);
                                        if (row1 != null) row1.setBackgroundResource(R.color.White);
                                        if (holder0 != null) holder0.tv.setTextColor(Color.BLACK);
                                        if (holder1 != null) holder1.tv.setTextColor(Color.BLACK);
                                        rowView.setBackgroundResource(R.color.DarkTurquoise);
                                        holder.tv.setTextColor(Color.WHITE);
                                        //Send comand to bulldi start testing
                                        byte value_data = (byte) (0x40);
                                        previous_data = (byte) (0x40);
                                        DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                        final Handler handler = new Handler();
                                    } else {
                                        rowView.setBackgroundResource(R.color.White);
                                        holder.tv.setTextColor(Color.BLACK);
                                        //Send comand to bulldi to stop testing
                                        byte value_data = (byte) (0x00);
                                        previous_data = (byte) (0x00);
                                        DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                    }
                                }
                            }, 100);
                        } else {
                            if (is_lampTest == false) is_lampTest = true;
                            else is_lampTest = false;
                            if (is_lampTest == true) {
                                if (row0 != null) row0.setBackgroundResource(R.color.White);
                                if (row1 != null) row1.setBackgroundResource(R.color.White);
                                if (holder0 != null) holder0.tv.setTextColor(Color.BLACK);
                                if (holder1 != null) holder1.tv.setTextColor(Color.BLACK);
                                rowView.setBackgroundResource(R.color.DarkTurquoise);
                                holder.tv.setTextColor(Color.WHITE);
                                //Send comand to bulldi start testing
                                byte value_data = (byte) (0x40);
                                previous_data = (byte) (0x40);
                                DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                final Handler handler = new Handler();
                            } else {
                                rowView.setBackgroundResource(R.color.White);
                                holder.tv.setTextColor(Color.BLACK);
                                //Send comand to bulldi to stop testing
                                byte value_data = (byte) (0x00);
                                previous_data = (byte) (0x00);
                                DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                            }
                        }
                    }
                } else {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                    dialog.setContentView(R.layout.customer_test_dialog);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    wlp.gravity = Gravity.CENTER;
                    window.setAttributes(wlp);
                    final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_custom_test_close);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogButton.setBackgroundResource(R.drawable.button_on);
                            dialogButton.setTextColor(Color.BLACK);
                            final Handler handler_reconnect = new Handler();
                            handler_reconnect.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();

                                }
                            }, 10);
                        }
                    });
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.show();
//                    Toast.makeText(context, context.getResources().getString(R.string.operation_test_no_support), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rowView;
    }

}
