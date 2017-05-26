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
 * Unit_setting.java: control unit selection
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Preference_etc.Unit_bean;
import openstack.bulldi.safe3x.R;
import openstack.util.CheckBoxImageView;
import openstack.util.TextViewPlus;


public class Unit_setting extends Activity {
    public static ImageView unit_setting_back;
    TextViewPlus unit_title;
    public ListView lv;
    public  MyCustomAdapter dataAdapter = null;
    public ArrayList<Unit_bean> UnitList;
    public  static boolean[] unit_choose={true,false};
    public static boolean is_celsius=true;
    public SharedPreferences sharedPreferences;
    public static boolean is_unit=false;
    public Button celsius_button;
    public Button fahrenheit_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.unit_title_bar);
        unit_setting_back=(ImageView)findViewById(R.id.unit_back);
        unit_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        unit_title=(TextViewPlus) findViewById(R.id.unit_title);
        unit_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        celsius_button=(Button) findViewById(R.id.celsius_button);
        fahrenheit_button=(Button) findViewById(R.id.fahrenheit_button);
        celsius_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_celsius=true;
                unit_choose[0]=true;unit_choose[1]=false;
                celsius_button.setBackgroundResource(R.drawable.unit_c_on);
                fahrenheit_button.setBackgroundResource(R.drawable.unit_f_off);
                finish();
            }
        });
        fahrenheit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_celsius=false;
                unit_choose[0]=false;unit_choose[1]=true;
                celsius_button.setBackgroundResource(R.drawable.unit_c_off);
                fahrenheit_button.setBackgroundResource(R.drawable.unit_f_on);
                finish();
            }
        });
    }
    private class MyCustomAdapter extends ArrayAdapter<Unit_bean> {

        private ArrayList<Unit_bean> UnitList_bean;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Unit_bean> UnitList) {
            super(context, textViewResourceId, UnitList);
            this.UnitList_bean = new ArrayList<Unit_bean>();
            this.UnitList_bean.addAll(UnitList);
        }

        private class ViewHolder {
            TextViewPlus tv;
            CheckBoxImageView check;
            //CheckBox check;
        }
        public void set_checkbox(boolean[] x){
            if(x.length!=0) for(int i=0;i<x.length;i++){
                UnitList.get(i).selected=x[i];
            }

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.unit_list, null);

                holder = new ViewHolder();
                holder.tv=(TextViewPlus) convertView.findViewById(R.id.unit_tv);
                holder.check=(CheckBoxImageView) convertView.findViewById(R.id.unit_checkbox);
                //holder.check=(CheckBox) convertView.findViewById(R.id.unit_checkbox);
                convertView.setTag(holder);

                holder.check.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        //CheckBox cb = (CheckBox) v ;
                        CheckBoxImageView cb = (CheckBoxImageView) v ;
                        Unit_bean bean = (Unit_bean) cb.getTag();
                        if(position==0){
                            //if(bean.isSelected()==false) bean.setSelected(true);
                            unit_choose[0]=true;unit_choose[1]=false;
                            is_celsius=true;
                            dataAdapter.set_checkbox(unit_choose);
                            lv.setAdapter(dataAdapter);
                        }
                        if(position==1){
                            //if(bean.isSelected()==false) bean.setSelected(true);
                            unit_choose[0]=false;unit_choose[1]=true;
                            is_celsius=false;
                            dataAdapter.set_checkbox(unit_choose);
                            lv.setAdapter(dataAdapter);
                        }
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Unit_bean bean = UnitList_bean.get(position);
            holder.tv.setText(bean.getTitle());
            holder.check.setChecked(bean.isSelected());
            holder.check.setTag(bean);

            return convertView;

        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        is_unit=false;
        //Save unit state
        SharedPreferences[] prefs_unit_state=new SharedPreferences[2];
        SharedPreferences.Editor[] editor_unit_state= new SharedPreferences.Editor[5];
        for(int i=0;i<2;i++) {
            prefs_unit_state[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_unit_state[i] = prefs_unit_state[i].edit();
            editor_unit_state[i].putBoolean("unit_" + Integer.toString(i), unit_choose[i]);
            editor_unit_state[i].commit(); //important, otherwise it wouldn't save.
        }


    }
    @Override
    protected void onResume() {
    super.onResume();
        is_unit=true;
        //if((DeviceActivity.isAlarm==true)&&(unit_setting_back!=null)) unit_setting_back.callOnClick();
    //Get unit setup back
     for (int i = 0; i < 2; i++) {
         if(i==0) {
             sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
             boolean state = sharedPreferences.getBoolean("unit_" + Integer.toString(i), true);
             unit_choose[i] = state;
         }
         else{
             sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
             boolean state = sharedPreferences.getBoolean("unit_" + Integer.toString(i), false);
             unit_choose[i] = state;
         }
     }
     //Set unit mode
     if(unit_choose[0]==true) is_celsius=true;
     else is_celsius=false;
        if(celsius_button!=null && fahrenheit_button!=null){
            if(is_celsius==true){
                celsius_button.setBackgroundResource(R.drawable.unit_c_on);
                fahrenheit_button.setBackgroundResource(R.drawable.unit_f_off);
            }else{
                celsius_button.setBackgroundResource(R.drawable.unit_c_off);
                fahrenheit_button.setBackgroundResource(R.drawable.unit_f_on);
            }
        }
    }


}
