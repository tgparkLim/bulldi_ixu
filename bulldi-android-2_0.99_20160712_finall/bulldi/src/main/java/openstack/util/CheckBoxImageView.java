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
 * CheckBoxImageView.java
 */

package openstack.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import openstack.bulldi.safe3x.R;


public class CheckBoxImageView extends ImageView implements View.OnClickListener {
    boolean checked;
    int defImageRes;
    int checkedImageRes;
    OnCheckedChangeListener onCheckedChangeListener;

    public CheckBoxImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init(attr, defStyle);
    }

    public CheckBoxImageView(Context context, AttributeSet attr) {
        super(context, attr);
        init(attr, -1);
    }

    public CheckBoxImageView(Context context) {
        super(context);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        setImageResource(checked ? checkedImageRes : defImageRes);
    }

    private void init(AttributeSet attributeSet, int defStyle) {
        TypedArray a = null;
        if (defStyle != -1)
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.CheckBoxImageView, defStyle, 0);
        else
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.CheckBoxImageView);
        defImageRes = a.getResourceId(0, 0);
        checkedImageRes = a.getResourceId(1, 0);
        checked = a.getBoolean(2, false);
        a.recycle();
        setImageResource(checked ? checkedImageRes : defImageRes);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        checked = !checked;
        setImageResource(checked ? checkedImageRes : defImageRes);
        onCheckedChangeListener.onCheckedChanged(this, checked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public static interface OnCheckedChangeListener {
        void  onCheckedChanged(View buttonView, boolean isChecked);
    }
}