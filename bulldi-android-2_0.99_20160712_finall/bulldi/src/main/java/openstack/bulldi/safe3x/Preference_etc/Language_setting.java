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
 * Language_setting.java: control language selection
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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
import openstack.bulldi.safe3x.PreferencesActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.CheckBoxImageView;
import openstack.util.TextViewPlus;


public class Language_setting extends Activity {
    public static ImageView language_setting_back;
    public TextViewPlus language_title;
    private Resources res = null;
    public ListView lv;
    public  MyCustomAdapter dataAdapter = null;
    public ArrayList<Language_bean> LanguageList;
    public static boolean[] language_choose={true,false};
    //public static boolean[] language_choose={true};
    public static boolean is_language=false;
    public static boolean is_english=true;
    public SharedPreferences sharedPreferences;
    Button korean_button;
    Button english_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.language_title_bar);
        res = this.getResources();
        language_setting_back=(ImageView) findViewById(R.id.language_back);
        language_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        language_title=(TextViewPlus) findViewById(R.id.language_title);
        language_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        korean_button=(Button) findViewById(R.id.korean_button);
        english_button=(Button) findViewById(R.id.english_button);
        korean_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_english=false;
                language_choose[0]=true;language_choose[1]=false;
                korean_button.setBackgroundResource(R.drawable.test_on);
                english_button.setBackgroundResource(R.drawable.test_off);
                LanguageHelper.changeLocale(res, "ko");
                Language_setting.language_setting_back.callOnClick();
                Language_setting.is_language=false;
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ETC_preference.etc_setting_back.callOnClick();
                        ETC_preference.is_etc = false;
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PreferencesActivity.setting_back.callOnClick();
                                PreferencesActivity.is_preference = false;
                            }
                        }, 10);
                    }
                }, 10);
            }
        });
        english_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_english=true;
                language_choose[0]=false;language_choose[1]=true;
                korean_button.setBackgroundResource(R.drawable.test_off);
                english_button.setBackgroundResource(R.drawable.test_on);
                Language_setting.language_setting_back.callOnClick();
                Language_setting.is_language=false;
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LanguageHelper.changeLocale(res, "en");
                        ETC_preference.etc_setting_back.callOnClick();
                        ETC_preference.is_etc = false;
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PreferencesActivity.setting_back.callOnClick();
                                PreferencesActivity.is_preference = false;
                            }
                        }, 10);
                    }
                }, 10);
            }
        });
    }
    private class MyCustomAdapter extends ArrayAdapter<Language_bean> {

        private ArrayList<Language_bean> LanguageList_bean;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Language_bean> LanguageList) {
            super(context, textViewResourceId, LanguageList);
            this.LanguageList_bean = new ArrayList<Language_bean>();
            this.LanguageList_bean.addAll(LanguageList);
        }

        private class ViewHolder {
            TextViewPlus tv;
            CheckBoxImageView check;
            //CheckBox check;
        }
        public void set_checkbox(boolean[] x){
            if(x.length!=0) for(int i=0;i<x.length;i++){
                LanguageList.get(i).selected=x[i];
            }

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.language_list, null);

                holder = new ViewHolder();
                holder.tv=(TextViewPlus) convertView.findViewById(R.id.language_tv);
                //holder.check=(CheckBox) convertView.findViewById(R.id.language_checkbox);
                holder.check=(CheckBoxImageView) convertView.findViewById(R.id.language_checkbox);
                convertView.setTag(holder);

                holder.check.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {

                        //CheckBox cb = (CheckBox) v ;
                        CheckBoxImageView cb = (CheckBoxImageView) v ;
                        Language_bean bean = (Language_bean) cb.getTag();
                        if(position==0){
                            if(bean.isSelected()==false) bean.setSelected(true);
                            language_choose[0]=true;language_choose[1]=false;
                            is_english=true;
                            dataAdapter.set_checkbox(language_choose);
                            lv.setAdapter(dataAdapter);
                            LanguageHelper.changeLocale(res, "en");
                            Language_setting.language_setting_back.callOnClick();
                            Language_setting.is_language=false;
                            final Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ETC_preference.etc_setting_back.callOnClick();
                                    ETC_preference.is_etc = false;
                                    final Handler handler2 = new Handler();
                                    handler2.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            PreferencesActivity.setting_back.callOnClick();
                                            PreferencesActivity.is_preference = false;
                                        }
                                    }, 10);
                                }
                            }, 10);
                        }
                       if(position==1){
                            if(bean.isSelected()==false) bean.setSelected(true);
                            language_choose[0]=false;language_choose[1]=true;
                           is_english=false;
                            dataAdapter.set_checkbox(language_choose);
                            lv.setAdapter(dataAdapter);
                           LanguageHelper.changeLocale(res, "ko");
                           Language_setting.language_setting_back.callOnClick();
                           Language_setting.is_language=false;
                           final Handler handler1 = new Handler();
                           handler1.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   ETC_preference.etc_setting_back.callOnClick();
                                   ETC_preference.is_etc = false;
                                   final Handler handler2 = new Handler();
                                   handler2.postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           PreferencesActivity.setting_back.callOnClick();
                                           PreferencesActivity.is_preference = false;
                                       }
                                   }, 10);
                               }
                           }, 10);
                        }
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Language_bean bean = LanguageList_bean.get(position);
            holder.tv.setText(bean.getTitle());
            holder.check.setChecked(bean.isSelected());
            holder.check.setTag(bean);

            return convertView;

        }

    }
    @Override
    public void onResume(){
        super.onResume();
        is_language=true;
        //if((DeviceActivity.isAlarm==true)&&(language_setting_back!=null)) language_setting_back.callOnClick();
        //Get language setup back
        for (int i = 0; i < 2; i++) {
            if(i==0) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean state = sharedPreferences.getBoolean("language_" + Integer.toString(i), false);
                language_choose[i] = state;
            }
            else{
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean state = sharedPreferences.getBoolean("language_" + Integer.toString(i), true);
                language_choose[i] = state;
            }
        }
        //Set language mode
        if(language_choose[1]==true) is_english=true;
        else is_english=false;
        if(korean_button!=null && english_button!=null){
            if(is_english==true){
                korean_button.setBackgroundResource(R.drawable.test_off);
                english_button.setBackgroundResource(R.drawable.test_on);
            }else{
                korean_button.setBackgroundResource(R.drawable.test_on);
                english_button.setBackgroundResource(R.drawable.test_off);
            }
        }
/*        dataAdapter.set_checkbox(language_choose);
        lv.setAdapter(dataAdapter);*/
    }
    @Override
    public void onPause(){
        super.onPause();
        is_language=false;
        //Save language
        SharedPreferences[] prefs_language=new SharedPreferences[2];
        SharedPreferences.Editor[] editor_language= new SharedPreferences.Editor[2];
        for(int i=0;i<2;i++) {
            prefs_language[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_language[i] = prefs_language[i].edit();
            editor_language[i].putBoolean("language_" + Integer.toString(i), language_choose[i]);
            editor_language[i].commit(); //important, otherwise it wouldn't save.
        }
    }

}
