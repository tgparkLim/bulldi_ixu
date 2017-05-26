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
 * Legal_information.java: control showing legal information
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;



public class Legal_information extends ViewPagerActivity_legal {
    private static Legal_information mThis = null;
    private Rule1 rule1 = null;
    private Rule2 rule2 = null;
    private Rule3 rule3 = null;
    public static ImageView legal_setting_back;
    TextViewPlus legal_information_title;
    public static boolean is_legal=false;
    public Legal_information() {
        mThis = this;
        mResourceFragmentPager = R.layout.fragment_pager_3;
        mResourceIdPager = R.id.pager_3;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE | Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.legal_title_bar);
        getActionBar().show();
        legal_setting_back = (ImageView) findViewById(R.id.legal_back);
        legal_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        legal_information_title=(TextViewPlus) findViewById(R.id.legal_information_title);
        legal_information_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rule1 = new Rule1();
        if(Language_setting.is_english==true) rule1.setParameters("use_en.html", R.layout.fragment_use, R.id.webpage_use);
        else rule1.setParameters("use_ko.html", R.layout.fragment_use, R.id.webpage_use);
        rule2 = new Rule2();
        if(Language_setting.is_english==true) rule2.setParameters("privacy_en.html", R.layout.fragment_privacy, R.id.webpage_privacy);
        else rule2.setParameters("privacy_ko.html", R.layout.fragment_privacy, R.id.webpage_privacy);
        rule3 = new Rule3();
        if(Language_setting.is_english==true) rule3.setParameters("location_en.html", R.layout.fragment_location, R.id.webpage_location);
        else rule3.setParameters("location_ko.html", R.layout.fragment_location, R.id.webpage_location);

        String title_rule1 = getResources().getString(R.string.rule1_title);
        String title_rule2 = getResources().getString(R.string.rule2_title);
        String title_rule3 = getResources().getString(R.string.rule3_title);
		mSectionsPagerAdapter.addSection(rule1, title_rule1);
		mSectionsPagerAdapter.addSection(rule2,title_rule2);
		mSectionsPagerAdapter.addSection(rule3, title_rule3);

    }
    @Override
    public void onResume(){
        super.onResume();
        is_legal=true;
        //if((DeviceActivity.isAlarm==true)&&(legal_setting_back!=null)) legal_setting_back.callOnClick();
    }
    @Override
    public void onPause(){
        super.onPause();
        is_legal=false;
    }
}

