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
 * ContactAdapter_slected.java: control list of registered contact
 */

package openstack.bulldi.safe3x.Preference_sharing;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import openstack.bulldi.safe3x.R;
import openstack.util.CheckBoxImageView;
import openstack.util.TextViewPlus;

public class ContactAdapter_slected extends ArrayAdapter<ContactBean> {

    public Activity activity;
    public ArrayList<ContactBean> items;
    public int row;
    public ContactBean objBean;
    public Context context;
    public ContactAdapter_slected(Activity act, int row, ArrayList<ContactBean> items) {
        super(act, row, items);

        this.activity = act;
        this.row = row;
        this.items = items;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);

            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((items == null) || ((position + 1) > items.size()))
            return view;

        objBean = items.get(position);

        holder.tvname = (TextViewPlus) view.findViewById(R.id.tvname);
        holder.tvPhoneNo = (TextViewPlus) view.findViewById(R.id.tvphone);
        holder.checkBox=(CheckBoxImageView) view.findViewById(R.id.but_add_contact);
        if (holder.tvname != null && null != objBean.getName()
                && objBean.getName().trim().length() > 0) {
            holder.tvname.setText(Html.fromHtml(objBean.getName()));
        }
        if (holder.tvPhoneNo != null && null != objBean.getPhoneNo()
                && objBean.getPhoneNo().trim().length() > 0) {
            holder.tvPhoneNo.setText(Html.fromHtml(objBean.getPhoneNo()));
        }
        holder.checkBox.setOnCheckedChangeListener( myCheckChangList);
        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(objBean.isSelected());
        view.setTag(holder);
        return view;
    }
    public int getSize(){
        return  items.size();
    }
    public ContactBean getContact(int position){
        return  ( getItem(position));
    }

    CheckBoxImageView.OnCheckedChangeListener myCheckChangList = new CheckBoxImageView.OnCheckedChangeListener() {
        public void onCheckedChanged(View buttonView,
                                     boolean isChecked) {
            ContactBean contact=getContact((Integer) buttonView.getTag());
            contact.selected = isChecked;
            if (isChecked==false){
                for (int i = 0; i < Notify_friend_preference.contact_size; i++) {
                    if (contact.getPhoneNo().compareTo((Notify_friend_preference.notify_contact.get(i)))==0) {
                        Notify_friend_preference.notify_contact.remove(i);
                        Notify_friend_preference.notify_name.remove(i);
                        Notify_friend_preference. notify_location.remove(i);
                        Notify_friend_preference.contact_size=Notify_friend_preference.contact_size-1;
                    }
                }
                for (int j = 0; j < Notify_friend_preference.list.size(); j++) {
//                    ContactBean bean = Notify_friend_preference.list.get(j);
                    if(Notify_friend_preference.contact_size>0) {
                        for (int k = 0; k < Notify_friend_preference.contact_size; k++) {
                            if (Notify_friend_preference.list.get(j).getPhoneNo().compareTo(Notify_friend_preference.notify_contact.get(k)) == 0) {
                                Notify_friend_preference.list.get(j).setSelected(true);
                                break;
                            } else Notify_friend_preference.list.get(j).setSelected(false);
                        }
                    }else {
                        Notify_friend_preference.list.get(j).setSelected(false);
                    }
                }
            }
            Notify_friend_preference.list_selected.clear();
            for (int i=0;i<Notify_friend_preference.contact_size;i++)
            {

                String name = Notify_friend_preference.notify_name.get(i);

                String phoneNumber = Notify_friend_preference.notify_contact.get(i);
                ContactBean objContact_selected = new ContactBean();
                objContact_selected.setName(name);
                objContact_selected.setPhoneNo(phoneNumber);
                objContact_selected.setSelected(true);
                Notify_friend_preference.list_selected.add(objContact_selected);
            }
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (Notify_friend_preference.objAdapter_selected.getSize() > 2) {
                        View item = Notify_friend_preference.objAdapter_selected.getView(0, null, Notify_friend_preference.listView_selected);
                        item.measure(0, 0);
                        ViewGroup.LayoutParams params = Notify_friend_preference.listView_selected.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = (int) (2.5 * item.getMeasuredHeight());
                        Notify_friend_preference.listView_selected.setLayoutParams(params);
                    } else {
                        View item = Notify_friend_preference.objAdapter_selected.getView(0, null, Notify_friend_preference.listView_selected);
                        item.measure(0, 0);
                        ViewGroup.LayoutParams params = Notify_friend_preference.listView_selected.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = (int) (Notify_friend_preference.objAdapter_selected.getSize() * item.getMeasuredHeight());
                        Notify_friend_preference.listView_selected.setLayoutParams(params);
                    }
                    Notify_friend_preference.objAdapter_selected.notifyDataSetChanged();
                    Notify_friend_preference.objAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    public class ViewHolder {
        public TextViewPlus tvname, tvPhoneNo;
        public CheckBoxImageView checkBox;
    }

}

