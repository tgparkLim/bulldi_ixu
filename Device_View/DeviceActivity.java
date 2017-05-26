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
 * DeviceActivity.java; main control operation of bulldi
 */

package openstack.bulldi.safe3x.Device_View;
import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.GattInfo;
import openstack.bulldi.common.GenericBluetoothProfile;
import openstack.bulldi.safe3x.Alarm.Alarm;
import openstack.bulldi.safe3x.BLE_Connection.Autoconnect_service;
import openstack.bulldi.safe3x.BLE_Connection.Connection;
import openstack.bulldi.safe3x.BuildConfig;
import openstack.bulldi.safe3x.Preference_customer_idea.Customer_preference;
import openstack.bulldi.safe3x.Preference_etc.Alias;
import openstack.bulldi.safe3x.Preference_etc.ETC_preference;
import openstack.bulldi.safe3x.Preference_etc.Version;
import openstack.bulldi.safe3x.Preference_history.History_bean;
import openstack.bulldi.safe3x.Preference_history.History_preference;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.Preference_etc.Legal_information;
import openstack.bulldi.safe3x.Preference_lighting.Lighting_preference;
import openstack.bulldi.safe3x.BLE_Connection.Loading;
import openstack.bulldi.safe3x.Preference_lighting.Listview_light;
import openstack.bulldi.safe3x.Preference_sharing.Message_content_preference;
import openstack.bulldi.safe3x.Alarm.Music;
import openstack.bulldi.safe3x.Preference_sharing.Notify_friend_preference;
import openstack.bulldi.safe3x.Alarm.Phone;
import openstack.bulldi.safe3x.Preference_test.Operation_test_preference;
import openstack.bulldi.safe3x.PreferencesActivity;
import openstack.bulldi.safe3x.PreferencesFragment;
import openstack.bulldi.safe3x.R;
import openstack.bulldi.safe3x.Alarm.SendSMS;
import openstack.bulldi.safe3x.Preference_etc.Session_out;
import openstack.bulldi.safe3x.Preference_etc.Unit_setting;
//import com.detection.bulldi.bulldi.common.IBMIoTCloudProfile;
import android.os.Vibrator;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


//Most of sensor data is 3D point data
@SuppressLint("InflateParams") public class DeviceActivity extends ViewPagerActivity_device {
	//@SuppressLint("InflateParams") public class DeviceActivity extends AndroidViewPagerActivity {
	//MAC address
	public static String MAC_addr;
	public static boolean auto_scan=false;
	//Store data
	public SharedPreferences sharedPreferences;
	//public static Calendar Device_time=Calendar.getInstance();
	public static DateFormat df =       new SimpleDateFormat("dd. MM. yyyy   HH:mm");
	public static DateFormat df_Korea = new SimpleDateFormat("yyyy. MM. dd   HH:mm");
	public static String starting_time = df.format(Calendar.getInstance().getTime());
	public static String starting_time_ko = df_Korea.format(Calendar.getInstance().getTime());
	// Activity
	public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
	private static final int PREF_ACT_REQ = 0;
	private static final int FWUPDATE_ACT_REQ = 1;

	private Device_Temp mDeviceView = null;
	private Device_Smoke mDeviceView_1 = null;
	private Device_CO mDeviceView_2 = null;
	//private static Device_Battery mDeviceView_3 = null;

	//
	static Context context;
	public static   TextView co_title;
	public static   TextView temperature_title;
	public static   TextView smoke_title;
	public static   TextView battery_title;
	Typeface font_title_light;
	Typeface font_title_regular;
	// BLE
	//private BluetoothLeService mBtLeService = null;
	public static BluetoothLeService mBtLeService = null;
	//private BluetoothDevice mBluetoothDevice = null;
	public static BluetoothDevice mBluetoothDevice = null;
	private BluetoothGatt mBtGatt = null;
	private List<BluetoothGattService> mServiceList = null;
	private boolean mServicesRdy = false;
	private boolean mIsReceiving = false;
	//private IBMIoTCloudProfile mqttProfile;

	// SensorTagGatt
	private BluetoothGattService mOadService = null;
	private BluetoothGattService mConnControlService = null;
	private boolean mIsSensorTag2;
	private String mFwRev;
	public ProgressDialog progressDialog;

	//GUI
	private GenericBluetoothProfile mProfiles;
	private GenericBluetoothProfile mProfiles_1;
	private GenericBluetoothProfile mProfiles_2;
	private GenericBluetoothProfile mProfiles_3;
	private GenericBluetoothProfile mProfiles_4;
	private GenericBluetoothProfile mProfiles_5;
	private GenericBluetoothProfile mProfiles_6;
	private GenericBluetoothProfile mProfiles_7;
	public static InOut_Service in_out;
	//Time store
	static Calendar current_time;
	//private GetTime time_store;

	//Call/Message
	private static final String OPT_CALL_MESSAGE = "call_mess_alarm";
	private static final boolean OPT_CALL_MESSAGE_DEF = true;

	//Music
	//private static final String OPT_MUSIC = "music";
	//private static final boolean OPT_MUSIC_DEF = true;
	//public  BackgroundSound mBackgroundSound = new BackgroundSound();
	//Connection control parameters
	public static Connection_control connection;

	//Alarm
	public static boolean isAlarm=false;
	public static SendSMS send_num = new SendSMS();
	public static Phone call_num = new Phone();
	public static boolean isDeviceActivity=true;
	public static boolean isNaturalRelaease=false;
	int count_release=0;

	//Service
	public static Intent intentMyIntentService;
	Intent startAlarmService;
	Intent stopAlarmService;

	//Lighting state
	private ImageView setting;
	//public static ImageView setting;
	public static ImageView lighting_state;
	//public static boolean light_press=false;
	private ImageView history_state;
	//Tab
	ImageView tab_co;
	ImageView tab_temp;
	ImageView tab_smoke;
	//
	public static FragmentManager fm;
	//public static Fragment prev;
	//Add icon to status bar
	private static final int NOTIFICATION_ID = 177;
	private NotificationManager notificationManager;
	//Get resource
	public static Resources res;


	////////////////////////////
	private static final String IN_LOCATION_FENCE_KEY = "IN_LOCATION_FENCE_KEY";
	private static final String EXITING_LOCATION_FENCE_KEY = "EXITING_LOCATION_FENCE_KEY";
	private static final String ENTERING_LOCATION_FENCE_KEY = "ENTERING_LOCATION_FENCE_KEY";

	public static final int STATUS_IN = 0;
	public static final int STATUS_OUT = 1;
	public static final int STATUS_ENTERING = 2;
	public static final int STATUS_EXITING = 3;

	private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;

	private static GoogleApiClient mGoogleApiClient;
	private static PendingIntent mPendingIntent;
	private LocationFenceReceiver mLocationFenceReceiver;

	static String latitude;
	static String longitude;

	static double newLatitude;
	static double newLongitude;

	static double newQLatitude;
	static double newQLongitude;
	static String result;
	static InputStream isr;



	public DeviceActivity() {
		mResourceFragmentPager = R.layout.fragment_pager_2;
		mResourceIdPager = R.id.pager_2;
		//Log.i("DeviceActivity", "value of mResourceIdPager: " + mResourceIdPager);
		mFwRev = new String("1.5"); // Assuming all SensorTags are up to date until actual FW revision is read
	}

	//private getDataJson gdj;

	public static DeviceActivity getInstance() {
		return (DeviceActivity) mThis;
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE | Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		fm = getSupportFragmentManager();
		context=getApplication();

		Log.i("Check device status", "is onCreate");
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.device_title_bar);
		//getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.device_title_bar);
		getActionBar().show();

