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
 * HCIDefines.java
 */

package openstack.bulldi.common;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import openstack.bulldi.safe3x.BLE_Connection.Connection;
import openstack.bulldi.safe3x.BLE_Connection.Connection_seperate;
import openstack.bulldi.safe3x.R;


public class HCIDefines {
    public static final Map<Integer,String> hciErrorCodeStrings;
    static
    {

        hciErrorCodeStrings = new HashMap<Integer,String>();
        hciErrorCodeStrings.put(0x01,Connection.res.getString(R.string.HCI_01));
        hciErrorCodeStrings.put(0x02,Connection.res.getString(R.string.HCI_02));
        hciErrorCodeStrings.put(0x03,Connection.res.getString(R.string.HCI_03));
        hciErrorCodeStrings.put(0x04,Connection.res.getString(R.string.HCI_04));
        hciErrorCodeStrings.put(0x05,Connection.res.getString(R.string.HCI_05));
        hciErrorCodeStrings.put(0x06,Connection.res.getString(R.string.HCI_06));
        hciErrorCodeStrings.put(0x07,Connection.res.getString(R.string.HCI_07));
        hciErrorCodeStrings.put(0x08,Connection.res.getString(R.string.HCI_08));
        hciErrorCodeStrings.put(0x09,Connection.res.getString(R.string.HCI_09));
        hciErrorCodeStrings.put(0x0A,Connection.res.getString(R.string.HCI_0A));
        hciErrorCodeStrings.put(0x0B,Connection.res.getString(R.string.HCI_0B));
        hciErrorCodeStrings.put(0x0C,Connection.res.getString(R.string.HCI_0C));
        hciErrorCodeStrings.put(0x0D,Connection.res.getString(R.string.HCI_0D));
        hciErrorCodeStrings.put(0x0E,Connection.res.getString(R.string.HCI_0E));
        hciErrorCodeStrings.put(0x0F,Connection.res.getString(R.string.HCI_0F));
        hciErrorCodeStrings.put(0x10,Connection.res.getString(R.string.HCI_10));
        hciErrorCodeStrings.put(0x11,Connection.res.getString(R.string.HCI_11));
        hciErrorCodeStrings.put(0x12,Connection.res.getString(R.string.HCI_12));
        hciErrorCodeStrings.put(0x13,Connection.res.getString(R.string.HCI_13));
        hciErrorCodeStrings.put(0x14,Connection.res.getString(R.string.HCI_14));
        hciErrorCodeStrings.put(0x15,Connection.res.getString(R.string.HCI_15));
        hciErrorCodeStrings.put(0x16,Connection.res.getString(R.string.HCI_16));
        hciErrorCodeStrings.put(0x17,Connection.res.getString(R.string.HCI_17));
        hciErrorCodeStrings.put(0x18,Connection.res.getString(R.string.HCI_18));
        hciErrorCodeStrings.put(0x19,Connection.res.getString(R.string.HCI_19));
        hciErrorCodeStrings.put(0x1A,Connection.res.getString(R.string.HCI_1A));
        hciErrorCodeStrings.put(0x1B,Connection.res.getString(R.string.HCI_1B));
        hciErrorCodeStrings.put(0x1C,Connection.res.getString(R.string.HCI_1C));
        hciErrorCodeStrings.put(0x1D,Connection.res.getString(R.string.HCI_1D));
        hciErrorCodeStrings.put(0x1E,Connection.res.getString(R.string.HCI_1E));
        hciErrorCodeStrings.put(0x1F,Connection.res.getString(R.string.HCI_1F));
        hciErrorCodeStrings.put(0x20,Connection.res.getString(R.string.HCI_20));
        hciErrorCodeStrings.put(0x21,Connection.res.getString(R.string.HCI_21));
        hciErrorCodeStrings.put(0x22,Connection.res.getString(R.string.HCI_22));
        hciErrorCodeStrings.put(0x23,Connection.res.getString(R.string.HCI_23));
        hciErrorCodeStrings.put(0x24,Connection.res.getString(R.string.HCI_24));
        hciErrorCodeStrings.put(0x25,Connection.res.getString(R.string.HCI_25));
        hciErrorCodeStrings.put(0x26,Connection.res.getString(R.string.HCI_26));
        hciErrorCodeStrings.put(0x27,Connection.res.getString(R.string.HCI_27));
        hciErrorCodeStrings.put(0x28,Connection.res.getString(R.string.HCI_28));
        hciErrorCodeStrings.put(0x29,Connection.res.getString(R.string.HCI_29));
        hciErrorCodeStrings.put(0x2A,Connection.res.getString(R.string.HCI_2A));
        hciErrorCodeStrings.put(0x2C,Connection.res.getString(R.string.HCI_2C));
        hciErrorCodeStrings.put(0x2D,Connection.res.getString(R.string.HCI_2D));
        hciErrorCodeStrings.put(0x2E,Connection.res.getString(R.string.HCI_2E));
        hciErrorCodeStrings.put(0x2F,Connection.res.getString(R.string.HCI_2F));
        hciErrorCodeStrings.put(0x30,Connection.res.getString(R.string.HCI_30));
        hciErrorCodeStrings.put(0x32,Connection.res.getString(R.string.HCI_32));
        hciErrorCodeStrings.put(0x34,Connection.res.getString(R.string.HCI_34));
        hciErrorCodeStrings.put(0x35,Connection.res.getString(R.string.HCI_35));
        hciErrorCodeStrings.put(0x36,Connection.res.getString(R.string.HCI_36));
        hciErrorCodeStrings.put(0x37,Connection.res.getString(R.string.HCI_37));
        hciErrorCodeStrings.put(0x38,Connection.res.getString(R.string.HCI_38));
        hciErrorCodeStrings.put(0x39,Connection.res.getString(R.string.HCI_39));
        hciErrorCodeStrings.put(0x3A,Connection.res.getString(R.string.HCI_3A));
        hciErrorCodeStrings.put(0x3B,Connection.res.getString(R.string.HCI_3B));
        hciErrorCodeStrings.put(0x3C,Connection.res.getString(R.string.HCI_3C));
        hciErrorCodeStrings.put(0x3D,Connection.res.getString(R.string.HCI_3D));
        hciErrorCodeStrings.put(0x3E,Connection.res.getString(R.string.HCI_3E));
        hciErrorCodeStrings.put(0x3F,Connection.res.getString(R.string.HCI_3F));
        hciErrorCodeStrings.put(0x40,Connection.res.getString(R.string.HCI_40));

    }
}
