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
 * PreferenceFragment.java: Preference fragement control
 */

package openstack.bulldi.safe3x;

import java.util.Locale;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import openstack.bulldi.safe3x.Device_View.Sensor;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.Preference_history.Custom_preference_Warning;
import openstack.bulldi.safe3x.Preference_history.History_preference;
import openstack.bulldi.safe3x.Preference_lighting.Custom_preference_Lighting;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.Preference_sharing.Custom_preference_Notify;
import openstack.bulldi.safe3x.Preference_sharing.Custom_preference_VoiceMessage;
import openstack.bulldi.safe3x.Preference_sharing.Message_content_preference;
import openstack.bulldi.safe3x.Preference_sharing.Notify_friend_preference;

public class PreferencesFragment extends PreferenceFragment  {

    private static final String TAG = "PreferencesFragment";
    private PreferencesListener preferencesListener;
    public SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "created");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferencesListener = new PreferencesListener(getActivity(), prefs, this);
        prefs.registerOnSharedPreferenceChangeListener(preferencesListener);
    }


    public boolean isEnabledByPrefs(final Sensor sensor) {
        String preferenceKeyString = "pref_" + sensor.name().toLowerCase(Locale.ENGLISH) + "_on";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!prefs.contains(preferenceKeyString)) {
            //throw new RuntimeException("Programmer error, could not find preference with key " + preferenceKeyString);
            return false;
        }

        return prefs.getBoolean(preferenceKeyString, true);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(Custom_preference_Notify.count_contact!=null){
            String s="";
            if(Notify_friend_preference.contact_size==0) s=getResources().getString(R.string.setting_notify_status);
            else s=Integer.toString(Notify_friend_preference.contact_size);
            Log.i("Check contact size", "value: " + s);
            Custom_preference_Notify.count_contact.setText(s);
        }
        if(Custom_preference_VoiceMessage.voice_message!=null){
            String s= Message_content_preference.show_message;
            Custom_preference_VoiceMessage.voice_message.setText(s);
        }
        if(Custom_preference_Lighting.show_light_state!=null){
            String s="";
            if(Lighting_preference.light_on){
                if(Lighting_preference.sleep_on[0]==true) s=getResources().getString(R.string.setting_light_status_on);
                else if(Lighting_preference.sleep_on[1]==true) s=getResources().getString(R.string.setting_light_status_on_5);
                else if(Lighting_preference.sleep_on[2]==true) s=getResources().getString(R.string.setting_light_status_on_10);
                else if(Lighting_preference.sleep_on[3]==true) s=getResources().getString(R.string.setting_light_status_on_20);
                else  s=getResources().getString(R.string.setting_light_status_on_30);}
            else s=getResources().getString(R.string.setting_light_status_off);
            Custom_preference_Lighting.show_light_state.setText(s);
        }
        if(Custom_preference_Warning.history_count!=null){
            //String s=Integer.toString(History_preference.count);
            String s =String.valueOf(History_preference.HistoryList.size());
            Custom_preference_Warning.history_count.setText(s);
        }
    }
}
