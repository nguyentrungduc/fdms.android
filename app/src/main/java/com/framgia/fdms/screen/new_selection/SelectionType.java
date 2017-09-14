package com.framgia.fdms.screen.new_selection;

import android.support.annotation.IntDef;

import static com.framgia.fdms.screen.new_selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.new_selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.new_selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.new_selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.new_selection.SelectionType.VENDOR;

/**
 * Created by Hoang Van Nha on 5/24/2017.
 * <></>
 */

@IntDef({ STATUS, CATEGORY, VENDOR, MARKER, MEETING_ROOM })
public @interface SelectionType {
    int STATUS = 0;
    int CATEGORY = 1;
    int VENDOR = 2;
    int MARKER = 3;
    int MEETING_ROOM = 4;
}
