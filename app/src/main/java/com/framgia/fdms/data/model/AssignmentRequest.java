package com.framgia.fdms.data.model;

import java.util.List;

/**
 * Created by MyPC on 13/09/2017.
 */

public class AssignmentRequest {
    private int mRequestId;
    private int mAssigneeId;
    private String mDescription;
    private List<Device> mDevices;

    public AssignmentRequest(int requestId, int assigneeId, String description,
        List<Device> devices) {
        mRequestId = requestId;
        mAssigneeId = assigneeId;
        mDescription = description;
        mDevices = devices;
    }

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

    public List<Device> getDevices() {
        return mDevices;
    }

    public void setDevices(List<Device> devices) {
        mDevices = devices;
    }
}
