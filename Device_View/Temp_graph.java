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
 * Temp_graph.java: draw graph of temperature data
 */

package openstack.bulldi.safe3x.Device_View;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import openstack.bulldi.common.Data_store;
import openstack.bulldi.safe3x.Preference_etc.Language_setting;
import openstack.bulldi.safe3x.R;
import openstack.bulldi.safe3x.Preference_etc.Unit_setting;
import openstack.util.TextViewPlus;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.String;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;

import android.content.Context;


public class Temp_graph extends DialogFragment {
    //public class CO_graph extends Fragment{
    Context context;
    //private DeviceActivity mActivity;
    private ImageView temp_up;
    //private View view;
    private TextViewPlus temp_label_starting_time;
    private TextViewPlus starting_time_temp;
    private TextViewPlus temp_highest;
    private TextViewPlus temp_lowest;
    private TextViewPlus temp_average;
    private TextViewPlus label_temp_highest;
    private TextViewPlus label_temp_lowest;
    private TextViewPlus label_temp_average;
    //private FrameLayout temp_graph;

    public Temp_graph() {
        super();
    }
    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) {
            return;
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth =  metrics.widthPixels;
        int screenHeight =  metrics.heightPixels;
        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        //getDialog().getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setBackgroundDrawable(null);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.temp_graph, container, false);
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
                            getDialog().dismiss();
                        }else if((e2.getY() - e1.getY()) > sensitvity){
                            swipe += "Swipe Down\n";
                        }else{
                            swipe += "\n";
                        }

                        //Log.i("Check swipe", "value:" + swipe);

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    String changeMonth(int intMonth){
        String mon="";
        if(intMonth==1) mon="January";
        else if(intMonth==1) mon="January";
        else if(intMonth==2) mon="February";
        else if(intMonth==3) mon="March";
        else if(intMonth==4) mon="April";
        else if(intMonth==5) mon="May";
        else if(intMonth==6) mon="June";
        else if(intMonth==7) mon="July";
        else if(intMonth==8) mon="August";
        else if(intMonth==9) mon="September";
        else if(intMonth==10) mon="October";
        else if(intMonth==11) mon="November";
        else if(intMonth==12) mon="December";
        return mon;
    }
    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = getActivity();
        final Typeface font_regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        final Typeface font_thin = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        temp_up=(ImageView) view.findViewById(R.id.temp_up);
        temp_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_up.setImageResource(R.drawable.up_on);
                final Handler handler_reconnect = new Handler();
                handler_reconnect.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDialog().dismiss();
                    }
                }, 10);
            }
        });
        temp_label_starting_time= (TextViewPlus) view.findViewById(R.id.temp_label_starting_time);
       // temp_label_starting_time.setTypeface(font_regular);
        starting_time_temp= (TextViewPlus) view.findViewById(R.id.starting_time_temp);
       // starting_time_temp.setTypeface(font_regular);
        if(Language_setting.is_english==true) {
            String full_en=DeviceActivity.starting_time;
            String stringDay=full_en.substring(0, 2);
            String stringMonth=full_en.substring(4,6);int intMonth=Integer.parseInt(stringMonth);
            String stringYear=full_en.substring(8, 12);
            String stringHour=full_en.substring(15,17);int intHour=Integer.parseInt(stringHour);
            String stringMinute=full_en.substring(18, 20);
            String mon=changeMonth(intMonth);
            String final_en="";
            if(intHour>12) final_en=mon+" "+stringDay+", "+stringYear+"   "+stringHour+":"+stringMinute+" p.m.";
            else final_en=mon+" "+stringDay+", "+stringYear+"   "+stringHour+":"+stringMinute+" a.m.";
            starting_time_temp.setText(final_en);
        }
        else {
            String full_ko=DeviceActivity.starting_time_ko;
            String stringDay=full_ko.substring(10,12);
            String stringMonth=full_ko.substring(6, 8);
            String stringYear=full_ko.substring(0,4);
            String stringHour=full_ko.substring(15,17);int intHour=Integer.parseInt(stringHour);
            String stringMinute=full_ko.substring(18, 20);
            String final_ko="";
            if(intHour>12) final_ko=stringYear+". "+stringMonth+". "+stringDay+"   오후 "+stringHour+":"+stringMinute;
            else final_ko=stringYear+". "+stringMonth+". "+stringDay+"   오전 "+stringHour+":"+stringMinute;
            starting_time_temp.setText(final_ko);
        }
        temp_highest= (TextViewPlus) view.findViewById(R.id.temp_highest);
       // temp_highest.setTypeface(font_thin);
        double temperature_max= SensorTagIRTemperatureProfile.temp_max;
        if(Unit_setting.is_celsius==false) temperature_max=(temperature_max*1.8)+32;
        //String s_highest = String.format("%.2f", SensorTagIRTemperatureProfile.temp_max);
        String s_highest = String.format("%.1f", temperature_max);
        Spannable span_highest = new SpannableString(s_highest);
        span_highest.setSpan(new RelativeSizeSpan(1.33f), 0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        temp_highest.setText(span_highest);

        temp_lowest= (TextViewPlus) view.findViewById(R.id.temp_lowest);
        //temp_lowest.setTypeface(font_thin);
        double temperature_min=SensorTagIRTemperatureProfile.temp_min;
        if(Unit_setting.is_celsius==false) temperature_min=(temperature_min*1.8)+32;
        //String s_lowest = String.format("%.2f", SensorTagIRTemperatureProfile.temp_min);
        String s_lowest = String.format("%.1f", temperature_min);
        Spannable span_lowest = new SpannableString(s_lowest);
        span_lowest.setSpan(new RelativeSizeSpan(1.33f), 0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        temp_lowest.setText(span_lowest);

        temp_average= (TextViewPlus) view.findViewById(R.id.temp_average);
        //temp_average.setTypeface(font_thin);
        double temperature_average=SensorTagIRTemperatureProfile.temp_average;
        if(Unit_setting.is_celsius==false) temperature_average=(temperature_average*1.8)+32;
        //String s_average = String.format("%.2f", SensorTagIRTemperatureProfile.temp_average);
        String s_average = String.format("%.1f", temperature_average);
        Spannable span_average = new SpannableString(s_average);
        span_average.setSpan(new RelativeSizeSpan(1.33f), 0, 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        temp_average.setText(span_average);

        label_temp_highest=(TextViewPlus) view.findViewById(R.id.label_temp_highest);
        //label_temp_highest.setTypeface(font_regular);
        label_temp_lowest=(TextViewPlus) view.findViewById(R.id.label_temp_lowest);
        //label_temp_lowest.setTypeface(font_regular);
        label_temp_average=(TextViewPlus) view.findViewById(R.id.label_temp_average);
        //label_temp_average.setTypeface(font_regular);

        Data_store[] temp_data= SensorTagIRTemperatureProfile.get_temp();
        //Log.i("Graph_Temp","value of data: "+temp_data[0].data+temp_data[1].data+temp_data[2].data+temp_data[3].data+temp_data[4].data+temp_data[5].data+temp_data[6].data+temp_data[7].data+temp_data[8].data+temp_data[9].data+temp_data[10].data+temp_data[11].data+temp_data[12].data+temp_data[13].data+temp_data[14].data+temp_data[15].data+temp_data[16].data+temp_data[17].data+temp_data[19].data+temp_data[19].data+temp_data[20].data+temp_data[21].data+temp_data[22].data+temp_data[23].data);
        Calendar current_time=Calendar.getInstance();
        Calendar yesterday=Calendar.getInstance();
        yesterday.add(Calendar.DATE,-1);

        double[] data=new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        if(Unit_setting.is_celsius==true){
            for(int i=0;i<24;i++){
                if(temp_data[i].day==current_time.get(Calendar.DAY_OF_MONTH)||(temp_data[i].day==yesterday.get(Calendar.DAY_OF_MONTH)))
                    data[i]=temp_data[i].data;
            }
        }
        else {
            for(int i=0;i<24;i++){
                if(temp_data[i].day==current_time.get(Calendar.DAY_OF_MONTH)||(temp_data[i].day==yesterday.get(Calendar.DAY_OF_MONTH)))
                    data[i]=(temp_data[i].data*1.8)+32;
            }
        }
        Log.i("Graph_Temp", "value of data: " + data[0] +";"+ data[1] +";"+ data[2] +";"+ data[3] +";"+ data[4] +";"+ data[5] +";"+ data[6] +";"+ data[7] +";"+ data[8] +";"+ data[9] +";"+ data[10] +";"+ data[11] +";"+ data[12] +";"+ data[13] +";"+ data[14] +";"+ data[15] +";"+ data[16] +";"+ data[17] +";"+ data[19] +";"+ data[19] +";"+ data[20] +";"+ data[21] +";"+ data[22] +";"+ data[23]);
        int cal = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        GraphView graph = (GraphView) view.findViewById(R.id.graph_temp);
        BarGraphSeries<DataPoint> series=null;
        LineGraphSeries<DataPoint> series1=null;
        if(cal==0) {
            double[] data_new=new double[]{data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"12:00", "", "16:00", "", "20:00", "", "00:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);

        } else if(cal==1){
            double[] data_new=new double[]{data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"13:00", "", "17:00", "", "21:00", "", "01:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
           // graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==2){
            double[] data_new=new double[]{data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"14:00", "", "18:00", "", "22:00", "", "02:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
           // graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==3) {
            double[] data_new=new double[]{data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"15:00", "", "19:00", "", "23:00", "", "03:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
           // graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==4){
            double[] data_new=new double[]{data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"16:00", "", "20:00", "", "00:00", "", "04:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==5){
            double[] data_new=new double[]{data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[17],data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"17:00", "", "21:00", "", "01:00", "", "05:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==6){
            double[] data_new=new double[]{data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[18],data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"18:00", "", "22:00", "", "02:00", "", "06:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==7){
            double[] data_new=new double[]{data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[19],data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"19:00", "", "23:00", "", "03:00", "", "07:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==8){
            double[] data_new=new double[]{data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[20],data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"20:00", "", "00:00", "", "04:00", "", "08:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==9){
            double[] data_new=new double[]{data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[21],data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
           DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"21:00", "", "01:00", "", "05:00", "", "09:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==10){
            double[] data_new=new double[]{data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[22],data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"22:00", "", "02:00", "", "06:00", "", "10:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==11){
            double[] data_new=new double[]{data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[23],data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"23:00", "", "03:00", "", "07:00", "", "11:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==12){
            double[] data_new=new double[]{data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"00:00", "", "04:00", "", "08:00", "", "12:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==13){
            double[] data_new=new double[]{data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"01:00", "", "05:00", "", "09:00", "", "13:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==14){
            double[] data_new=new double[]{data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"02:00", "", "06:00", "", "10:00", "", "14:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==15){
            double[] data_new=new double[]{data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"03:00", "", "07:00", "", "11:00", "", "15:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==16){
            double[] data_new=new double[]{data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"04:00", "", "08:00", "", "12:00", "", "16:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==17){
            double[] data_new=new double[]{data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"05:00", "", "09:00", "", "13:00", "", "17:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==18){
            double[] data_new=new double[]{data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[6],data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"06:00", "", "10:00", "", "14:00", "", "18:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==19){
            double[] data_new=new double[]{data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"07:00", "", "11:00", "", "15:00", "", "19:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }else if(cal==20){
            double[] data_new=new double[]{data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[8],data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"08:00", "", "12:00", "", "16:00", "", "20:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }
        else if(cal==21){
            double[] data_new=new double[]{data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[9],data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));             series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"09:00", "", "13:00", "", "17:00", "", "21:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }
        else if(cal==22){
            double[] data_new=new double[]{data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[10],data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);
            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"10:00", "", "14:00", "", "18:00", "", "22:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }
        else {
            double[] data_new=new double[]{data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23]};
            double max=data_new[0];
            double min=data_new[0];
            for(int i = 1; i < data_new.length; i++) {
                if(data_new[i]>max)
                    max=data_new[i];
                if(data_new[i]<min)
                    min=data_new[i];}
            double[] sort_data=new double[]{data[11],data[12],data[13],data[14],data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23]};
            Arrays.sort(sort_data);
            if(min==0){for(int i=0;i<sort_data.length;i++) {
                if(sort_data[i]==0) {}
                else {min=sort_data[i];
                    break;}}}
            DataPoint[] data_graph=new DataPoint[]{
                    new DataPoint(0, data_new[0]), new DataPoint(1, data_new[1]), new DataPoint(2, data_new[2]),
                    new DataPoint(3, data_new[3]), new DataPoint(4, data_new[4]), new DataPoint(5, data_new[5]),
                    new DataPoint(6, data_new[6]), new DataPoint(7, data_new[7]), new DataPoint(8, data_new[8]),
                    new DataPoint(9, data_new[9]), new DataPoint(10, data_new[10]), new DataPoint(11, data_new[11]),new DataPoint(12, data_new[12] )
            };
            series = new BarGraphSeries<>(data_graph);
            if(Unit_setting.is_celsius==true){
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, SensorTagIRTemperatureProfile.temp_average), new DataPoint(1, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(2, SensorTagIRTemperatureProfile.temp_average), new DataPoint(3, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(4, SensorTagIRTemperatureProfile.temp_average), new DataPoint(5, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(6, SensorTagIRTemperatureProfile.temp_average), new DataPoint(7, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(8, SensorTagIRTemperatureProfile.temp_average), new DataPoint(9, SensorTagIRTemperatureProfile.temp_average),
                        new DataPoint(10, SensorTagIRTemperatureProfile.temp_average), new DataPoint(11, SensorTagIRTemperatureProfile.temp_average),new DataPoint(12, SensorTagIRTemperatureProfile.temp_average)
                });
            }
            else {
                series1 = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(1, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(2, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(3, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(4, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(5, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(6, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(7, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(8, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(9, (SensorTagIRTemperatureProfile.temp_average*1.8)+32),
                        new DataPoint(10, (SensorTagIRTemperatureProfile.temp_average*1.8)+32), new DataPoint(11,(SensorTagIRTemperatureProfile.temp_average*1.8)+32),new DataPoint(12, (SensorTagIRTemperatureProfile.temp_average*1.8)+32)
                });
            }
            series.setColor(getResources().getColor(R.color.Gold));
            series.setSpacing(15);

            series1.setColor(getResources().getColor(R.color.Green));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.Cornsilk));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);
            graph.getGridLabelRenderer().setNumHorizontalLabels(7);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"11:00", "", "15:00", "", "19:00", "", "23:00"});
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2); nf.setMinimumFractionDigits(2);
            staticLabelsFormatter.setDynamicLabelFormatter(new DefaultLabelFormatter(nf, nf));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(min - 0.1);
            graph.getViewport().setMaxY(max + 0.1);
            //graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature('C)");
            //graph.getGridLabelRenderer().setHorizontalAxisTitle("Time(h)");
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.Gold));
            graph.addSeries(series);
            graph.addSeries(series1);
        }
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    public static DataPoint[] insertData(DataPoint[] values, int insertIndex, DataPoint numberToInsert){
        DataPoint[] newArray = new DataPoint[values.length + 1];
        for (int i = 0; i < newArray.length - 1; i++)
            newArray[i < insertIndex ? i : i + 1] = values[i];
        newArray[insertIndex] = numberToInsert;
        return  newArray;
    }
}
