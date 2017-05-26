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
 * LanguageHelper.java: Language setup
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;


public class LanguageHelper {
    public  static void changeLocale(Resources res, String locale){
        Configuration config;
        config=new Configuration(res.getConfiguration());
        switch (locale){
            case "ko":
                config.locale=new Locale("ko");
                break;
            case "en":
                config.locale=Locale.ENGLISH;
                break;
            default:
                config.locale=Locale.ENGLISH;
                break;
        }
        res.updateConfiguration(config,res.getDisplayMetrics());
    }
}
