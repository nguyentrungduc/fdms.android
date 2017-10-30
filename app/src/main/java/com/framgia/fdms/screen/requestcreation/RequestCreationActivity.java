package com.framgia.fdms.screen.requestcreation;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

import static com.framgia.fdms.utils.Constant.BundleRequest.BUNDLE_REQUEST_TYPE;

/**
 * Requestcreation Screen.
 */
public class RequestCreationActivity extends AppCompatActivity {

    private RequestCreationContract.ViewModel mViewModel;
    private int mManageRequestType;

    public static Intent getInstance(Context context, @RequestCreatorType int manageRequestType) {
        Intent intent = new Intent(context, RequestCreationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_REQUEST_TYPE, manageRequestType);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        mViewModel = new RequestCreationViewModel(this, mManageRequestType);

        RequestCreationContract.Presenter presenter = new RequestCreationPresenter(mViewModel,
                mManageRequestType,
                new RequestRepository(new RequestRemoteDataSource(FDMSServiceClient.getInstance())),
                new UserRepository(new UserLocalDataSource(new SharePreferenceImp(this))));
        mViewModel.setPresenter(presenter);

        ActivityRequestCreationBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_request_creation);
        binding.setViewModel((RequestCreationViewModel) mViewModel);
        setTitle(getString(R.string.title_create_request));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        mManageRequestType = bundle.getInt(BUNDLE_REQUEST_TYPE);
    }
}
