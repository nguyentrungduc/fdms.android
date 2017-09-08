package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 08/09/2017.
 */

public class MeetingRoom extends BaseObservable implements Parcelable {

    @Expose
    @SerializedName("id")
    private int mId;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("description")
    private String mDescription;
    @Expose
    @SerializedName("device")
    private List<Device> mDevices = new ArrayList<>();

    protected MeetingRoom(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mDescription = in.readString();
        mDevices = in.createTypedArrayList(Device.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeTypedList(mDevices);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeetingRoom> CREATOR = new Creator<MeetingRoom>() {
        @Override
        public MeetingRoom createFromParcel(Parcel in) {
            return new MeetingRoom(in);
        }

        @Override
        public MeetingRoom[] newArray(int size) {
            return new MeetingRoom[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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
