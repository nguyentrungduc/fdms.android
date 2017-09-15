package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.fdms.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TITLE_NA;

/**
 * Created by MyPC on 03/05/2017.
 */

public class Status extends BaseObservable implements Parcelable {
    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
    public static String USING_STATUS = "using";
    @Expose
    @SerializedName("id")
    protected int mId;
    @Expose
    @SerializedName("name")
    protected String mName;

    public Status() {
    }

    public Status(int id) {
        mId = id;
    }

    public Status(int id, String name) {
        mId = id;
        mName = name;
    }

    protected Status(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
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
        return mId == OUT_OF_INDEX ? TITLE_NA : mName;
    }

    public void setName(String name) {
        mName = name;
        notifyPropertyChanged(BR.name);
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
    }
}
