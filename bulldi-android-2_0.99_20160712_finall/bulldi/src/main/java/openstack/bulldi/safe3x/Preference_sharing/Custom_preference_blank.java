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
 * Custom_preference_blank.java: blank preference (show nothing)
 */

package openstack.bulldi.safe3x.Preference_sharing;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import openstack.util.TextViewPlus;


public class Custom_preference_blank extends Preference {
    public static TextViewPlus show_light_state;

    public Custom_preference_blank(Context context) {
        super(context);
    }

    public Custom_preference_blank(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



}


