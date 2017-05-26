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
 * Custom_preference_Notify.java: Notify my friends show
 */

package openstack.bulldi.safe3x.Preference_sharing;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class Custom_preference_Notify  extends Preference
{
    public static TextViewPlus count_contact;

    public Custom_preference_Notify (Context context) {
        super(context);
    }

    public Custom_preference_Notify(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        View v = super.getView(convertView, parent);
        count_contact=(TextViewPlus) v.findViewById(R.id.count_contact);
        String s="";
        if(Notify_friend_preference.contact_size==0) s= getContext().getResources().getString(R.string.setting_notify_status);
        else s=Integer.toString(Notify_friend_preference.contact_size);
        Log.i("Check contact size", "value at getView: "+s);
        count_contact.setText(s);
        return v;
    }


}