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
 * Version.java: show software and hardware status
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;
import com.winsontan520.wversionmanager.library.OnReceiveListener;
import com.winsontan520.wversionmanager.library.WVersionManager;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Login.Bulldi_rule;
import openstack.bulldi.safe3x.Login.Greeting;
import openstack.bulldi.safe3x.Login.KakaoSignupActivity;
import openstack.bulldi.safe3x.MainActivity;
import openstack.bulldi.safe3x.Preference_history.History_preference;
import openstack.bulldi.safe3x.R;
import openstack.util.TextViewPlus;


public class Version extends Activity {
    public static ImageView version_setting_back;
    TextViewPlus version_title;
    public static boolean is_version=false;
    public static String f_version="1.0";
    public String currentVersion="";
    public  String newVersion="";
    TextViewPlus firmware_version;
    TextViewPlus MAC_address;
    TextViewPlus software_version;
    TextViewPlus account_information;
    TextViewPlus android_ID;
//    TextViewPlus software_version_update;
    Button sofware_upgrade_button;
//    TextViewPlus text_updateRequire;
    Button OAD_button;
    Context context;
    SharedPreferences sharedPreferences;
    public static final String ACTION_VIEW_CLICKED = "openstack.bulldi.safe3x.Preference_etc.Version.ACTION_VIEW_CLICKED";

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version);
        context=this;
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.version_titlebar);
        version_setting_back =(ImageView) findViewById(R.id.version_back);
        version_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        version_title=(TextViewPlus) findViewById(R.id.version_title);
        version_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firmware_version=(TextViewPlus) findViewById(R.id.firmware_version);
        firmware_version.setText(f_version);
        MAC_address=(TextViewPlus) findViewById(R.id.bulldi_MACaddress);
        MAC_address.setText(DeviceActivity.MAC_addr);
        software_version=(TextViewPlus) findViewById(R.id.software_version);
        try {
            PackageInfo  packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            currentVersion = packageInfo.versionName;
            if(Language_setting.is_english==true) software_version.setText(currentVersion+" (June 03, 2016)");
            else software_version.setText(currentVersion+" (2016.06.03)");
            //Log.i("Check version","value:"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        newVersion=currentVersion;
//        software_version_update=(TextViewPlus) findViewById(R.id.software_version_update);
//        software_version_update.setText(newVersion);
        sofware_upgrade_button=(Button) findViewById(R.id.sofware_upgrade_button);
        sofware_upgrade_button.setEnabled(false);
//        text_updateRequire=(TextViewPlus) findViewById(R.id.text_updateRequire);
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
                                sofware_upgrade_button.setBackgroundResource(R.drawable.button2_on);
                                sofware_upgrade_button.setEnabled(true);
                            } else {
                                sofware_upgrade_button.setBackgroundResource(R.drawable.but_trash3);
                            }
                        }
                    }
                }

                return false; // return true if you want to use library's default logic & dialog
            }
        });
            sofware_upgrade_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sofware_upgrade_button.setBackgroundResource(R.drawable.button2_off);
                    sofware_upgrade_button.setTextColor(Color.WHITE);
                    showToast(getResources().getString(R.string.version_software_update_check));
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=openstack.bulldi.safe3x"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        account_information=(TextViewPlus)findViewById(R.id.bulldi_accountInfo);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bulldi_rule.login_facbook = sharedPreferences.getBoolean("login_facebook_state", false);
        Bulldi_rule.user_id_facebook=sharedPreferences.getString("login_facebook_id", "");
        Bulldi_rule.user_mail_facebook=sharedPreferences.getString("login_facebook_mail", "");
        //Get back login status
        Bulldi_rule.login_instagram = sharedPreferences.getBoolean("login_instagram_state", false);
        Bulldi_rule.user_id_instagram=sharedPreferences.getString("login_instagram_id", "");
        Bulldi_rule.user_name_instagram=sharedPreferences.getString("login_instagram_name", "");
        //Get back login status
        KakaoSignupActivity.login_kakaotalk = sharedPreferences.getBoolean("login_kakaotalk_state", false);
        KakaoSignupActivity.userID_kakaotalk=sharedPreferences.getLong("login_kakdotalk_id", 0);
        KakaoSignupActivity.user_nickname_kakaotalk=sharedPreferences.getString("login_kakao_name","");
        //Get back login status
        Greeting.login_other = sharedPreferences.getBoolean("login_other_state", false);
        Greeting.username=sharedPreferences.getString("login_other_name", "");
        Greeting.usermail=sharedPreferences.getString("login_other_mail","");
        if(Bulldi_rule.login_facbook==true){
            account_information.setText("Facebook/"+Bulldi_rule.user_mail_facebook);
        }
        else if(Bulldi_rule.login_instagram==true){
            account_information.setText("Instagram/"+Bulldi_rule.user_name_instagram);
        }
        else  if(KakaoSignupActivity.login_kakaotalk==true){
            account_information.setText("Kakaotalk/"+KakaoSignupActivity.user_nickname_kakaotalk);
        }
        else {
            account_information.setText("Other/"+Greeting.usermail);
        }
        String iid = InstanceID.getInstance(context).getId();
        Log.i("Instance ID","value: "+iid.toUpperCase());