		Intent intent = getIntent();
		// BLE
		mBtLeService = BluetoothLeService.getInstance();
		mBluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE);
		//Log.i("Check ble","value: "+mBluetoothDevice);
		setting= (ImageView) findViewById(R.id.setting_icon);
		setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				setting.setImageResource(R.drawable.setting_on);
				startPreferenceActivity();
				//setting.setImageResource(R.drawable.setting_off);
			}
		});
		lighting_state=(ImageView) findViewById(R.id.lighting_state);
		lighting_state.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				/*Intent i = new Intent(DeviceActivity.this, Lighting_preference.class);
				startActivity(i);*/
				if(BatteryInformationServiceProfile.battery_data>30){
					if ((Lighting_preference.handler_1 != null) && (Lighting_preference.runnable_1 != null))
						Lighting_preference.handler_1.removeCallbacks(Lighting_preference.runnable_1);
					if ((Lighting_preference.handler_2 != null) && (Lighting_preference.runnable_2 != null))
						Lighting_preference.handler_2.removeCallbacks(Lighting_preference.runnable_2);
					if ((Lighting_preference.handler_3 != null) && (Lighting_preference.runnable_3 != null))
						Lighting_preference.handler_3.removeCallbacks(Lighting_preference.runnable_3);
					if ((Lighting_preference.handler_4 != null) && (Lighting_preference.runnable_4 != null))
						Lighting_preference.handler_4.removeCallbacks(Lighting_preference.runnable_4);
					if(Lighting_preference.light_on==false) Lighting_preference.light_on=true;
					else Lighting_preference.light_on=false;
					Lighting_preference.sleep_on[0]=true;
					Lighting_preference.sleep_on[1]=false;
					Lighting_preference.sleep_on[2]=false;
					Lighting_preference.sleep_on[3]=false;
					Lighting_preference.sleep_on[4]=false;
						if (Lighting_preference.light_on == true) {
							byte value_data = (byte) (0x40);
							//Log.i("Check in/out","value: "+value_config+";"+value_data);
							if (DeviceActivity.lighting_state != null)
								DeviceActivity.lighting_state.setImageResource(R.drawable.lightting);
							DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
						} else {
							byte value_data = (byte) (0x00);
							//Log.i("Check in/out","value: "+value_config+";"+value_data);
							if (DeviceActivity.lighting_state != null)
								DeviceActivity.lighting_state.setImageResource(R.drawable.icn_lighting);
							DeviceActivity.mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
						}
					Log.i("Check update","Light status: "+Lighting_preference.light_on+lighting_state+";"+mBtLeService+";"+InOut_Service.charac_data);
				}
				else{
					if(Lighting_preference.light_on==true){
					Lighting_preference.light_on =false;
					byte value_data_off = (byte) (0x00);
					if (lighting_state != null) lighting_state.setImageResource(R.drawable.icn_lighting);
					mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data_off);}
					final Dialog dialog = new Dialog(DeviceActivity.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
					dialog.setContentView(R.layout.customer_light_dialog);
					Window window = dialog.getWindow();
					WindowManager.LayoutParams wlp = window.getAttributes();

					wlp.gravity = Gravity.CENTER;
					window.setAttributes(wlp);
					final Button dialogButton = (Button) dialog.findViewById(R.id.dialog_custom_light_close);
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

								}
							}, 10);
						}
					});
					dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					dialog.show();
//					Toast.makeText(context, context.getResources().getString(R.string.lighting_no_support), Toast.LENGTH_SHORT).show();
				}
			}

		});
		//Log.i("Check light", "value1: " + lighting_state + " " + Lighting_preference.light_on);
		if(lighting_state!=null) lighting_state.setImageResource(R.drawable.icn_lighting);

		history_state=(ImageView) findViewById(R.id.history_state);
		history_state.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				history_state.setImageResource(R.drawable.history_on);
				Intent i = new Intent(DeviceActivity.this, History_preference.class);
				startActivity(i);
			}

		});

		//Log.i("Check light", "value2: " + lighting_state);
		tab_co = new ImageView(this);
		tab_co.setImageResource(R.drawable.menu_co_off);
		//tab_co.setWidth(getResources().getDisplayMetrics().widthPixels / 3);
		tab_temp = new ImageView(this);
		tab_temp.setImageResource(R.drawable.menu_temper_off);
		tab_smoke = new ImageView(this);
		tab_smoke.setImageResource(R.drawable.menu_smoke_off);
		//Save MAC addr
		MAC_addr= mBluetoothDevice.getAddress();
		Log.i("Check action", "connected MAC 1: "+MAC_addr);
		auto_scan=true;
		start_service();
		//Save battery
		SharedPreferences prefs_MAC = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor_MAC = prefs_MAC.edit();
		editor_MAC.putString("Connected_address", MAC_addr);
		editor_MAC.commit(); //important, otherwise it wouldn't save.
		mServiceList = new ArrayList<BluetoothGattService>();

		mIsSensorTag2 = false;

		// Determine type of SensorTagGatt
		String deviceName = mBluetoothDevice.getName();
		if ((deviceName.equals("SensorTag2")) || (deviceName.equals("bulldi"))) {
			mIsSensorTag2 = true;
		} else mIsSensorTag2 = false;

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		font_title_light=Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
		font_title_regular = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");


		co_title = new TextView(context);
		co_title.setText("CO Gas");
		co_title.setGravity(Gravity.BOTTOM);
		co_title.setTypeface(font_title_regular);
		co_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 54);
		co_title.setTextColor(Color.BLACK);
		//co_title.setPadding(40, 40, 40, 40);

		temperature_title = new TextView(context);
		temperature_title.setText("Temperature");
		temperature_title.setGravity(Gravity.CENTER_VERTICAL);
		temperature_title.setTypeface(font_title_regular);
		temperature_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 54);
		temperature_title.setTextColor(Color.BLACK);
		//temperature_title.setPadding(40, 40, 40, 40);

		smoke_title = new TextView(context);
		smoke_title.setText("Smoke");
		smoke_title.setGravity(Gravity.CENTER_VERTICAL);
		smoke_title.setTypeface(font_title_regular);
		smoke_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 54);
		smoke_title.setTextColor(Color.BLACK);
		//smoke_title.setPadding(40, 40, 40, 40);

		battery_title = new TextView(context);
		battery_title.setText("Battery");
		battery_title.setTypeface(font_title_regular);
		battery_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 54);
		battery_title.setTextColor(Color.BLACK);
		battery_title.setPadding(40, 40, 40, 40);

		mDeviceView = new Device_Temp();
		mDeviceView_1 = new Device_Smoke();
		mDeviceView_2 = new Device_CO();
		//mDeviceView_3 = new Device_Battery();

		String title_co="CO Gas";
		String title_temp="Temperature";
		String title_smoke="Smoke";

		mSectionsPagerAdapter.addSection(mDeviceView_2, tab_co);
		mSectionsPagerAdapter.addSection(mDeviceView,tab_temp);
		mSectionsPagerAdapter.addSection(mDeviceView_1, tab_smoke);
		mViewPager.setCurrentItem(1);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int screenWidth = displaymetrics.widthPixels;
		final View tabView = getActionBar().getTabAt(0).getCustomView();
		final View tabContainerView = (View) tabView.getParent();
		final int tabPadding = tabContainerView.getPaddingLeft() + tabContainerView.getPaddingRight();
		final int tabs = getActionBar().getTabCount();
		for(int i=0 ; i < tabs ; i++) {
			View tab = getActionBar().getTabAt(i).getCustomView();
			LinearLayout tabItemLayout = (LinearLayout) tab.findViewById(R.id.tabItemLayout);
			ImageView tab_image=(ImageView) tab.findViewById(R.id.imageViewTab);
			//if(i==0) tab_image.setImageResource(R.drawable.menu_co_on);
			if(i==0) tab_image.setImageResource(R.drawable.menu_co_off);
			else if(i==1) tab_image.setImageResource(R.drawable.menu_temper_on);
			else tab_image.setImageResource(R.drawable.menu_smoke_off);
			tabItemLayout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth/tabs-tabPadding-1, LinearLayout.LayoutParams.WRAP_CONTENT));
		}

		mProfiles = new GenericBluetoothProfile();
		mProfiles_1 = new GenericBluetoothProfile();
		mProfiles_2 = new GenericBluetoothProfile();
		mProfiles_3 = new GenericBluetoothProfile();
		mProfiles_4 = new GenericBluetoothProfile();
		mProfiles_5 = new GenericBluetoothProfile();
		mProfiles_6 = new GenericBluetoothProfile();

		//show dialog
		Loading frag_loading = new Loading();
		FragmentTransaction ft_loading = fm.beginTransaction();
		ft_loading.add(frag_loading, "fragment_loading");
		ft_loading.commitAllowingStateLoss();


		// GATT database
		res = getResources();
		XmlResourceParser xpp = res.getXml(R.xml.gatt_uuid);
		new GattInfo(xpp);

		//Connection status
		Connection.is_onConnection=false;
		//Start add icon to status bar
		/*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.icon_96)
				.setContentTitle(getResources().getString(R.string.app_name));
		Intent resultIntent = new Intent(this, MainActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, notification);*/

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Awareness.API)
				.build();
		mGoogleApiClient.connect();

		mLocationFenceReceiver = new LocationFenceReceiver();
		Intent intent0524 = new Intent(LocationFenceReceiver.FENCE_RECEIVER_ACTION);
		mPendingIntent = PendingIntent.getBroadcast(this, 1, intent0524, 0);

		new getData().execute("");

		//phpDown task;
	}

	protected void onStart() {
		super.onStart();
		registerFences();
		registerReceiver(mLocationFenceReceiver, new IntentFilter(LocationFenceReceiver.FENCE_RECEIVER_ACTION));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					registerFences();
				} else {
//                    Snackbar.make(mLayoutLocationFence,
//                            getString(R.string.error_loading_places),
//                            Snackbar.LENGTH_LONG).show();
				}
			}
		}
	}

	public void registerFences() {

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
		} else {
			AwarenessFence inLocationFence = LocationFence.in(newLatitude, newLongitude, 200, 1);
			//AwarenessFence inLocationFence = LocationFence.in(50.830951, -0.146978, 200, 1);
			AwarenessFence exitingLocationFence = LocationFence.exiting(50.830951, -0.146978, 200);
			AwarenessFence enteringLocationFence = LocationFence.entering(50.830951, -0.146978, 200);

			Awareness.FenceApi.updateFences(
					mGoogleApiClient,
					new FenceUpdateRequest.Builder()
							.addFence(IN_LOCATION_FENCE_KEY, inLocationFence, mPendingIntent)
							.addFence(EXITING_LOCATION_FENCE_KEY, exitingLocationFence, mPendingIntent)
							.addFence(ENTERING_LOCATION_FENCE_KEY, enteringLocationFence, mPendingIntent)
							.build())
					.setResultCallback(new ResultCallback<Status>() {
						@Override
						public void onResult(@NonNull Status status) {
							if (status.isSuccess()) {
//                                Snackbar.make(mLayoutLocationFence,
//                                        "Fence Registered",
//                                        Snackbar.LENGTH_LONG).show();
							} else {
//                                Snackbar.make(mLayoutLocationFence,
//                                        "Fence Not Registered",
//                                        Snackbar.LENGTH_LONG).show();
							}
						}
					});
		}
	}

	private void unregisterFences() {
		Awareness.FenceApi.updateFences(
				mGoogleApiClient,
				new FenceUpdateRequest.Builder()
						.removeFence(IN_LOCATION_FENCE_KEY)
						.removeFence(EXITING_LOCATION_FENCE_KEY)
						.removeFence(ENTERING_LOCATION_FENCE_KEY)
						.build()).setResultCallback(new ResultCallbacks<Status>() {
			@Override
			public void onSuccess(@NonNull Status status) {
//                Snackbar.make(mLayoutLocationFence,
//                        "Fence Removed",
//                        Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(@NonNull Status status) {
//                Snackbar.make(mLayoutLocationFence,
//                        "Fence Not Removed",
//                        Snackbar.LENGTH_LONG).show();
			}
		});
	}

	private static void setHeadphoneState(int status) {
		switch (status) {
			case STATUS_IN:
				String url0524 = "http://13.124.11.28/fcm/push_notification.php";

				new upadate0524().execute(url0524);

				//Toast.makeText(this, "123", Toast.LENGTH_LONG).show();

				break;
			case STATUS_OUT:
				url0524 = "http://13.124.11.28/fcm/push_notification.php";

				new upadate0524().execute(url0524);

				break;
			case STATUS_ENTERING:
				url0524 = "http://13.124.11.28/fcm/push_notification.php";

				new upadate0524().execute(url0524);

				break;
			case STATUS_EXITING:
				url0524 = "http://13.124.11.28/fcm/push_notification.php";

				new upadate0524().execute(url0524);

				break;
		}
	}

	public static class LocationFenceReceiver extends BroadcastReceiver {

		public static final String FENCE_RECEIVER_ACTION =
				//"com.hitherejoe.aware.ui.fence.LocationFenceReceiver.FENCE_RECEIVER_ACTION";
				"openstack.bulldi.safe3x.Device_View.DeviceActivity.FENCE_RECEIVER_ACTION";

		@Override
		public void onReceive(Context context, Intent intent) {
			FenceState fenceState = FenceState.extract(intent);

			if (TextUtils.equals(fenceState.getFenceKey(), IN_LOCATION_FENCE_KEY)) {
				switch (fenceState.getCurrentState()) {
					case FenceState.TRUE:
						setHeadphoneState(STATUS_IN);
						break;
					case FenceState.FALSE:
						setHeadphoneState(STATUS_OUT);
						break;
					case FenceState.UNKNOWN:
						break;
				}
			} else if (TextUtils.equals(fenceState.getFenceKey(), EXITING_LOCATION_FENCE_KEY)) {
				switch (fenceState.getCurrentState()) {
					case FenceState.TRUE:
						setHeadphoneState(STATUS_EXITING);
						break;
					case FenceState.FALSE:
						break;
					case FenceState.UNKNOWN:
						break;
				}
			} else if (TextUtils.equals(fenceState.getFenceKey(), ENTERING_LOCATION_FENCE_KEY)) {
				switch (fenceState.getCurrentState()) {
					case FenceState.TRUE:
						setHeadphoneState(STATUS_ENTERING);
						break;
					case FenceState.FALSE:

						break;
					case FenceState.UNKNOWN:

						break;
				}
			}
		}
	}

	public static class upadate0524 extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpURLConnection conn = null;

			try {
				URL url;
				url = new URL(params[0]);
				conn = (HttpURLConnection) url.openConnection();
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream is = conn.getInputStream();
				} else {
					InputStream err = conn.getErrorStream();
				}
				return "Done";
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(conn != null) {
					conn.disconnect();
				}
			}

			return null;
		}
	}

	public static class getData extends AsyncTask<String, Void, String> {
		String name;

		@Override
		protected String doInBackground(String... params) {
			result = "";
			isr = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://13.124.11.28/temp/GetData.php"); //YOUR PHP SCRIPT ADDRESS
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());

			}

			//convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				isr.close();

				result = sb.toString();
			} catch (Exception e) {
				Log.e("log_tag", "Error  converting result " + e.toString());
			}


			try {

				JSONArray jArray = new JSONArray(result);

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json = jArray.getJSONObject(i);


					latitude = json.getString("latitude");
					longitude = json.getString("longitude");
					//longitude = longitude +"\n"+  json.getString("longitude");

					newLatitude = Double.parseDouble(latitude);
					newLongitude = Double.parseDouble(longitude);
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error Parsing Data " + e.toString());
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {

			newQLatitude = 37.529648;
			newQLongitude = 126.722083;

			//tv.setText(""+ newLatitude + "," + newLongitude);


		}

		@Override
		protected void onPreExecute() {}

		@Override
		protected void onProgressUpdate(Void... values) {}
	}

	public static boolean getCallMessage(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_CALL_MESSAGE, OPT_CALL_MESSAGE_DEF);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mIsReceiving) {
			unregisterReceiver(mGattUpdateReceiver);
			mIsReceiving = false;
		}
		if((mBtLeService!=null) && (mBluetoothDevice!=null)) {
			mBtLeService.disconnect(mBluetoothDevice.getAddress());
		}
		Lighting_preference.light_on=false;
		if (mBtGatt != null) {
			// Log.i(TAG, "close");
			mBtGatt.close();
			mBtGatt = null;
		}
		mProfiles.onPause();
		mProfiles_1.onPause();
		mProfiles_2.onPause();
		mProfiles_3.onPause();
		mProfiles_4.onPause();
		mProfiles_5.onPause();
		mProfiles_6.onPause();
		//mProfiles_7.onPause();
		//View should be started again from scratch

		this.mProfiles = null;
		this.mDeviceView = null;

		this.mProfiles_1 = null;
		this.mDeviceView_1 = null;

		this.mProfiles_2 = null;
		this.mDeviceView_2 = null;

		this.mProfiles_3=null;
		//this.mDeviceView_3=null;

		this.mProfiles_4=null;
		this.mProfiles_5=null;
		this.mProfiles_6=null;

