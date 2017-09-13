package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 9/13/17.
 */

public interface MarkerDataSource {
    Observable<List<Producer>> getListMarker(String name, int page, int perPage);

    Observable<Producer> addMarker(Producer marker);

    Observable<Respone<String>> deleteMarker(Producer marker);

    Observable<String> editMarker(Producer marker);
}
