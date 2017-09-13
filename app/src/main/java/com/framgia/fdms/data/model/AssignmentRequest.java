package com.framgia.fdms.data.model;

import java.util.List;

/**
 * Created by MyPC on 13/09/2017.
 */

public class AssignmentRequest {
    private int mRequestId;
    private int mAssigneeId;
    private String mDescription;
    private List<AssignmentItemRequest> mItemRequests;

    public int getRequestId() {
        return mRequestId;
    }

    public void setRequestId(int requestId) {
        mRequestId = requestId;
    }

    public int getAssigneeId() {
        return mAssigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        mAssigneeId = assigneeId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<AssignmentItemRequest> getItemRequests() {
        return mItemRequests;
    }

    public void setItemRequests(List<AssignmentItemRequest> itemRequests) {
        mItemRequests = itemRequests;
    }
}
