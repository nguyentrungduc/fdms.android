package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.MeetingRoom;
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

    public Observable<List<MeetingRoom>> getListMeetingRoom(String roomName, int page,
        int perPage) {
        return mRemoteDataSource.getListMeetingRoom(roomName, page, perPage);
    }
}
