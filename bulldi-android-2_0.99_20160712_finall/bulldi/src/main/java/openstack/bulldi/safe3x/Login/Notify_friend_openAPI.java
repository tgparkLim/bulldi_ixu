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
 * Notify_friend_openAPI.java: show contact list for registering when alarm happen
 */

package openstack.bulldi.safe3x.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import openstack.bulldi.safe3x.BLE_Connection.Connection;
import openstack.bulldi.safe3x.BLE_Connection.Connection_seperate;
import openstack.bulldi.safe3x.Preference_sharing.ContactAdapter_api;
import openstack.bulldi.safe3x.Preference_sharing.ContactBean;
import openstack.bulldi.safe3x.Preference_sharing.Notify_friend_preference;
import openstack.bulldi.safe3x.R;
import openstack.util.EditTextPlus;
import openstack.util.TextViewPlus;

public class Notify_friend_openAPI extends Activity {
    private ListView listView;
    private ArrayList<ContactBean> list = new ArrayList<ContactBean>();
    private ArrayList<ContactBean> searchResults = new ArrayList<ContactBean>();
    public SharedPreferences sharedPreferences;
    ContactAdapter_api objAdapter;
    //ImageView but_add_contact;
    public  ImageView but_check_contact_api;
    public  View notify_setting_back_api;
    TextViewPlus notify_friend_api_title;
    EditTextPlus searchBox;
    ImageView but_delete_search;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        //Log.i("Check onCreate", "onCreate Notify: " + notify_contact.size());
        setContentView(R.layout.notify_friend_api);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.notify_friend_titlebar_api);
        notify_setting_back_api = findViewById(R.id.notify_friend_back_api);
        notify_setting_back_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        notify_friend_api_title=(TextViewPlus) findViewById(R.id.notify_friend_api_title);
        notify_friend_api_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        but_check_contact_api = (ImageView) findViewById(R.id.but_check_contact_api);
        //Set checked box here
        if (Notify_friend_preference.notify_contact == null) {
            Notify_friend_preference.notify_contact = new ArrayList<String>();
            Notify_friend_preference.notify_location = new ArrayList<Integer>();
            Notify_friend_preference.contact_size = 0;
        }
        searchBox=(EditTextPlus) findViewById(R.id.searchContact_api);
        but_delete_search=(ImageView) findViewById(R.id.delete_search_api);
        listView = (ListView) findViewById(R.id.list_contact_api);
        //listView.setOnItemClickListener(this);

        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC");
        //ImageView but_add_contact=(ImageView) findViewById(R.id.but_add_contact);
        while (phones.moveToNext()) {

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContactBean objContact = new ContactBean();
            objContact.setName(name);
            objContact.setPhoneNo(phoneNumber);
            list.add(objContact);

        }
        phones.close();
        searchResults=new ArrayList<ContactBean>(list);
        objAdapter = new ContactAdapter_api(this, R.layout.alluser_row, searchResults);

        listView.setAdapter(objAdapter);

        if (null != list && list.size() != 0) {
            Collections.sort(list, new Comparator<ContactBean>() {

                @Override
                public int compare(ContactBean lhs, ContactBean rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
            showToast(getResources().getString(R.string.toast_contact_notify));

        } else {
            showToast(getResources().getString(R.string.toast_no_contact));
        }
        //Search
        searchBox.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the text in the EditText
                String searchString = searchBox.getText().toString();
                int textLength = searchString.length();
                if (textLength == 0) {
                    but_delete_search.setImageResource(R.drawable.but_trash3);
                    searchResults.clear();
                    searchResults = new ArrayList<ContactBean>(list);
                    Log.i("Contact List", "size: " + list.size());
                    objAdapter = new ContactAdapter_api(Notify_friend_openAPI.this, R.layout.alluser_row, searchResults);
                    listView.setAdapter(objAdapter);
                } else {
                    but_delete_search.setImageResource(R.drawable.icn_del);
                    //clear the initial data set
                    searchResults.clear();
                    for (int i = 0; i < list.size(); i++) {
                        String playerName = list.get(i).getName();
                        String playerPhone = list.get(i).getPhoneNo();
                        if (textLength <= playerName.length()) {
                            //compare the String in EditText with Names in the ArrayList
                            if (searchString.equalsIgnoreCase(playerName.substring(0, textLength)) || searchString.equalsIgnoreCase(playerPhone.substring(0, textLength)))
                                searchResults.add(list.get(i));
                        }
                    }

                    objAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

        });
        //Delete search
        but_delete_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.setText("");
            }
        });
        //Set check box here
        //objAdapter.set_checkbox(notify_location);
        but_check_contact_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_check_contact_api.setImageResource(R.drawable.but_arrow_on);
                Notify_friend_preference.notify_contact = new ArrayList<String>();
                Notify_friend_preference.notify_location = new ArrayList<Integer>();
                Notify_friend_preference.contact_size = 0;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list.size(); i++) {
                            ContactBean bean = list.get(i);
                            if (bean.isSelected()) {
                                Notify_friend_preference.notify_contact.add(bean.getPhoneNo());
                                Notify_friend_preference.notify_location.add(i);
                            }
                        }
                        Notify_friend_preference.contact_size = Notify_friend_preference.notify_contact.size();
                        but_check_contact_api.setImageResource(R.drawable.but_arrow_off);
                        //finish();
