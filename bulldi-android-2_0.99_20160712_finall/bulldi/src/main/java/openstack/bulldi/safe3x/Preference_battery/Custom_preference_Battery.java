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
 * Custom_preference_Battery.java: battery preference showing
 */

package openstack.bulldi.safe3x.Preference_battery;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import openstack.bulldi.safe3x.Device_View.BatteryInformationServiceProfile;
import openstack.bulldi.safe3x.Preference_sharing.Notify_friend_preference;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;

public class Custom_preference_Battery extends Preference
{
    public static TextViewPlus battery_value;
    public static TextViewPlus battery_status;
    View battery_img;
    public Custom_preference_Battery (Context context) {
        super(context);
    }

    public Custom_preference_Battery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        View v = super.getView(convertView, parent);
        battery_value=(TextViewPlus) v.findViewById(R.id.batery_value);
        battery_status=(TextViewPlus) v.findViewById(R.id.battery_status);
        battery_img=v.findViewById(R.id.battery_change);
        String s="";
        if((BatteryInformationServiceProfile.battery_data<=100) && (BatteryInformationServiceProfile.battery_data>=10)) {
            battery_value.setText(Integer.toString(BatteryInformationServiceProfile.battery_data) + "%");
            if (BatteryInformationServiceProfile.battery_data == 100)
                battery_status.setText(getContext().getResources().getString(R.string.battery_status_full));
            else if ((BatteryInformationServiceProfile.battery_data < 100) && (BatteryInformationServiceProfile.battery_data >= 20))
                battery_status.setText(getContext().getResources().getString(R.string.battery_status_stable));
            else  battery_status.setText(getContext().getResources().getString(R.string.battery_status_low));
        }
        else
        {
            battery_img.setBackgroundResource(R.drawable.battery_warn);
            Typeface font_title_bold= Typeface.createFromAsset(getContext().getAssets(), "Roboto-Bold.ttf");
//            battery_value.setTypeface(font_title_bold);
            battery_value.setText(Integer.toString(BatteryInformationServiceProfile.battery_data) + "%");
            battery_value.setTextColor(getContext().getResources().getColor(R.color.Black));
            battery_status.setText(getContext().getResources().getString(R.string.battery_status_replace));
        }

//        battery_status.setText(s);
        return v;
    }


}