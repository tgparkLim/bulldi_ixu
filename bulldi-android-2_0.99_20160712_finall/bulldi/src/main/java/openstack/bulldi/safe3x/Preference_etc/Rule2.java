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
 * Rule2.java: show "Privacy Policy"
 */

package openstack.bulldi.safe3x.Preference_etc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class Rule2 extends Fragment {
    private String mFile = "privacy.html";
    private int mIdFragment;
    private int mIdWebPage;

    public Rule2() {

    }

    public void setParameters(String file, int idFragment, int idWebPage) {
        if (file != null)
            mFile = file;
        mIdFragment = idFragment;
        mIdWebPage = idWebPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(mIdFragment, container, false);
        WebView wv = (WebView) rootView.findViewById(mIdWebPage);
        //wv.getSettings();
        wv.setBackgroundColor(Color.WHITE);
        wv.loadUrl("file:///android_asset/" + mFile);
        return rootView;
    }

}