package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by Vinh on 08/09/2017.
 */

public class MeetingRoomRepository {
    private final MeetingRoomDataSource.RemoteDataSource mRemoteDataSource;

    public MeetingRoomRepository(MeetingRoomDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public Observable<List<Producer>> getListMeetingRoom(String roomName, int branchId, int page,
        int perPage) {
        return mRemoteDataSource.getListMeetingRoom(roomName, branchId, page, perPage);
    }

    public Observable<Producer> addMeetingRoom(Producer marker) {
        return mRemoteDataSource.addMeetingRoom(marker);
    }

    public Observable<Respone<String>> deleteMeetingRoom(Producer marker) {
        return mRemoteDataSource.deleteMeetingRoom(marker);
    }

    public Observable<String> editMeetingRoom(Producer marker) {
        return mRemoteDataSource.editMeetingRoom(marker);
    }
}
