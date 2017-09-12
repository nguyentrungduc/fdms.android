package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.framgia.fdms.R;
import com.framgia.fdms.databinding.ActivityDetailMeetingRoomBinding;

/**
 * DetailMeetingRoom Screen.
 */
public class DetailMeetingRoomActivity extends AppCompatActivity {

    private DetailMeetingRoomContract.ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new DetailMeetingRoomViewModel();

        DetailMeetingRoomContract.Presenter presenter = new DetailMeetingRoomPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityDetailMeetingRoomBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_detail_meeting_room);
        binding.setViewModel((DetailMeetingRoomViewModel) mViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
