package com.framgia.fdms.screen.producer;

import android.support.annotation.IntDef;

import static com.framgia.fdms.screen.producer.ProducerType.CATEGORIES_GROUPS;
import static com.framgia.fdms.screen.producer.ProducerType.DEVICE_GROUPS;
import static com.framgia.fdms.screen.producer.ProducerType.MARKER;
import static com.framgia.fdms.screen.producer.ProducerType.MEETING_ROOM;
import static com.framgia.fdms.screen.producer.ProducerType.VENDOR;

/**
 * Created by toand on 9/25/2017.
 */

/**
 * 2 type of product fragment to show
 * - Vendor
 * - Marker
 */
@IntDef({ VENDOR, MARKER, DEVICE_GROUPS, CATEGORIES_GROUPS, MEETING_ROOM })
public @interface ProducerType {
    int VENDOR = 0;
    int MARKER = 1;
    int DEVICE_GROUPS = 3;
    int CATEGORIES_GROUPS = 4;
    int MEETING_ROOM = 5;
}
