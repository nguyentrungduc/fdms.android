package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by beepi on 09/05/2017.
 */

public class Request extends BaseObservable implements Parcelable, Cloneable {
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
    private Status mRequestStatus;
    @Expose
    @SerializedName("assignee")
    private Status mAssignee;
    @Expose
    @SerializedName("for_user")
    private Status mRequestFor;
    @Expose
    @SerializedName("creator")
    private Status mCreater;
    @Expose
    @SerializedName("updater")
    private Status mUpdater;
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
    @Expose
    @SerializedName("handler")
    private String mHandler;

    public Request() {
    }

    public Request(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mDescription = in.readString();
        mRequestStatus = in.readParcelable(Status.class.getClassLoader());
        mAssignee = in.readParcelable(Status.class.getClassLoader());
        mRequestFor = in.readParcelable(Status.class.getClassLoader());
        mCreater = in.readParcelable(Status.class.getClassLoader());
        mUpdater = in.readParcelable(Status.class.getClassLoader());
        mDevices = in.createTypedArrayList(DeviceRequest.CREATOR);
        mRequestActionList = in.createTypedArrayList(RequestAction.CREATOR);
        mHandler = in.readString();
        mCreatedAt = (Date) in.readSerializable();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

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
    public Status getRequestStatus() {
        return mRequestStatus;
    }

    public void setRequestStatus(Status requestStatus) {
        mRequestStatus = requestStatus;
        notifyPropertyChanged(BR.requestStatus);
    }

    @Bindable
    public String getHandler() {
        return mHandler;
    }

    public void setHandler(String handler) {
        mHandler = handler;
        notifyPropertyChanged(BR.handler);
    }

    @Bindable
    public Status getAssignee() {
        return mAssignee;
    }

    public void setAssignee(Status assignee) {
        mAssignee = assignee;
        notifyPropertyChanged(BR.assignee);
    }

    @Bindable
    public Status getRequestFor() {
        return mRequestFor;
    }

    public void setRequestFor(Status requestFor) {
        mRequestFor = requestFor;
        notifyPropertyChanged(BR.requestFor);
    }

    @Bindable
    public Status getCreater() {
        return mCreater;
    }

    public void setCreater(Status creater) {
        mCreater = creater;
    }

    @Bindable
    public Status getUpdater() {
        return mUpdater;
    }

    public void setUpdater(Status updater) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeParcelable(mRequestStatus, i);
        parcel.writeParcelable(mAssignee, i);
        parcel.writeParcelable(mRequestFor, i);
        parcel.writeParcelable(mCreater, i);
        parcel.writeParcelable(mUpdater, i);
        parcel.writeTypedList(mDevices);
        parcel.writeTypedList(mRequestActionList);
        parcel.writeString(mHandler);
        parcel.writeSerializable(mCreatedAt);
    }

    public static class DeviceRequest extends BaseObservable implements Parcelable, Cloneable {
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

        protected DeviceRequest(Parcel in) {
            mId = in.readInt();
            mDeviceName = in.readString();
            mDescription = in.readString();
            mNumber = in.readInt();
            mCategoryName = in.readString();
            mCategoryId = in.readInt();
            mCategory = in.readParcelable(Status.class.getClassLoader());
        }

        public static final Creator<DeviceRequest> CREATOR = new Creator<DeviceRequest>() {
            @Override
            public DeviceRequest createFromParcel(Parcel in) {
                return new DeviceRequest(in);
            }

            @Override
            public DeviceRequest[] newArray(int size) {
                return new DeviceRequest[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mId);
            parcel.writeString(mDeviceName);
            parcel.writeString(mDescription);
            parcel.writeInt(mNumber);
            parcel.writeString(mCategoryName);
            parcel.writeInt(mCategoryId);
            parcel.writeParcelable(mCategory, i);
        }
    }

    public static class RequestAction extends BaseObservable implements Parcelable {
        @Expose
        @SerializedName("id")
        private int mId;
        @Expose
        @SerializedName("name")
        private String mName;

        public RequestAction() {
        }

        public RequestAction(Parcel in) {
            mId = in.readInt();
            mName = in.readString();
        }

        public static final Creator<RequestAction> CREATOR = new Creator<RequestAction>() {
            @Override
            public RequestAction createFromParcel(Parcel in) {
                return new RequestAction(in);
            }

            @Override
            public RequestAction[] newArray(int size) {
                return new RequestAction[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mId);
            parcel.writeString(mName);
        }
    }
}
