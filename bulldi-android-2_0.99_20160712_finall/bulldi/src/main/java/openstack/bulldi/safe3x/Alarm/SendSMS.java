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
 * SendSMS.java: control sending sms when alarm happen
 */

package openstack.bulldi.safe3x.Alarm;

import android.content.Context;
import android.telephony.SmsManager;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Preference_sharing.Message_content_preference;
import openstack.bulldi.safe3x.Preference_sharing.Notify_friend_preference;
import openstack.bulldi.safe3x.PreferencesActivity;
import openstack.bulldi.safe3x.R;

public class SendSMS extends PreferencesActivity {


    public void send_sms(Context context) {
        if(Notify_friend_preference.notify_contact.size()!=0)
            for(int i=0;i<Notify_friend_preference.notify_contact.size();i++) {
            String dest =Notify_friend_preference.notify_contact.get(i);
            String input_text =  DeviceActivity.res.getString(R.string.message_sms);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(dest, null, input_text, null, null);
        }
        else{

        }
    }
}