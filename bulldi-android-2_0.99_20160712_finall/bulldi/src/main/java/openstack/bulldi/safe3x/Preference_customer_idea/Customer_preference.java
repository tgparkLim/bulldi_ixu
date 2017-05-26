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
 * Customer_preference.java: Customer preference control
 */

package openstack.bulldi.safe3x.Preference_customer_idea;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import openstack.bulldi.common.SpinnerModel;
import openstack.bulldi.safe3x.Alarm.GMailSender;
import openstack.bulldi.safe3x.BuildConfig;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Login.Bulldi_rule;
import openstack.bulldi.safe3x.Login.Greeting;
import openstack.bulldi.safe3x.Login.KakaoSignupActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;
import openstack.util.EditTextPlus;

import java.util.ArrayList;


public class Customer_preference extends Activity {
    final Context context = this;
    public ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
    CustomAdapterSpinner adapter;
    Customer_preference activity = null;
    public  Button customer_submit;
    public  EditTextPlus customer_text;
    public  String content;
    public  String recipients;
//    public  Spinner Spinner_Idea;
//    public  String spinner_text;
    public static boolean is_customer=false;
    public static View message_setting_back;
    public TextViewPlus customer_title;

    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_idea);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customer_titlebar);
        message_setting_back = findViewById(R.id.customer_back);
        message_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        customer_title=(TextViewPlus) findViewById(R.id.customer_title);
        customer_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activity = this;
        customer_submit = (Button) findViewById(R.id.customer_submit);
        customer_text = (EditTextPlus) findViewById(R.id.text_custom_idea);
        customer_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customer_submit.setBackgroundResource(R.drawable.button_on);
                customer_submit.setTextColor(Color.BLACK);
                content = customer_text.getText().toString();
                if (content.length() == 0) {
                    showToast(getResources().getString(R.string.customer_idea_warning));
                    customer_submit.setBackgroundResource(R.drawable.button_off);
                    customer_submit.setTextColor(Color.WHITE);
                }
                else {
                    recipients = "bulldi@openstack.co.kr";
                   final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                    if (activeNetwork != null && activeNetwork.isConnected()) {
                        // notify user you are online
                        new SendEmailAsyncTask().execute();
                        Toast toast= Toast.makeText(Customer_preference.this, getResources().getString(R.string.toast_sent_email), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                        dialog.setContentView(R.layout.customer_idea_dialog);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.CENTER;
                        window.setAttributes(wlp);
                        final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_custom_close);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogButton.setBackgroundResource(R.drawable.button_on);
                                dialogButton.setTextColor(Color.BLACK);
                                final Handler handler_reconnect = new Handler();
                                handler_reconnect.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        if(message_setting_back!=null) message_setting_back.callOnClick();
                                    }
                                }, 10);
                                //dialog.dismiss();
                                customer_submit.setBackgroundResource(R.drawable.button_off);
                                customer_submit.setTextColor(Color.WHITE);
                            }
                        });
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.show();

                    } else {
                        // notify user you are not online
                        Toast toast =Toast.makeText(Customer_preference.this, getResources().getString(R.string.toast_sent_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                        centerText(toast.getView());
                        toast.show();
                        customer_submit.setBackgroundResource(R.drawable.button_off);
                        customer_submit.setTextColor(Color.WHITE);
                    }
                }
            }
        });
    }

    /******
     * Function to set data in ArrayList
     *************/
    public void setListData() {
        final SpinnerModel sched = new SpinnerModel();
        sched.setIdea(getResources().getString(R.string.customer_idea_spinner_1));
        CustomListViewValuesArr.add(sched);
        final SpinnerModel sched_1 = new SpinnerModel();
        sched_1.setIdea(getResources().getString(R.string.customer_idea_spinner_2));
        CustomListViewValuesArr.add(sched_1);
        final SpinnerModel sched_2 = new SpinnerModel();
        sched_2.setIdea(getResources().getString(R.string.customer_idea_spinner_3));
        CustomListViewValuesArr.add(sched_2);
        final SpinnerModel sched_3 = new SpinnerModel();
        sched_3.setIdea(getResources().getString(R.string.customer_idea_spinner_4));
        CustomListViewValuesArr.add(sched_3);
        final SpinnerModel sched_4 = new SpinnerModel();
        sched_4.setIdea(getResources().getString(R.string.customer_idea_spinner_5));
        CustomListViewValuesArr.add(sched_4);
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {


        public SendEmailAsyncTask() {
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
            String send_name;
            String send_auth;
            String send_from;
            String password;
            String phone_num;
            String subject;

            send_auth="openstack.bulldi@gmail.com";
            password="openstack";
            String to_recieve="bulldi@openstack.co.kr";
            TelephonyManager tMgr = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            phone_num = tMgr.getLine1Number();
            send_from="openstack.bulldi@gmail.com";
            if(Bulldi_rule.login_facbook==true){
                subject="Customer suggestion from: "+phone_num+ " with Facebook ID: "+Bulldi_rule.user_id_facebook+" Email: "+Bulldi_rule.user_mail_facebook;
            }
            else if(Bulldi_rule.login_instagram==true){
                subject="Customer suggestion from: "+phone_num+ " with Instagram ID: "+Bulldi_rule.user_id_instagram+" Nickname: "+Bulldi_rule.user_name_instagram;
            }
            else  if(KakaoSignupActivity.login_kakaotalk==true){
                subject="Customer suggestion from: "+phone_num+ " with Kakaotalk ID: "+ KakaoSignupActivity.userID_kakaotalk+" Nickname: "+KakaoSignupActivity.user_nickname_kakaotalk;
            }
            else {
                subject="Customer suggestion from: "+phone_num+ " with User name: "+ Greeting.username+" Email: "+Greeting.usermail;
            }
            GMailSender sender = new GMailSender(send_auth, password);
            try {
                try {
                    sender.sendMail(subject,
                            content,
                            send_from,
                            to_recieve);

                    boolean send_result=true;
                    return  send_result;
                } catch (Exception e) {
                    return false;
                }
               // return true;
            }
             catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.i("Check result","value: "+result);
            //if (processVisibility) {
            if (result) {
/*                Toast toast= Toast.makeText(Customer_preference.this, getResources().getString(R.string.toast_sent_email), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                if(message_setting_back!=null) message_setting_back.callOnClick();*/
            } else {
/*                Toast toast =Toast.makeText(Customer_preference.this, getResources().getString(R.string.toast_sent_error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();*/
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        is_customer=true;
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
        //if((DeviceActivity.isAlarm==true)&&(message_setting_back!=null)) message_setting_back.callOnClick();
    }
    @Override
    public void onPause() {
        super.onPause();
        is_customer=false;
    }
    private void showToast(String msg) {
        Toast toast= Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        centerText(toast.getView());
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
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
}
