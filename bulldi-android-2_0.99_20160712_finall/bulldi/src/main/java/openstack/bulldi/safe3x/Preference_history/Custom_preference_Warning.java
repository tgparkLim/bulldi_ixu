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
 * Custom_preference_Warning.java; history preference showing
 */

package openstack.bulldi.safe3x.Preference_history;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class Custom_preference_Warning extends Preference {
    public static TextViewPlus history_count;

    public Custom_preference_Warning(Context context) {
        super(context);
    }

    public Custom_preference_Warning(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public View getView(View convertView, ViewGroup parent) {
        View v = super.getView(convertView, parent);
        history_count = (TextViewPlus) v.findViewById(R.id.history_count);
        //String s = Integer.toString(History_preference.count);
        String s;
        if(History_preference.HistoryList!=null) s =String.valueOf(History_preference.HistoryList.size());
        else s="0";
        history_count.setText(s);
        return v;
    }
}
