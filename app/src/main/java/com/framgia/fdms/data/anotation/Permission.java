package com.framgia.fdms.data.anotation;

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

@StringDef({ADMIN, BO_MANAGER, BO_STAFF, ACCOUNTANT, DIVISION_MANAGER, SECTION_MANAGER, NORMAL_USER})
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
    String ADMIN = "Admin";

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
    String BO_MANAGER = "manager";

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
    String BO_STAFF = "BO Staff";

    /**
     * Action
     * - view all devices
     * - export devices list to excel format
     * - View Dashboard Device
     */
    String ACCOUNTANT = "Accountant";

    /**
     * Action of DIVISION MANAGER & SECTION MANAGER
     * - view all devices
     * - view device using histories
     * - manage requests: chuyển trạng thái từ "waiting approve" sang "approved"
     * - View Dashboard  Request, Device
     */
    String DIVISION_MANAGER = "Division Manager";
    String SECTION_MANAGER = "Section Manager";
    String GROUP_LEADER = "Group Leader";

    /**
     * Action
     * - view my devices
     * - manage my requests: khi tạo request thì sẽ có trạng thái "waiting approve"
     * - View Dashboard  Request
     */
    String NORMAL_USER = "Normal User";
}
