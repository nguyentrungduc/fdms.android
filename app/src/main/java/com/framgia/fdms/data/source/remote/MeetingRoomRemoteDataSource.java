package com.framgia.fdms.data.source.remote;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.MeetingRoomDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.framgia.fdms.utils.Constant.ApiParram.PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.PER_PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.ROOM_NAME;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by ASUS on 08/09/2017.
 */

public class MeetingRoomRemoteDataSource extends BaseRemoteDataSource
    implements MeetingRoomDataSource.RemoteDataSource {

    public MeetingRoomRemoteDataSource(FDMSApi fdmsApi) {
        super(fdmsApi);
    }

    @Override
    public Observable<List<Producer>> getListMeetingRoom(String roomName, int page, int perPage) {
        return mFDMSApi.getListMeetingRoom(getRoomParams(roomName, page, perPage))
            .flatMap(
                new Function<Respone<List<Producer>>, ObservableSource<List<Producer>>>() {
                    @Override
                    public ObservableSource<List<Producer>> apply(
                        Respone<List<Producer>> listRespone) {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    private Map<String, String> getRoomParams(String roomName, int page, int perPage) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(roomName)) {
            params.put(ROOM_NAME, roomName);
        }
        if (page != OUT_OF_INDEX) {
            params.put(PAGE, String.valueOf(page));
        }
        if (perPage != OUT_OF_INDEX) {
            params.put(PER_PAGE, String.valueOf(perPage));
        }

        return params;
    }
}
