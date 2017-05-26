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
 * CustomAdapter.java: alarm sound selection control
 */

package openstack.bulldi.safe3x.Preference_sharing;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;

public class CustomAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId_1;
    int [] imageId_2;
    public static Holder holder1;
    public static Holder holder2;
    public static Holder holder3;
    public static Holder holder4;
    public static View row1;
    public static View row2;
    public static View row3;
    public static View row4;
    private static LayoutInflater inflater=null;
    public CustomAdapter(Message_content_preference mainActivity, String[] prgmNameList, int[] prgmImages_1, int[] prgmImages_2) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId_1=prgmImages_1;
        imageId_2=prgmImages_2;
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
//        ImageView img_1;
        TextViewPlus tv;
        ImageView img_2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.message_list1, null);
        holder.tv=(TextViewPlus) rowView.findViewById(R.id.message_tv);
        holder.img_2=(ImageView) rowView.findViewById(R.id.message_img2);
        holder.tv.setText(result[position]);
        holder.img_2.setImageResource(imageId_2[position]);
        if((Message_content_preference.show_message.compareTo(context.getResources().getString(R.string.message_content_item_1)) == 0) && (position==0)){
            rowView.setBackgroundResource(R.color.PowderBlue);
            row1=rowView;
            holder1=holder;
        } else if((Message_content_preference.show_message.compareTo(context.getResources().getString(R.string.message_content_item_2)) == 0) && (position==1)){
            rowView.setBackgroundResource(R.color.PowderBlue);
            row2=rowView;
            holder2=holder;
        } else if((Message_content_preference.show_message.compareTo(context.getResources().getString(R.string.message_content_item_3)) == 0) && (position==2)){
            rowView.setBackgroundResource(R.color.PowderBlue);
            row3=rowView;
            holder3=holder;
        } else if((Message_content_preference.show_message.compareTo(context.getResources().getString(R.string.message_content_item_4)) == 0) && (position==3)){
            rowView.setBackgroundResource(R.color.PowderBlue);
            row4=rowView;
            holder4=holder;
        }
        Log.i("Check audio", "value1: " + row1 +  ";" + row2 + ";"  + row3 + ";" + row4 );
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message_content_preference.show_message = result[position];
                if(row1!=null)row1.setBackgroundResource(R.color.White);
                if(row2!=null)row2.setBackgroundResource(R.color.White);
                if(row3!=null)row3.setBackgroundResource(R.color.White);
                if(row4!=null)row4.setBackgroundResource(R.color.White);
                Log.i("Check audio", "value2: " + row1 +  ";" + row2 + ";"  + row3 + ";" + row4 );
                if (position == 0) {
                    Music.play(context, R.raw.siren);
                    holder1 = holder;
                    holder1.img_2.setImageResource(R.drawable.play_emergency_on);
                    if (holder2 != null)holder2.img_2.setImageResource(R.drawable.play_emergency_off);
                    if (holder3 != null)holder3.img_2.setImageResource(R.drawable.play_emergency_off);
                    if (holder4 !=null)holder4.img_2.setImageResource(R.drawable.play_emergency_off);
                    row1=rowView;
                    row1.setBackgroundResource(R.color.PowderBlue);
                    if (row2 != null) row2.setBackgroundResource(R.color.White);
                    if (row3 != null) row3.setBackgroundResource(R.color.White);
                    if (row4 != null) row4.setBackgroundResource(R.color.White);
                } else if (position == 1) {
                    Music.play(context, R.raw.fire_car);
                    holder2 = holder;
                    holder2.img_2.setImageResource(R.drawable.play_emergency_on);
                    if (holder1 != null)holder1.img_2.setImageResource(R.drawable.play_emergency_off);
                    if (holder3 != null)holder3.img_2.setImageResource(R.drawable.play_emergency_off);
                    if(holder4!=null)holder4.img_2.setImageResource(R.drawable.play_emergency_off);
                    row2=rowView;
                    row2.setBackgroundResource(R.color.PowderBlue);
                    if (row1 != null) row1.setBackgroundResource(R.color.White);
                    if (row3 != null) row3.setBackgroundResource(R.color.White);
                    if (row4 != null) row4.setBackgroundResource(R.color.White);
                } else if (position == 2) {
                    Music.play(context, R.raw.emergency1);
                    holder3 = holder;
                    holder3.img_2.setImageResource(R.drawable.play_emergency_on);
                    if (holder1 != null)holder1.img_2.setImageResource(R.drawable.play_emergency_off);
                    if (holder2 != null)holder2.img_2.setImageResource(R.drawable.play_emergency_off);
                    if(holder4!=null)holder4.img_2.setImageResource(R.drawable.play_emergency_off);
                    row3=rowView;
                    row3.setBackgroundResource(R.color.PowderBlue);
                    if (row1 != null) row1.setBackgroundResource(R.color.White);
                    if (row2 != null) row2.setBackgroundResource(R.color.White);
                    if (row4 != null) row4.setBackgroundResource(R.color.White);
                } else {
                    Music.play(context, R.raw.emergency2);
                    holder4 = holder;
                    holder4.img_2.setImageResource(R.drawable.play_emergency_on);
                    if (holder1 != null)holder1.img_2.setImageResource(R.drawable.play_emergency_off);
                    if (holder2 != null)holder2.img_2.setImageResource(R.drawable.play_emergency_off);
                    if(holder3!=null)holder3.img_2.setImageResource(R.drawable.play_emergency_off);
                    row4=rowView;
                    row4.setBackgroundResource(R.color.PowderBlue);
                    if (row1 != null) row1.setBackgroundResource(R.color.White);
                    if (row2 != null) row2.setBackgroundResource(R.color.White);
                    if (row3 != null) row3.setBackgroundResource(R.color.White);
                }
            }
        });
        return rowView;
    }

}
