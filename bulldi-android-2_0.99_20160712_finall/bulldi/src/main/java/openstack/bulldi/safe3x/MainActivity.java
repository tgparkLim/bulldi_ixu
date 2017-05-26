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
 * MainActivity.java: Starting application
 */

package openstack.bulldi.safe3x;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.winsontan520.wversionmanager.library.OnReceiveListener;
import com.winsontan520.wversionmanager.library.WVersionManager;

import java.util.Locale;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
//import openstack.bulldi.safe3x.Device_View.MyServiceData;
import openstack.bulldi.safe3x.Login.Bulldi_rule;
import openstack.bulldi.safe3x.Login.KakaoSignupActivity;
import openstack.bulldi.safe3x.Preference_etc.LanguageHelper;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.util.TextViewPlus;

public class MainActivity extends Activity {
	public SharedPreferences sharedPreferences;
	private Resources res = null;
	public  static boolean is_MainActivity=false;
	public String currentVersion="";
	public  String newVersion="";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		res = this.getResources();
		if (getIntent() != null) {
			if (("exit").equalsIgnoreCase(getIntent().getStringExtra(("exit")))) {
				onBackPressed();
			}
		}
		// Check new software version available???
		try {
			PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			currentVersion = packageInfo.versionName;
			//Log.i("Check version","value:"+version);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		newVersion=currentVersion;
		WVersionManager versionManager = new WVersionManager(this);
		versionManager.setVersionContentUrl("https://play.google.com/store/apps/details?id=openstack.bulldi.safe3x&hl=en"); // your update content url, see the response format below
		versionManager.checkVersion();
		versionManager.setOnReceiveListener(new OnReceiveListener() {
			@Override
			public boolean onReceive(int status, String result) {
				String search = "softwareVersion";
				if (result != null) {
					if (result.split(search)[1] != null) {
						String str = result.split(search)[1];
						if (str.length() > 8) {
							newVersion = str.substring(3, 7);
							if (value(currentVersion) < value(newVersion)) {
								showToast(getResources().getString(R.string.toast_change_software));
							} else {

							}
						}
					}
				}

				return false; // return true if you want to use library's default logic & dialog
			}
		});
		// Get back agreement state
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final boolean is_checked = sharedPreferences.getBoolean("rule_agreement", false);
		// Splash screen
		Thread connection = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 1500) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();
					if(is_checked==false) {
						Intent i = new Intent();
						i.setClassName("openstack.bulldi.safe3x", "openstack.bulldi.safe3x.Introduction.Introduction");
						startActivity(i);
					}
					else{
						Intent i = new Intent();
//						i.setClassName("openstack.bulldi.safe3x", "openstack.bulldi.safe3x.BLE_Connection.Connection_seperate");
						i.setClassName("openstack.bulldi.safe3x", "openstack.bulldi.safe3x.BLE_Connection.Connection");
						startActivity(i);
					}
				}
			}
		};
		connection.start();

		FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

		//startService(new Intent(getBaseContext(), MyServiceData.class));

	}



	//  Exchange from string to long
	private long value(String string) {
		string = string.trim();
		if( string.contains( "." )){
			final int index = string.lastIndexOf( "." );
			return value( string.substring( 0, index ))* 100 + value( string.substring( index + 1 ));
		}
		else {
			return Long.valueOf( string );
		}
	}
	// Show Toast at the center
	private void showToast(String msg) {
		Toast toast= Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
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
		is_MainActivity=true;

		Log.i("Check ble","value: "+DeviceActivity.mBtLeService+";"+DeviceActivity.mBluetoothDevice+";"+DeviceActivity.intentMyIntentService);
		if((DeviceActivity.mBtLeService!=null) && (DeviceActivity.mBluetoothDevice!=null)) {
			DeviceActivity.mBtLeService.disconnect(DeviceActivity.mBluetoothDevice.getAddress());

			if(DeviceActivity.intentMyIntentService!=null) stopService(DeviceActivity.intentMyIntentService);
		}
		//Get language setup back
		for (int i = 0; i < 2; i++) {
			if(i==0) {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				boolean state = sharedPreferences.getBoolean("language_" + Integer.toString(i), false);
				Language_setting.language_choose[i] = state;
			}
			else{
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				boolean state = sharedPreferences.getBoolean("language_" + Integer.toString(i), false);
				Language_setting.language_choose[i] = state;
			}
		}
		if(Language_setting.language_choose[0]==false && Language_setting.language_choose[1]==false){
			String locale=Locale.getDefault().getLanguage();
			Log.i("Localization","value: "+locale);
			if (locale.compareTo("ko")==0) Language_setting.language_choose[0]=true;
			else Language_setting.language_choose[1]=true;
		}
		//Set language mode
		if(Language_setting.language_choose[1]==true) {
			Language_setting.is_english=true;
			LanguageHelper.changeLocale(res, "en");
		}
		else {
			Language_setting.is_english=false;
			LanguageHelper.changeLocale(res, "ko");
		}

		FirebaseMessaging.getInstance().subscribeToTopic("test");
		FirebaseInstanceId.getInstance().getToken();

	}
	@Override
	protected void onPause() {
		is_MainActivity=false;
		//Save language
		SharedPreferences[] prefs_language=new SharedPreferences[2];
		SharedPreferences.Editor[] editor_language= new SharedPreferences.Editor[2];
		for(int i=0;i<2;i++) {
			prefs_language[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			editor_language[i] = prefs_language[i].edit();
			editor_language[i].putBoolean("language_" + Integer.toString(i), Language_setting.language_choose[i]);
			editor_language[i].commit(); //important, otherwise it wouldn't save.
		}
		super.onPause();
		finish();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}


}