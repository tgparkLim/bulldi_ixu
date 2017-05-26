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
 * Custom_preference_VoiceMessage.java: Alam Sound preference show
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


public class Custom_preference_VoiceMessage extends Preference
{
    public static TextViewPlus voice_message;

    public Custom_preference_VoiceMessage (Context context) {
        super(context);
    }

    public Custom_preference_VoiceMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        View v = super.getView(convertView, parent);
        voice_message=(TextViewPlus) v.findViewById(R.id.voice_message);
        String s= Message_content_preference.show_message;
        Log.i("Check voice", "value at getView: " + s);
        voice_message.setText(s);

        return v;
    }

}