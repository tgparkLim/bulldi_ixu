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
 * Bulldi_rule.java: control login through social api
 */

package openstack.bulldi.safe3x.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import openstack.bulldi.safe3x.BuildConfig;
import openstack.util.CheckBoxImageView;
import openstack.bulldi.safe3x.Alarm.GMailSender;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class Bulldi_rule extends Activity {
     TextViewPlus rule1;
     TextViewPlus rule2;
     TextViewPlus rule3;
    TextViewPlus agree_text;
    CheckBoxImageView checkbox_agree;
    ImageView facebook_login;
    ImageView instagram_login;
    ImageView kakao_login;
    TextViewPlus other_login;
    public static boolean login_facbook=false;
    public static boolean login_instagram=false;

    public static boolean is_checked=false;
    private CallbackManager callbackManager;
    public static String user_mail_facebook="" ;
    public static String user_id_facebook="" ;
    public static String user_name_instagram="" ;
    public static String user_id_instagram="" ;
    public static InstagramApp mApp;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    public static List<String> userInfo_List = new ArrayList<String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
                userInfo_List=mApp.get_information();
                Log.i("Check instagram", "Value: "+userInfo_List.size() );
                if(userInfo_List.size()==2) {
                    new SendEmailAsyncTask_instagram().execute();
                    is_checked=true;
                    login_instagram=true;
                    user_id_instagram=userInfo_List.get(0);
                    user_name_instagram=userInfo_List.get(1);
                    //Save login instagram state
                    SharedPreferences prefs_instagram = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor_instagram = prefs_instagram.edit();
                    editor_instagram.putBoolean("login_instagram_state", login_instagram);
                    editor_instagram.putString("login_instagram_id", user_id_instagram);
                    editor_instagram.putString("login_instagram_name",user_name_instagram);
                    editor_instagram.commit(); //important, otherwise it wouldn't save.

                }
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(Bulldi_rule.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
                is_checked=false;
                login_instagram=false;
            }
            Log.i("Check login", "value instagram: " + Bulldi_rule.login_instagram+is_checked);
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("Check bulldi", "onCreate");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String[] requiredFields = new String[]{"email"};
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", TextUtils.join(",", requiredFields));

                        GraphRequest requestEmail = new GraphRequest(loginResult.getAccessToken(), "me", parameters, null, new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    GraphRequest.GraphJSONObjectCallback callbackEmail = new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject me, GraphResponse response) {
                                            if (response.getError() != null) {
                                                is_checked=false;
                                                login_facbook=false;
                                            } else {
                                                user_mail_facebook = me.optString("email");
                                                user_id_facebook = me.optString("id");

                                                //Log.i("Check profile", "value: " + user_mail + ";" + user_id+";"+me);
                                                new SendEmailAsyncTask_fb().execute();
                                                login_facbook = true;
                                                is_checked=true;
                                                //Save login facebook state
                                                SharedPreferences prefs_facebook = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                SharedPreferences.Editor editor_facebook = prefs_facebook.edit();
                                                editor_facebook.putBoolean("login_facebook_state", login_facbook);
                                                editor_facebook.putString("login_facebook_id", user_id_facebook);
                                                editor_facebook.putString("login_facebook_mail",user_mail_facebook);
                                                editor_facebook.commit(); //important, otherwise it wouldn't save.
                                            }
                                        }
                                    };

                                    callbackEmail.onCompleted(response.getJSONObject(), response);
                                }
                            }
                        });

                        requestEmail.executeAsync();
                        Intent i = new Intent(Bulldi_rule.this, Notify_friend_openAPI.class);
                        startActivity(i);
                        Log.i("Check login", "value facebook: " + Bulldi_rule.login_facbook+is_checked);
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException e) {

                    }
                });
        setContentView(R.layout.bulldi_rule);
        rule1=(TextViewPlus)findViewById(R.id.gretting_rule1);
        String text1 = getResources().getString(R.string.login_use_title);
        String text1_1=getResources().getString(R.string.login_use_content);
        rule1.setText(Html.fromHtml("<b>" + text1 + "</b>" +"<br/>"+text1_1));
        rule2=(TextViewPlus)findViewById(R.id.gretting_rule2);
        String text2 = getResources().getString(R.string.login_privacy_title);
        String text2_1 = getResources().getString(R.string.login_privacy_content);
        rule2.setText(Html.fromHtml("<b>" + text2 + "</b>" +"<br/>"+text2_1));
        rule3=(TextViewPlus)findViewById(R.id.gretting_rule3);
        String text3 = getResources().getString(R.string.login_location_title);
        String text3_1 = getResources().getString(R.string.login_location_content);
        rule3.setText(Html.fromHtml("<b>" + text3 + "</b>" +"<br/>"+text3_1));
        //Get back agreement state
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        is_checked = sharedPreferences.getBoolean("rule_agreement", false);
        checkbox_agree=(CheckBoxImageView) findViewById(R.id.check_agree);
        checkbox_agree.setChecked(is_checked);
        checkbox_agree.setOnCheckedChangeListener(new CheckBoxImageView.OnCheckedChangeListener() {
            public void onCheckedChanged(View buttonView,
                                         boolean isChecked) {
                is_checked = isChecked;
            }
        });
        agree_text=(TextViewPlus) findViewById(R.id.agree_text);
        agree_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(is_checked==true){
                    is_checked=false;
                    checkbox_agree.setChecked(is_checked);
                }
                else {
                    is_checked=true;
                    checkbox_agree.setChecked(is_checked);
                }
            }

        });
        //Facebook login
        facebook_login=(ImageView) findViewById(R.id.facebook_img);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_checked == true) {
                    facebook_login.setImageResource(R.drawable.facebook_on);
                    is_checked=false;
                    LoginManager.getInstance().logInWithReadPermissions(Bulldi_rule.this, Arrays.asList("public_profile", "user_friends", "email"));
                }
            }
        });
        kakao_login=(ImageView) findViewById(R.id.kakaotalk_img);
        kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_checked == true) {
                    kakao_login.setImageResource(R.drawable.kakatalk_on);
                    is_checked=false;
                    Intent i=new Intent(Bulldi_rule.this, KakaoLoginActivity.class);
                    startActivity(i);
                }
            }
        });
        other_login=(TextViewPlus) findViewById(R.id.login_other);
        other_login.setPaintFlags(other_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        other_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_checked == true) {
                    is_checked=false;
                    Intent i=new Intent(Bulldi_rule.this, Greeting.class);
                    startActivity(i);
                }
            }
        });

    }
    private void connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    Bulldi_rule.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void onClick_rule1(View v) {
        Intent i=new Intent(Bulldi_rule.this, Bulldi_rule1.class);
        startActivity(i);
    }
    public void onClick_rule2(View v) {
        Intent i=new Intent(Bulldi_rule.this, Bulldi_rule2.class);
        startActivity(i);
    }
    public void onClick_rule3(View v) {
        Intent i=new Intent(Bulldi_rule.this, Bulldi_rule3.class);
        startActivity(i);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Check bulldi rule", "onResume" +  is_checked);
        if(checkbox_agree!=null) checkbox_agree.setChecked(is_checked);
        //Instagram login
        if(facebook_login!=null) facebook_login.setImageResource(R.drawable.facebook_off);
        if(instagram_login!=null) instagram_login.setImageResource(R.drawable.instagram_off);
        if(kakao_login!=null) kakao_login.setImageResource(R.drawable.kakatalk_off);
        mApp = new InstagramApp(this, Instagram_Data.CLIENT_ID,
                Instagram_Data.CLIENT_SECRET, Instagram_Data.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                mApp.fetchUserName(handler);

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(Bulldi_rule.this, error, Toast.LENGTH_SHORT)
                        .show();
                is_checked=false;
                if(instagram_login!=null) instagram_login.setImageResource(R.drawable.instagram_off);
                if(checkbox_agree!=null) checkbox_agree.setChecked(is_checked);
            }
        });
        instagram_login=(ImageView) findViewById(R.id.instagram_img);
        instagram_login.setImageResource(R.drawable.instagram_off);
        instagram_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_checked == true) {
                    //is_checked=false;
                    instagram_login.setImageResource(R.drawable.instagram_on);
                    mApp.authorize();
                    //connectOrDisconnectUser();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Check bulldi rule", "onPause:" + is_checked);
        //Save agreement
        SharedPreferences prefs_agreement = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_agreement = prefs_agreement.edit();
        editor_agreement.putBoolean("rule_agreement", is_checked);
        editor_agreement.commit(); //important, otherwise it wouldn't save.
        //Save login facebook state
        SharedPreferences prefs_facebook = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_facebook = prefs_facebook.edit();
        editor_facebook.putBoolean("login_facebook_state", login_facbook);
        editor_facebook.putString("login_facebook_id", user_id_facebook);
        editor_facebook.putString("login_facebook_mail",user_mail_facebook);
        editor_facebook.commit(); //important, otherwise it wouldn't save.
        //Save login instagram state
        SharedPreferences prefs_instagram = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_instagram = prefs_instagram.edit();
        editor_instagram.putBoolean("login_instagram_state", login_instagram);
        editor_instagram.putString("login_instagram_id", user_id_instagram);
        editor_instagram.putString("login_instagram_name",user_name_instagram);
        editor_instagram.commit(); //important, otherwise it wouldn't save.
        Log.i("Check login", "value: " + Bulldi_rule.login_facbook + Bulldi_rule.login_instagram + KakaoSignupActivity.login_kakaotalk + Greeting.login_other+is_checked);
        }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        //Log.i("Check bulldi","onDestroy");
    }
    class SendEmailAsyncTask_fb extends AsyncTask<Void, Void, Boolean> {


        public SendEmailAsyncTask_fb() {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");

            String send_auth;
            String send_from;
            String password;
            String content="Facebook ID: "+user_id_facebook+"\nEmail: "+user_mail_facebook;

            send_auth="openstack.bulldi@gmail.com";
            password="openstack";
            String to_recieve="bulldi@openstack.co.kr";
            send_from="openstack.bulldi@gmail.com";
            GMailSender sender = new GMailSender(send_auth, password);
            try {
                try {
                    sender.sendMail("User login from Facebook",
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
    class SendEmailAsyncTask_instagram extends AsyncTask<Void, Void, Boolean> {


        public SendEmailAsyncTask_instagram() {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");

            String send_auth;
            String send_from;
            String password;
            String content="Instagram ID: "+Bulldi_rule.userInfo_List.get(0)+"\nNick name: "+Bulldi_rule.userInfo_List.get(1);

            send_auth="openstack.bulldi@gmail.com";
            password="openstack";
            String to_recieve="bulldi@openstack.co.kr";
            send_from="openstack.bulldi@gmail.com";
            GMailSender sender = new GMailSender(send_auth, password);
            try {
                try {
                    sender.sendMail("User login from Instagram",
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
