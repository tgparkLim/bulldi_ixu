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
 * Custom_preference_Lighting.java: customer suggestion preference show
 */

package openstack.bulldi.safe3x.Preference_lighting;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class Custom_preference_Lighting  extends Preference
{
    public static TextViewPlus show_light_state;

    public Custom_preference_Lighting (Context context) {
        super(context);
    }

    public Custom_preference_Lighting(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        View v = super.getView(convertView, parent);
        show_light_state=(TextViewPlus) v.findViewById(R.id.show_light_state);
        String s="";
        if(Lighting_preference.light_on){
        if(Lighting_preference.sleep_on[0]==true) s=getContext().getResources().getString(R.string.setting_light_status_on);
        else if(Lighting_preference.sleep_on[1]==true) s=getContext().getResources().getString(R.string.setting_light_status_on_5);
        else if(Lighting_preference.sleep_on[2]==true) s=getContext().getResources().getString(R.string.setting_light_status_on_10);
        else if(Lighting_preference.sleep_on[3]==true) s=getContext().getResources().getString(R.string.setting_light_status_on_20);
        else  s=getContext().getResources().getString(R.string.setting_light_status_on_30);}
        else s=getContext().getResources().getString(R.string.setting_light_status_off);
        show_light_state.setText(s);
        return v;
    }


}