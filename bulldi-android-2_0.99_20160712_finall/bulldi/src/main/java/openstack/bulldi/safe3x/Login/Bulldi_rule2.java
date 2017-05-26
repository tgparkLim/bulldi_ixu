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
 * Bulldi_rule2.java; shows "Privacy Policy"
 */

package openstack.bulldi.safe3x.Login;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class Bulldi_rule2  extends Activity {
    public ImageView rule2_back;
    public TextViewPlus legal_information_title_item2;
    WebView webpage_rule2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulldi_rule2);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bulldi_rule2_title);
        webpage_rule2=(WebView) findViewById(R.id.webpage_rule2);
        webpage_rule2.setBackgroundColor(Color.WHITE);
        if(Language_setting.is_english==true) webpage_rule2.loadUrl("file:///android_asset/" + "privacy_en.html");
        else webpage_rule2.loadUrl("file:///android_asset/" + "privacy_ko.html");
        rule2_back =(ImageView) findViewById(R.id.bulldi_rule2_back);
        rule2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        legal_information_title_item2=(TextViewPlus) findViewById(R.id.legal_information_title_item2);
        legal_information_title_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
