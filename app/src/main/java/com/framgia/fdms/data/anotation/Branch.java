package com.framgia.fdms.data.anotation;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import static com.framgia.fdms.data.anotation.Branch.Id.ID_DA_NANG;
import static com.framgia.fdms.data.anotation.Branch.Id.ID_HA_NOI;
import static com.framgia.fdms.data.anotation.Branch.Id.ID_HO_CHI_MINH;
import static com.framgia.fdms.data.anotation.Branch.Code.DA_NANG;
import static com.framgia.fdms.data.anotation.Branch.Code.HA_NOI;
import static com.framgia.fdms.data.anotation.Branch.Code.HO_CHI_MINH;
import static com.framgia.fdms.data.anotation.RequestStatus.ALL;


/**
 * All request status id
 */


public interface Branch {
    @IntDef({ALL, ID_HA_NOI, ID_DA_NANG, ID_HO_CHI_MINH})
    @interface Id {
        int ALL = -1;
        int ID_HA_NOI = 1;
        int ID_DA_NANG = 2;
        int ID_HO_CHI_MINH = 3;
    }

    @StringDef({HA_NOI, DA_NANG, HO_CHI_MINH})
    @interface Code {
        String HA_NOI = "FHN";
        String DA_NANG = "FDN";
        String HO_CHI_MINH = "FHCM";
    }
}