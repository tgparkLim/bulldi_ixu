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
 * Lisview_light.java: lamp setup
 */

package openstack.bulldi.safe3x.Preference_lighting;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import openstack.bulldi.safe3x.Device_View.BatteryInformationServiceProfile;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.Preference_history.History_bean;
import openstack.bulldi.safe3x.Preference_history.History_preference;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.util.CheckBoxImageView;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Device_View.InOut_Service;
import openstack.bulldi.safe3x.R;

public class Listview_light extends BaseAdapter {
    private static final int TYPE_ITEM1 = 0;
    private static final int TYPE_ITEM2 = 1;
    //private static final int TYPE_ITEM3 = 2;
    int type;
     Holder holder_lamp;
    public static int IO_data = 0x40;//control the lamp
    public static int IO_config = 0x01;
    public static int IO_data_off = 0x00;
    public static int IO_config_off = 0x00;


    Context context;
    String[] result;
    int[] imageId;
    private static LayoutInflater inflater = null;
    public Listview_light(Lighting_preference mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        context = mainActivity;
        imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            type = TYPE_ITEM1;
        }
        else type = TYPE_ITEM2;
        return type;
    }

    @Override
    public int getViewTypeCount() {
        //return 3;
        return 2;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return 3;
        return 2;
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

    public  class Holder {
        CheckBoxImageView img;
        TextView tv;
        public void setCheckbox(boolean check){
            img.setChecked(check);
        }
    }
    public class Holder_sleep
    {
        CheckBoxImageView s1;
        CheckBoxImageView s2;
        CheckBoxImageView s3;
        CheckBoxImageView s4;
        CheckBoxImageView s5;
        public void setCheckbox_sleep(boolean[] check){
            s1.setChecked(check[0]);
            s2.setChecked(check[1]);
            s3.setChecked(check[2]);
            s4.setChecked(check[3]);
            s5.setChecked(check[4]);
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0: {
                    holder_lamp=new Holder();
                    convertView = inflater.inflate(R.layout.lighting_list1, null);
                    holder_lamp.tv = (TextView) convertView.findViewById(R.id.light_tv);
                    holder_lamp.img = (CheckBoxImageView) convertView.findViewById(R.id.switch_light);
                    holder_lamp.img.setChecked(Lighting_preference.light_on);
                    //holder_lamp.img.setOnCheckedChangeListener(myCheckChangLight);
                    if(BatteryInformationServiceProfile.battery_data>30){
                    holder_lamp.img.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
                        public void onCheckedChanged(View buttonView,
                                                     boolean isChecked) {

                                if(Lighting_preference.sleep_on[0]==true) {
                                    Lighting_preference.light_on = isChecked;
                                    if (isChecked == true) {
                                        byte value_config = (byte) (IO_config);
                                        byte value_data = (byte) (IO_data);
                                        //Log.i("Check in/out","value: "+value_config+";"+value_data);
                                        if (DeviceActivity.lighting_state != null)
                                            DeviceActivity.lighting_state.setImageResource(R.drawable.lightting);
                                        DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                    } else {
                                        byte value_config = (byte) (IO_config_off);
                                        byte value_data = (byte) (IO_data_off);
                                        //Log.i("Check in/out","value: "+value_config+";"+value_data);
                                        if (DeviceActivity.lighting_state != null)
                                            DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                                        DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                    }
                                }
                                else {
                                    holder_lamp.setCheckbox(Lighting_preference.light_on);
                                }

                        }
                    });
                    }
                    else{
                        Lighting_preference.light_on =false;
                        byte value_data = (byte) (IO_data_off);
                        Lighting_preference.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                        holder_lamp.img.setClickable(false);
//                                Toast.makeText(context, context.getResources().getString(R.string.lighting_no_support), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                case 1: {
                    final Holder_sleep holder = new Holder_sleep();
                    convertView = inflater.inflate(R.layout.lighting_list3, null);
                    holder.s1 = (CheckBoxImageView) convertView.findViewById(R.id.Sleeping_1);
                    holder.s1.setChecked(Lighting_preference.sleep_on[0]);
                    holder.s2=(CheckBoxImageView) convertView.findViewById(R.id.Sleeping_2);
                    holder.s2.setChecked(Lighting_preference.sleep_on[1]);
                    holder.s3=(CheckBoxImageView) convertView.findViewById(R.id.Sleeping_3);
                    holder.s3.setChecked(Lighting_preference.sleep_on[2]);
                    holder.s4=(CheckBoxImageView) convertView.findViewById(R.id.Sleeping_4);
                    holder.s4.setChecked(Lighting_preference.sleep_on[3]);
                    holder.s5=(CheckBoxImageView) convertView.findViewById(R.id.Sleeping_5);
                    holder.s5.setChecked(Lighting_preference.sleep_on[4]);

                    holder.setCheckbox_sleep(Lighting_preference.sleep_on);

                    if(BatteryInformationServiceProfile.battery_data>30) {
                        holder.s1.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
                            public void onCheckedChanged(View buttonView,
                                                         boolean isChecked) {
                                if (isChecked == false) holder.s1.setChecked(true);
                                else {
                                    holder.s2.setChecked(false);
                                    holder.s3.setChecked(false);
                                    holder.s4.setChecked(false);
                                    holder.s5.setChecked(false);
                                }
                                Lighting_preference.sleep_on[0] = true;
                                Lighting_preference.sleep_on[1] = false;
                                Lighting_preference.sleep_on[2] = false;
                                Lighting_preference.sleep_on[3] = false;
                                Lighting_preference.sleep_on[4] = false;
                                if ((Lighting_preference.handler_1 != null) && (Lighting_preference.runnable_1 != null))
                                    Lighting_preference.handler_1.removeCallbacks(Lighting_preference.runnable_1);
                                if ((Lighting_preference.handler_2 != null) && (Lighting_preference.runnable_2 != null))
                                    Lighting_preference.handler_2.removeCallbacks(Lighting_preference.runnable_2);
                                if ((Lighting_preference.handler_3 != null) && (Lighting_preference.runnable_3 != null))
                                    Lighting_preference.handler_3.removeCallbacks(Lighting_preference.runnable_3);
                                if ((Lighting_preference.handler_4 != null) && (Lighting_preference.runnable_4 != null))
                                    Lighting_preference.handler_4.removeCallbacks(Lighting_preference.runnable_4);
                            }
                        });
                    }else holder.s1.setClickable(false);

                    if(BatteryInformationServiceProfile.battery_data>30) {
                    holder.s2.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
                        public void onCheckedChanged(View buttonView,
                                                     boolean isChecked) {
                            if (isChecked == false) holder.s2.setChecked(true);
                            else {
                                holder.s1.setChecked(false);
                                holder.s3.setChecked(false);
                                holder.s4.setChecked(false);
                                holder.s5.setChecked(false);
                            }
                            Lighting_preference.sleep_on[0] = false;
                            Lighting_preference.sleep_on[1] = true;
                            Lighting_preference.sleep_on[2] = false;
                            Lighting_preference.sleep_on[3] = false;
                            Lighting_preference.sleep_on[4] = false;
                            Lighting_preference.light_on = true;

                                byte value_data = (byte) (IO_data);
                                if (DeviceActivity.lighting_state != null)
                                    DeviceActivity.lighting_state.setImageResource(R.drawable.lightting);
                                DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                Lighting_preference.handler_1 = new Handler();
                                Lighting_preference.runnable_1 = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Lighting_preference.light_on == true) {
                                            Lighting_preference.light_on = false;
                                            byte value_config = (byte) (Listview_light.IO_config_off);
                                            byte value_data = (byte) (Listview_light.IO_data_off);
                                            if (DeviceActivity.lighting_state != null)
                                                DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                                            //mBtLeService.writeCharacteristic(InOut_Service.charc_config, value_config);
                                            Lighting_preference.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);

                                            Lighting_preference.sleep_on[0] = true;
                                            Lighting_preference.sleep_on[1] = false;
                                            Lighting_preference.sleep_on[2] = false;
                                            Lighting_preference.sleep_on[3] = false;
                                            Lighting_preference.sleep_on[4] = false;
                                            holder.setCheckbox_sleep(Lighting_preference.sleep_on);

                                        }
                                    }
                                };
                                if ((Lighting_preference.handler_2 != null) && (Lighting_preference.runnable_2 != null))
                                    Lighting_preference.handler_2.removeCallbacks(Lighting_preference.runnable_2);
                                if ((Lighting_preference.handler_3 != null) && (Lighting_preference.runnable_3 != null))
                                    Lighting_preference.handler_3.removeCallbacks(Lighting_preference.runnable_3);
                                if ((Lighting_preference.handler_4 != null) && (Lighting_preference.runnable_4 != null))
                                    Lighting_preference.handler_4.removeCallbacks(Lighting_preference.runnable_4);
                                Lighting_preference.handler_1.postDelayed(Lighting_preference.runnable_1, 300000);

                            if (Lighting_preference.lighting_back != null) {
                                Lighting_preference.lighting_back.callOnClick();
                                Lighting_preference.is_lighting = false;
                            }
                        }
                    });
                    } else holder.s2.setClickable(false);

                    if (BatteryInformationServiceProfile.battery_data > 30) {
                    holder.s3.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
                        public void onCheckedChanged(View buttonView,
                                                     boolean isChecked) {
                            if (isChecked == false) holder.s3.setChecked(true);
                            else {
                                holder.s1.setChecked(false);
                                holder.s2.setChecked(false);
                                holder.s4.setChecked(false);
                                holder.s5.setChecked(false);
                            }
                            Lighting_preference.sleep_on[0] = false;
                            Lighting_preference.sleep_on[1] = false;
                            Lighting_preference.sleep_on[2] = true;
                            Lighting_preference.sleep_on[3] = false;
                            Lighting_preference.sleep_on[4] = false;
                            Lighting_preference.light_on = true;

                                byte value_data = (byte) (IO_data);
                                if (DeviceActivity.lighting_state != null)
                                    DeviceActivity.lighting_state.setImageResource(R.drawable.lightting);
                                DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                Lighting_preference.handler_2 = new Handler();
                                Lighting_preference.runnable_2 = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Lighting_preference.light_on == true) {
                                            Lighting_preference.light_on = false;
                                            byte value_config = (byte) (Listview_light.IO_config_off);
                                            byte value_data = (byte) (Listview_light.IO_data_off);
                                            if (DeviceActivity.lighting_state != null)
                                                DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                                            //mBtLeService.writeCharacteristic(InOut_Service.charc_config, value_config);
                                            Lighting_preference.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                            Lighting_preference.sleep_on[0] = true;
                                            Lighting_preference.sleep_on[1] = false;
                                            Lighting_preference.sleep_on[2] = false;
                                            Lighting_preference.sleep_on[3] = false;
                                            Lighting_preference.sleep_on[4] = false;
                                            holder.setCheckbox_sleep(Lighting_preference.sleep_on);
                                            //Lighting_preference.sleep_on[0] = true;Lighting_preference.sleep_on[1] = false;Lighting_preference.sleep_on[2] = false;Lighting_preference.sleep_on[3] = false;Lighting_preference.sleep_on[4] = false;
                                            //holder_lamp.img.setChecked(false);
                                        }
                                    }
                                };
                                if ((Lighting_preference.handler_1 != null) && (Lighting_preference.runnable_1 != null))
                                    Lighting_preference.handler_1.removeCallbacks(Lighting_preference.runnable_1);
                                if ((Lighting_preference.handler_3 != null) && (Lighting_preference.runnable_3 != null))
                                    Lighting_preference.handler_3.removeCallbacks(Lighting_preference.runnable_3);
                                if ((Lighting_preference.handler_4 != null) && (Lighting_preference.runnable_4 != null))
                                    Lighting_preference.handler_4.removeCallbacks(Lighting_preference.runnable_4);
                                Lighting_preference.handler_2.postDelayed(Lighting_preference.runnable_2, 600000);

                            if (Lighting_preference.lighting_back != null) {
                                Lighting_preference.lighting_back.callOnClick();
                                Lighting_preference.is_lighting = false;
                            }
                        }
                    });
                    } else holder.s3.setClickable(false);

                    if (BatteryInformationServiceProfile.battery_data > 30) {
                    holder.s4.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
                        public void onCheckedChanged(View buttonView,
                                                     boolean isChecked) {
                            if (isChecked == false) holder.s4.setChecked(true);
                            else {
                                holder.s1.setChecked(false);
                                holder.s2.setChecked(false);
                                holder.s3.setChecked(false);
                                holder.s5.setChecked(false);
                            }
                            Lighting_preference.sleep_on[0] = false;
                            Lighting_preference.sleep_on[1] = false;
                            Lighting_preference.sleep_on[2] = false;
                            Lighting_preference.sleep_on[3] = true;
                            Lighting_preference.sleep_on[4] = false;
                            Lighting_preference.light_on = true;

                                byte value_data = (byte) (IO_data);
                                if (DeviceActivity.lighting_state != null)
                                    DeviceActivity.lighting_state.setImageResource(R.drawable.lightting);
                                DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                Lighting_preference.handler_3 = new Handler();
                                Lighting_preference.runnable_3 = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Lighting_preference.light_on == true) {
                                            Lighting_preference.light_on = false;
                                            byte value_config = (byte) (Listview_light.IO_config_off);
                                            byte value_data = (byte) (Listview_light.IO_data_off);
                                            if (DeviceActivity.lighting_state != null)
                                                DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                                            //mBtLeService.writeCharacteristic(InOut_Service.charc_config, value_config);
                                            Lighting_preference.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);

                                            Lighting_preference.sleep_on[0] = true;
                                            Lighting_preference.sleep_on[1] = false;
                                            Lighting_preference.sleep_on[2] = false;
                                            Lighting_preference.sleep_on[3] = false;
                                            Lighting_preference.sleep_on[4] = false;
                                            holder.setCheckbox_sleep(Lighting_preference.sleep_on);
                                            //Lighting_preference.sleep_on[0] = true;Lighting_preference.sleep_on[1] = false;Lighting_preference.sleep_on[2] = false;Lighting_preference.sleep_on[3] = false;Lighting_preference.sleep_on[4] = false;
                                            //holder_lamp.img.setChecked(false);
                                        }
                                    }
                                };
                                if ((Lighting_preference.handler_1 != null) && (Lighting_preference.runnable_1 != null))
                                    Lighting_preference.handler_1.removeCallbacks(Lighting_preference.runnable_1);
                                if ((Lighting_preference.handler_2 != null) && (Lighting_preference.runnable_2 != null))
                                    Lighting_preference.handler_2.removeCallbacks(Lighting_preference.runnable_2);
                                if ((Lighting_preference.handler_4 != null) && (Lighting_preference.runnable_4 != null))
                                    Lighting_preference.handler_4.removeCallbacks(Lighting_preference.runnable_4);
                                Lighting_preference.handler_3.postDelayed(Lighting_preference.runnable_3, 1200000);

                            if (Lighting_preference.lighting_back != null) {
                                Lighting_preference.lighting_back.callOnClick();
                                Lighting_preference.is_lighting = false;
                            }
                        }
                    });
                    } else holder.s4.setClickable(false);

                    if (BatteryInformationServiceProfile.battery_data > 30) {
                        holder.s5.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
                        public void onCheckedChanged(View buttonView,
                                                     boolean isChecked) {
                            if (isChecked == false) holder.s5.setChecked(true);
                            else {
                                holder.s1.setChecked(false);
                                holder.s2.setChecked(false);
                                holder.s3.setChecked(false);
                                holder.s4.setChecked(false);
                            }
                            Lighting_preference.sleep_on[0] = false;Lighting_preference.sleep_on[1] = false;
                            Lighting_preference.sleep_on[2] = false;Lighting_preference.sleep_on[3] = false;Lighting_preference.sleep_on[4] = true;Lighting_preference.light_on = true;

                            byte value_data = (byte) (IO_data);
                            if (DeviceActivity.lighting_state != null)
                                DeviceActivity.lighting_state.setImageResource(R.drawable.lightting);
                            DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                            Lighting_preference.handler_4 = new Handler();
                            Lighting_preference.runnable_4=new Runnable() {
                                @Override
                                public void run() {
                                    if(Lighting_preference.light_on==true) {
                                        Lighting_preference.light_on = false;
                                        byte value_config =  (byte) (Listview_light.IO_config_off);
                                        byte value_data =  (byte) (Listview_light.IO_data_off);
                                        if(DeviceActivity.lighting_state!=null) DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                                        //mBtLeService.writeCharacteristic(InOut_Service.charc_config, value_config);
                                        Lighting_preference.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                                        Lighting_preference.sleep_on[0] = true;
                                        Lighting_preference.sleep_on[1] = false;
                                        Lighting_preference.sleep_on[2] = false;
                                        Lighting_preference.sleep_on[3] = false;
                                        Lighting_preference.sleep_on[4] = false;
                                        holder.setCheckbox_sleep(Lighting_preference.sleep_on);
                                        //Lighting_preference.sleep_on[0] = true;Lighting_preference.sleep_on[1] = false;Lighting_preference.sleep_on[2] = false;Lighting_preference.sleep_on[3] = false;Lighting_preference.sleep_on[4] = false;
                                        //holder_lamp.img.setChecked(false);
                                    }
                                }
                            };
                            if((Lighting_preference.handler_1!=null)&&(Lighting_preference.runnable_1!=null)) Lighting_preference.handler_1.removeCallbacks(Lighting_preference.runnable_1);
                            if((Lighting_preference.handler_2!=null)&&(Lighting_preference.runnable_2!=null)) Lighting_preference.handler_2.removeCallbacks(Lighting_preference.runnable_2);
                            if((Lighting_preference.handler_3!=null)&&(Lighting_preference.runnable_3!=null)) Lighting_preference.handler_3.removeCallbacks(Lighting_preference.runnable_3);
                            Lighting_preference.handler_4.postDelayed(Lighting_preference.runnable_4, 1800000);

                            if (Lighting_preference.lighting_back != null) {
                                Lighting_preference.lighting_back.callOnClick();
                                Lighting_preference.is_lighting = false;
                            }
                        }
                    });
                    } else holder.s5.setClickable(false);

                    break;
                }
            }
        }
        return convertView;
    }
    CheckBoxImageView.OnCheckedChangeListener myCheckChangLight = new CheckBoxImageView.OnCheckedChangeListener() {
        public void onCheckedChanged(View buttonView,
                                     boolean isChecked) {
                Lighting_preference.light_on = isChecked;
                if (isChecked == true) {
                    byte value_config = (byte) (IO_config);
                    byte value_data = (byte) (IO_data);
                    //Log.i("Check in/out","value: "+value_config+";"+value_data);
                    if (DeviceActivity.lighting_state != null) DeviceActivity.lighting_state.setImageResource(R.drawable.lightting);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                } else {
                    byte value_config = (byte) (IO_config_off);
                    byte value_data = (byte) (IO_data_off);
                    //Log.i("Check in/out","value: "+value_config+";"+value_data);
                    if (DeviceActivity.lighting_state != null) DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
                    DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
                }
        }
    };

}
