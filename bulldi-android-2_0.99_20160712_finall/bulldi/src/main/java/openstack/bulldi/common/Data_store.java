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
 * Data_store.java
 */

package openstack.bulldi.common;


public class Data_store {

    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public double data;
    public Data_store(){
        year=0;
        month=0;
        day=0;
        hour=0;
        minute=0;
        data=0;
    }
   public void set_time(int y,int mo,int d, int h, int mi){
        year=y;
        month=mo;
        day=d;
       hour=h;
       minute=mi;
    }
    public void set_data(double da){
        data=da;
    }
}
