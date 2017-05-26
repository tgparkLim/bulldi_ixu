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
 * PreferenceActivity.java: Preference control
 */

package openstack.bulldi.safe3x;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;

import openstack.bulldi.safe3x.Device_View.BatteryInformationServiceProfile;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.util.TextViewPlus;

public class PreferencesActivity extends PreferenceActivity {
    static public TextViewPlus battery_value;
    static public TextViewPlus battery_status;
    //static public TextViewPlus show_light_state;
    public SharedPreferences sharedPreferences;
    public static boolean is_preference=false;
    public static View setting_back;
    public TextViewPlus setting;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
     super.onCreate(savedInstanceState);
      //addPreferencesFromResource(R.xml.preferences);

//      getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.preference_title_bar);
      getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.setting_title_bar);
//      setting_back= findViewById(R.id.settting_back);
      setting_back= findViewById(R.id.setting_back);
      setting_back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });

//      setting=(TextViewPlus) findViewById(R.id.setting);
      setting=(TextViewPlus) findViewById(R.id.setting_title);
      setting.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });
    
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
    // Respond to the action bar's Up/Home button
    case android.R.id.home:
      onBackPressed();
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }
  
  @Override
  public boolean isValidFragment(String fragmentName) {
  	return true;
  }
    @Override
    public void onResume(){
        super.onResume();
        is_preference=true;
    }
    @Override
    public void onPause() {
        super.onPause();
        is_preference = false;
    }
}
