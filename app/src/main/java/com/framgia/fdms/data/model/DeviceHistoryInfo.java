package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Created by toand on 9/21/2017.
 */

public class DeviceHistoryInfo extends BaseObservable {
    @Expose
    @SerializedName("id")
    private int mId;
    @Expose
    @SerializedName("dms_object_id")
    private int mDmsObjectId;
    @Expose
    @SerializedName("dms_history_data")
    private String mHistoryData;
    @Expose
    @SerializedName("dms_object_type")
    private int mDmsObjectType;
    @Expose
    @SerializedName("created_at")
    private Date mCreatedAt;
    @Expose
    @SerializedName("updated_at")
    private Date mUpdatedAt;

    /**
     * Content of device
     */
    public class Content extends BaseObservable {
        @Expose
        @SerializedName("content")
        private String mContent;
        @Expose
        @SerializedName("status")
        private String mStatus;

        @Bindable
        public String getContent() {
            return mContent;
        }

        public void setContent(String content) {
            mContent = content;
            notifyPropertyChanged(BR.content);
        }

        @Bindable
        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
            notifyPropertyChanged(BR.status);
        }
    }

    @Bindable
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getDmsObjectId() {
        return mDmsObjectId;
    }

    public void setDmsObjectId(int dmsObjectId) {
        mDmsObjectId = dmsObjectId;
        notifyPropertyChanged(BR.dmsObjectId);
    }

    @Bindable
    public String getHistoryData() {
        return mHistoryData;
    }

    public void setHistoryData(String historyData) {
        mHistoryData = historyData;
        notifyPropertyChanged(BR.historyData);
    }

    @Bindable
    public int getDmsObjectType() {
        return mDmsObjectType;
    }

    public void setDmsObjectType(int dmsObjectType) {
        mDmsObjectType = dmsObjectType;
        notifyPropertyChanged(BR.dmsObjectType);
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
    public DeviceHistoryInfo.Content getHistoryContent() {
        if (getHistoryData() == null) {
            return null;
        }
        return new Gson().fromJson(getHistoryData(), DeviceHistoryInfo.Content.class);
    }
}
