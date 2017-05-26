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
 * StartAlarmService.java: start alarm service
 */

package openstack.bulldi.safe3x.Device_View;

import android.app.IntentService;
import android.content.Intent;

import openstack.bulldi.safe3x.Alarm.DialogAlarm;


public class StartAlarmService  extends IntentService {
    public StartAlarmService() {
        super("Start Alarm Service");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        Intent i = new Intent(this, DialogAlarm.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}