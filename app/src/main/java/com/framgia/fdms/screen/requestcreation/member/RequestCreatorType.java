package com.framgia.fdms.screen.requestcreation.member;

import android.support.annotation.IntDef;

import static com.framgia.fdms.screen.requestcreation.member.RequestCreatorType.MEMBER_REQUEST;
import static com.framgia.fdms.screen.requestcreation.member.RequestCreatorType.MY_REQUEST;

/**
 * Requestcreation Screen.
 */
@IntDef({MEMBER_REQUEST, MY_REQUEST})
public @interface RequestCreatorType {
    int MEMBER_REQUEST = 0;
    int MY_REQUEST = 1;
}
