package com.framgia.fdms.screen.deviceusingmanager;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.R;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.DeviceUsingHistoryRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.data.source.remote.DeviceUsingHistoryRemoteDataSource;
import com.framgia.fdms.databinding.FragmentDeviceUsingBinding;

/**
 * DeviceUsing Screen.
 */
public class DeviceUsingManagerFragment extends Fragment {

    private DeviceUsingManagerContract.ViewModel mViewModel;

    public static DeviceUsingManagerFragment newInstance() {
        return new DeviceUsingManagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new DeviceUsingManagerViewModel(this);

        DeviceUsingHistoryRepository deviceUsingHistoryRepository =
                new DeviceUsingHistoryRepository(DeviceUsingHistoryRemoteDataSource.getInstances());
        DeviceRepository deviceRepository =
                new DeviceRepository(new DeviceRemoteDataSource(FDMSServiceClient.getInstance()));

        DeviceUsingManagerContract.Presenter presenter =
                new DeviceUsingManagerPresenter(mViewModel, deviceUsingHistoryRepository, deviceRepository);

        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentDeviceUsingBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_device_using, container, false);
        binding.setViewModel((DeviceUsingManagerViewModel) mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
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
