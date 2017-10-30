package com.framgia.fdms.screen.requestcreation;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.framgia.fdms.R;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.local.UserLocalDataSource;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceImp;
import com.framgia.fdms.data.source.remote.RequestRemoteDataSource;
import com.framgia.fdms.databinding.ActivityRequestCreationBinding;

import static com.framgia.fdms.screen.requestcreation.RequestCreatorType.MEMBER_REQUEST;
import static com.framgia.fdms.screen.requestcreation.RequestCreatorType.MY_REQUEST;
import static com.framgia.fdms.utils.Constant.BundleRequest.BUNDLE_REQUEST_TYPE;

/**
 * Requestcreation Screen.
 */
@IntDef({MEMBER_REQUEST, MY_REQUEST})
public @interface RequestCreatorType {
    int MEMBER_REQUEST = 0;
    int MY_REQUEST = 1;
}
