package com.framgia.fdms.screen.requestdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.databinding.ActivityRequestDetailBinding;
import com.framgia.fdms.utils.navigator.Navigator;

import static com.framgia.fdms.utils.Constant.BundleRequest.BUND_REQUEST;

/**
 * RequestDetail Screen.
 */
public class RequestDetailActivity extends AppCompatActivity {

    private RequestDetailContract.ViewModel mViewModel;
    private Request mRequest;

    public static Intent getInstance(Context context, Request request) {
        Intent intent = new Intent(context, RequestDetailActivity.class);
        intent.putExtra(BUND_REQUEST, request);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        Navigator navigator = new Navigator(this);
        mViewModel = new RequestDetailViewModel(this, mRequest, navigator);

        RequestDetailContract.Presenter presenter = new RequestDetailPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityRequestDetailBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_request_detail);
        binding.setViewModel((RequestDetailViewModel) mViewModel);
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
        mRequest = (Request) bundle.getSerializable(BUND_REQUEST);
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
}
