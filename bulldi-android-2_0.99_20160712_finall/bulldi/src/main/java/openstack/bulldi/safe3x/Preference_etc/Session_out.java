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
 * Session_out.java: control quit bulldi's service
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import openstack.bulldi.safe3x.Alarm.GMailSender;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Login.Bulldi_rule;
import openstack.bulldi.safe3x.Login.Greeting;
import openstack.bulldi.safe3x.Login.KakaoSignupActivity;
import openstack.bulldi.safe3x.MainActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.CheckBoxImageView;
import openstack.util.TextViewPlus;


public class Session_out extends Activity {
    public static ImageView session_setting_back;
    TextViewPlus session_title;
    Button out;
    Context context;
    CheckBoxImageView checkbox;
    boolean is_checked=false;
    public static boolean is_session=false;

    SharedPreferences sharedPreferences;
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_out);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.session_out_title_bar);
        context=this;
        session_setting_back =(ImageView) findViewById(R.id.session_back);
        session_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session_title=(TextViewPlus) findViewById(R.id.session_title);
        session_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        out=(Button)findViewById(R.id.session_button);
        checkbox=(CheckBoxImageView) findViewById(R.id.sessionOut_checkbox);
        checkbox.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
            public void onCheckedChanged(View buttonView,
                                         boolean isChecked) {
                is_checked = isChecked;
                if(is_checked==true){
                    out.setBackgroundResource(R.drawable.button_off);
                    out.setTextColor(Color.WHITE);
                }else{
                    out.setBackgroundResource(R.drawable.button_die);
                    out.setTextColor(Color.WHITE);
                }
            }
        });
        out=(Button)findViewById(R.id.session_button);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_checked == true)
                {
                    out.setBackgroundResource(R.drawable.button_on);
                    out.setTextColor(Color.BLACK);
                    new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle(getResources().getString(R.string.waring_sessionOut_alert_title))
                        .setMessage(getResources().getString(R.string.waring_sessionOut_alert_message))
                        .setPositiveButton(getResources().getString(R.string.waring_sessionOut_alert_positive), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                                boolean internet_status = true;
                                if (activeNetwork != null && activeNetwork.isConnected()) {
                                    // notify user you are online
                                    internet_status = true;
                                } else {
                                    // notify user you are not online
                                    internet_status = false;
                                }
                                if (internet_status == true) {
                                    new SendEmailAsyncTask_session_out().execute();
                                    Bulldi_rule.is_checked=false;
                                    //Save agreement
                                    SharedPreferences prefs_agreement = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor_agreement = prefs_agreement.edit();
                                    editor_agreement.putBoolean("rule_agreement", Bulldi_rule.is_checked);
                                    editor_agreement.commit(); //important, otherwise it wouldn't save.

                                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(Session_out.this, getResources().getString(R.string.toast_internet_checking), Toast.LENGTH_LONG);
                                    //toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                    out.setBackgroundResource(R.drawable.button_off);
                                    out.setTextColor(Color.WHITE);
                                }

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.waring_sessionOut_alert_negative), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                out.setBackgroundResource(R.drawable.button_off);
                                out.setTextColor(Color.WHITE);
                            }
                        }).show();
            }
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        is_session=true;
        //Get back login status
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bulldi_rule.login_facbook = sharedPreferences.getBoolean("login_facebook_state", false);
        Bulldi_rule.user_id_facebook=sharedPreferences.getString("login_facebook_id", "");
        Bulldi_rule.user_mail_facebook=sharedPreferences.getString("login_facebook_mail", "");
        //Get back login status
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bulldi_rule.login_instagram = sharedPreferences.getBoolean("login_instagram_state", false);
        Bulldi_rule.user_id_instagram=sharedPreferences.getString("login_instagram_id", "");
        Bulldi_rule.user_name_instagram=sharedPreferences.getString("login_instagram_name", "");
        //Get back login status
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        KakaoSignupActivity.login_kakaotalk = sharedPreferences.getBoolean("login_kakaotalk_state", false);
        KakaoSignupActivity.userID_kakaotalk=sharedPreferences.getLong("login_kakdotalk_id", 0);
        KakaoSignupActivity.user_nickname_kakaotalk=sharedPreferences.getString("login_kakao_name","");
        //Get back login status
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Greeting.login_other = sharedPreferences.getBoolean("login_other_state", false);
        Greeting.username=sharedPreferences.getString("login_other_name", "");
        Greeting.usermail=sharedPreferences.getString("login_other_mail","");
        //if((DeviceActivity.isAlarm==true)&&(session_setting_back!=null)) session_setting_back.callOnClick();
    }
    @Override
    public void onPause(){
        super.onPause();
        is_session=false;
    }
    class SendEmailAsyncTask_session_out extends AsyncTask<Void, Void, Boolean> {


        public SendEmailAsyncTask_session_out() {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");

            String send_auth;
            String send_from;
            String password;
            String subject;
            TelephonyManager tMgr = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String phone_num = tMgr.getLine1Number();
            if(Bulldi_rule.login_facbook==true){
                subject=phone_num+ " with Facebook ID: "+Bulldi_rule.user_id_facebook+" Email: "+Bulldi_rule.user_mail_facebook;
            }
            else if(Bulldi_rule.login_instagram==true){
                subject=phone_num+ " with Instagram ID: "+Bulldi_rule.user_id_instagram+" Nickname: "+Bulldi_rule.user_name_instagram;
            }
            else  if(KakaoSignupActivity.login_kakaotalk==true){
                subject=phone_num+ " with Kakaotalk ID: "+ KakaoSignupActivity.userID_kakaotalk+" Nickname: "+KakaoSignupActivity.user_nickname_kakaotalk;
            }
            else {
                subject=phone_num+ " with User name: "+ Greeting.username+" Email: "+Greeting.usermail;
            }
            String content="User at phone number: "+subject+" want to quit our service. Please remove user out of system";

            send_auth="openstack.bulldi@gmail.com";
            password="openstack";
            String to_recieve="bulldi@openstack.co.kr";
            send_from="openstack.bulldi@gmail.com";
            GMailSender sender = new GMailSender(send_auth, password);
            try {
                try {
                    sender.sendMail("User logout",
                            content,
                            send_from,
                            to_recieve);

                } catch (Exception e) {

                }
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            //if (processVisibility) {
            if (result) {

            } else {

            }
        }
    }
}
