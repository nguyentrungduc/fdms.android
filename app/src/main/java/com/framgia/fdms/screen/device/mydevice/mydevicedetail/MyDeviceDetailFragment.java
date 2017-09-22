package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.R;
import com.framgia.fdms.databinding.FragmentMyDeviceDetailBinding;

/**
 * MyDeviceDetail Screen.
 */
public class MyDeviceDetailFragment extends Fragment {

    private MyDeviceDetailContract.ViewModel mViewModel;

    public static MyDeviceDetailFragment newInstance() {
        return new MyDeviceDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new MyDeviceDetailViewModel();

        MyDeviceDetailContract.Presenter presenter = new MyDeviceDetailPresenter(mViewModel);
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
