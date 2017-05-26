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
 * Device_CO.java: showing current CO data
 */

package openstack.bulldi.safe3x.Device_View;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import openstack.bulldi.safe3x.Alarm.DialogAlarm;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.R;
import openstack.util.Point3D;
import java.lang.String;
import android.content.Context;


public class Device_CO extends Fragment {

    static Context context;
    static private DeviceActivity mActivity;
    private boolean mBusy=true;
    static public View view;
    static public TextView co_text;
    static public TextView co_unit;
    static public TextView co_status;
    static public ImageView co_image;
    //static public Button co_down;
     public ImageView co_down;
    Intent intent;
    public Device_CO() {
        super();
        //mDialog=(DialogAlarm) getActivity();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("CO check","onCreateView()");
        mActivity = (DeviceActivity) getActivity();
        //inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.generic_co, container, false);
        mActivity.onViewInflated(view);
        context = getActivity();
        final Typeface font_value = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        final Typeface font_unit = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
        final Typeface font_status = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        co_text= (TextView) view.findViewById(R.id.co_text);
        co_text.setTypeface(font_value);
        co_unit= (TextView) view.findViewById(R.id.co_unit);
        co_unit.setTypeface(font_unit);
        co_status = (TextView) view.findViewById(R.id.co_status);
        co_status.setTypeface(font_status);
        co_image=(ImageView) view.findViewById(R.id.co_image);
        final FragmentManager fm_1=getActivity().getSupportFragmentManager();
        //co_down=(Button) view_2.findViewById(R.id.co_down);
        co_down=(ImageView) view.findViewById(R.id.co_down);
        co_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                co_down.setImageResource(R.drawable.bot_co_on);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CO_graph frag = new CO_graph();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(frag, "fragment_co_graph");
                        ft.commitAllowingStateLoss();
                        co_down.setImageResource(R.drawable.bot_cogas);
                        //frag.show(fm_1, "fragment_co_graph");
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
                            CO_graph frag = new CO_graph();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(frag, "fragment_co_graph");
                            ft.commitAllowingStateLoss();
                            //frag.show(fm_1, "fragment_co_graph");
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
            if (co_status != null) {
                if (co_status.getText().toString().compareTo("쾌적한 상태입니다") == 0)
                    co_status.setText("normal");
                else if (co_status.getText().toString().compareTo("화재발생이 의심됩니다") == 0)
                    co_status.setText("Watch out! fire suspected");
            }
        }else {
            if (co_status != null) {
                if (co_status.getText().toString().compareTo("normal") == 0)
                    co_status.setText("쾌적한 상태입니다");
                else if (co_status.getText().toString().compareTo("Watch out! fire suspected") == 0)
                    co_status.setText("화재발생이 의심됩니다");
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
            mBusy= f;
        }
    }
    public  void add_value(Point3D a){
        double value=a.x;
        co_text.setText(String.format("%.0f ", value));
        //co_text.setTextColor(Color.BLUE);
        co_status.setText(getResources().getString(R.string.sensor_status_zeros));
    }


}
