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
 * SensorTagGatt.java: UUID definition of bulldi's services
 */

package openstack.bulldi.safe3x.Device_View;

import static java.util.UUID.fromString;

import java.util.UUID;

public class SensorTagGatt {

  public final static UUID

     //UUID for Battery
       UUID_BATTERY_SERV = fromString("0000180f-0000-1000-8000-00805f9b34fb"),
       UUID_BATTERY_DATA = fromString("00002a19-0000-1000-8000-00805f9b34fb"),

    //UUID for IRT
    UUID_IRT_SERV = fromString("f000ab20-0451-4000-b000-000000000000"),
    UUID_IRT_DATA = fromString("f000ab21-0451-4000-b000-000000000000"),
    UUID_IRT_CONF = fromString("f000ab22-0451-4000-b000-000000000000"), // 0: disable, 1: enable
     UUID_IRT_PERI = fromString("f000ab23-0451-4000-b000-000000000000"), // Period in tens of milliseconds

    //UUID for smoke
    UUID_SMO_SERV = fromString("f000ab00-0451-4000-b000-000000000000"),
    UUID_SMO_DATA = fromString("f000ab01-0451-4000-b000-000000000000"),
    UUID_SMO_CONF = fromString("f000ab02-0451-4000-b000-000000000000"),
    UUID_SMO_PERI = fromString("f000ab03-0451-4000-b000-000000000000"),

    //UUID for CO
    UUID_CO_SERV = fromString("f000ab10-0451-4000-b000-000000000000"),
    UUID_CO_DATA = fromString("f000ab11-0451-4000-b000-000000000000"),
    UUID_CO_CONF = fromString("f000ab12-0451-4000-b000-000000000000"),
    UUID_CO_PERI = fromString("f000ab13-0451-4000-b000-000000000000"),

    //UUID for connection control
    UUID_CONNECT_SERV = fromString("f000ccc0-0451-4000-b000-000000000000"),
    UUID_CONNECT_CURRENT = fromString("f000ccc1-0451-4000-b000-000000000000"),
    UUID_CONNECT_NEW = fromString("f000ccc2-0451-4000-b000-000000000000"),
    UUID_CONNECT_DISCONNECT = fromString("f000ccc3-0451-4000-b000-000000000000"),

   //UUID for I/O service
    UUID_IO_SERV = fromString("f000aa64-0451-4000-b000-000000000000"),
    UUID_IO_DATA = fromString("f000aa65-0451-4000-b000-000000000000"),
    UUID_IO_CONFIG = fromString("f000aa66-0451-4000-b000-000000000000"),

  //UUID for OAD service
  UUID_OAD_SERV = fromString("f000ffc0-0451-4000-b000-000000000000"),
  UUID_OAD_IDENTIFY = fromString("f000ffc1-0451-4000-b000-000000000000"),
  UUID_OAD_BLOCK = fromString("f000ffc2-0451-4000-b000-000000000000");
}
