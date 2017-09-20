package com.framgia.fdms.screen.new_selection;

import android.support.annotation.IntDef;

import static com.framgia.fdms.screen.new_selection.SelectionType.BRANCH;
import static com.framgia.fdms.screen.new_selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.new_selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.new_selection.SelectionType.DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.new_selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.new_selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.new_selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.new_selection.SelectionType.STATUS_REQUEST;
import static com.framgia.fdms.screen.new_selection.SelectionType.VENDOR;

/**
 * Created by Hoang Van Nha on 5/24/2017.
 * <></>
 */

@IntDef({
    STATUS, CATEGORY, VENDOR, MARKER, BRANCH, MEETING_ROOM, DEVICE_GROUP, DEVICE_USING_HISTORY,
    STATUS_REQUEST
})
public @interface SelectionType {
    int STATUS = 0;
    int CATEGORY = 1;
    int VENDOR = 2;
    int MARKER = 3;
    int BRANCH = 4;
    int MEETING_ROOM = 5;
    int DEVICE_GROUP = 6;
    int DEVICE_USING_HISTORY = 7;
    int STATUS_REQUEST = 8;
}
