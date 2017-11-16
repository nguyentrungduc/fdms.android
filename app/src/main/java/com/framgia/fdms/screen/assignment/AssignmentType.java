package com.framgia.fdms.screen.assignment;

import android.support.annotation.IntDef;

import static com.framgia.fdms.screen.assignment.AssignmentType.ASSIGN_BY_MEETING_ROOM;
import static com.framgia.fdms.screen.assignment.AssignmentType.ASSIGN_BY_NEW_MEMBER;
import static com.framgia.fdms.screen.assignment.AssignmentType.ASSIGN_BY_REQUEST;

/**
 * Created by toand on 9/26/2017.
 */

@IntDef({ASSIGN_BY_REQUEST, ASSIGN_BY_NEW_MEMBER, ASSIGN_BY_MEETING_ROOM})
public @interface AssignmentType {
    int ASSIGN_BY_REQUEST = 0;
    int ASSIGN_BY_NEW_MEMBER = 1;
    int ASSIGN_BY_MEETING_ROOM = 2;
}
