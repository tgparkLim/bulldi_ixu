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
 * Phone.java: control phone call when alarm happens
 */

package openstack.bulldi.safe3x.Alarm;

import android.net.Uri;
import android.content.Context;
import android.content.Intent;

import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.PreferencesActivity;


public class Phone extends PreferencesActivity {

/*    private EditText number;*/

    public void call(final Context context) {
        String dest="119";
        String uri = "tel:" + dest;
        //final Intent callIntent = new Intent(Intent.ACTION_CALL);
        final Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(uri));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Dial
        final Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        if (DeviceActivity.getCallMessage(context)) {
            Music.stop(context);
            context.startActivity(callIntent);
        }
    }
    public void dial(final Context context) {
        String dest = "";
        String uri = "tel:" + dest;

        //Dial
        final Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        if (DeviceActivity.getCallMessage(context)) {
            Music.stop(context);
            context.startActivity(dialIntent);
        }
    }

}
