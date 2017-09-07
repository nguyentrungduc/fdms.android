package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.databinding.FragmentListMeetingRoomBinding;

/**
 * ListMeetingRoom Screen.
 */
public class ListMeetingRoomFragment extends Fragment {

    private ListMeetingRoomContract.ViewModel mViewModel;

    public static ListMeetingRoomFragment newInstance() {
        return new ListMeetingRoomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ListMeetingRoomViewModel();

        ListMeetingRoomContract.Presenter presenter = new ListMeetingRoomPresenter(mViewModel);
        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        FragmentListMeetingRoomBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_list_meeting_room, container,
                        false);
        binding.setViewModel((ListMeetingRoomViewModel) mViewModel);
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
