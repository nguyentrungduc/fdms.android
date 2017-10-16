package com.framgia.fdms.screen.selection;

import android.support.annotation.IntDef;

import static com.framgia.fdms.screen.selection.SelectionType.ASSIGNEE;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_DIALOG;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.selection.SelectionType.RELATIVE_STAFF;
import static com.framgia.fdms.screen.selection.SelectionType.REQUEST_CREATED_BY;
import static com.framgia.fdms.screen.selection.SelectionType.REQUEST_FOR;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.USER_BORROW;
import static com.framgia.fdms.screen.selection.SelectionType.VENDOR;

/**
 * Created by Hoang Van Nha on 5/24/2017.
 * <></>
 */

@IntDef({
    STATUS, CATEGORY, VENDOR, MARKER, BRANCH, MEETING_ROOM, DEVICE_GROUP, DEVICE_USING_HISTORY,
    STATUS_REQUEST, RELATIVE_STAFF, ASSIGNEE, USER_BORROW, DEVICE_GROUP_DIALOG, REQUEST_CREATED_BY,
    REQUEST_FOR, BRANCH_ALL, DEVICE_GROUP_ALL
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
    int RELATIVE_STAFF = 9;
    int ASSIGNEE = 10;
    int USER_BORROW = 11;
    int DEVICE_GROUP_DIALOG = 12;
    int REQUEST_CREATED_BY = 13;
    int REQUEST_FOR = 14;
    int BRANCH_ALL = 15;
    int DEVICE_GROUP_ALL = 16;
}