//		this.mBtLeService.timedDisconnect();

		finishActivity(PREF_ACT_REQ);
		finishActivity(FWUPDATE_ACT_REQ);


		if(Alias.is_alias==true){
			Alias.alias_setting_back.callOnClick();
			Alias.is_alias=false;
			ETC_preference.etc_setting_back.callOnClick();
			ETC_preference.is_etc=false;
		}
		else if(Language_setting.is_language==true){
			Language_setting.language_setting_back.callOnClick();
			Language_setting.is_language=false;
			ETC_preference.etc_setting_back.callOnClick();
			ETC_preference.is_etc=false;
		}
		else if(Unit_setting.is_unit==true){
			Unit_setting.unit_setting_back.callOnClick();
			Unit_setting.is_unit=false;
			ETC_preference.etc_setting_back.callOnClick();
			ETC_preference.is_etc=false;
		}
		else if(Legal_information.is_legal==true){
			Legal_information.legal_setting_back.callOnClick();
			Legal_information.is_legal=false;
			ETC_preference.etc_setting_back.callOnClick();
			ETC_preference.is_etc=false;
		}
		else if(Version.is_version==true){
			Version.version_setting_back.callOnClick();
			Version.is_version=false;
			ETC_preference.etc_setting_back.callOnClick();
			ETC_preference.is_etc=false;
		}
		else if(Session_out.is_session==true){
			Session_out.session_setting_back.callOnClick();
			Session_out.is_session=false;
			ETC_preference.etc_setting_back.callOnClick();
			ETC_preference.is_etc=false;
		}
		else if(Notify_friend_preference.is_notify==true){
			Notify_friend_preference.notify_setting_back.callOnClick();
			Notify_friend_preference.is_notify=false;
		}
		else if(Message_content_preference.is_message==true){
			Message_content_preference.message_setting_back.callOnClick();
			Message_content_preference.is_message=false;
		}
		else if(Lighting_preference.is_lighting==true){
			Lighting_preference.lighting_back.callOnClick();
			Lighting_preference.is_lighting=false;
		}
		else if(Customer_preference.is_customer==true){
			Customer_preference.message_setting_back.callOnClick();
			Customer_preference.is_customer=false;
		}
		else if(History_preference.is_history==true){
			History_preference.history_setting_back.callOnClick();
			History_preference.is_history=false;
		}
		else if(Operation_test_preference.is_test==true){
			Operation_test_preference.operation_test_back.callOnClick();
			Operation_test_preference.is_test=false;
		}
		else if(ETC_preference.is_etc==true){
			ETC_preference.etc_setting_back.callOnClick();
			ETC_preference.is_etc=false;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.optionsMenu = menu;
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.opt_prefs:
				startPreferenceActivity();
				break;
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
	@Override
	public void onBackPressed() {
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeIntent);
	}

	public boolean isEnabledByPrefs(String prefName) {
		String preferenceKeyString = "pref_"
				+ prefName;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mBtLeService);

		Boolean defaultValue = true;
		return prefs.getBoolean(preferenceKeyString, defaultValue);
	}

	@Override
	protected void onResume() {
		Log.i("Check device status", "is onResume");
		super.onResume();
		isDeviceActivity=true;
		Music.stop(this);
		if ((lighting_state != null) && (Lighting_preference.light_on == false)) lighting_state.setImageResource(R.drawable.icn_lighting);
		if(history_state!=null) history_state.setImageResource(R.drawable.history_off);
		if(Alarm.mActivity!=null){
			isAlarm = true;
		}
		else isAlarm=false;

		if (setting != null) setting.setImageResource(R.drawable.setting_off);
		if (!mIsReceiving) {
			registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			mIsReceiving = true;
		}

		this.mBtLeService.abortTimedDisconnect();

		//Get data battery back
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		BatteryInformationServiceProfile.battery_data = sharedPreferences.getInt("key", 0);
		//Get data contact size back
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Notify_friend_preference.contact_size = sharedPreferences.getInt("contact_size", 0);
		//Get data contact list back
		Notify_friend_preference.notify_contact = new ArrayList<String>();
		for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String contact = sharedPreferences.getString("contact_" + Integer.toString(i), "empty");
			Notify_friend_preference.notify_contact.add(contact);
		}
		//Get location back
		Notify_friend_preference.notify_location = new ArrayList<Integer>();
		for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Integer location = sharedPreferences.getInt("contact_location" + Integer.toString(i), 0);
			Notify_friend_preference.notify_location.add(location);

		}

		//Light state
		if(Lighting_preference.light_on==true) lighting_state.setImageResource(R.drawable.lightting);
		else lighting_state.setImageResource(R.drawable.icn_lighting);
		//Get back voice state
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Message_content_preference.show_message = sharedPreferences.getString("voice_state", getResources().getString(R.string.message_content_item_1));
		//Get unit setup back
		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				boolean state = sharedPreferences.getBoolean("unit_" + Integer.toString(i), true);
				Unit_setting.unit_choose[i] = state;
			} else {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				boolean state = sharedPreferences.getBoolean("unit_" + Integer.toString(i), false);
				Unit_setting.unit_choose[i] = state;
			}
		}
		//Set unit mode
		if (Unit_setting.unit_choose[0] == true) Unit_setting.is_celsius = true;
		else Unit_setting.is_celsius = false;
		if(SensorTagIRTemperatureProfile.myList.size()>1) {
			double temperature_show = SensorTagIRTemperatureProfile.myList.get(SensorTagIRTemperatureProfile.myList.size() - 1);
			if (Unit_setting.is_celsius == false)
				temperature_show = (SensorTagIRTemperatureProfile.myList.get(SensorTagIRTemperatureProfile.myList.size() - 1) * 1.8) + 32;
			//String s = String.format("%.2f", v.z);
			String s = String.format("%.1f", temperature_show);
			Spannable span = new SpannableString(s);
			span.setSpan(new RelativeSizeSpan(1.5f), 0, 3,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			//String span=String.format("%.0f", temperature_show);
			Device_Temp.temp_text.setText(span);
			if (Unit_setting.is_celsius == true) Device_Temp.temp_unit.setText("C");
			else Device_Temp.temp_unit.setText("F");
		}

/*        else if(SensorTagIRTemperatureProfile.myList.size() <= 1 && SensorTagIRTemperatureProfile.myList.size() >= 0) {
            double temperature_show = SensorTagIRTemperatureProfile.myList.get(SensorTagIRTemperatureProfile.myList.size() );
            if (Unit_setting.is_celsius == false)
                temperature_show = (SensorTagIRTemperatureProfile.myList.get(SensorTagIRTemperatureProfile.myList.size() - 1) * 1.8) + 32;
            //String s = String.format("%.2f", v.z);
            String s = String.format("%.1f", temperature_show);
            Spannable span = new SpannableString(s);
            span.setSpan(new RelativeSizeSpan(1.5f), 0, 3,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //String span=String.format("%.0f", temperature_show);
            Device_Temp.temp_text.setText(span);
            if (Unit_setting.is_celsius == true) Device_Temp.temp_unit.setText("C");
            else Device_Temp.temp_unit.setText("F");
        }*/

		//Warning history

		//Get history_size back
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		History_preference.history_size = sharedPreferences.getInt("history_size", 0);
		Log.i("Check history_size","value device: "+History_preference.history_size);
		if ((History_preference.history_size == 0)&&(History_preference.is_clear_all==false)) {
			History_preference.HistoryList = new ArrayList<History_bean>();
			try {
				History_preference.packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
				History_preference.installTime1 = History_preference.df1.format(new Date(History_preference.packageInfo.firstInstallTime));
				History_preference.installTime2 = History_preference.df2.format(new Date(History_preference.packageInfo.firstInstallTime));
				Log.i("Check install","value: "+History_preference.installTime1+" "+History_preference.installTime2);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			History_bean objHistory = new History_bean();
			History_preference.install=getResources().getString(R.string.warning_history_install);
			History_preference.install_value=getResources().getString(R.string.warning_history_install_value);
			objHistory.setImg1(History_preference.install_icon);objHistory.setTitle(History_preference.install);objHistory.setValue(History_preference.install_value);objHistory.setTime1(History_preference.installTime1);objHistory.setTime2(History_preference.installTime2);
			History_preference.HistoryList.add(objHistory);
			History_preference.history_size=History_preference.HistoryList.size();
			History_preference.his_image=new int[History_preference.history_size];History_preference.his_image[0]=History_preference.install_icon;
			History_preference.his_title=new String[History_preference.history_size];History_preference.his_title[0]=History_preference.install;
			History_preference.his_value=new String[History_preference.history_size];History_preference.his_value[0]=History_preference.install_value;
			History_preference.his_time1=new String[History_preference.history_size];History_preference.his_time1[0]=History_preference.installTime1;
			History_preference.his_time2=new String[History_preference.history_size];History_preference.his_time2[0]=History_preference.installTime2;
		}
		else {
			History_preference.HistoryList = new ArrayList<History_bean>();
			//Get history image back
			History_preference.his_image=new int[History_preference.history_size];
			for (int i = 0; i < History_preference.history_size; i++) {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				int img = sharedPreferences.getInt("history_img_" + Integer.toString(i), 0);
				History_preference.his_image[i]=img;
			}
			//Get history title back
			History_preference.his_title=new String[History_preference.history_size];
			for (int i = 0; i < History_preference.history_size; i++) {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String title = sharedPreferences.getString("history_title_" + Integer.toString(i), "");
				History_preference.his_title[i]=title;
			}
			//Get history value back
			History_preference.his_value=new String[History_preference.history_size];
			for (int i = 0; i < History_preference.history_size; i++) {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String value = sharedPreferences.getString("history_value_" + Integer.toString(i), "");
				History_preference.his_value[i]=value;
			}
			//Get history time1 back
			History_preference.his_time1=new String[History_preference.history_size];
			for (int i = 0; i < History_preference.history_size; i++) {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String time1 = sharedPreferences.getString("history_time1_" + Integer.toString(i), "");
				History_preference.his_time1[i]=time1;
			}
			//Get history time2 back
			History_preference.his_time2=new String[History_preference.history_size];
			for (int i = 0; i < History_preference.history_size; i++) {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				String time2 = sharedPreferences.getString("history_time2_" + Integer.toString(i), "");
				History_preference.his_time2[i]=time2;
			}
			//Change the language
			try {
				History_preference.packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			if(Language_setting.is_english==true){
				for (int i = 0; i < History_preference.history_size; i++) {
					if(History_preference.his_title[i].compareTo("환영합니다, 만나서 반갑습니다")==0){
						History_preference.his_title[i]="Hi, nice to meet you";
						History_preference.his_value[i]="installation";
						History_preference.his_time1[i] = History_preference.df1.format(new Date(History_preference.packageInfo.firstInstallTime));
						History_preference.his_time2[i] = History_preference.df2.format(new Date(History_preference.packageInfo.firstInstallTime));
					}
					if(History_preference.his_title[i].compareTo("잠자기 설정")==0){
						History_preference.his_title[i]="Sleeping mode";
						if(History_preference.his_value[i].compareTo("없음")==0)History_preference.his_value[i]="None";
						else if(History_preference.his_value[i].compareTo("5분")==0)History_preference.his_value[i]="5m";
						else if(History_preference.his_value[i].compareTo("10분")==0)History_preference.his_value[i]="10m";
						else if(History_preference.his_value[i].compareTo("20분")==0)History_preference.his_value[i]="20m";
						else History_preference.his_value[i]="30m";}
					if(History_preference.his_title[i].compareTo("일산화탄소 감지")==0){
						History_preference.his_title[i]="Carbon monoxide Emergency";}
					if(History_preference.his_title[i].compareTo("온도이상 감지")==0){
						History_preference.his_title[i]="Temperature Emergency";}
					if(History_preference.his_title[i].compareTo("연기 감지")==0){
						History_preference.his_title[i]="Smoke Emergency";}
					if(History_preference.his_title[i].compareTo("블루투스 연결")==0){
						History_preference.his_title[i]="Connected";}
					if(History_preference.his_title[i].compareTo("블루투스 끊어짐")==0){
						History_preference.his_title[i]="Disconnected";}
				}
				if(Device_CO.co_status!=null){
					if(Device_CO.co_status.getText().toString().compareTo("쾌적한 상태입니다")==0) Device_CO.co_status.setText("normal");
					else if(Device_CO.co_status.getText().toString().compareTo("화재발생이 의심됩니다")==0) Device_CO.co_status.setText("Watch out! fire suspected");
				}
				if(Device_Temp.temp_status!=null){
					if(Device_Temp.temp_status.getText().toString().compareTo("화재발생이 의심됩니다")==0) Device_Temp.temp_status.setText("Watch out! fire suspected");
					else if(Device_Temp.temp_status.getText().toString().compareTo("무진장 덥습니다")==0) Device_Temp.temp_status.setText("hot");
					else if(Device_Temp.temp_status.getText().toString().compareTo("아주 따뜻합니다")==0) Device_Temp.temp_status.setText("warm");
					else if(Device_Temp.temp_status.getText().toString().compareTo("쾌적한 상태입니다")==0) Device_Temp.temp_status.setText("nice");
					else if(Device_Temp.temp_status.getText().toString().compareTo("약간 서늘합니다")==0) Device_Temp.temp_status.setText("cool");
					else if(Device_Temp.temp_status.getText().toString().compareTo("추위가 느껴집니다")==0) Device_Temp.temp_status.setText("cold");
					else if(Device_Temp.temp_status.getText().toString().compareTo("매우 찬 공기가 느껴져요")==0) Device_Temp.temp_status.setText("very cold");
				}
				if(Device_Smoke.smoke_status!=null){
					if(Device_Smoke.smoke_status.getText().toString().compareTo("쾌적한 상태입니다")==0) Device_Smoke.smoke_status.setText("normal");
					else if(Device_Smoke.smoke_status.getText().toString().compareTo("화재발생이 의심됩니다")==0) Device_Smoke.smoke_status.setText("Watch out! fire suspected");
				}
			}
			else {
				for (int i = 0; i < History_preference.history_size; i++) {
					if(History_preference.his_title[i].compareTo("Hi, nice to meet you")==0){
						History_preference.his_title[i]="환영합니다, 만나서 반갑습니다";
						History_preference.his_value[i]="앱 설치";
						History_preference.his_time1[i] = History_preference.df1.format(new Date(History_preference.packageInfo.firstInstallTime));
						History_preference.his_time2[i] = History_preference.df2.format(new Date(History_preference.packageInfo.firstInstallTime));}
					if(History_preference.his_title[i].compareTo("Sleeping mode")==0){
						History_preference.his_title[i]="잠자기 설정";
						if(History_preference.his_value[i].compareTo("None")==0)History_preference.his_value[i]="없음";
						else if(History_preference.his_value[i].compareTo("5m")==0)History_preference.his_value[i]="5분";
						else if(History_preference.his_value[i].compareTo("10m")==0)History_preference.his_value[i]="10분";
						else if(History_preference.his_value[i].compareTo("20m")==0)History_preference.his_value[i]="20분";
						else History_preference.his_value[i]="30분";}
					if(History_preference.his_title[i].compareTo("Carbon monoxide Emergency")==0){
						History_preference.his_title[i]="일산화탄소 감지";}
					if(History_preference.his_title[i].compareTo("Temperature Emergency")==0){
						History_preference.his_title[i]="온도이상 감지";}
					if(History_preference.his_title[i].compareTo("Smoke Emergency")==0){
						History_preference.his_title[i]="연기 감지";}
					if(History_preference.his_title[i].compareTo("Connected")==0){
						History_preference.his_title[i]="블루투스 연결";}
					if(History_preference.his_title[i].compareTo("Disconnected")==0){
						History_preference.his_title[i]="블루투스 끊어짐";}
				}
				if(Device_CO.co_status!=null){
					if(Device_CO.co_status.getText().toString().compareTo("normal")==0) Device_CO.co_status.setText("쾌적한 상태입니다");
					else if(Device_CO.co_status.getText().toString().compareTo("Watch out! fire suspected")==0) Device_CO.co_status.setText("화재발생이 의심됩니다");
				}
				if(Device_Temp.temp_status!=null){
					if(Device_Temp.temp_status.getText().toString().compareTo("Watch out! fire suspected")==0) Device_Temp.temp_status.setText("화재발생이 의심됩니다");
					else if(Device_Temp.temp_status.getText().toString().compareTo("hot")==0) Device_Temp.temp_status.setText("무진장 덥습니다");
					else if(Device_Temp.temp_status.getText().toString().compareTo("warm")==0) Device_Temp.temp_status.setText("아주 따뜻합니다");
					else if(Device_Temp.temp_status.getText().toString().compareTo("nice")==0) Device_Temp.temp_status.setText("쾌적한 상태입니다");
					else if(Device_Temp.temp_status.getText().toString().compareTo("cool")==0) Device_Temp.temp_status.setText("약간 서늘합니다");
					else if(Device_Temp.temp_status.getText().toString().compareTo("cold")==0) Device_Temp.temp_status.setText("추위가 느껴집니다");
					else if(Device_Temp.temp_status.getText().toString().compareTo("very cold")==0) Device_Temp.temp_status.setText("매우 찬 공기가 느껴져요");
				}
				if(Device_Smoke.smoke_status!=null){
					if(Device_Smoke.smoke_status.getText().toString().compareTo("normal")==0) Device_Smoke.smoke_status.setText("쾌적한 상태입니다");
					else if(Device_Smoke.smoke_status.getText().toString().compareTo("Watch out! fire suspected")==0) Device_Smoke.smoke_status.setText("화재발생이 의심됩니다");
				}
			}
			//Create History bean
			for (int i = 0; i < History_preference.history_size; i++) {
				History_bean objHistory = new History_bean();
				objHistory.setImg1(History_preference.his_image[i]);objHistory.setTitle(History_preference.his_title[i]);objHistory.setValue(History_preference.his_value[i]);objHistory.setTime1(History_preference.his_time1[i]);objHistory.setTime2(History_preference.his_time2[i]);
				History_preference.HistoryList.add(objHistory);
			}
			History_preference.history_size=History_preference.HistoryList.size();
		}
	}

	@Override
	protected void onPause() {
		Log.i("Check device status", "is onPause");
		// Log.d(TAG, "onPause");
		super.onPause();
		isDeviceActivity=false;
		Fragment prev = getSupportFragmentManager().findFragmentByTag("fragment_loading");
		if (prev != null) {
			DialogFragment df = (DialogFragment) prev;
			df.dismiss();
		}
		//Save battery
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("key", BatteryInformationServiceProfile.battery_data);
		editor.commit(); //important, otherwise it wouldn't save.
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter fi = new IntentFilter();
		fi.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		fi.addAction(BluetoothLeService.ACTION_DATA_WRITE);
		fi.addAction(BluetoothLeService.ACTION_DATA_READ);
/*		fi.addAction(DeviceInformationServiceProfile.ACTION_FW_REV_UPDATED);
		fi.addAction(TIOADProfile.ACTION_PREPARE_FOR_OAD);*/
		return fi;
	}

	void onViewInflated(View view) {
		// Log.d(TAG, "Gatt view ready");
		setBusy(true);

		// Set title bar to device name
		setTitle(mBluetoothDevice.getName());

		// Create GATT object
		mBtGatt = BluetoothLeService.getBtGatt();
		// Start service discovery
		if (!mServicesRdy && mBtGatt != null) {
			if (mBtLeService.getNumServices() == 0)
				discoverServices();
			else {
			}
		}
	}

	boolean isSensorTag2() {
		return mIsSensorTag2;
	}

	String firmwareRevision() {
		return mFwRev;
	}

	BluetoothGattService getOadService() {
		return mOadService;
	}

	BluetoothGattService getConnControlService() {
		return mConnControlService;
	}

	private void startPreferenceActivity() {
		// Launch preferences
		final Intent i = new Intent(this, PreferencesActivity.class);
		//final Intent i = new Intent(this, FragementPreference.class);
		i.putExtra(PreferencesActivity.EXTRA_SHOW_FRAGMENT,
				PreferencesFragment.class.getName());
		i.putExtra(PreferencesActivity.EXTRA_NO_HEADERS, true);
		i.putExtra(EXTRA_DEVICE, mBluetoothDevice);
		startActivityForResult(i, PREF_ACT_REQ);
	}

	private void discoverServices() {
		if (mBtGatt.discoverServices()) {
			mServiceList.clear();
			setBusy(true);

		} else {

		}
	}

	private void setBusy(boolean b) {
		mDeviceView.setBusy(b);
		mDeviceView_1.setBusy(b);
		mDeviceView_2.setBusy(b);
		//
		// mDeviceView_3.setBusy(b);
	}

	// Activity result handling
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			default:
				break;
		}
	}

	private void setError(String txt) {
		setBusy(false);
		Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
	}

	private void setStatus(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}


	final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		List<BluetoothGattService> serviceList;
		List<BluetoothGattCharacteristic> charList = new ArrayList<BluetoothGattCharacteristic>();

		@Override
		public void onReceive(final Context context, Intent intent) {
			final String action = intent.getAction();
			final int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
					BluetoothGatt.GATT_SUCCESS);
			Log.i("Check device status", "is onRecieve "+action+"; "+status+"; "+ Connection.action);
			//Log.i("Check onReceive","value of action: "+action);
			if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					Fragment prev = getSupportFragmentManager().findFragmentByTag("fragment_loading");
					if (prev != null) {
						DialogFragment df = (DialogFragment) prev;
						df.dismiss();
					};
