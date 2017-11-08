package com.framgia.fdms.data.anotation;

import android.support.annotation.IntDef;

import static com.framgia.fdms.data.anotation.RequestStatus.ALL;
import static com.framgia.fdms.data.anotation.RequestStatus.APPROVED;
import static com.framgia.fdms.data.anotation.RequestStatus.CANCELLED;
import static com.framgia.fdms.data.anotation.RequestStatus.DONE;
import static com.framgia.fdms.data.anotation.RequestStatus.EDIT;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_APPROVE;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_DONE;
import static com.framgia.fdms.data.anotation.RequestStatus.RESEND;
import static com.framgia.fdms.data.anotation.RequestStatus.ASSIGNMENT;


/**
 * All request status id
 */

@IntDef({ALL, CANCELLED, WAITING_APPROVE, APPROVED, WAITING_DONE, DONE, EDIT, RESEND, ASSIGNMENT})
public @interface RequestStatus {
    int ALL = -1;
    int CANCELLED = 1;
    int WAITING_APPROVE = 2;
    int APPROVED = 3;
    int WAITING_DONE = 4;
    int DONE = 5;
    int EDIT = 6;
    int RESEND = 7;
    int ASSIGNMENT = 8;
}