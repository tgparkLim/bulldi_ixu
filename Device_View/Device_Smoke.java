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
 * Device_Smoke.java: show current smoke data
 */

package openstack.bulldi.safe3x.Device_View;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.R;
import openstack.util.Point3D;
import java.lang.String;


public class Device_Smoke extends Fragment {

    static Context context;
    static private DeviceActivity mActivity;
    private boolean mBusy=true;
    static public View view;
    static public TextView smoke_status;
    static public ImageView smoke_image;
     public ImageView smoke_down;
    public Device_Smoke() {
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
        view = inflater.inflate(R.layout.generic_smoke, container, false);
        mActivity.onViewInflated(view);
        context = getActivity();
        final Typeface font_value = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        final Typeface font_unit = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
        final Typeface font_status = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        smoke_status = (TextView) view.findViewById(R.id.smoke_status);
        smoke_status.setTypeface(font_status);
        smoke_image=(ImageView) view.findViewById(R.id.smoke_img);
        final FragmentManager fm_1=getActivity().getSupportFragmentManager();
        smoke_down=(ImageView) view.findViewById(R.id.smoke_down);
        smoke_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoke_down.setImageResource(R.drawable.bot_smoke_on);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Smoke_graph frag = new Smoke_graph();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(frag, "fragment_smoke_graph");
                        ft.commitAllowingStateLoss();
                        //frag.show(fm_1, "fragment_smoke_graph");
                        smoke_down.setImageResource(R.drawable.bot_smoke);
                    }
                }, 10);


            }
        });
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        String swipe = "";
                        float sensitvity = 200;

                        // TODO Auto-generated method stub
                        if((e1.getX() - e2.getX()) > sensitvity){
                            swipe += "Swipe Left\n";
                        }else if((e2.getX() - e1.getX()) > sensitvity){
                            swipe += "Swipe Right\n";
                        }else{
                            swipe += "\n";
                        }

                        if((e1.getY() - e2.getY()) > sensitvity){
                            swipe += "Swipe Up\n";
                        }else if((e2.getY() - e1.getY()) > sensitvity){
                            swipe += "Swipe Down\n";
                            Smoke_graph frag = new Smoke_graph();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(frag, "fragment_smoke_graph");
                            ft.commitAllowingStateLoss();
                            //frag.show(fm_1, "fragment_smoke_graph");
                        }else{
                            swipe += "\n";
                        }

                        //Log.i("Check swipe","value:"+swipe);

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Language_setting.is_english==true) {
            if (smoke_status != null) {
                if (smoke_status.getText().toString().compareTo("쾌적한 상태입니다") == 0)
                    smoke_status.setText("normal");
                else if (smoke_status.getText().toString().compareTo("화재발생이 의심됩니다") == 0)
                    smoke_status.setText("Watch out! fire suspected");
            }
        }else {
            if (smoke_status != null) {
                if (smoke_status.getText().toString().compareTo("normal") == 0)
                    smoke_status.setText("쾌적한 상태입니다");
                else if (smoke_status.getText().toString().compareTo("Watch out! fire suspected") == 0)
                    smoke_status.setText("화재발생이 의심됩니다");
            }
        }

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
    public  void add_value(Point3D a){
        double value=a.x;
        smoke_image.setImageResource(R.drawable.smoke_stable);
//        smoke_text.setText(String.format("%.0f", value));
        smoke_status.setText(getResources().getString(R.string.sensor_status_zeros));
    }
}
