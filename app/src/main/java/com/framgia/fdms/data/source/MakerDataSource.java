package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by beepi on 05/07/2017.
 */
public interface MakerDataSource {
    interface RemoteDataSource {
        Observable<List<Producer>> getListMarkers(int page, int perPage);
    }
}
