package com.framgia.fdms.screen.producer.marker;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.R;
import com.framgia.fdms.data.source.MakerRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.MakerRemoteDataSource;
import com.framgia.fdms.databinding.FragmentMarkerBinding;

/**
 * MarkerFragment Screen.
 */
public class MarkerFragment extends Fragment {
    private MarkerContract.ViewModel mViewModel;

    public static MarkerFragment newInstance() {
        return new MarkerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new MarkerViewModel(getActivity());
        MarkerContract.Presenter presenter = new MarkerPresenter(mViewModel, MakerRepository
            .getInstant(new MakerRemoteDataSource(FDMSServiceClient.getInstance())));
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentMarkerBinding binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_marker, container, false);
        binding.setViewModel((MarkerViewModel) mViewModel);
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