//        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        android_ID=(TextViewPlus) findViewById(R.id.status_androidID);
        android_ID.setText(iid.toUpperCase());
/*        OAD_button=(Button) findViewById(R.id.OAD_button);
        OAD_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Override click and launch the OAD screen
                Intent intent = new Intent(ACTION_VIEW_CLICKED);
                context.sendBroadcast(intent);
            }
        });*/
    }
    private void showToast(String msg) {
        Toast toast= Toast.makeText(this, msg, Toast.LENGTH_LONG);
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
    public void onResume(){
        super.onResume();
        is_version=true;
        if(sofware_upgrade_button!=null) {
            if (value(currentVersion) < value(newVersion)) {
                sofware_upgrade_button.setBackgroundResource(R.drawable.button2_on);
//                sofware_upgrade_button.setText(getResources().getString(R.string.version_software_update_require));
                sofware_upgrade_button.setTextColor(Color.WHITE);
//                                text_updateRequire.setText(getResources().getString(R.string.version_software_update_require));
                sofware_upgrade_button.setEnabled(true);
            } else {
                sofware_upgrade_button.setBackgroundResource(R.drawable.but_trash3);
            }
        }
        //if((DeviceActivity.isAlarm==true)&&(version_setting_back!=null)) version_setting_back.callOnClick();
    }
    @Override
    public void onPause(){
        super.onPause();
        is_version=false;
    }
    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }
    public static String[] getAppVersionInfo(String playUrl) {
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setAllowHtmlInsideAttributes(true);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);
        try {
            URL url = new URL(playUrl);
            URLConnection conn = url.openConnection();
            TagNode node = cleaner.clean(new InputStreamReader(conn.getInputStream()));
            Object[] new_nodes = node.evaluateXPath("/[@class='recent-change']");
            Object[] version_nodes = node.evaluateXPath("/[@itemprop='softwareVersion']");

            String version = "", whatsNew = "";
            for (Object new_node : new_nodes) {
                TagNode info_node = (TagNode) new_node;
                whatsNew += info_node.getAllChildren().get(0).toString().trim()
                        + "\n";
            }
            if (version_nodes.length > 0) {
                TagNode ver = (TagNode) version_nodes[0];
                version = ver.getAllChildren().get(0).toString().trim();
            }
            return new String[]{version, whatsNew};
        } catch (IOException | XPatherException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getLatestVersionNumber()
    {
        String versionNumber = "0.0.0";

        try
        {
            versionNumber = Jsoup.connect("https://play.google.com/store/apps/details?id=openstack.bulldi.safe3x&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
            return versionNumber;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return versionNumber;
    }
    private String web_update() {
        try {

                String curVersion = "0.0";
            try {
                PackageInfo  packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
                curVersion = packageInfo.versionName;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Log.i("Check version","value current:"+curVersion);
                String newVersion = curVersion;
            Document doc=Jsoup.connect("https://play.google.com/store/apps/details?id="+this.getPackageName()+"&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            Log.i("Check version","value doc:"+doc);
            Elements changeLog = doc.select("div.clDesc");
            Log.i("Check version","value changeLog:"+changeLog);
            for (Element div : changeLog)
            {
                String divText = div.text();
                Log.i("Check version","value div:"+divText);
                if (divText.contains("Version"))
                {
                    newVersion= divText.split(" ")[1];
                }

            }
            Log.i("Check version","value new:"+newVersion);
                return newVersion;
            } catch (Exception e) {
                e.printStackTrace();
                return "no";
            }
    }
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
}
