package com.framgia.fdms.screen.requestcreation.forme;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.framgia.fdms.R;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.local.UserLocalDataSource;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceImp;
import com.framgia.fdms.data.source.remote.RequestRemoteDataSource;
import com.framgia.fdms.data.source.remote.StatusRemoteDataSource;
import com.framgia.fdms.databinding.ActivityRequestForMeCreationBinding;

/**
 * Requestcreation Screen.
 */
public class RequestForMeActivity extends AppCompatActivity {

    private RequestForMeContract.ViewModel mViewModel;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, RequestForMeActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new RequestForMeViewModel(this);

        RequestForMeContract.Presenter presenter = new RequestForMePresenter(mViewModel,
                new RequestRepository(new RequestRemoteDataSource(FDMSServiceClient.getInstance())),
                new UserRepository(new UserLocalDataSource(new SharePreferenceImp(this))),
                new StatusRepository(new StatusRemoteDataSource(FDMSServiceClient.getInstance())));
        mViewModel.setPresenter(presenter);

        ActivityRequestForMeCreationBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_request_for_me_creation);
        binding.setViewModel((RequestForMeViewModel) mViewModel);
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
}
