package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.fdms.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by doan.van.toan on 1/2/18.
 */

public class AssigneeUser extends BaseObservable implements Parcelable {
    @Expose
    @SerializedName("id")
    protected int mId;
    @Expose
    @SerializedName("name")
    protected String mName;
    @SerializedName("groups")
    @Expose
    private List<Status> mGroups;

    public AssigneeUser(int id, String name) {
        mId = id;
        mName = name;
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
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public List<Status> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Status> groups) {
        mGroups = groups;
        notifyPropertyChanged(BR.groups);
    }

    public AssigneeUser(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mGroups = in.createTypedArrayList(Status.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeTypedList(mGroups);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssigneeUser> CREATOR = new Creator<AssigneeUser>() {
        @Override
        public AssigneeUser createFromParcel(Parcel in) {
            return new AssigneeUser(in);
        }

        @Override
        public AssigneeUser[] newArray(int size) {
            return new AssigneeUser[size];
        }
    };
}
