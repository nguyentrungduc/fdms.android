package com.framgia.fdms.data.anotation;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import static com.framgia.fdms.data.anotation.Permission.ACCOUNTANT;
import static com.framgia.fdms.data.anotation.Permission.ADMIN;
import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.BO_STAFF;
import static com.framgia.fdms.data.anotation.Permission.DIVISION_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.NORMAL_USER;
import static com.framgia.fdms.data.anotation.Permission.SECTION_MANAGER;

/**
 * All user permission in system
 */

@IntDef({ADMIN, BO_MANAGER, BO_STAFF, ACCOUNTANT, DIVISION_MANAGER, SECTION_MANAGER, NORMAL_USER})
public @interface Permission {
    /**
     * Action
     * - manage branches
     * - manage countries
     * - manage offices
     * - general settings
     * - manage users
     * - manage roles
     */
    int ADMIN = 0;

    /**
     * Action
     * - manage device groups
     * - manage device categories
     * - manage devices
     * - manage vendors
     * - manage makers
     * - manage meeting rooms
     * - view device using histories
     * - manage requests: chuyển trạng thái từ "approved" sang "waiting done"
     * - View Dashboard Device, Request
     */
    // TODO: 11/6/2017 change to "BO Manager" when server is available
    int BO_MANAGER = 1;

    /**
     * Action
     * - manage device groups
     * - manage device categories
     * - manage devices
     * - manage vendors
     * - manage makers
     * - manage meeting rooms
     * - view device using histories
     * - manage requests: chuyển trạng thái từ "waiting done" sang "done"
     * - View Dashboard  Request, Device
     */
    int BO_STAFF = 4;

    /**
     * Action
     * - view all devices
     * - export devices list to excel format
     * - View Dashboard Device
     */
    int ACCOUNTANT = 6;

    /**
     * Action of DIVISION MANAGER & SECTION MANAGER
     * - view all devices
     * - view device using histories
     * - manage requests: chuyển trạng thái từ "waiting approve" sang "approved"
     * - View Dashboard  Request, Device
     */
    int DIVISION_MANAGER = 2;
    int SECTION_MANAGER = 7;
    int GROUP_LEADER = 3;

    /**
     * Action
     * - view my devices
     * - manage my requests: khi tạo request thì sẽ có trạng thái "waiting approve"
     * - View Dashboard  Request
     */
    int NORMAL_USER = 5;
}
