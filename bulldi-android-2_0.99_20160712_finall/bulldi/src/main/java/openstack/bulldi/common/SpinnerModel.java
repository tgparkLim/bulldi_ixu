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
 * SpinnerModel.java
 */

package openstack.bulldi.common;


public class SpinnerModel {

    private  String Idea="";

    /*********** Set Methods ******************/
    public void setIdea(String Custom_idea)
    {
        this.Idea = Custom_idea;
    }


    /*********** Get Methods ****************/
    public String getIdea()
    {
        return this.Idea;
    }

}