//					serviceList = null;
					serviceList = mBtLeService.getSupportedGattServices();
					Log.d("DeviceActivity", "Total service " + serviceList);
					if (serviceList.size() > 0) {
						for (int ii = 0; ii < serviceList.size(); ii++) {
							BluetoothGattService s = serviceList.get(ii);
							Log.d("DeviceActivity", "service UUID" + s.getUuid().toString());
							List<BluetoothGattCharacteristic> c = s.getCharacteristics();
							if (c.size() > 0) {
								for (int jj = 0; jj < c.size(); jj++) {
									charList.add(c.get(jj));
								}
							}
						}
					}
					Log.d("DeviceActivity", "Total characteristics " + charList.size());

					//
					Thread transfer = new Thread() {
						@Override
						public void run() {
							while (!isInterrupted()) {
								try {
									Thread.sleep(30000); //1000=1초

									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Device_Temp device_temp = new Device_Temp();
											device_temp.Data_temp_co_smoke_TranferToServer();

                                            //LocationFenceActivity lfa = new LocationFenceActivity();
											//lfa.onStart();
											new getData().execute("");




										}
									});
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					};
					transfer.start();


//					Thread transferjson= new Thread() {
//						@Override
//						public void run() {
//							while (!isInterrupted()) {
//								try {
//									Thread.sleep(10000); //1000=1초
//
//									runOnUiThread(new Runnable() {
//										@Override
//										public void run() {
//                                            getDataJson gdj = new getDataJson();
//                                        }
//									});
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//							}
//						}
//					};
//					transferjson.start();


					Thread worker = new Thread(new Runnable() {
						@Override
						public void run() {

							//Iterate through the services and add GenericBluetoothServices for each service
							int nrNotificationsOn = 0;
							int maxNotifications;
							int servicesDiscovered = 0;
							int totalCharacteristics = 0;
							//serviceList = mBtLeService.getSupportedGattServices();
							for (BluetoothGattService s : serviceList) {
								List<BluetoothGattCharacteristic> chars = s.getCharacteristics();
								totalCharacteristics += chars.size();
							}
							//Log.i("Check total","value: "+totalCharacteristics);
							if (totalCharacteristics == 0) {
								//Something bad happened, we have a problem
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Fragment prev = getSupportFragmentManager().findFragmentByTag("fragment_loading");
										if (prev != null) {
											DialogFragment df = (DialogFragment) prev;
											df.dismiss();
										}
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
										alertDialogBuilder.setTitle("Error !");
										alertDialogBuilder.setMessage(serviceList.size() + " Services found, but no characteristics found, device will be disconnected !");
										alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												mBtLeService.refreshDeviceCache(mBtGatt);
												//Try again
												discoverServices();
											}
										});
										alertDialogBuilder.setNegativeButton("Disconnect", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												mBtLeService.disconnect(mBluetoothDevice.getAddress());
											}
										});
										AlertDialog a = alertDialogBuilder.create();
										a.show();
									}
								});
								return;
							}
							if (Build.VERSION.SDK_INT > 18) maxNotifications = 7;
							else {
								maxNotifications = 4;
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(context, "Android version 4.3 detected, max 4 notifications enabled", Toast.LENGTH_LONG).show();
									}
								});
							}
							for (int ii = 0; ii < serviceList.size(); ii++) {
								BluetoothGattService s = serviceList.get(ii);
								List<BluetoothGattCharacteristic> chars = s.getCharacteristics();
								if (chars.size() == 0) {

									Log.d("DeviceActivity", "No characteristics found for this service !!!");
									return;
								}
								servicesDiscovered++;
								final float serviceDiscoveredcalc = (float) servicesDiscovered;
								final float serviceTotalcalc = (float) serviceList.size();
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										//progressDialog.setProgress((int) ((serviceDiscoveredcalc / (serviceTotalcalc - 1)) * 100));
									}
								});
								Log.d("DeviceActivity", "Configuring service with uuid : " + s.getUuid().toString());
								if (SensorTagIRTemperatureProfile.isCorrectService(s)) {
									SensorTagIRTemperatureProfile irTemp = new SensorTagIRTemperatureProfile(context,mBluetoothDevice,s,mBtLeService);
									mProfiles = irTemp;
									current_time=Calendar.getInstance();
//									Device_Temp device_temp = new Device_Temp();
//									device_temp.TempTranferToServer();
									if (nrNotificationsOn < maxNotifications) {
										irTemp.configureService();
										nrNotificationsOn++;
									} else {
										irTemp.grayOutCell(true);
									}

									Log.d("DeviceActivity", "Found IR Temperature !");
								}

								if (SensorTagSmokeProfile.isCorrectService(s)) {
									SensorTagSmokeProfile smoTemp = new SensorTagSmokeProfile(context, mBluetoothDevice, s, mBtLeService);
									mProfiles_1 = smoTemp;
									if (nrNotificationsOn < maxNotifications) {
										smoTemp.configureService();
										nrNotificationsOn++;
									} else {
										smoTemp.grayOutCell(true);
									}

									Log.d("DeviceActivity", "Found Smoke Temperature !");
								}

								if (SensorTagCOProfile.isCorrectService(s)) {
									SensorTagCOProfile coTemp = new SensorTagCOProfile(context, mBluetoothDevice, s, mBtLeService);
									mProfiles_2 = coTemp;
									if (nrNotificationsOn < maxNotifications) {
										coTemp.configureService();
										nrNotificationsOn++;
									} else {
										coTemp.grayOutCell(true);
									}
									Log.d("DeviceActivity", "Found CO !");
								}
								if (BatteryInformationServiceProfile.isCorrectService(s)) {
									BatteryInformationServiceProfile BaInfo = new BatteryInformationServiceProfile(context,mBluetoothDevice,s,mBtLeService);
									mProfiles_3=BaInfo;
									if (nrNotificationsOn < maxNotifications) {
										BaInfo.configureService();
										nrNotificationsOn++;
									} else {
										BaInfo.grayOutCell(true);
									}
									Log.d("DeviceActivity", "Found Battery !");
								}
								if ((s.getUuid().toString().compareTo(SensorTagGatt.UUID_IO_SERV.toString())) == 0) {
									in_out = new InOut_Service(context,mBluetoothDevice,s,mBtLeService);
									mProfiles_4=in_out;
									//alway turn on the light when starting
									//Lighting_preference.light_on=true;
									if( Lighting_preference.light_on==true)
									{
										byte value_config = (byte) (Listview_light.IO_config);
										byte value_data = (byte) (Listview_light.IO_data);
										//Log.i("Check in/out","value_device: "+value_config+";"+value_data);
										mBtLeService.writeCharacteristic(InOut_Service.charc_config, value_config);
										mBtLeService.writeCharacteristic(InOut_Service.charac_data, value_data);
									}
									else{
										mBtLeService.writeCharacteristic(InOut_Service.charc_config, (byte) (0x01));
										mBtLeService.writeCharacteristic(InOut_Service.charac_data, (byte) (0x00));
									}
									//in_out.enableService();
									Log.d("DeviceActivity", "Found In/Out service!");

								}
								if (DeviceInformationServiceProfile.isCorrectService(s)) {
									DeviceInformationServiceProfile devInfo = new DeviceInformationServiceProfile(context,mBluetoothDevice,s,mBtLeService);
									mProfiles_5=devInfo;
									devInfo.configureService();
									Log.d("DeviceActivity","Found Device Information Service");
								}
								if(OADService.isCorrectService(s)){
									OADService oadService= new OADService(context,mBluetoothDevice,s,mBtLeService);
									mProfiles_6=oadService;
									mOadService=s;
								}

							}


							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									//add temperature to textview and graph
