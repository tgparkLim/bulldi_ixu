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
 * Device_Temp.java: show current temperature data
 */

package openstack.bulldi.safe3x.Device_View;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import openstack.bulldi.common.BluetoothLeService;
import openstack.bulldi.common.GenericBluetoothProfile;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.R;
import openstack.util.GenericCharacteristicTableRow;
import openstack.util.Point3D;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device_Temp extends Fragment {

    static Context context;
    static private DeviceActivity mActivity;
    private boolean mBusy=true;

    static public Button transferTemp;

    static public View view;
    static public TextView temp_text;
    static public TextView temp_unit;
    static public TextView temp_status;
    static public ImageView temp_image;
    public ImageView temp_down;
    Intent intent;

    //public Point3D a;

    public Device_Temp() {
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
        view = inflater.inflate(R.layout.generic_temp, container, false);
        mActivity.onViewInflated(view);
        context = getActivity();
        final Typeface font_value = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        final Typeface font_unit = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
        final Typeface font_status = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");

        temp_text= (TextView) view.findViewById(R.id.temp_text);
        temp_text.setTypeface(font_value);

        temp_unit= (TextView) view.findViewById(R.id.temp_unit);
        temp_unit.setTypeface(font_unit);
        temp_status = (TextView) view.findViewById(R.id.temp_status);
        temp_status.setTypeface(font_status);
        temp_image=(ImageView) view.findViewById(R.id.temp_image);
        final FragmentManager fm_1=getActivity().getSupportFragmentManager();
        temp_down=(ImageView) view.findViewById(R.id.temp_down);
        temp_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_down.setImageResource(R.drawable.bot_temper_on);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Temp_graph frag = new Temp_graph();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(frag, "fragment_temp_graph");
                        ft.commitAllowingStateLoss();
                        temp_down.setImageResource(R.drawable.bot_temper);
                        //frag.show(fm_1, "fragment_temp_graph");
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
                            Temp_graph frag = new Temp_graph();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(frag, "fragment_temp_graph");
                            ft.commitAllowingStateLoss();
                            //frag.show(fm_1, "fragment_temp_graph");
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
            if (temp_status != null) {
                if (temp_status.getText().toString().compareTo("화재발생이 의심됩니다") == 0)
                    temp_status.setText("Watch out! fire suspected");
                else if (temp_status.getText().toString().compareTo("무진장 덥습니다") == 0)
                    temp_status.setText("hot");
                else if (temp_status.getText().toString().compareTo("아주 따뜻합니다") == 0)
                    temp_status.setText("warm");
                else if (temp_status.getText().toString().compareTo("쾌적한 상태입니다") == 0)
                    temp_status.setText("nice");
                else if (temp_status.getText().toString().compareTo("약간 서늘합니다") == 0)
                    temp_status.setText("cool");
                else if (temp_status.getText().toString().compareTo("추위가 느껴집니다") == 0)
                    temp_status.setText("cold");
                else if (temp_status.getText().toString().compareTo("매우 찬 공기가 느껴져요") == 0)
                    temp_status.setText("very cold");
            }
        }else {
            if (temp_status != null) {
                if (temp_status.getText().toString().compareTo("Watch out! fire suspected") == 0)
                    temp_status.setText("화재발생이 의심됩니다");
                else if (temp_status.getText().toString().compareTo("hot") == 0)
                    temp_status.setText("무진장 덥습니다");
                else if (temp_status.getText().toString().compareTo("warm") == 0)
                    temp_status.setText("아주 따뜻합니다");
                else if (temp_status.getText().toString().compareTo("nice") == 0)
                    temp_status.setText("쾌적한 상태입니다");
                else if (temp_status.getText().toString().compareTo("cool") == 0)
                    temp_status.setText("약간 서늘합니다");
                else if (temp_status.getText().toString().compareTo("cold") == 0)
                    temp_status.setText("추위가 느껴집니다");
                else if (temp_status.getText().toString().compareTo("very cold") == 0)
                    temp_status.setText("매우 찬 공기가 느껴져요");
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
    public void add_value(Point3D a){
        double value=a.x;
        temp_text.setText(String.format("%.0f ", value));
        temp_status.setText(getResources().getString(R.string.sensor_temperature_status_1826));

    }

    public void Data_temp_co_smoke_TranferToServer() {

        //temperature

        double temperature_show = 0;
        if (SensorTagIRTemperatureProfile.myList.size() > 1) {
            temperature_show = SensorTagIRTemperatureProfile.myList.get(SensorTagIRTemperatureProfile.myList.size() - 1);
        }

/*        else if (SensorTagIRTemperatureProfile.myList.size() <= 1 && SensorTagIRTemperatureProfile.myList.size() >= 0) {
            temperature_show = SensorTagIRTemperatureProfile.myList.get(SensorTagIRTemperatureProfile.myList.size());
        }*/

        final String tempNow = String.format("%.1f", temperature_show);

        //co
        double co_show = 0;
        if (SensorTagCOProfile.myList_co.size() > 1) {
            co_show = SensorTagCOProfile.myList_co.get(SensorTagCOProfile.myList_co.size() - 1);
        }
        final String coNow = String.format("%.0f ", co_show);

        //String smokeNow = "";

//        String smokeNowT = "";
//        double smoke_show = SensorTagSmokeProfile.myList_smoke.get(SensorTagSmokeProfile.myList_smoke.size() - 1);
//        if (smoke_show == 0) {
//            smokeNowT = context.getString(R.string.sensor_status_zeros);
//        } else {
//            smokeNowT = context.getString(R.string.sensor_status_suspect);
//        }
//
//        final String smokeNow = String.format("%.0f ", smoke_show);
        double smoke_show = 0;
        if (SensorTagSmokeProfile.myList_smoke.size() > 1) {
            smoke_show = SensorTagSmokeProfile.myList_smoke.get(SensorTagSmokeProfile.myList_smoke.size() - 1);
        }
        final String smokeNow = String.format("%.0f ", smoke_show);


        //transfer Temperature
        //get to current location
        final Gpsinfo gps;

        gps = new Gpsinfo(context);

        if (gps.isGetLocation()) {
            //gps.isGetLocation();
            //gps.showSettingsAlert();

            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();

            final String slatitude = Double.toString(latitude);
            final String slongitude = Double.toString(longitude);



            //부평스타벅스
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.bupyeong_3data,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("temp", tempNow);
                        params.put("co", coNow);
                        params.put("smoke", smokeNow);
                        params.put("latitude", slatitude);
                        params.put("longitude", slongitude);
                        return params;
                    }
                };
                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);


        } else {
            gps.showSettingsAlert();
        }

    }
}
