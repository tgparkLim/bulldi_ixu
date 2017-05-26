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
 * Device_Battery.java: showing current battery data
 */

package openstack.bulldi.safe3x.Device_View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.R;
import openstack.util.Point3D;
import java.lang.String;


public class Device_Battery extends Fragment {

    static Context context;
    static private DeviceActivity mActivity;
    private boolean mBusy=true;
    static public View view;
    static public TextView battery_text;
    static public TextView battery_unit;
    static public TextView battery_status;
    static public ImageView battery_image;
    Intent intent;
    public Device_Battery() {
        super();
    }
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (DeviceActivity) getActivity();
        //inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.generic_battery, container, false);
        mActivity.onViewInflated(view);
        context = getActivity();
        final Typeface font_value = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        final Typeface font_unit = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
        final Typeface font_status = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        battery_text= (TextView) view.findViewById(R.id.battery_text);
        battery_text.setTypeface(font_value);
        battery_unit= (TextView) view.findViewById(R.id.battery_unit);
        battery_unit.setTypeface(font_unit);
        battery_status = (TextView) view.findViewById(R.id.battery_status);
        battery_status.setTypeface(font_status);
        battery_image=(ImageView) view.findViewById(R.id.battery_image);
        final FragmentManager fm_1=getActivity().getSupportFragmentManager();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    void setBusy(boolean f) {
        if (f != mBusy)
        {
            mActivity.showBusyIndicator(f);
            mBusy = f;
        }
    }
    public static void add_value(Point3D a){
        double value=a.x;
        battery_text.setText(String.format("%.0f", value));
        battery_status.setText("Good battery");
    }
}