//									Device_Temp device_temp = new Device_Temp();
//									device_temp.TempTranferToServer();
									mDeviceView.add_value(mProfiles.get_data());

									//mDeviceView.add_map();

									//add graph
									mProfiles.enableService();
									//progressDialog.setProgress(progressDialog.getProgress() + 1);
								}
							});
							mProfiles.onResume();


//							runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									//transfer data to server
//									try {
//
//										mDeviceView.TempTranferToServer();
//										mProfiles.enableService();
//										//Device_Temp device_temp = new Device_Temp();
//										//device_temp.TempTranferToServer();
//										Thread.sleep(2000);
//									} catch (Exception e) {
//
//									}
//								}
//							});
//							mProfiles.onResume();


							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mDeviceView_1.add_value(mProfiles_1.get_data());
									//mDeviceView_1.add_map();
									mProfiles_1.enableService();
									//progressDialog.setProgress(progressDialog.getProgress() + 1);
								}
							});
							mProfiles_1.onResume();

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mDeviceView_2.add_value(mProfiles_2.get_data());
									//mDeviceView_2.add_map();
									mProfiles_2.enableService();
									//progressDialog.setProgress(progressDialog.getProgress() + 1);
								}
							});
							mProfiles_2.onResume();

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									//mDeviceView_3.add_value(mProfiles_3.get_data());
									mProfiles_3.enableService();
									//Log.i("enableService", "Battery Service is enable");
								}
							});
							mProfiles_3.onResume();

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									//mDeviceView_3.add_value(mProfiles_3.get_data());
									mProfiles_4.enableService();
									//Log.i("enableService", "In out Service is enable");
								}
							});
							mProfiles_4.onResume();

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									//mDeviceView_3.add_value(mProfiles_3.get_data());
									mProfiles_5.enableService();
									//Log.i("enableService", "Device Information Service is enable");
								}
							});
							mProfiles_5.onResume();

							/*runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mProfiles_6.enableService();
								}
							});
							mProfiles_6.onResume();*/
