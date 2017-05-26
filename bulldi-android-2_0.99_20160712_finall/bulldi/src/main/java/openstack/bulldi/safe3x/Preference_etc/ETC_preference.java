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
 * ETC_preference.java; ETC preference control
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import openstack.bulldi.safe3x.Alarm.Alarm;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;



public class ETC_preference extends Activity {
    public static ImageView etc_setting_back;
    public TextViewPlus etc_title;
    public ListView lv;
    public  String [] prgmNameList={"My bulldi","Language","Unit change","Legal information","Version","Session out"};
    public  int [] prgmImages={R.drawable.arrow_list,R.drawable.arrow_list,R.drawable.arrow_list,R.drawable.arrow_list,R.drawable.arrow_list,R.drawable.arrow_list};
    public static boolean is_etc=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etc_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.etc_title_bar);
        prgmNameList=new String[]{getResources().getString(R.string.etc_item_6),getResources().getString(R.string.etc_item_1),getResources().getString(R.string.etc_item_2),getResources().getString(R.string.etc_item_3),getResources().getString(R.string.etc_item_4),getResources().getString(R.string.etc_item_5)};
        etc_setting_back= (ImageView)findViewById(R.id.etc_back);
        etc_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etc_title=(TextViewPlus) findViewById(R.id.etc_title);
        etc_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv=(ListView) findViewById(R.id.list_etc);
        lv.setAdapter(new ETC_Adapter(ETC_preference.this, prgmNameList, prgmImages));

    }
    public class ETC_Adapter extends BaseAdapter {
        String [] result;
        Context context;
        int [] imageId;
        private LayoutInflater inflater=null;
        public ETC_Adapter(ETC_preference mainActivity, String[] prgmNameList, int[] prgmImages) {
            // TODO Auto-generated constructor stub
            result=prgmNameList;
            context=mainActivity;
            imageId=prgmImages;
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
            TextViewPlus tv;
            ImageView img;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.etc_list, null);
            holder.tv=(TextViewPlus) rowView.findViewById(R.id.etc_tv);
            holder.img=(ImageView) rowView.findViewById(R.id.etc_img);
            holder.tv.setText(result[position]);
            holder.img.setImageResource(imageId[position]);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(context, "You choose: " + result[position], Toast.LENGTH_SHORT).show();
                    if (position == 0) {
                        Intent i=new Intent(ETC_preference.this, Alias.class);
                        startActivity(i);
                    } else if (position == 1) {
                        Intent i=new Intent(ETC_preference.this, Language_setting.class);
                        startActivity(i);
                    } else if (position == 2) {
                        Intent i=new Intent(ETC_preference.this, Unit_setting.class);
                        startActivity(i);
                    } else if (position == 3) {
                        Intent i=new Intent(ETC_preference.this, Legal_information.class);
                        startActivity(i);
                    } else if (position == 4){
                        Intent i=new Intent(ETC_preference.this, Version.class);
                        startActivity(i);
                    } else {
                        Intent i=new Intent(ETC_preference.this, Session_out.class);
                        startActivity(i);
                    }
                }
            });
            return rowView;
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        is_etc=true;
        //if((DeviceActivity.isAlarm==true)&&(etc_setting_back!=null)) etc_setting_back.callOnClick();
    }
    @Override
    public void onPause(){
        super.onPause();
        is_etc=false;
    }

}