//                        Intent i=new Intent(Notify_friend_openAPI.this, Connection_seperate.class);
                        Intent i=new Intent(Notify_friend_openAPI.this, Connection.class);
                        startActivity(i);
                    }
                }, 10);
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
    protected void onPause() {
        super.onPause();

        //Save agreement
        SharedPreferences prefs_agreement = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_agreement = prefs_agreement.edit();
        editor_agreement.putBoolean("rule_agreement", Bulldi_rule.is_checked);
        editor_agreement.commit(); //important, otherwise it wouldn't save.

        //Save contact size
        SharedPreferences prefs_contact_size = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_contact_size = prefs_contact_size.edit();
        editor_contact_size.putInt("contact_size", Notify_friend_preference.contact_size);
        editor_contact_size.commit(); //important, otherwise it wouldn't save.
        //Save contact
        SharedPreferences[] prefs_contact = new SharedPreferences[Notify_friend_preference.notify_contact.size()];
        SharedPreferences.Editor[] editor_contact = new SharedPreferences.Editor[Notify_friend_preference.notify_contact.size()];
        for (int i = 0; i < Notify_friend_preference.notify_contact.size(); i++) {
            prefs_contact[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_contact[i] = prefs_contact[i].edit();
            editor_contact[i].putString("contact_" + Integer.toString(i), Notify_friend_preference.notify_contact.get(i));
            editor_contact[i].commit(); //important, otherwise it wouldn't save.
        }
        //Save contact location
        SharedPreferences[] prefs_location = new SharedPreferences[Notify_friend_preference.notify_contact.size()];
        SharedPreferences.Editor[] editor_location = new SharedPreferences.Editor[Notify_friend_preference.notify_contact.size()];
        for (int i = 0; i < Notify_friend_preference.notify_location.size(); i++) {
            prefs_location[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_location[i] = prefs_location[i].edit();
            editor_location[i].putInt("contact_location" + Integer.toString(i), Notify_friend_preference.notify_location.get(i));
            editor_location[i].commit(); //important, otherwise it wouldn't save.
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bulldi_rule.is_checked=true;
        //Get contact_size back
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Notify_friend_preference.contact_size = sharedPreferences.getInt("contact_size", 0);
        //Get data contact list back
        Notify_friend_preference.notify_contact = new ArrayList<String>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String contact = sharedPreferences.getString("contact_" + Integer.toString(i), "empty");
            //Log.i("Check backup", "value of contact onResume of Notify friend: " + contact);
            Notify_friend_preference.notify_contact.add(contact);
        }
        //Get location back
        Notify_friend_preference.notify_location = new ArrayList<Integer>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Integer location = sharedPreferences.getInt("contact_location" + Integer.toString(i), 0);
            //Log.i("Check backup", "value of location: " + location);
            Notify_friend_preference.notify_location.add(location);
        }

        //Set checked box here
        List<Integer> notify_check=new ArrayList<Integer>();
        if((Notify_friend_preference.contact_size>0) && (objAdapter.getSize()>0)) {
            for(int i=0;i<objAdapter.getSize();i++){
                ContactBean x=objAdapter.getContact(i);
                for(int j=0;j<Notify_friend_preference.contact_size;j++){
                    if(x.getPhoneNo().toString().compareTo(Notify_friend_preference.notify_contact.get(j).toString())==0){
                        notify_check.add(i);
                    }
                }
            }
            //objAdapter.set_checkbox(Notify_friend_preference.notify_location);
            objAdapter.set_checkbox(notify_check);
        }
    }
}
