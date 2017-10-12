package com.framgia.fdms.screen.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceImp;
import com.framgia.fdms.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.data.source.local.sharepref.SharePreferenceKey.LANGUAGE_PRES;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.ENGLISH;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.ENGLISH_POSITION;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.JAPANESE;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.JAPANESE_POSITION;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.VIETNAMESE;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.VIETNAMESE_POSITION;
import static com.framgia.fdms.utils.Constant.PICK_IMAGE_REQUEST;
import static com.framgia.fdms.utils.Utils.changeLanguage;

/**
 * Exposes the data to be used in the Profile screen.
 */

public class ProfileViewModel extends BaseObservable
    implements ProfileContract.ViewModel, DatePickerDialog.OnDateSetListener {

    private final AppCompatActivity mActivity;
    private final Context mContext;
    private final ProfileFragment mFragment;
    private Calendar mCalendar = Calendar.getInstance();
    private SharePreferenceImp mPreferences;
    private ProfileContract.Presenter mPresenter;
    private User mUser;
    private boolean mIsEdit;
    private String mBirthDay;
    private String mContractDate;
    private String mStartProbationDate;
    private String mLanguage;
    private int mItemLanguageSelectedPosition;
    private boolean mIsRefresh;

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getCurrentUser();
            }
        };

    public ProfileViewModel(AppCompatActivity activity, ProfileFragment fragment) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mFragment = fragment;
        mPreferences = new SharePreferenceImp(mContext);
        initLanguage(mPreferences.get(LANGUAGE_PRES, Integer.class));
    }

    private void initLanguage(int language) {
        switch (language) {
            case ENGLISH_POSITION:
                setLanguage(mContext.getString(R.string.text_english));
                changeLanguage(ENGLISH, mActivity);
                setItemLanguageSelectedPosition(ENGLISH_POSITION);
                break;
            case VIETNAMESE_POSITION:
                setLanguage(mContext.getString(R.string.text_vietnamese));
                changeLanguage(VIETNAMESE, mActivity);
                setItemLanguageSelectedPosition(VIETNAMESE_POSITION);
                break;
            case JAPANESE_POSITION:
                setLanguage(mContext.getString(R.string.text_japanese));
                changeLanguage(JAPANESE, mActivity);
                setItemLanguageSelectedPosition(JAPANESE_POSITION);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            mUser.setAvatar(Utils.getPathFromUri(mActivity, uri));
            notifyChange();
        }
    }

    @Override
    public void setCurrentUser(User user) {
        mUser = user;
        if (mUser == null) mUser = new User();

        DateFormat format = new SimpleDateFormat("dd - MM - yyyy", Locale.getDefault());
        mBirthDay = mUser.getBirthday() == null ? "" : format.format(mUser.getBirthday());
        mContractDate =
            mUser.getContractDate() == null ? "" : format.format(mUser.getContractDate());
        mStartProbationDate = mUser.getStartProbationDate() == null ? ""
            : format.format(mUser.getStartProbationDate());

        setUser(user);
        setRefresh(false);
        notifyChange();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPickDateTime() {
        if (mCalendar == null) mCalendar = Calendar.getInstance();
        DatePickerDialog datePicker =
            DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show(mActivity.getFragmentManager(), "");
    }

    @Override
    public void onClickPickAvatar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mFragment.startActivityForResult(
            Intent.createChooser(intent, mContext.getString(R.string.title_select_file_upload)),
            PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClickEditProfile() {
        mIsEdit = !mIsEdit;
        notifyChange();
    }

    @Override
    public void onUpdateProfileSuccess(User user) {
        mUser.setGender(user.getGender());
        mUser.setAddress(user.getAddress());
        mUser.setBirthday(user.getBirthday());
        mIsEdit = !mIsEdit;
        notifyPropertyChanged(BR.edit);
        notifyPropertyChanged(BR.user);
    }

    @Override
    public void onClickDoneEditProfile() {
        mPresenter.updateUserProfile(mUser.getId(), mUser.getGender(), mUser.getAddress(),
            mBirthDay);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mBirthDay = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        setBirthDay(mBirthDay);
    }

    public void onClickChangeLanguage() {
        new AlertDialog.Builder(mActivity).setSingleChoiceItems(R.array.language,
            getItemLanguageSelectedPosition(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int itemPosition) {
                    mPresenter.saveLanguage(itemPosition);
                    initLanguage(itemPosition);
                    setItemLanguageSelectedPosition(itemPosition);
                    dialog.dismiss();
                    restartActivity();
                }
            }).show();
    }

    private void restartActivity() {
        Intent intent = mActivity.getIntent();
        mActivity.finish();
        mActivity.startActivity(intent);
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public boolean isEdit() {
        return mIsEdit;
    }

    public void setEdit(boolean edit) {
        mIsEdit = edit;
        notifyPropertyChanged(BR.edit);
    }

    @Bindable
    public String getBirthDay() {
        return mBirthDay;
    }

    public void setBirthDay(String birthDay) {
        mBirthDay = birthDay;
        notifyPropertyChanged(BR.birthDay);
    }

    @Bindable
    public String getContractDate() {
        return mContractDate;
    }

    public void setContractDate(String contractDate) {
        mContractDate = contractDate;
        notifyPropertyChanged(BR.contractDate);
    }

    @Bindable
    public String getStartProbationDate() {
        return mStartProbationDate;
    }

    public void setStartProbationDate(String startProbationDate) {
        mStartProbationDate = startProbationDate;
        notifyPropertyChanged(BR.startProbationDate);
    }

    @Bindable
    public boolean isRefresh() {
        return mIsRefresh;
    }

    public void setRefresh(boolean refresh) {
        mIsRefresh = refresh;
        notifyPropertyChanged(BR.refresh);
    }

    @Bindable
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return mOnRefreshListener;
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
        notifyPropertyChanged(BR.onRefreshListener);
    }

    @Bindable
    public String getLanguage() {
        return mLanguage != null ? mLanguage : "";
    }

    public void setLanguage(String language) {
        mLanguage = language;
        notifyPropertyChanged(BR.language);
    }

    @Bindable
    public int getItemLanguageSelectedPosition() {
        return mItemLanguageSelectedPosition;
    }

    public void setItemLanguageSelectedPosition(int itemLanguageSelectedPosition) {
        mItemLanguageSelectedPosition = itemLanguageSelectedPosition;
        notifyPropertyChanged(BR.itemLanguageSelectedPosition);
    }
}
