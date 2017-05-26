
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
 * StopAlarmService.java: stop alarm service
 */
package openstack.bulldi.safe3x.Device_View;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import openstack.bulldi.safe3x.Alarm.Alarm;
import openstack.bulldi.safe3x.Alarm.DialogAlarm;
import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.Preference_lighting.Listview_light;
import openstack.bulldi.safe3x.R;

public class StopAlarmService extends IntentService {
    public StopAlarmService() {
        super("Stop Alarm Service");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        if(Alarm.mActivity!=null) {
            final FragmentTransaction ft = Alarm.mActivity.getSupportFragmentManager().beginTransaction();
            final Fragment prev_alarm = Alarm.mActivity.getSupportFragmentManager().findFragmentByTag("fragment_alarm");

            if (prev_alarm != null) {
                Music.stop(getApplication());
                ft.remove(prev_alarm);

                ft.commitAllowingStateLoss();

                DeviceActivity.isAlarm = false;

                Lighting_preference.light_on = false;
                Alarm.mActivity.finish();
            }
        }
        else {
            //Do nothing
        }
    }
}