/*							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									*//*progressDialog.hide();
									progressDialog.dismiss();*//*
									Fragment prev = getSupportFragmentManager().findFragmentByTag("fragment_loading");
									if (prev != null) {
										DialogFragment df = (DialogFragment) prev;
										df.dismiss();
									}
								}
							});*/
						}
					});
					worker.start();
				} else {
					Toast.makeText(getApplication(), "Service discovery failed",
							Toast.LENGTH_LONG).show();
					return;
				}
			} else if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
				// Notification

				byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);

				for (int ii = 0; ii < charList.size(); ii++) {
					BluetoothGattCharacteristic tempC = charList.get(ii);
					Log.i("Check update","BluetoothGattCharacteristic: "+tempC.getUuid());
					if ((tempC.getUuid().toString().equals(uuidStr))) {
						//Temperature
						GenericBluetoothProfile p = mProfiles;
						if (p.isDataC(tempC)) {
							p.didUpdateValueForCharacteristic(tempC);// update new data of sensor
						}
						//Smoke
						GenericBluetoothProfile p_1 = mProfiles_1;
						if (p_1.isDataC(tempC)) {
							p_1.didUpdateValueForCharacteristic(tempC);// update new data of sensor
						}
						//CO
						GenericBluetoothProfile p_2 = mProfiles_2;
						if (p_2.isDataC(tempC)) {
							p_2.didUpdateValueForCharacteristic(tempC);// update new data of sensor
						}
						//Battery
						GenericBluetoothProfile p_3 = mProfiles_3;
						if (p_3.isDataC(tempC)) {
							p_3.didUpdateValueForCharacteristic(tempC);
						}
						break;
					}
				}

				//Alarm
