package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.databinding.FragmentListDeviceAssignmentBinding;

import static com.framgia.fdms.utils.Constant.BundleRequest.BUND_REQUEST;

/**
 * RequestDetail Screen.
 */
public class ListDeviceAssignmentFragment extends Fragment {

    private ListDeviceAssignmentContract.ViewModel mViewModel;
    private Request mRequest;

    public static ListDeviceAssignmentFragment newInstance(Request request) {
        ListDeviceAssignmentFragment fragment = new ListDeviceAssignmentFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUND_REQUEST, request);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDataFromIntent();
        mViewModel = new ListDeviceAssignmentViewModel(this, mRequest);

        DeviceRepository repository = DeviceRepository.getInstance(
                new DeviceRemoteDataSource(FDMSServiceClient.getInstance()));
        ListDeviceAssignmentContract.Presenter presenter =
                new ListDeviceAssignmentPresenter(mViewModel, repository);
        mViewModel.setPresenter(presenter);

        FragmentListDeviceAssignmentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_list_device_assignment, container,
                        false);
        binding.setViewModel((ListDeviceAssignmentViewModel) mViewModel);
        return binding.getRoot();
    }

    private void getDataFromIntent() {
        mRequest = (Request) getActivity().getIntent().getSerializableExtra(BUND_REQUEST);
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
