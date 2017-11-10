package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 13/06/2017.
 */

public interface BranchDataSource {
    Observable<List<Status>> getListBranch();

    Observable<List<Status>> getListBranch(String querry);
}
