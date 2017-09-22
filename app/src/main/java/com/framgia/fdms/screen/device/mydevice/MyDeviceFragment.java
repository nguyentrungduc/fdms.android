package com.framgia.fdms.screen.device.mydevice;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.databinding.FragmentMyDeviceBinding;

/**
 * MyDevice Screen.
 */
public class MyDeviceFragment extends Fragment {

    private MyDeviceContract.ViewModel mViewModel;

    public static MyDeviceFragment newInstance() {
        return new MyDeviceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new MyDeviceViewModel(this);

        MyDeviceContract.Presenter presenter = new MyDevicePresenter(mViewModel);
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        FragmentMyDeviceBinding binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_device, container, false);
        binding.setViewModel((MyDeviceViewModel) mViewModel);
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
