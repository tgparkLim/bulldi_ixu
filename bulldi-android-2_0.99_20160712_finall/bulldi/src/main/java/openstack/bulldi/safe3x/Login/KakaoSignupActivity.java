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
 * KakaoSignupActivity.java: kakaotalk api
 */

package openstack.bulldi.safe3x.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import openstack.bulldi.safe3x.Alarm.GMailSender;
import openstack.bulldi.safe3x.R;


public class KakaoSignupActivity extends BaseActivity {
    public static boolean login_kakaotalk=false;
    public static long userID_kakaotalk;
    public static String user_nickname_kakaotalk;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
        Log.i("Kakaotalk check", "onCreate");
        //Find keyhash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "openstack.bulldi.safe3x",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    protected void showSignup() {
        setContentView(R.layout.layout_usermgmt_signup);
        final ExtraUserPropertyLayout extraUserPropertyLayout = (ExtraUserPropertyLayout) findViewById(R.id.extra_user_property);
        Button signupButton = (Button) findViewById(R.id.buttonSignup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                requestSignUp(extraUserPropertyLayout.getProperties());
            }
        });
    }

    private void requestSignUp(final Map<String, String> properties) {
        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onNotSignedUp() {
                Log.i("Kakaotalk check","onNotSignedUp");
            }

            @Override
            public void onSuccess(Long result) {
                requestMe();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                final String message = "UsermgmtResponseCallback : failure : " + errorResult;
                com.kakao.util.helper.log.Logger.w(message);
                finish();
                Log.i("Kakaotalk check","onFailure1");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }
        }, properties);
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {

                    finish();
                } else {
                    redirectLoginActivity();
                }
                Bulldi_rule.is_checked=false;
                Log.i("Kakaotalk check","onFailure2");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
                Bulldi_rule.is_checked=false;
                Log.i("Kakaotalk check","onSessionClosed");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                //Logger.d("UserProfile : " + userProfile);
                redirectMainActivity();
                userID_kakaotalk=userProfile.getId();
                user_nickname_kakaotalk=userProfile.getNickname();
                new SendEmailAsyncTask_kakaotalk().execute();
                login_kakaotalk=true;
                Bulldi_rule.is_checked=true;
                //Save login kakaotalk state
                SharedPreferences prefs_kakaotalk = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor_kakaotalk = prefs_kakaotalk.edit();
                editor_kakaotalk.putBoolean("login_kakaotalk_state", login_kakaotalk);
                editor_kakaotalk.putLong("login_kakdotalk_id", userID_kakaotalk);
                editor_kakaotalk.putString("login_kakao_name", user_nickname_kakaotalk);
                editor_kakaotalk.commit(); //important, otherwise it wouldn't save.
            }

            @Override
            public void onNotSignedUp() {
                showSignup();
            }
        });
    }

    private void redirectMainActivity() {
        startActivity(new Intent(this, Notify_friend_openAPI.class));
        finish();
    }
    class SendEmailAsyncTask_kakaotalk extends AsyncTask<Void, Void, Boolean> {


        public SendEmailAsyncTask_kakaotalk() {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");

            String send_auth;
            String send_from;
            String password;
            String content="KakaoTalk ID: "+userID_kakaotalk+"\nNick name: "+user_nickname_kakaotalk;

            send_auth="openstack.bulldi@gmail.com";
            password="openstack";
            String to_recieve="bulldi@openstack.co.kr";
            send_from="openstack.bulldi@gmail.com";
            GMailSender sender = new GMailSender(send_auth, password);
            try {
                try {
                    sender.sendMail("User login from KakaoTalk",
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
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Kakaotalk check", "onPause");
        //Save agreement
        SharedPreferences prefs_agreement = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_agreement = prefs_agreement.edit();
        editor_agreement.putBoolean("rule_agreement", Bulldi_rule.is_checked);
        editor_agreement.commit(); //important, otherwise it wouldn't save.
        //Save login kakaotalk state
        SharedPreferences prefs_kakaotalk = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_kakaotalk = prefs_kakaotalk.edit();
        editor_kakaotalk.putBoolean("login_kakaotalk_state", login_kakaotalk);
        editor_kakaotalk.putLong("login_kakdotalk_id", userID_kakaotalk);
        editor_kakaotalk.putString("login_kakao_name", user_nickname_kakaotalk);
        editor_kakaotalk.commit(); //important, otherwise it wouldn't save.
        Log.i("Kakaotalk check","value: "+Bulldi_rule.is_checked);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("Kakaotalk check", "onResume");
    }
}
