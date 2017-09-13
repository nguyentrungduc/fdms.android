package com.framgia.fdms.screen.producer;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.data.source.MarkerRepository;
import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.MarkerRemoteDataSource;
import com.framgia.fdms.data.source.remote.VendorRemoteDataSource;
import com.framgia.fdms.databinding.FragmentProducerBinding;

import static com.framgia.fdms.screen.producer.ProducerFragment.ProductType.MARKER;
import static com.framgia.fdms.screen.producer.ProducerFragment.ProductType.VENDOR;

/**
 * Vendor Screen.
 */
public class ProducerFragment extends Fragment {
    private static final String ARGUMENT_TYPE = "ARGUMENT_TYPE";
    private ProducerContract.ViewModel mViewModel;
    @ProductType
    private int mFragmentType;

    public static ProducerFragment newInstance(@ProductType int type) {
        ProducerFragment fragment = new ProducerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ProducerViewModel(getActivity());
        getDataIntent();

        ProducerContract.Presenter presenter = new ProducerPresenter(mViewModel, mFragmentType,
            VendorRepository.getInstances(
                new VendorRemoteDataSource(FDMSServiceClient.getInstance())),
            MarkerRepository.getInstance(
                MarkerRemoteDataSource.getInstance(FDMSServiceClient.getInstance())));

        mViewModel.setPresenter(presenter);
    }

    private void getDataIntent() {
        if (getArguments() != null) {
            mFragmentType = getArguments().getInt(ARGUMENT_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        FragmentProducerBinding binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_producer, container, false);
        binding.setViewModel((ProducerViewModel) mViewModel);
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

    /**
     * 2 type of product fragment to show
     * - Vendor
     * - Marker
     */
    @IntDef({ VENDOR, MARKER })
    public @interface ProductType {
        int VENDOR = 0;
        int MARKER = 1;
    }
}
