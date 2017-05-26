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
 * DeviceView.java
 */

package openstack.bulldi.safe3x.Device_View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import openstack.bulldi.safe3x.R;


// Fragment for Device View
public class DeviceView extends Fragment {

	private Context context;
	public static DeviceView mInstance = null;

	// GUI
	//private TableLayout table=new TableLayout(this.context);
	private TableLayout table;
	public boolean first = true;

	// House-keeping
	//private DeviceActivity mActivity=new DeviceActivity();
	private DeviceActivity mActivity;
	private boolean mBusy;

	// The last two arguments ensure LayoutParams are inflated properly.
	private View view;

	public DeviceView() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.i("CreateAccountFragment", "onCreateView");
		mInstance = this;
		mActivity = (DeviceActivity) getActivity();
		//Log.i("mActivity","Value of temperature activity " + mActivity);
		view = inflater.inflate(R.layout.generic_services_browser, container,false);
		table = (TableLayout) view.findViewById(R.id.generic_services_layout);

		// Notify activity that UI has been inflated
		mActivity.onViewInflated(view);

		return view;
	}

	public void showProgressOverlay(String title) {

	}

	public void addRowToTable(TableRow row) {
		if(table!=null) {
			table.removeAllViews();
			table.addView(row);
			table.requestLayout();
		}
	}
	public void removeRowsFromTable() {
		table.removeAllViews();
	}

	public TableLayout gettable(){return table;}

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
			if(mActivity==null) mActivity=new DeviceActivity();
			mActivity.showBusyIndicator(f);
			mBusy = f;
		}
	}
}
