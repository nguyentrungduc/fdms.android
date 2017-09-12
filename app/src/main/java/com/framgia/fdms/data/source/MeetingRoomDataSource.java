package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.MeetingRoom;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by Vinh on 08/09/2017.
 */

public interface MeetingRoomDataSource {
    interface RemoteDataSource {
        Observable<List<MeetingRoom>> getListMeetingRoom(String roomName, int page, int perPage);
    }
}
