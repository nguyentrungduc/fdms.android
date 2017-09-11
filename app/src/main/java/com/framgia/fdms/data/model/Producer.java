package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import com.framgia.fdms.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by framgia on 03/07/2017.
 */
public class Producer extends BaseObservable implements Parcelable {
    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("description")
    @Expose
    private String mDescription;

    public Producer() {
    }

    public Producer(String name, String description) {
        mName = name;
        mDescription = description;
    }

    @Bindable
    public String getName() {
        return mName == null ? "" : mName;
    }

    public void setName(String name) {
        mName = name;
        notifyPropertyChanged(BR.name);
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
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    protected Producer(Parcel in) {
        mName = in.readString();
        mDescription = in.readString();
        mId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mId);
        dest.writeString(mDescription);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Producer> CREATOR = new Parcelable.Creator<Producer>() {
        @Override
        public Producer createFromParcel(Parcel in) {
            return new Producer(in);
        }

        @Override
        public Producer[] newArray(int size) {
            return new Producer[size];
        }
    };
}