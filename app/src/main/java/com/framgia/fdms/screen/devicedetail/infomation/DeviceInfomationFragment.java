package com.framgia.fdms.screen.devicedetail.infomation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.databinding.FragmentDeviceInfomationBinding;
import com.framgia.fdms.utils.navigator.Navigator;

/**
 * Deviceinfomation Screen.
 */
public class DeviceInfomationFragment extends Fragment {

    public static final String EXTRA_DEVIVE = "EXTRA_DEVIVE";
    private DeviceInfomationContract.ViewModel mViewModel;
    private Device mDevice;

    public static DeviceInfomationFragment newInstance() {
        return new DeviceInfomationFragment();
    }

    public static DeviceInfomationFragment newInstance(Device device) {
        DeviceInfomationFragment fragment = new DeviceInfomationFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_DEVIVE, device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Navigator navigator = new Navigator(this);
        mDevice = getArguments().getParcelable(EXTRA_DEVIVE);
        mViewModel = new DeviceInfomationViewModel(getContext(), getActivity(), mDevice, navigator);

        DeviceInfomationContract.Presenter presenter = new DeviceInfomationPresenter(mViewModel,
            new DeviceRepository(new DeviceRemoteDataSource(FDMSServiceClient.getInstance())),
            mDevice);
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        FragmentDeviceInfomationBinding binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_device_infomation, container,
                false);
        binding.setViewModel((DeviceInfomationViewModel) mViewModel);
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

    public void onStartEditDevice() {
        if (mViewModel != null) {
            mViewModel.onEditDevice();
        }
    }

    public void onDeleteDevice() {
        if (mViewModel != null) {
            mViewModel.onDeleteDevice();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }
}
