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
 * Introduction.java: control introduction activity
 */

package openstack.bulldi.safe3x.Introduction;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import openstack.bulldi.safe3x.R;
import openstack.bulldi.safe3x.BLE_Connection.ViewPagerActivity;

public class Introduction extends ViewPagerActivity {
    static Context context;
    private Intro_CO intro_co;
    private Intro_Smoke intro_smoke;
    private Intro_Temp intro_temp;
    private Intro_Share intro_share;
    public Introduction() {
        mResourceFragmentPager = R.layout.fragment_pager;
        mResourceIdPager = R.id.pager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();

        context=getApplication();

        intro_co = new Intro_CO();
        intro_smoke = new Intro_Smoke();
        intro_temp = new Intro_Temp();
        intro_share = new Intro_Share();

        mSectionsPagerAdapter.addSection(intro_co, "CO Introduction");
        mSectionsPagerAdapter.addSection(intro_temp, "Smoke");
        mSectionsPagerAdapter.addSection(intro_smoke,"Temperature" );
        mSectionsPagerAdapter.addSection(intro_share, "Share");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}