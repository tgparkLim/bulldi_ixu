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
 * Notify_friend_preference.java: Notify my friends show
 */

package openstack.bulldi.safe3x.Preference_sharing;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.EditTextPlus;
import openstack.util.TextViewPlus;


//public class Notify_friend_preference extends Activity implements AdapterView.OnItemClickListener{
public class Notify_friend_preference extends Activity {
    public static List<String> notify_contact;
    public static List<String> notify_name;
    public static List<Integer> notify_location;
    public static int contact_size;
    private ListView listView;
    public static ListView listView_selected;
    public static ArrayList<ContactBean> list;
    public static ArrayList<ContactBean> list_selected;
    private ArrayList<ContactBean> searchResults = new ArrayList<ContactBean>();
    public SharedPreferences sharedPreferences;
    public static ContactAdapter objAdapter;
    public static ContactAdapter_slected objAdapter_selected;
    //ImageView but_add_contact;
    public static ImageView but_check_contact;
    public static boolean is_notify=false;
    public static View notify_setting_back;
    public TextViewPlus notify_friend_title;
     EditTextPlus searchBox;
    ImageView but_delete_search;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        //Log.i("Check onCreate", "onCreate Notify: " + notify_contact.size());
        list = new ArrayList<ContactBean>();
        list_selected = new ArrayList<ContactBean>();
        setContentView(R.layout.notify_friend);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.notify_friend_titlebar);
        notify_setting_back= findViewById(R.id.notify_friend_back);
        notify_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        but_check_contact=(ImageView) findViewById(R.id.but_check_contact);

        notify_friend_title=(TextViewPlus) findViewById(R.id.notify_friend_title);
        notify_friend_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(notify_contact==null){
            notify_contact=new ArrayList<String>();
            notify_location=new ArrayList<Integer>();
            notify_name=new ArrayList<String>();
            contact_size=0;
        }
        searchBox=(EditTextPlus) findViewById(R.id.searchContact);
        but_delete_search=(ImageView) findViewById(R.id.delete_search);
        listView_selected = (ListView) findViewById(R.id.list_selected_contact);
        listView = (ListView) findViewById(R.id.list_contact);


        //Section 2
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
        //searchResults=list;
        searchResults=new ArrayList<ContactBean>(list);
        objAdapter = new ContactAdapter(this, R.layout.alluser_row, searchResults);

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
                    objAdapter = new ContactAdapter(Notify_friend_preference.this, R.layout.alluser_row, searchResults);
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
        but_check_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_check_contact.setImageResource(R.drawable.but_arrow_on);
                finish();
            }
        });

        //Get contact_size back
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Notify_friend_preference.contact_size = sharedPreferences.getInt("contact_size", 0);
        //Get data contact list back
        Notify_friend_preference.notify_contact=new ArrayList<String>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String contact = sharedPreferences.getString("contact_" + Integer.toString(i), "empty");
            //Log.i("Check backup", "value of contact onResume of Notify friend: " + contact);
            Notify_friend_preference.notify_contact.add(contact);
        }
        //Get data name list back
        Notify_friend_preference.notify_name=new ArrayList<String>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String namecontact = sharedPreferences.getString("name_contact_" + Integer.toString(i), "empty");
            //Log.i("Check backup", "value of contact onResume of Notify friend: " + contact);
            Notify_friend_preference.notify_name.add(namecontact);
        }
        //Get location back
        Notify_friend_preference.notify_location=new ArrayList<Integer>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Integer location = sharedPreferences.getInt("contact_location" + Integer.toString(i), 0);
            //Log.i("Check backup", "value of location: " + location);
            Notify_friend_preference.notify_location.add(location);
        }
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
            objAdapter.set_checkbox(notify_check);
        }

        //Section 1
        for (int i=0;i<contact_size;i++)
        {

            String name = notify_name.get(i);

            String phoneNumber = notify_contact.get(i);
            ContactBean objContact_selected = new ContactBean();
            objContact_selected.setName(name);
            objContact_selected.setPhoneNo(phoneNumber);
            objContact_selected.setSelected(true);
            list_selected.add(objContact_selected);
        }
        objAdapter_selected = new ContactAdapter_slected(this, R.layout.alluser_row, list_selected);
        if(objAdapter_selected.getSize() > 2){
            View item = objAdapter_selected.getView(0, null, listView_selected);
            item.measure(0, 0);
            ViewGroup.LayoutParams params = listView_selected.getLayoutParams();
            params.width = ViewGroup.LayoutParams .MATCH_PARENT;
            params.height =(int) (2.5 * item.getMeasuredHeight());
            listView_selected.setLayoutParams(params);
        }
        listView_selected.setAdapter(objAdapter_selected);

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
        is_notify=false;
        //Save contact size
        SharedPreferences prefs_contact_size = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor_contact_size = prefs_contact_size.edit();
        editor_contact_size.putInt("contact_size", contact_size);
        editor_contact_size.commit(); //important, otherwise it wouldn't save.
        //Save contact
        SharedPreferences[] prefs_contact=new SharedPreferences[notify_contact.size()];
        SharedPreferences.Editor[] editor_contact= new SharedPreferences.Editor[notify_contact.size()];
        for(int i=0;i<notify_contact.size();i++) {
            prefs_contact[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_contact[i] = prefs_contact[i].edit();
            editor_contact[i].putString("contact_"+Integer.toString(i), notify_contact.get(i));
            editor_contact[i].commit(); //important, otherwise it wouldn't save.
        }
        //Save contact name
        SharedPreferences[] prefs_contactname=new SharedPreferences[notify_contact.size()];
        SharedPreferences.Editor[] editor_contactname= new SharedPreferences.Editor[notify_contact.size()];
        for(int i=0;i<notify_name.size();i++) {
            prefs_contactname[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_contactname[i] = prefs_contactname[i].edit();
            editor_contactname[i].putString("name_contact_"+Integer.toString(i), notify_name.get(i));
            editor_contactname[i].commit(); //important, otherwise it wouldn't save.
        }
        //Save contact location
        SharedPreferences[] prefs_location=new SharedPreferences[notify_contact.size()];
        SharedPreferences.Editor[] editor_location= new SharedPreferences.Editor[notify_contact.size()];
        for(int i=0;i<notify_location.size();i++) {
            prefs_location[i] = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor_location[i] = prefs_location[i].edit();
            editor_location[i].putInt("contact_location" + Integer.toString(i), notify_location.get(i));
            editor_location[i].commit(); //important, otherwise it wouldn't save.
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        is_notify=true;
        //if((DeviceActivity.isAlarm==true)&&(notify_setting_back!=null)) notify_setting_back.callOnClick();
        //Get contact_size back
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Notify_friend_preference.contact_size = sharedPreferences.getInt("contact_size", 0);
        //Log.i("Check backup", "value of contact size onResume of Notify friend: " + Notify_friend_preference.contact_size);
        //Get data contact list back
        Notify_friend_preference.notify_contact=new ArrayList<String>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String contact = sharedPreferences.getString("contact_" + Integer.toString(i), "???");
            //Log.i("Check backup", "value of contact onResume of Notify friend: " + contact);
            Notify_friend_preference.notify_contact.add(contact);
        }
        //Get data name list back
        Notify_friend_preference.notify_name=new ArrayList<String>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String namecontact = sharedPreferences.getString("name_contact_" + Integer.toString(i), "Unknown");
            //Log.i("Check backup", "value of contact onResume of Notify friend: " + contact);
            Notify_friend_preference.notify_name.add(namecontact);
        }
        //Get location back
        Notify_friend_preference.notify_location=new ArrayList<Integer>();
        for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Integer location = sharedPreferences.getInt("contact_location" + Integer.toString(i), 0);
            //Log.i("Check backup", "value of location: " + location);
            Notify_friend_preference.notify_location.add(location);
        }
        //Log.i("Check backup", "value of notify_location: " + Notify_friend_preference.notify_location + " value of notify_contact: "+Notify_friend_preference.notify_contact);
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