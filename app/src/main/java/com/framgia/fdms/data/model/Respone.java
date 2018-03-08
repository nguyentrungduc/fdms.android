package com.framgia.fdms.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by MyPC on 26/04/2017.
 */

public class Respone<T> implements Parcelable {
    @Expose
    @SerializedName("status")
    private int mStatus;
    @Expose
    @SerializedName("error")
    private boolean mError;
    @Expose
    @SerializedName("message")
    private String mMessage;
    @Expose
    @SerializedName("data")
    private T mData;

    protected Respone(Parcel in) {
        mStatus = in.readInt();
        mError = in.readByte() != 0;
        mMessage = in.readString();
    }

    public static final Creator<Respone> CREATOR = new Creator<Respone>() {
        @Override
        public Respone createFromParcel(Parcel in) {
            return new Respone(in);
        }

        @Override
        public Respone[] newArray(int size) {
            return new Respone[size];
        }
    };

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public boolean isError() {
        return mError;
    }

    public void setError(boolean error) {
        mError = error;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mStatus);
        parcel.writeByte((byte) (mError ? 1 : 0));
        parcel.writeString(mMessage);
    }
}
