package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.framgia.fdms.utils.Utils.FORMAT_DATE_DD_MM_YYYY;
import static com.framgia.fdms.utils.Utils.INPUT_TIME_FORMAT;

/**
 * Created by beepi on 09/05/2017.
 */

public class Request extends BaseObservable implements Serializable, Cloneable {
    @Expose
    @SerializedName("id")
    private int mId;
    @Expose
    @SerializedName("title")
    private String mTitle;
    @Expose
    @SerializedName("description")
    private String mDescription;
    @Expose
    @SerializedName("request_status")
    private String mRequestStatus;
    @Expose
    @SerializedName("assignee")
    private String mAssignee;
    @Expose
    @SerializedName("assignee_id")
    private int mAssigneeId;
    @Expose
    @SerializedName("request_for")
    private String mRequestFor;
    @Expose
    @SerializedName("creater")
    private String mCreater;
    @Expose
    @SerializedName("updater")
    private String mUpdater;
    @Expose
    @SerializedName("device_assignment")
    private List<DeviceRequest> mDevices;
    @Expose
    @SerializedName("created_at")
    private Date mCreatedAt;
    @Expose
    @SerializedName("list_action")
    private List<RequestAction> mRequestActionList = new ArrayList<>();
    @Expose
    @SerializedName("create_at")
    private Date mCreatAt;
    @SerializedName("device")
    private List<Device> mDevice;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Bindable
    public List<RequestAction> getRequestActionList() {
        return mRequestActionList;
    }

    public void setRequestActionList(List<RequestAction> requestActionList) {
        mRequestActionList = requestActionList;
        notifyPropertyChanged(BR.requestActionList);
    }

    @Bindable

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        notifyPropertyChanged(BR.title);
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Bindable
    public String getRequestStatus() {
        return mRequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        mRequestStatus = requestStatus;
        notifyPropertyChanged(BR.requestStatus);
    }

    public String getAssignee() {
        return mAssignee;
    }

    public void setAssignee(String assignee) {
        mAssignee = assignee;
    }

    @Bindable
    public String getRequestFor() {
        return mRequestFor;
    }

    public void setRequestFor(String requestFor) {
        mRequestFor = requestFor;
        notifyPropertyChanged(BR.requestFor);
    }

    @Bindable
    public String getCreater() {
        return mCreater;
    }

    public void setCreater(String creater) {
        mCreater = creater;
    }

    @Bindable
    public String getUpdater() {
        return mUpdater;
    }

    public void setUpdater(String updater) {
        mUpdater = updater;
        notifyPropertyChanged(BR.updater);
    }

    @Bindable
    public List<DeviceRequest> getDevices() {
        return mDevices;
    }

    public void setDevices(List<DeviceRequest> devices) {
        mDevices = devices;
        notifyPropertyChanged(BR.devices);
    }

    @Bindable
    public Date getCreatAt() {
        return mCreatAt;
    }

    public void setCreatAt(Date creatAt) {
        mCreatAt = creatAt;
        notifyPropertyChanged(BR.creatAt);
    }

    @Bindable
    public List<Device> getDevice() {
        return mDevice;
    }

    public void setDevice(List<Device> device) {
        mDevice = device;
        notifyPropertyChanged(BR.device);
    }

    @Bindable
    public int getAssigneeId() {
        return mAssigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        mAssigneeId = assigneeId;
        notifyPropertyChanged(BR.assigneeId);
    }

    @Bindable
    public String getRequestDescription() {
        String nameDevice;
        if (getDevices() == null || getDevices().size() == 0) {
            nameDevice = FDMSApplication.getInstant().getString(R.string.title_device);
        } else {
            nameDevice = getDevices().get(0).getCategoryName();
        }
        return String.format(FDMSApplication.getInstant().getString(R.string.title_request),
            getCreater(), nameDevice, getRequestFor());
    }

    public static class DeviceRequest extends BaseObservable implements Serializable, Cloneable {
        @Expose
        @SerializedName("id")
        private int mId;
        @Expose
        @SerializedName("product_name")
        private String mDeviceName;
        @Expose
        @SerializedName("description")
        private String mDescription;
        @Expose
        @SerializedName("number")
        private int mNumber;
        @Expose
        @SerializedName("device_category")
        private String mCategoryName;
        @Expose
        @SerializedName("device_category_id")
        private int mCategoryId;
        private Status mCategory;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Bindable
        public Status getCategory() {
            return mCategory;
        }

        public void setCategory(Status category) {
            mCategory = category;
            notifyPropertyChanged(BR.category);
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        @Bindable
        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
            notifyPropertyChanged(BR.description);
        }

        @Bindable
        public int getNumber() {
            return mNumber;
        }

        public void setNumber(int number) {
            mNumber = number;
            notifyPropertyChanged(BR.number);
        }

        @Bindable
        public String getCategoryName() {
            return mCategoryName;
        }

        public void setCategoryName(String categoryName) {
            mCategoryName = categoryName;
            notifyPropertyChanged(BR.categoryName);
        }

        @Bindable
        public String getDeviceName() {
            return mDeviceName;
        }

        public void setDeviceName(String deviceName) {
            mDeviceName = deviceName;
            notifyPropertyChanged(BR.deviceName);
        }

        @Bindable
        public int getCategoryId() {
            return mCategoryId;
        }

        public void setCategoryId(int categoryId) {
            mCategoryId = categoryId;
            notifyPropertyChanged(BR.categoryId);
        }

        public void onDecrement() {
            if (getNumber() > 1) {
                setNumber(getNumber() - 1);
            }
        }

        public void onIncrement() {
            setNumber(getNumber() + 1);
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public class RequestAction extends BaseObservable implements Serializable {
        @Expose
        @SerializedName("id")
        private int mId;
        @Expose
        @SerializedName("name")
        private String mName;

        @Bindable
        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
            notifyPropertyChanged(BR.id);
        }

        @Bindable
        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
            notifyPropertyChanged(BR.name);
        }
    }
}
