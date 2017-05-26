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
 * CustomToast.java
 */

package openstack.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/* This class encapsulates utility functions */
public class CustomToast {

  public static void middleBottom(Context c, String txt) {
    Toast m = Toast.makeText(c, txt, Toast.LENGTH_SHORT);
    m.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 300);
    m.show();
  }

}
