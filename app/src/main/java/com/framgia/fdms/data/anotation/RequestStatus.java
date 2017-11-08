package com.framgia.fdms.data.anotation;

import android.support.annotation.IntDef;

import static com.framgia.fdms.data.anotation.RequestStatus.ALL;
import static com.framgia.fdms.data.anotation.RequestStatus.APPROVED;
import static com.framgia.fdms.data.anotation.RequestStatus.CANCELLED;
import static com.framgia.fdms.data.anotation.RequestStatus.DONE;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_APPROVE;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_DONE;


/**
 * All request status id
 */

@IntDef({ALL, CANCELLED, WAITING_APPROVE, APPROVED, WAITING_DONE, DONE})
public @interface RequestStatus {
    int ALL = -1;
    int CANCELLED = 1;
    int WAITING_APPROVE = 2;
    int APPROVED = 3;
    int WAITING_DONE = 4;
    int DONE = 5;
}
