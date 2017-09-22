package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import android.support.annotation.IntDef;

import static com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceType.ALL;
import static com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceType.RETURNED;
import static com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceType.USING;

/**
 * Created by toand on 9/22/2017.
 */
@IntDef({ ALL, USING, RETURNED })
public @interface MyDeviceType {
    int ALL = 0;
    int USING = 1;
    int RETURNED = 2;
}