//				Log.i("Check alarm", "isAlarm 1:" + isAlarm);
				Vibrator phone_vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				if((SensorTagIRTemperatureProfile.temp_alarm==true)||(SensorTagSmokeProfile.smoke_alarm ==true)||(SensorTagCOProfile.co_alarm==true))
				{
					if(isAlarm==true){
						//Do nothing
					}
					else {
						//Start alarm
						isAlarm=true;
						//isNaturalRelaease=true;
						save_history();
						//Log.i("Check location","Alarm situiation:"+History_preference.history_size);
						// Comeback to the main screen
						if(Alias.is_alias==true){
							Alias.alias_setting_back.callOnClick();
							Alias.is_alias=false;
							ETC_preference.etc_setting_back.callOnClick();
							ETC_preference.is_etc=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Language_setting.is_language==true){
							Language_setting.language_setting_back.callOnClick();
							Language_setting.is_language=false;
							ETC_preference.etc_setting_back.callOnClick();
							ETC_preference.is_etc=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Unit_setting.is_unit==true){
							//isAlarm=true;
							Unit_setting.unit_setting_back.callOnClick();
							Unit_setting.is_unit=false;
							ETC_preference.etc_setting_back.callOnClick();
							ETC_preference.is_etc=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Legal_information.is_legal==true){
							//isAlarm=true;
							Legal_information.legal_setting_back.callOnClick();
							Legal_information.is_legal=false;
							ETC_preference.etc_setting_back.callOnClick();
							ETC_preference.is_etc=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Version.is_version==true){
							//isAlarm=true;
							Version.version_setting_back.callOnClick();
							Version.is_version=false;
							ETC_preference.etc_setting_back.callOnClick();
							ETC_preference.is_etc=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Session_out.is_session==true){
							//isAlarm=true;
							Session_out.session_setting_back.callOnClick();
							Session_out.is_session=false;
							ETC_preference.etc_setting_back.callOnClick();
							ETC_preference.is_etc=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Notify_friend_preference.is_notify==true){
							//isAlarm=true;
							Notify_friend_preference.notify_setting_back.callOnClick();
							Notify_friend_preference.is_notify=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Message_content_preference.is_message==true){
							//isAlarm=true;
							Message_content_preference.message_setting_back.callOnClick();
							Message_content_preference.is_message=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(Lighting_preference.is_lighting==true){
							//isAlarm=true;
							Lighting_preference.lighting_back.callOnClick();
							Lighting_preference.is_lighting=false;
							if(PreferencesActivity.setting_back!=null){
										PreferencesActivity.setting_back.callOnClick();
										PreferencesActivity.is_preference=false;
										//alarm_status();
									}
						}
						else if(Customer_preference.is_customer==true){
							//isAlarm=true;
							Customer_preference.message_setting_back.callOnClick();
							Customer_preference.is_customer=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(History_preference.is_history==true){
							//isAlarm=true;
							History_preference.history_setting_back.callOnClick();
							History_preference.is_history=false;
							if(PreferencesActivity.setting_back!=null){
								PreferencesActivity.setting_back.callOnClick();
								PreferencesActivity.is_preference=false;}
						}
						else if(Operation_test_preference.is_test==true){
							//isAlarm=true;
							Operation_test_preference.operation_test_back.callOnClick();
							Operation_test_preference.is_test=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(ETC_preference.is_etc==true){
							//isAlarm=true;
							ETC_preference.etc_setting_back.callOnClick();
							ETC_preference.is_etc=false;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						else if(PreferencesActivity.is_preference==true){
							//isAlarm=true;
							PreferencesActivity.setting_back.callOnClick();
							PreferencesActivity.is_preference=false;
						}
						startAlarmService = new Intent(DeviceActivity.this, StartAlarmService.class);
						startService(startAlarmService);
					}
				}
				else if((SensorTagIRTemperatureProfile.temp_alarm==false)&&(SensorTagSmokeProfile.smoke_alarm ==false)&&(SensorTagCOProfile.co_alarm==false)){

					new StopAlarm().execute();

				}

			} else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
				// Data written
				byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				for (int ii = 0; ii < charList.size(); ii++) {
					BluetoothGattCharacteristic tempC = charList.get(ii);
					if ((tempC.getUuid().toString().equals(uuidStr))) {

						GenericBluetoothProfile p = mProfiles;
						p.didWriteValueForCharacteristic(tempC);


						GenericBluetoothProfile p_1 = mProfiles_1;
						p_1.didWriteValueForCharacteristic(tempC);


						GenericBluetoothProfile p_2 = mProfiles_2;
						p_2.didWriteValueForCharacteristic(tempC);


						GenericBluetoothProfile p_3 = mProfiles_3;
						p_3.didWriteValueForCharacteristic(tempC);

						GenericBluetoothProfile p_5 = mProfiles_5;
						p_5.didWriteValueForCharacteristic(tempC);

						//Log.d("DeviceActivity","Got Characteristic : " + tempC.getUuid().toString());
						break;
					}
				}
			} else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
				// Data read
				byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				for (int ii = 0; ii < charList.size(); ii++) {
					BluetoothGattCharacteristic tempC = charList.get(ii);
					if ((tempC.getUuid().toString().equals(uuidStr))) {

						GenericBluetoothProfile p = mProfiles;
						p.didReadValueForCharacteristic(tempC);


						GenericBluetoothProfile p_1 = mProfiles_1;
						p_1.didReadValueForCharacteristic(tempC);


						GenericBluetoothProfile p_2 = mProfiles_2;
						p_2.didReadValueForCharacteristic(tempC);


						GenericBluetoothProfile p_3 = mProfiles_3;
						p_3.didReadValueForCharacteristic(tempC);

						GenericBluetoothProfile p_5 = mProfiles_5;
						p_5.didReadValueForCharacteristic(tempC);

						//Log.d("DeviceActivity","Got Characteristic : " + tempC.getUuid().toString());
						break;
					}
				}
			} else {
				if (OADService.ACTION_PREPARE_FOR_OAD.equals(action)) {
					new firmwareUpdateStart(progressDialog,context).execute();
				}
			}
			if (status != BluetoothGatt.GATT_SUCCESS) {
				setError("GATT error code: " + status);
			}
		}
	};



	class firmwareUpdateStart extends AsyncTask<String, Integer, Void> {
		ProgressDialog pd;
		Context con;

		public firmwareUpdateStart(ProgressDialog p, Context c) {
			this.pd = p;
			this.con = c;
		}

		@Override
		protected void onPreExecute() {
			this.pd = new ProgressDialog(DeviceActivity.this);
			this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			this.pd.setIndeterminate(false);
			this.pd.setTitle("Starting firmware update");
			this.pd.setMessage("");
			this.pd.setMax(5);
			this.pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			Integer ii = 1;

			mProfiles.disableService();
			mProfiles.deConfigureService();
			publishProgress(ii);

			Integer ii_1 = 1;

			mProfiles_1.disableService();
			mProfiles_1.deConfigureService();
			publishProgress(ii_1);

			Integer ii_2 = 1;

			mProfiles_2.disableService();
			mProfiles_2.deConfigureService();
			publishProgress(ii_2);

			Integer ii_3 = 1;

			mProfiles_3.disableService();
			mProfiles_3.deConfigureService();
			publishProgress(ii_3);

			Integer ii_4 = 1;

			mProfiles_4.disableService();
			mProfiles_4.deConfigureService();
			publishProgress(ii_4);

			Integer ii_5 = 1;

			mProfiles_5.disableService();
			mProfiles_5.deConfigureService();
			publishProgress(ii_5);


			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			this.pd.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			this.pd.dismiss();
			super.onPostExecute(result);
		}

	}
	@Override
	protected void onStop()
	{
		//unregisterReceiver(mGattUpdateReceiver);
		super.onStop();
		unregisterFences();
		unregisterReceiver(mLocationFenceReceiver);
	}
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte b: a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
	public static int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16*val + d;
		}
		return val;
	}
	public void start_service(){
		//intentMyIntentService = new Intent(Connection.this, Scan_service.class);
		intentMyIntentService = new Intent(DeviceActivity.this, Autoconnect_service.class);
//		intentMyIntentService = new Intent(DeviceActivity.this, Autoconnect_service_seperate.class);
		startService(intentMyIntentService);
	}
	public class BackgroundSound extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			MediaPlayer player = MediaPlayer.create(context, R.raw.siren);
			player.setLooping(true); // Set looping
			player.setVolume(1.0f, 1.0f);
			player.start();

			return null;
		}

	}
	public void alarm_status()
	{
		//isAlarm = true;
		if(Message_content_preference.show_message.compareTo(getResources().getString(R.string.message_content_item_1))==0){
			AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			//mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
			Music.play(context, R.raw.siren);
		}
		else if(Message_content_preference.show_message.compareTo(getResources().getString(R.string.message_content_item_2))==0){
			AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			//mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
			Music.play(context, R.raw.fire_car);
		}
		else if(Message_content_preference.show_message.compareTo(getResources().getString(R.string.message_content_item_3))==0){
			AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			//mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
			Music.play(context, R.raw.emergency1);
		}
		else {
			AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			//mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
			Music.play(context, R.raw.emergency2);
		}
		Vibrator phone_vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		phone_vib.vibrate(3000);
		send_num.send_sms(context);
		Alarm frag = new Alarm();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(frag, "fragment_alarm");
		ft.commitAllowingStateLoss();

	}
	void save_history(){
		//Save to history part
		History_preference.co_emergecy=getResources().getString(R.string.warning_history_co);
		History_preference.temp_emergecy=getResources().getString(R.string.warning_history_temperature);
		History_preference.smoke_emergecy=getResources().getString(R.string.warning_history_smoke);
		if(SensorTagIRTemperatureProfile.temp_alarm==true){
			History_bean objHistory = new History_bean();
			objHistory.setImg1(History_preference.temp_icon);objHistory.setTitle(History_preference.temp_emergecy);objHistory.setValue(SensorTagIRTemperatureProfile.alarm_value);objHistory.setTime1(SensorTagIRTemperatureProfile.time1);objHistory.setTime2(SensorTagIRTemperatureProfile.time2);
			History_preference.HistoryList.add(0,objHistory);
		}
		if(SensorTagSmokeProfile.smoke_alarm ==true){
			History_bean objHistory = new History_bean();
			objHistory.setImg1(History_preference.smoke_icon);objHistory.setTitle(History_preference.smoke_emergecy);objHistory.setValue(SensorTagSmokeProfile.alarm_value);objHistory.setTime1(SensorTagSmokeProfile.time1);objHistory.setTime2(SensorTagSmokeProfile.time2);
			History_preference.HistoryList.add(0,objHistory);
		}
		if(SensorTagCOProfile.co_alarm ==true){
			History_bean objHistory = new History_bean();
			objHistory.setImg1(History_preference.co_icon);objHistory.setTitle(History_preference.co_emergecy);objHistory.setValue(SensorTagCOProfile.alarm_value);objHistory.setTime1(SensorTagCOProfile.time1);objHistory.setTime2(SensorTagCOProfile.time2);
			History_preference.HistoryList.add(0,objHistory);
		}
		History_preference.history_size=History_preference.HistoryList.size();
		SharedPreferences prefs_history_size = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor_history_size = prefs_history_size.edit();
		editor_history_size.putInt("history_size", History_preference.history_size);
		editor_history_size.commit(); //important, otherwise it wouldn't save.
		//Save history image
		SharedPreferences[] prefs_history_img=new SharedPreferences[History_preference.history_size];
		SharedPreferences.Editor[] editor_history_img= new SharedPreferences.Editor[History_preference.history_size];
		for(int i=0;i<History_preference.history_size;i++) {
			prefs_history_img[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			editor_history_img[i] = prefs_history_img[i].edit();
			editor_history_img[i].putInt("history_img_" + Integer.toString(i), History_preference.HistoryList.get(i).getImg1());
			editor_history_img[i].commit(); //important, otherwise it wouldn't save.
		}
		//Save history title
		SharedPreferences[] prefs_history_title=new SharedPreferences[History_preference.history_size];
		SharedPreferences.Editor[] editor_history_title= new SharedPreferences.Editor[History_preference.history_size];
		for(int i=0;i<History_preference.history_size;i++) {
			prefs_history_title[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			editor_history_title[i] = prefs_history_title[i].edit();
			editor_history_title[i].putString("history_title_" + Integer.toString(i), History_preference.HistoryList.get(i).getTitle());
			editor_history_title[i].commit(); //important, otherwise it wouldn't save.
		}
		//Save history value
		SharedPreferences[] prefs_history_value=new SharedPreferences[History_preference.history_size];
		SharedPreferences.Editor[] editor_history_value= new SharedPreferences.Editor[History_preference.history_size];
		for(int i=0;i<History_preference.history_size;i++) {
			prefs_history_value[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			editor_history_value[i] = prefs_history_value[i].edit();
			editor_history_value[i].putString("history_value_" + Integer.toString(i), History_preference.HistoryList.get(i).getValue());
			editor_history_value[i].commit(); //important, otherwise it wouldn't save.
		}
		//Save history time1
		SharedPreferences[] prefs_history_time1=new SharedPreferences[History_preference.history_size];
		SharedPreferences.Editor[] editor_history_time1= new SharedPreferences.Editor[History_preference.history_size];
		for(int i=0;i<History_preference.history_size;i++) {
			prefs_history_time1[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			editor_history_time1[i] = prefs_history_time1[i].edit();
			editor_history_time1[i].putString("history_time1_" + Integer.toString(i), History_preference.HistoryList.get(i).getTime1());
			editor_history_time1[i].commit(); //important, otherwise it wouldn't save.
		}
		//Save history time1
		SharedPreferences[] prefs_history_time2=new SharedPreferences[History_preference.history_size];
		SharedPreferences.Editor[] editor_history_time2= new SharedPreferences.Editor[History_preference.history_size];
		for(int i=0;i<History_preference.history_size;i++) {
			prefs_history_time2[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			editor_history_time2[i] = prefs_history_time2[i].edit();
			editor_history_time2[i].putString("history_time2_" + Integer.toString(i), History_preference.HistoryList.get(i).getTime2());
			editor_history_time2[i].commit(); //important, otherwise it wouldn't save.
		}
	}
	class Alarm_background extends AsyncTask<Void, Void, Boolean> {


		public Alarm_background() {
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			alarm_status();
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

		}
	}
	class Preference_background extends AsyncTask<Void, Void, Boolean> {


		public Preference_background() {
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			startPreferenceActivity();
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

		}
	}
/*	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}*/
class StopAlarm extends AsyncTask<Void, Void, Boolean> {


	public StopAlarm() {
		if (BuildConfig.DEBUG)
			Log.v(StopAlarm.class.getName(), "StopAlarm()");
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (BuildConfig.DEBUG) Log.v(StopAlarm.class.getName(), "doInBackground()");
		if(Alarm.mActivity!=null) {
			Log.i("Check stop alarm", "run on background");
			final FragmentTransaction ft = Alarm.mActivity.getSupportFragmentManager().beginTransaction();
			final Fragment prev_alarm = Alarm.mActivity.getSupportFragmentManager().findFragmentByTag("fragment_alarm");

			if (prev_alarm != null) {
				Music.stop(getApplication());
				ft.remove(prev_alarm);

				ft.commitAllowingStateLoss();

				DeviceActivity.isAlarm = false;

				Lighting_preference.light_on = false;
				Alarm.mActivity.finish();
			}
		}
		else {
			//Do nothing
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}
}
}