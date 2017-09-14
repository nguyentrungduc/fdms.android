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
public class Producer extends Status implements Parcelable {
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
    @Expose
    @SerializedName("description")
    private String mDescription;
    @Expose
    @SerializedName("created_at")
    private String mCreatedAt;
    @Expose
    @SerializedName("updated_at")
    private String mUpdatedAt;

    public Producer(){
    }

    public Producer(Parcel in) {
        super(in);
        mDescription = in.readString();
        mCreatedAt = in.readString();
        mUpdatedAt = in.readString();
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
    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
        notifyPropertyChanged(BR.creatAt);
    }

    @Bindable
    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
        notifyPropertyChanged(BR.updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mCreatedAt);
        dest.writeString(mUpdatedAt);
    }
}