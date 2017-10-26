package com.framgia.fdms.screen.requestdetail.information;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.local.UserLocalDataSource;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceImp;
import com.framgia.fdms.databinding.FragmentRequestInformationBinding;

import static com.framgia.fdms.utils.Constant.BundleRequest.BUND_REQUEST;

/**
 * Created by tuanbg on 5/24/17.
 */
public class RequestInformationFragment extends Fragment {
    private RequestInformationContract.ViewModel mViewModel;
    private Request mRequest;

    public static RequestInformationFragment newInstance(Request request) {
        RequestInformationFragment fragment = new RequestInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUND_REQUEST, request);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getRequestFromIntent();
        FragmentRequestInformationBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_request_information, container,
                        false);
        mViewModel = new RequestInformationViewModel(this, mRequest.getRequestActionList(),
                mRequest.getRequestStatus(), mRequest, binding.floatActionMenu);

        RequestInformationContract.Presenter presenter = new RequestInformationPresenter(mViewModel,
                new UserRepository(new UserLocalDataSource(new SharePreferenceImp(getContext()))));
        mViewModel.setPresenter(presenter);
        binding.setViewModel((RequestInformationViewModel) mViewModel);
        return binding.getRoot();
    }

    public void getRequestFromIntent() {
        mRequest = (Request) getArguments().get(BUND_REQUEST);
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }
}
