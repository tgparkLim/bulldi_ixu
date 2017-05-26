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
 * Music.java: control music
 */

package openstack.bulldi.safe3x.Alarm;


import android.content.Context;
import android.media.MediaPlayer;
public class Music {
    private static MediaPlayer mp = null;
    public static void play(Context context, int resource) {
        stop(context);
            mp = MediaPlayer.create(context, resource);
            mp.setLooping(true);
            mp.start();
    }
    public static void stop(Context context) {
        if (mp != null) {
            mp.stop();
            //mp.reset();
            mp.release();
            mp = null;
        }
    }
    public static void pause(Context context)
    {
        if (mp != null) {
            mp.pause();
            mp.release();
            mp = null;
        }
    }

}
