package com.framgia.fdms.screen.requestdetail.information;

import android.content.Intent;
import android.os.Parcelable;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;

import java.io.Serializable;

/**
 * Created by tuanbg on 5/24/17.
 */
public interface OnRequestUpdateSuccessListenner extends Serializable{
    void onUpdateSuccessFull(int requestId);
}
