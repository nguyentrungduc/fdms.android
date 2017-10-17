package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by Vinh on 08/09/2017.
 */

public interface MeetingRoomDataSource {
    interface RemoteDataSource {
        Observable<List<Producer>> getListMeetingRoom(String roomName, int branchId, int page,
            int perPage);

        Observable<Producer> addMeetingRoom(Producer meetingRoom);

        Observable<Respone<String>> deleteMeetingRoom(Producer meetingRoom);

        Observable<String> editMeetingRoom(Producer meetingRoom);
    }
}
