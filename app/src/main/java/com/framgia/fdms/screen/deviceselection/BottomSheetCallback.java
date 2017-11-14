package com.framgia.fdms.screen.deviceselection;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.view.View;

import static com.framgia.fdms.data.anotation.Branch.Code.DA_NANG;
import static com.framgia.fdms.data.anotation.Branch.Code.HA_NOI;
import static com.framgia.fdms.data.anotation.Branch.Code.HO_CHI_MINH;
import static com.framgia.fdms.data.anotation.Branch.Id.ID_DA_NANG;
import static com.framgia.fdms.data.anotation.Branch.Id.ID_HA_NOI;
import static com.framgia.fdms.data.anotation.Branch.Id.ID_HO_CHI_MINH;
import static com.framgia.fdms.data.anotation.RequestStatus.ALL;


/**
 * All request status id
 */


public interface BottomSheetCallback {
    void onStateChanged(@NonNull View bottomSheet, int newState);
}