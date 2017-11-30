package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Created by framgia on 23/05/2017.
 */
public class NewDeviceUsingHistory extends BaseObservable {
    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("return_date")
    @Expose
    private Date mReturnDate;
    @SerializedName("borrow_date")
    @Expose
    private Date mBorrowDate;
    @SerializedName("device_id")
    @Expose
    private int mDeviceId;
    @SerializedName("assignment_id")
    @Expose
    private int mAssignmentId;
    @SerializedName("created_at")
    @Expose
    private Date mCreatedAt;
    @SerializedName("updated_at")
    @Expose
    private Date mUpdatedAt;
    @SerializedName("assignee")
    @Expose
    private User mAssignee;

    @Bindable
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Date getReturnDate() {
        return mReturnDate;
    }

    public void setReturnDate(Date returnDate) {
        mReturnDate = returnDate;
        notifyPropertyChanged(BR.returnDate);
    }

    @Bindable
    public Date getBorrowDate() {
        return mBorrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        mBorrowDate = borrowDate;
        notifyPropertyChanged(BR.borrowDate);
    }

    @Bindable
    public int getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(int deviceId) {
        mDeviceId = deviceId;
        notifyPropertyChanged(BR.deviceId);
    }

    @Bindable
    public int getAssignmentId() {
        return mAssignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        mAssignmentId = assignmentId;
        notifyPropertyChanged(BR.assignmentId);
    }

    @Bindable
    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }

    @Bindable
    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
        notifyPropertyChanged(BR.updatedAt);
    }

    @Bindable
    public User getAssignee() {
        return mAssignee;
    }

    public void setAssignee(User assignee) {
        mAssignee = assignee;
        notifyPropertyChanged(BR.assignee);
    }
}
