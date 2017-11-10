package com.framgia.fdms.screen.device.listdevice.meetingroom;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.data.source.MeetingRoomRepository;
import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.MeetingRoomRemoteDataSource;
import com.framgia.fdms.data.source.remote.VendorRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

/**
 * StatusSelection Screen.
 */
public class SelectMeetingRoomActivity extends BaseSelectionActivity {
    public static Intent getInstance(Context context) {
        return new Intent(context, SelectMeetingRoomActivity.class);
    }

    @Override
    public void getDataFromIntent() {

    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        MeetingRoomRepository meetingRoomRepository = new MeetingRoomRepository(
                new MeetingRoomRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectMeetingRoomPresenter(mViewModel, meetingRoomRepository);
    }
}
