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
 * KakaoSpinnerAdapter.java: kakaotalk api
 */

package openstack.bulldi.safe3x.Login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import openstack.bulldi.safe3x.R;


public class KakaoSpinnerAdapter extends BaseAdapter {
    public static interface ISpinnerListener {
        int getSelectedItemPosition();

        void onItemSelected(BaseAdapter adapter, int position);
    }


    public static class KakaoSpinnerItems {
        private final List<String> titleList;
        private final int iconResId;

        private KakaoSpinnerItems() {
            titleList = null;
            iconResId = 0;
        }
        public KakaoSpinnerItems(int iconResId, List<String> titleList) {
            this.titleList = titleList;
            this.iconResId = iconResId;
        }

        public String getTitle(int position) {
            return titleList.get(position);
        }

        public int getIconResId() {
            return iconResId;
        }

        public int getSize() {
            return titleList.size();
        }
    }

    private final KakaoSpinnerItems items;
    private final ISpinnerListener listener;

    private KakaoSpinnerAdapter() {
        this.items = null;
        this.listener = null;
    }

    public KakaoSpinnerAdapter(KakaoSpinnerItems items, ISpinnerListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.getSize();
    }

    @Override
    public Object getItem(int position) {
        return items.getTitle(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            item = inflater.inflate(R.layout.view_spinner_item, null, false);
        }

        TextView title = (TextView)item.findViewById(R.id.menu_title);
        title.setText(items.getTitle(position));

        if (listener != null) {
            final CheckBox checked = (CheckBox) item.findViewById(R.id.menu_checkbox);
            int selectedPosition = listener.getSelectedItemPosition();
            checked.setChecked(selectedPosition == position);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemSelected(KakaoSpinnerAdapter.this, position);
                    }
                }
            });
        }
        return item;
    }
}
