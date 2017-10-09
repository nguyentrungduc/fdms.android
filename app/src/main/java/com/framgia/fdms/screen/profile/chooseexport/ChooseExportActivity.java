package com.framgia.fdms.screen.profile.chooseexport;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.databinding.ActivityChooseExportBinding;
import java.util.ArrayList;
import java.util.List;

import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEIVER_USER;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICES;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_RECEIVER_USER;

/**
 * Created by tuanbg on 6/15/17.
 */

public class ChooseExportActivity extends AppCompatActivity {
    private ChooseExportViewModel mViewModel;
    private List<Device> mDevices;
    private User mDeliver;
    private User mReceiver;

    public static Intent newInstance(Context context, User deliver, User receiver,
        ArrayList<Device> devices) {
        Intent intent = new Intent(context, ChooseExportActivity.class);
        intent.putExtra(BUNDLE_DEIVER_USER, deliver);
        intent.putExtra(BUNDLE_RECEIVER_USER, receiver);
        intent.putParcelableArrayListExtra(BUNDLE_DEVICES, devices);
        return intent;
    }

    public void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        mDevices = bundle.getParcelableArrayList(BUNDLE_DEVICES);
        mDeliver = bundle.getParcelable(BUNDLE_DEIVER_USER);
        mReceiver = bundle.getParcelable(BUNDLE_RECEIVER_USER);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChooseExportBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_choose_export);
        getDataFromIntent();
        mViewModel = new ChooseExportViewModel(this, mDeliver, mDevices);
        ChooseExportContract.Presenter presenter = new ChooseExportPresenter(mViewModel);
        mViewModel.setPresenter(presenter);
        binding.setViewModel(mViewModel);
        mViewModel.initToolbar(binding.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_using:
                // TODO: 6/15/17 getList Using
                return true;
            case R.id.item_used:
                // TODO: 6/15/17 getList Used
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
