package com.framgia.fdms.screen.devicedetail.usinghistory;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.databinding.FragmentDeviceUsingHistoryBinding;

import static com.framgia.fdms.utils.Constant.BundleConstant.EXTRA_DEVICE_CODE;

/**
 * UsingHistory Screen.
 */
public class DeviceUsingHistoryFragment extends Fragment {

    private DeviceUsingHistoryContract.ViewModel mViewModel;

    public static DeviceUsingHistoryFragment newInstance(String deviceCode) {
        DeviceUsingHistoryFragment fragment = new DeviceUsingHistoryFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_DEVICE_CODE, deviceCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new DeviceUsingHistoryViewModel(this);

        DeviceRepository repository =
            new DeviceRepository(new DeviceRemoteDataSource(FDMSServiceClient.getInstance()));
        DeviceUsingHistoryContract.Presenter presenter =
            new DeviceUsingHistoryPresenter(mViewModel, repository,
                getArguments().getString(EXTRA_DEVICE_CODE));

        mViewModel.setPresenter(presenter);
        mViewModel.setDeviceUsingHistoryPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        FragmentDeviceUsingHistoryBinding binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_device_using_history, container,
                false);
        binding.setViewModel((DeviceUsingHistoryViewModel) mViewModel);
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
