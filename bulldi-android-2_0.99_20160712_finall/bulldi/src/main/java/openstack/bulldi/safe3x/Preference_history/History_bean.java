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
 * History_bean.java: history holder
 */

package openstack.bulldi.safe3x.Preference_history;


public class History_bean {
    int img1;
    String title;
    String value;
    String time1;
    String time2;
    boolean selected;
    //private CheckBoxImageView add;
    public String getValue() {
        return value;
    }
    public void setValue(String str) {
        this.value = str;
    }
    public int getImg1(){return img1;}
    public void setImg1(int img){this.img1=img;}
    public String getTitle(){return title;}
    public void setTitle(String str){this.title=str;}
    public String getTime1() {
        return time1;
    }
    public void setTime1(String str) {
        this.time1 = str;
    }
    public String getTime2() {
        return time2;
    }
    public void setTime2(String str) {
        this.time2 = str;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
