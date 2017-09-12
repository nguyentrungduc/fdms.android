package com.framgia.fdms.screen.deviceusingmanager.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.databinding.FragmentBaseDeviceUsingBinding;
import com.framgia.fdms.screen.deviceusingmanager.DeviceUsingManagerContract;

/**
 * Created by lamvu on 06/09/2017.
 */

public class DeviceUsingBaseFragment extends Fragment {

    private static final String ARGUMENT_FRAGMENT_NAME = "ARGUMENT_FRAGMENT_NAME";

    private DeviceUsingManagerContract.ViewModel mViewModel;

    public static DeviceUsingBaseFragment newInstance(String fragmentName) {
        DeviceUsingBaseFragment fragment = new DeviceUsingBaseFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_FRAGMENT_NAME, fragmentName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new DeviceUsingBaseViewModel();
        DeviceUsingManagerContract.Presenter presenter = new DeviceUsingBasePresenter(mViewModel);
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        FragmentBaseDeviceUsingBinding binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_base_device_using, container,
                false);
        binding.setViewModel((DeviceUsingBaseViewModel) mViewModel);
        return binding.getRoot();
    }
}
