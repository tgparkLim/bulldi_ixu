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
 * Bulldi_rule1.java: shows "Terms of Service"
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


public class Bulldi_rule1 extends Activity{
    public  ImageView rule1_back;
    public TextViewPlus legal_information_title_item1;
    WebView webpage_rule1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulldi_rule1);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bulldi_rule1_title);
        webpage_rule1=(WebView) findViewById(R.id.webpage_rule1);
        webpage_rule1.setBackgroundColor(Color.WHITE);
        if(Language_setting.is_english==true) webpage_rule1.loadUrl("file:///android_asset/" + "use_en.html");
        else webpage_rule1.loadUrl("file:///android_asset/" + "use_ko.html");
        rule1_back =(ImageView) findViewById(R.id.bulldi_rule1_back);
        rule1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        legal_information_title_item1=(TextViewPlus) findViewById(R.id.legal_information_title_item1);
        legal_information_title_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
