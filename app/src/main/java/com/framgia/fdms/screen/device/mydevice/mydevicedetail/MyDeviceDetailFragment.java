package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.databinding.FragmentMyDeviceDetailBinding;

import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_TYPE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_USER;

/**
 * MyDeviceDetail Screen.
 */
public class MyDeviceDetailFragment extends Fragment {

    private MyDeviceDetailContract.ViewModel mViewModel;

    public static MyDeviceDetailFragment newInstance(User user, @MyDeviceType int type) {
        MyDeviceDetailFragment fragment = new MyDeviceDetailFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_TYPE, type);
        args.putParcelable(BUNDLE_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new MyDeviceDetailViewModel(this);
        User user = getArguments().getParcelable(BUNDLE_USER);
        MyDeviceDetailContract.Presenter presenter =
            new MyDeviceDetailPresenter(mViewModel, getArguments().getInt(BUNDLE_TYPE),
                user.getEmail(),
                new DeviceRepository(new DeviceRemoteDataSource(FDMSServiceClient.getInstance())));
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        FragmentMyDeviceDetailBinding binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_device_detail, container, false);
        binding.setViewModel((MyDeviceDetailViewModel) mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
