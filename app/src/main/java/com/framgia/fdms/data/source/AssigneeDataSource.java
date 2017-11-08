package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by MyPC on 05/05/2017.
 */

public interface AssigneeDataSource {
    Observable<List<Status>> getListAssignee();

    Observable<List<Status>> getListAssignee(String name);
}
