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
 * CustomCheckBox.java
 */

package openstack.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;

import openstack.bulldi.safe3x.R;


public class CustomCheckBox extends CheckBox {
    private final Drawable buttonDrawable;

    public CustomCheckBox(Context context, AttributeSet set) {
        super(context, set);
        buttonDrawable = getResources().getDrawable(R.drawable.custom_check_box);
        try {
            setButtonDrawable(android.R.color.transparent);
        } catch (Exception e) {
            // DO NOTHING
        }
        setPadding(10, 5, 50, 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        buttonDrawable.setState(getDrawableState());

        final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        final int height = buttonDrawable.getIntrinsicHeight();
        if (buttonDrawable != null) {
            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            int buttonWidth = buttonDrawable.getIntrinsicWidth();
            int buttonLeft = getWidth() - buttonWidth - 5;
            buttonDrawable.setBounds(buttonLeft, y, buttonLeft + buttonWidth, y + height);
            buttonDrawable.draw(canvas);
        }
    }
}