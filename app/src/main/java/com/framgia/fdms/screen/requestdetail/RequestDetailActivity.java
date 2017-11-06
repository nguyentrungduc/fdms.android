package com.framgia.fdms.screen.requestdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.framgia.fdms.R;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.RequestRemoteDataSource;
import com.framgia.fdms.databinding.ActivityRequestDetailBinding;
import com.framgia.fdms.screen.requestdetail.information.OnRequestUpdateSuccessListenner;
import com.framgia.fdms.utils.navigator.Navigator;

/**
 * RequestDetail Screen.
 */
public class RequestDetailActivity extends AppCompatActivity
        implements OnRequestUpdateSuccessListenner {

    private static final String BUND_REQUEST_ID = "BUND_REQUEST_ID";

    private RequestDetailContract.ViewModel mViewModel;
    private int mRequestId;

    public static Intent getInstance(Context context, int requestId) {
        Intent intent = new Intent(context, RequestDetailActivity.class);
        intent.putExtra(BUND_REQUEST_ID, requestId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        Navigator navigator = new Navigator(this);
        mViewModel = new RequestDetailViewModel(this, navigator);

        ActivityRequestDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_request_detail);
        binding.setViewModel((RequestDetailViewModel) mViewModel);

        RequestDetailContract.Presenter presenter = new RequestDetailPresenter(mViewModel,
                RequestRepository.getInstant(
                        new RequestRemoteDataSource(FDMSServiceClient.getInstance())),
                mRequestId);

        mViewModel.setPresenter(presenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        mRequestId = bundle.getInt(BUND_REQUEST_ID);
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
    public void onUpdateSuccessFull(int requestId) {
        mViewModel.onUpdateSuccessFull(requestId);
    }
}
