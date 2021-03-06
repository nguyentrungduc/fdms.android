package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.data.anotation.Permission;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 * Created by levutantuan on 3/31/17.
 */
public class User extends BaseObservable implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @Expose
    @SerializedName("id")
    private int mId;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("first_name")
    private String mFirstName;
    @Expose
    @SerializedName("last_name")
    private String mLastName;
    @Expose
    @SerializedName("email")
    private String mEmail;
    @Expose
    @SerializedName("address")
    private String mAddress;
    @Expose
    @SerializedName("password_digest")
    private String mPasswordDigest;
    @Expose
    @SerializedName("reset_digest")
    private String mResetDigest;
    @Expose
    @SerializedName("created_by")
    private String mCreatedBy;
    @Expose
    @SerializedName("updated_by")
    private String mUpdatedBy;
    @Expose
    @SerializedName("created_at")
    private Date mCreatedAt;
    @Expose
    @SerializedName("updated_at")
    private Date mUpdatedAt;
    @Expose
    @SerializedName("remember_digest")
    private String mRememberDigest;
    @Expose
    @SerializedName("avatar")
    private String mAvatar;
    @Expose
    @SerializedName("from_excel")
    private boolean mFromExcel;
    @Expose
    @SerializedName("gender")
    private String mGender;
    @Expose
    @SerializedName("role")
    @Permission
    private int mRole;
    @Expose
    @SerializedName("birthday")
    private Date mBirthday;
    @Expose
    @SerializedName("employee_code")
    private String mEmployeeCode;
    @Expose
    @SerializedName("status")
    private String mStatus;
    @Expose
    @SerializedName("contract_date")
    private Date mContractDate;
    @Expose
    @SerializedName("start_probation_date")
    private Date mStartProbationDate;
    @Expose
    @SerializedName("end_probation_date")
    private Date mEndProbationDate;
    @Expose
    @SerializedName("token")
    private String mToken;
    @Expose
    @SerializedName("card_number")
    private String mCardNumber;
    @Expose
    @SerializedName("branch")
    private String mBranch;
    @SerializedName("groups")
    @Expose
    private List<Status> mGroups;

    public User() {
    }

    protected User(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
        mAddress = in.readString();
        mPasswordDigest = in.readString();
        mResetDigest = in.readString();
        mCreatedBy = in.readString();
        mUpdatedBy = in.readString();
        mRememberDigest = in.readString();
        mAvatar = in.readString();
        mFromExcel = in.readByte() != 0;
        mGender = in.readString();
        mRole = in.readInt();
        mEmployeeCode = in.readString();
        mStatus = in.readString();
        mToken = in.readString();
        mCardNumber = in.readString();
        mBranch = in.readString();
        mGroups = in.createTypedArrayList(Status.CREATOR);
    }

    @Bindable
    public String getCardNumber() {
        return mCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        mCardNumber = cardNumber;
        notifyPropertyChanged(BR.cardNumber);
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
    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getPasswordDigest() {
        return mPasswordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        mPasswordDigest = passwordDigest;
        notifyPropertyChanged(BR.passwordDigest);
    }

    @Bindable
    public String getResetDigest() {
        return mResetDigest;
    }

    public void setResetDigest(String resetDigest) {
        mResetDigest = resetDigest;
        notifyPropertyChanged(BR.resetDigest);
    }

    @Bindable
    public String getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        mCreatedBy = createdBy;
        notifyPropertyChanged(BR.createdBy);
    }

    @Bindable
    public String getUpdatedBy() {
        return mUpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        mUpdatedBy = updatedBy;
        notifyPropertyChanged(BR.updatedBy);
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
    public String getRememberDigest() {
        return mRememberDigest;
    }

    public void setRememberDigest(String rememberDigest) {
        mRememberDigest = rememberDigest;
        notifyPropertyChanged(BR.rememberDigest);
    }

    @Bindable
    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public boolean isFromExcel() {
        return mFromExcel;
    }

    public void setFromExcel(boolean fromExcel) {
        mFromExcel = fromExcel;
        notifyPropertyChanged(BR.fromExcel);
    }

    @Bindable
    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Permission
    @Bindable
    public int getRole() {
        return mRole;
    }

    public void setRole(@Permission int role) {
        mRole = role;
        notifyPropertyChanged(BR.role);
    }

    @Bindable
    public Date getBirthday() {
        return mBirthday;
    }

    public void setBirthday(Date birthday) {
        mBirthday = birthday;
        notifyPropertyChanged(BR.birthday);
    }

    @Bindable
    public String getEmployeeCode() {
        return mEmployeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        mEmployeeCode = employeeCode;
        notifyPropertyChanged(BR.employeeCode);
    }

    @Bindable
    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public Date getContractDate() {
        return mContractDate;
    }

    public void setContractDate(Date contractDate) {
        mContractDate = contractDate;
        notifyPropertyChanged(BR.contractDate);
    }

    @Bindable
    public Date getStartProbationDate() {
        return mStartProbationDate;
    }

    public void setStartProbationDate(Date startProbationDate) {
        mStartProbationDate = startProbationDate;
        notifyPropertyChanged(BR.startProbationDate);
    }

    @Bindable
    public Date getEndProbationDate() {
        return mEndProbationDate;
    }

    public void setEndProbationDate(Date endProbationDate) {
        mEndProbationDate = endProbationDate;
        notifyPropertyChanged(BR.endProbationDate);
    }

    @Bindable
    public String getBranch() {
        return mBranch;
    }

    public void setBranch(String branch) {
        mBranch = branch;
        notifyPropertyChanged(BR.branch);
    }

    @Bindable
    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
        notifyPropertyChanged(BR.token);
    }

    @Bindable
    public List<Status> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Status> groups) {
        mGroups = groups;
        notifyPropertyChanged(com.framgia.fdms.BR.groups);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mAddress);
        dest.writeString(mPasswordDigest);
        dest.writeString(mResetDigest);
        dest.writeString(mCreatedBy);
        dest.writeString(mUpdatedBy);
        dest.writeString(mRememberDigest);
        dest.writeString(mAvatar);
        dest.writeByte((byte) (mFromExcel ? 1 : 0));
        dest.writeString(mGender);
        dest.writeInt(mRole);
        dest.writeString(mEmployeeCode);
        dest.writeString(mStatus);
        dest.writeString(mToken);
        dest.writeString(mCardNumber);
        dest.writeString(mBranch);
        dest.writeTypedList(mGroups);
    }
}
