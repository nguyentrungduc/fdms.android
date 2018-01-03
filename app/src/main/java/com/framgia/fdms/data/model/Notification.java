package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.fdms.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Nhahv0902 on 6/1/2017.
 * <></>
 */

public class Notification extends BaseObservable {
    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("body")
    @Expose
    private String mBody;
    @SerializedName("link")
    @Expose
    private String mLink;
    @SerializedName("checked")
    @Expose
    private boolean mChecked;
    @SerializedName("created_at")
    @Expose
    private Date mCreatedAt;
    @SerializedName("sender")
    @Expose
    private User mSender;

    @Bindable
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
        notifyPropertyChanged(BR.body);
    }

    @Bindable
    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
        notifyPropertyChanged(BR.link);
    }

    @Bindable
    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        notifyPropertyChanged(BR.checked);
    }

    @Bindable
    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
        notifyPropertyChanged(BR.creatAt);
    }

    @Bindable
    public User getSender() {
        return mSender;
    }

    public void setSender(User sender) {
        mSender = sender;
        notifyPropertyChanged(BR.sender);
    }
}
