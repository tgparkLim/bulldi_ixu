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
 * BleDeviceInfo.java
 */

package openstack.bulldi.common;

import android.bluetooth.BluetoothDevice;

public class BleDeviceInfo {
  // Data
  private BluetoothDevice mBtDevice;
  private int mRssi;

  public BleDeviceInfo(BluetoothDevice device, int rssi) {
    mBtDevice = device;
    mRssi = rssi;
  }

  public BluetoothDevice getBluetoothDevice() {
    return mBtDevice;
  }

  public int getRssi() {
    return mRssi;
  }

  public void updateRssi(int rssiValue) {
    mRssi = rssiValue;
  }

}
