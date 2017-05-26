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
 * Intro_share.java; introduction about sharing emergency status
 */

package openstack.bulldi.safe3x.Introduction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import openstack.bulldi.safe3x.Login.Bulldi_rule;
import openstack.bulldi.safe3x.Device_View.DeviceActivity;
import openstack.bulldi.safe3x.Login.Greeting;
import openstack.bulldi.safe3x.R;


public class Intro_Share extends Fragment {

    static Context context;
    private DeviceActivity mActivity;
    private View view;
    private Button share_intro_button;
    public Intro_Share() {
        super();
    }
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_share_layout, container, false);
        context = getActivity();
        share_intro_button=(Button) view.findViewById(R.id.share_intro_button);
        share_intro_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_intro_button.setBackgroundResource(R.drawable.button_on1);
                share_intro_button.setTextColor(Color.BLACK);
                //Intent i = new Intent(getActivity(), Greeting.class);
                Intent i = new Intent(getActivity(), Bulldi_rule.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(share_intro_button!=null) {
            share_intro_button.setBackgroundResource(R.drawable.button_off1);
            share_intro_button.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
