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
 * History_preference.java: history record control
 */

package openstack.bulldi.safe3x.Preference_history;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class History_preference  extends Activity {
    public static ListView lv;
    public static Context context;
    History_preference his;
    public static ImageView history_setting_back;
    public TextViewPlus history_title;
    public static ImageView history_delete;
    public static DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
    public static DateFormat df2 = new SimpleDateFormat("HH:mm");
    public static DateFormat df1_ko = new SimpleDateFormat("yyyy.MM.dd");
    public static DateFormat df2_ko = new SimpleDateFormat("HH:mm");
    public static String co_emergecy="CO Emergency";
    public static String co_value="Not yet";
    public static String coTime1="";
    public static String coTime2="";
    public static int co_icon= R.drawable.icn_cogas;
    public static int co_delete=R.drawable.but_trash3;
    public static String temp_emergecy="Temperature Emergency";
    public static String temp_value="Not yet";
    public static String tempTime1="";
    public static String tempTime2="";
    public static int temp_icon=R.drawable.icn_temper;
    public static int temp_delete=R.drawable.but_trash3;
    public static String smoke_emergecy="Smoke Emergency";
    public static String smoke_value="Not yet";
    public static String smokeTime1="";
    public static String smokeTime2="";
    public static int smoke_icon=R.drawable.icn_smoke;
    public static int smoke_delete=R.drawable.but_trash3;
    public static String slepping="Sleeping mode";
    public static String slepping_value="None";
    public static String sleppingTime1="";
    public static String sleppingTime2="";
    public static int slepping_icon=R.drawable.icn_sleeping;
    public static int slepping_delete=R.drawable.but_trash3;
    public static String install="Hi, nice to meet you";
    public static String install_value="installation";
    public static String installTime1="Fri.31.07.15";
    public static String installTime2="06:16 PM";
    public static int install_icon=R.drawable.icn_hi;

    public static String ble_connect="Connected";
    public static String connect_value="on";
    public static String connectTime1="Fri.31.07.15";
    public static String connectTime2="06:16 PM";
    public static int connect_icon=R.drawable.ble_connect;

    public static String ble_disconnect="Disconnected";
    public static String disconnect_value="off";
    public static String disconnectTime1="Fri.31.07.15";
    public static String disconnectTime2="06:16 PM";
    public static int disconnect_icon=R.drawable.ble_disconnect;

    public static int install_delete=R.drawable.but_trash3;
    public static PackageInfo packageInfo;
    //Data is update by changing the list string/image

    public static boolean is_history=false;
    public static MyCustomAdapter dataAdapter = null;
    public static  ArrayList<History_bean> HistoryList;
    private ArrayList<History_bean> list = new ArrayList<History_bean>();
    public static int history_size=0;
    public static int [] his_image;
    public static String [] his_title;
    public static String [] his_value;
    public static String [] his_time1;
    public static String [] his_time2;
    public static boolean is_clear_all=false;
    public SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.warning_history);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.history_titlebar);

        co_emergecy=getResources().getString(R.string.warning_history_co);
        temp_emergecy=getResources().getString(R.string.warning_history_temperature);
        smoke_emergecy=getResources().getString(R.string.warning_history_smoke);
        slepping=getResources().getString(R.string.warning_history_sleep_mode);
        install=getResources().getString(R.string.warning_history_install);
        install_value=getResources().getString(R.string.warning_history_install_value);
        ble_connect=getResources().getString(R.string.warning_history_connection);
        ble_disconnect=getResources().getString(R.string.warning_history_disconnection);

        history_setting_back= (ImageView) findViewById(R.id.history_back);
        history_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        history_title=(TextViewPlus) findViewById(R.id.history_title);
        history_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        his=this;
        context=this;
        Log.i("Check history_size","value history: "+history_size);
        //if(HistoryList!=null) history_size=HistoryList.size();
        dataAdapter = new MyCustomAdapter(this, R.layout.history_list, HistoryList);
        lv=(ListView) findViewById(R.id.history_list);
        lv.setAdapter(dataAdapter);
        history_delete=(ImageView) findViewById(R.id.but_delete_history);
        history_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_delete.setImageResource(R.drawable.but_trash2_on);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        history_delete.setImageResource(R.drawable.but_trash2_off);
                    }
                }, 10);
                new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle(getResources().getString(R.string.waring_history_alert_title))
                        .setMessage(getResources().getString(R.string.waring_history_alert_message))
                        .setPositiveButton(getResources().getString(R.string.waring_history_alert_positive), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                int []delete=null;
                                for (int i =0; i<HistoryList.size(); i++) {
                                    delete = addElements(delete, i);
                                }
                                if(delete!=null){
                                    bubbleSort(delete);
                                    for (int i = 0; i < delete.length; i++) {
                                        HistoryList.remove(HistoryList.get(delete[i]));
                                    }
                                }
                                dataAdapter = new MyCustomAdapter(context, R.layout.history_list, HistoryList);
                                lv.setAdapter(dataAdapter);
                                history_size=HistoryList.size();
                                history_delete.setImageResource(R.drawable.but_trash2_off);
                                is_clear_all=true;
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.waring_history_alert_negative), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();

            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        is_history=true;
        //if((DeviceActivity.isAlarm==true)&&(history_setting_back!=null)) history_setting_back.callOnClick();


    }
    @Override
    public void onPause(){
        super.onPause();
        is_history=false;
        //Save history size
        history_size=HistoryList.size();
        Log.i("Check history_size","value: "+History_preference.history_size);
        SharedPreferences prefs_history_size = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_history_size = prefs_history_size.edit();
        editor_history_size.putInt("history_size", history_size);
        editor_history_size.commit(); //important, otherwise it wouldn't save.
        //Save history image
        SharedPreferences[] prefs_history_img=new SharedPreferences[history_size];
        SharedPreferences.Editor[] editor_history_img= new SharedPreferences.Editor[history_size];
        for(int i=0;i<history_size;i++) {
            prefs_history_img[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_history_img[i] = prefs_history_img[i].edit();
            editor_history_img[i].putInt("history_img_" + Integer.toString(i), HistoryList.get(i).getImg1());
            editor_history_img[i].commit(); //important, otherwise it wouldn't save.
        }
        //Save history title
        SharedPreferences[] prefs_history_title=new SharedPreferences[history_size];
        SharedPreferences.Editor[] editor_history_title= new SharedPreferences.Editor[history_size];
        for(int i=0;i<history_size;i++) {
            prefs_history_title[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_history_title[i] = prefs_history_title[i].edit();
            editor_history_title[i].putString("history_title_" + Integer.toString(i), HistoryList.get(i).getTitle());
            editor_history_title[i].commit(); //important, otherwise it wouldn't save.
        }
        //Save history value
        SharedPreferences[] prefs_history_value=new SharedPreferences[history_size];
        SharedPreferences.Editor[] editor_history_value= new SharedPreferences.Editor[history_size];
        for(int i=0;i<history_size;i++) {
            prefs_history_value[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_history_value[i] = prefs_history_value[i].edit();
            editor_history_value[i].putString("history_value_" + Integer.toString(i), HistoryList.get(i).getValue());
            editor_history_value[i].commit(); //important, otherwise it wouldn't save.
        }
        //Save history time1
        SharedPreferences[] prefs_history_time1=new SharedPreferences[history_size];
        SharedPreferences.Editor[] editor_history_time1= new SharedPreferences.Editor[history_size];
        for(int i=0;i<history_size;i++) {
            prefs_history_time1[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_history_time1[i] = prefs_history_time1[i].edit();
            editor_history_time1[i].putString("history_time1_" + Integer.toString(i), HistoryList.get(i).getTime1());
            editor_history_time1[i].commit(); //important, otherwise it wouldn't save.
        }
        //Save history time1
        SharedPreferences[] prefs_history_time2=new SharedPreferences[history_size];
        SharedPreferences.Editor[] editor_history_time2= new SharedPreferences.Editor[history_size];
        for(int i=0;i<history_size;i++) {
            prefs_history_time2[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_history_time2[i] = prefs_history_time2[i].edit();
            editor_history_time2[i].putString("history_time2_" + Integer.toString(i), HistoryList.get(i).getTime2());
            editor_history_time2[i].commit(); //important, otherwise it wouldn't save.
        }
    }
    private class MyCustomAdapter extends ArrayAdapter<History_bean> {

        private ArrayList<History_bean> HistoryList_bean;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<History_bean> HistoryList) {
            super(context, textViewResourceId, HistoryList);
            this.HistoryList_bean = new ArrayList<History_bean>();
            this.HistoryList_bean.addAll(HistoryList);
        }

        private class ViewHolder {
            ImageView img_1;
            TextView tv_1, tv_2,tv_3,tv_4;
            SwipeLayout swip;
            //CheckBox check;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.history_list, null);

                holder = new ViewHolder();
                holder.tv_1=(TextView) convertView.findViewById(R.id.history_txt_1);
                holder.tv_2 = (TextView) convertView.findViewById(R.id.history_txt_2);
                holder.tv_3=(TextView) convertView.findViewById(R.id.history_txt_3);
                holder.tv_4=(TextView) convertView.findViewById(R.id.history_txt_4);
                holder.img_1 = (ImageView) convertView.findViewById(R.id.history_img_1);
                holder.swip=(SwipeLayout) convertView.findViewById(R.id.test_swipe_swipe);
                //Log.i("Check swip","value: "+holder.swip);
                holder.swip.findViewById(R.id.trash_history).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       HistoryList.remove(position);
                        dataAdapter = new MyCustomAdapter(context, R.layout.history_list, HistoryList);
                        lv.setAdapter(dataAdapter);
                    }
                });
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            History_bean bean = HistoryList_bean.get(position);
            holder.img_1.setImageResource(bean.getImg1());
            holder.tv_1.setText(bean.getTitle());
            holder.tv_2.setText(bean.getValue());
            String full_time1=bean.getTime1();
            String full_time2=bean.getTime2();
            Log.i("Check time","value: "+full_time1+";"+full_time2);
            String stringDay=full_time1.substring(0, 2);
            String stringMonth=full_time1.substring(3,5);int intMonth=Integer.parseInt(stringMonth);
            String stringYear=full_time1.substring(6, 10);
            String stringHour=full_time2.substring(0,2);int intHour=Integer.parseInt(stringHour);
            String stringMinute=full_time2.substring(3, 5);
            String mon=changeMonth(intMonth);
            if(Language_setting.is_english==true) {
                String final_en1="";
                String final_en2="";
                final_en1=mon+" "+stringDay+", "+stringYear;
                if(intHour>12) final_en2=stringHour+":"+stringMinute+" p.m.";
                else final_en2=stringHour+":"+stringMinute+" a.m.";
                holder.tv_3.setText(final_en1);
                holder.tv_4.setText(final_en2);
            }else {
                String final_en1="";
                String final_en2="";
                final_en1=stringYear+". "+stringMonth+". "+stringDay;
                if(intHour>12) final_en2="오후 "+stringHour+":"+stringMinute;
                else final_en2="오전 "+stringHour+":"+stringMinute;
                holder.tv_3.setText(final_en1);
                holder.tv_4.setText(final_en2);
            }

            //holder.check.setChecked(bean.isSelected());
            //holder.check.setTag(bean);

            return convertView;

        }

    }
    public  int[] addElements(int[] array, int value) {
        if(array==null){
            int[] array_add={value};
            return array_add;
        }
        else{
            int[] array_add = Arrays.copyOf(array, array.length + 1); //create new array from old array and allocate one more element
            array_add[array_add.length - 1] = value;
            return array_add;
        }
    }
    public  void bubbleSort(int[] intArray) {
        int n = intArray.length;
        int temp = 0;

        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){

                if(intArray[j-1] < intArray[j]){
                    //swap the elements!
                    temp = intArray[j-1];
                    intArray[j-1] = intArray[j];
                    intArray[j] = temp;
                }
            }
        }
    }
    String changeMonth(int intMonth){
        String mon="";
        if(intMonth==1) mon="January";
        else if(intMonth==1) mon="January";
        else if(intMonth==2) mon="February";
        else if(intMonth==3) mon="March";
        else if(intMonth==4) mon="April";
        else if(intMonth==5) mon="May";
        else if(intMonth==6) mon="June";
        else if(intMonth==7) mon="July";
        else if(intMonth==8) mon="August";
        else if(intMonth==9) mon="September";
        else if(intMonth==10) mon="October";
        else if(intMonth==11) mon="November";
        else if(intMonth==12) mon="December";
        return mon;
    }
}