package com.framgia.fdms.data.anotation;

import android.support.annotation.IntDef;

import static com.framgia.fdms.data.anotation.UsingStatus.ALL;
import static com.framgia.fdms.data.anotation.UsingStatus.RETURNED;
import static com.framgia.fdms.data.anotation.UsingStatus.USING;


/**
 * All request status id
 */

@IntDef({ALL, USING, RETURNED})
public @interface UsingStatus {
    int ALL = -1;
    int USING = 0;
    int RETURNED = 1;
}
