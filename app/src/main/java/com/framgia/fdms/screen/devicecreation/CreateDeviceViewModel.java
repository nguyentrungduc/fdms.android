package com.framgia.fdms.screen.devicecreation;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.devicecreation.selectdevicestatus.SelectDeviceStatusActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.screen.selection.SelectionType;
import com.framgia.fdms.utils.Utils;
import com.framgia.fdms.utils.navigator.Navigator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.widget.Toast.makeText;
import static com.framgia.fdms.FDMSApplication.sUpdatedDevice;
import static com.framgia.fdms.screen.devicecreation.DeviceStatusType.CREATE;
import static com.framgia.fdms.screen.devicecreation.DeviceStatusType.EDIT;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.AVAIABLE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SUCCESS;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_BRANCH;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CATEGORY;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_MAKER;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_MEETING_ROOM;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_STATUS;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_VENDOR;
import static com.framgia.fdms.utils.Constant.USING;

/**
 * Exposes the data to be used in the Createdevice screen.
 */
public class CreateDeviceViewModel extends BaseObservable
        implements CreateDeviceContract.ViewModel, DatePickerDialog.OnDateSetListener {

    private static final int DEFAULT_WIDTH_QRCODE = 300;
    private static final int DEFAULT_HEIGHT_QRCODE = 300;
    private static final int DEFAULT_WIDTH_BARCODE = 200;
    private static final int DEFAULT_HEIGHT_BARCODE = 100;
    private static final int SCALE_BITMAP = 7;

    private DeviceStatusType mDeviceType = CREATE;
    private Context mContext;
    private CreateDeviceActivity mActivity;
    private CreateDeviceContract.Presenter mPresenter;

    private String mDeviceCodeError;
    private String mNameDeviceError;
    private String mSerialNumberError;
    private String mModelNumberError;
    private String mBoughtDateError;
    private String mCategoryError;
    private String mVendorError;
    private String mMakerError;
    private String mOriginalPriceError;
    private String mWarrantyError;
    private String mMeetingRoomError;

    private String mBoughtDate;
    private Device mDevice;
    private String mStatusError;
    private Status mCategory;
    private Status mStatus;
    private Status mBranch;
    private Calendar mCalendar = Calendar.getInstance();
    private boolean mIsQrCode = true;
    private boolean mIsBarCode;
    private Bitmap mDeviceCode;
    private int mProgressBarVisibility = GONE;
    private Navigator mNavigator;
    private boolean mIsAllowEditMeetingRoom;

    public CreateDeviceViewModel(CreateDeviceActivity activity, Device device,
                                 DeviceStatusType type) {
        mContext = activity.getApplicationContext();
        mActivity = activity;
        if (device == null) {
            mDevice = initDefaultDevice();
            setAllowEditMeetingRoom(true);
        } else {
            mDevice = device;
        }
        mDeviceType = type;
        mNavigator = new Navigator(activity);
    }

    private Device initDefaultDevice() {
        Device device = new Device();
        mStatus = new Status(AVAIABLE, mActivity.getString(R.string.title_available));
        device.setVendor(new Producer(OUT_OF_INDEX, ""));
        device.setMarker(new Producer(OUT_OF_INDEX, ""));
        device.setDeviceStatusId(AVAIABLE);
        return device;
    }

    @Override
    public void onActionDeviceClick() {
        switch (mDeviceType) {
            case CREATE:
                mPresenter.registerDevice(mDevice);
                break;
            case EDIT:
                mPresenter.updateDevice(mDevice);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPickDateTimeClick() {
        if (mDeviceType == EDIT) {
            return;
        }
        if (mCalendar == null) mCalendar = Calendar.getInstance();
        DatePickerDialog datePicker =
                DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show(mActivity.getFragmentManager(), "");
    }

    @Override
    public void onLoadError(String msg) {
        makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void onChooseCategory() {
        if (mDeviceType == EDIT) {
            return;
        }
        mActivity.startActivityForResult(
                SelectionActivity.getInstance(mContext, SelectionType.CATEGORY, OUT_OF_INDEX),
                REQUEST_CATEGORY);
    }

    public void onChooseMeetingRoom() {
        mActivity.startActivityForResult(
                SelectionActivity.getInstance(mContext, SelectionType.MEETING_ROOM),
                REQUEST_MEETING_ROOM);
    }

    public void onChooseStatus() {
        if ((mStatus.getName() != null && mStatus.getName().equals(Status.USING_STATUS))
                || mDeviceType == CREATE) {
            return;
        }
        mActivity.startActivityForResult(
                SelectDeviceStatusActivity.getInstance(mContext, mDevice.getDeviceStatusId()),
                REQUEST_STATUS);
    }

    public void onChooseBranch() {
        if (mDeviceType == EDIT) {
            return;
        }
        mActivity.startActivityForResult(
                SelectionActivity.getInstance(mContext, SelectionType.BRANCH), REQUEST_BRANCH);
    }

    public void onChooseVendor() {
        mActivity.startActivityForResult(
                SelectionActivity.getInstance(mContext, SelectionType.VENDOR), REQUEST_VENDOR);
    }

    public void onChooseMaker() {
        mActivity.startActivityForResult(
                SelectionActivity.getInstance(mContext, SelectionType.MARKER), REQUEST_MAKER);
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
    public void setPresenter(CreateDeviceContract.Presenter presenter) {
        mPresenter = presenter;
        if (mDeviceType == EDIT) {
            mPresenter.getDeviceById(mDevice);
        } else {
            mPresenter.getDefaultBranch();
        }
    }

    @Override
    public void showProgressbar() {
        setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        setProgressBarVisibility(GONE);
    }

    @Override
    public void onRegisterError() {
        Snackbar.make(mActivity.findViewById(android.R.id.content),
                R.string.msg_create_device_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRegisterSuccess(Device device) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SUCCESS, R.string.msg_create_device_success);
        bundle.putParcelable(BUNDLE_DEVICE, device);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
    }

    @Override
    public void onGetDeviceSuccess(Device device) {
        setDevice(device);
        setCategory(new Status(device.getDeviceCategoryId(), device.getDeviceCategoryName()));
        setBoughtDate(Utils.stringBoughtDateDevice(device.getBoughtDate()));
        setStatus(new Status(device.getDeviceStatusId(), device.getDeviceStatusName()));
        setBarCode(device.isBarcode());
        setQrCode(!device.isBarcode());
        setAllowEditMeetingRoom((device.isDeviceMeetingRoom() &&
                device.getDeviceStatusId() == USING) ||
                device.getDeviceStatusId() == AVAIABLE);
    }

    @Override
    public void onGetDeviceError(String error) {
        mNavigator.showToast(error);
    }

    @Override
    public void onInputProductionNameError() {
        mNameDeviceError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.nameDeviceError);
    }

    @Override
    public void onInputSerialNumberError() {
        mSerialNumberError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.serialNumberError);
    }

    @Override
    public void onInputModellNumberError() {
        mModelNumberError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.modelNumberError);
    }

    @Override
    public void onInputDeviceCodeError() {
        mDeviceCodeError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.deviceCodeError);
    }

    @Override
    public void onInputCategoryError() {
        mCategoryError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.categoryError);
    }

    @Override
    public void onInputMeetingRoomError() {
        mMeetingRoomError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.meetingRoomError);
    }

    @Override
    public void onInputStatusError() {
        mStatusError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.statusError);
    }

    @Override
    public void onInputBoughtDateError() {
        mBoughtDateError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.boughtDateError);
    }

    @Override
    public void onInputOriginalPriceError() {
        mOriginalPriceError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.originalPriceError);
    }

    @Override
    public void onInputVendorError() {
        mVendorError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.vendorError);
    }

    @Override
    public void onInputMakerError() {
        mMakerError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.makerError);
    }

    @Override
    public void onInputWarrantyError() {
        mWarrantyError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.warrantyError);
    }

    @Override
    public void onGetDeviceCodeSuccess(String deviceCode) {
        mDevice.setDeviceCode(deviceCode);
        onGenerateBarCode(mIsQrCode);
    }

    @Override
    public void onPrintClick() {
        if (mDevice == null || mDevice.getDeviceCode() == null || mDeviceCode == null) return;
        Bitmap dstBitmap = Bitmap.createBitmap(mDeviceCode.getWidth() * SCALE_BITMAP,
                mDeviceCode.getHeight() * SCALE_BITMAP, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mDeviceCode, mDeviceCode.getWidth() * (SCALE_BITMAP - 1),
                mDeviceCode.getHeight() * (SCALE_BITMAP - 1), null);
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap(mDevice.getDeviceCode(), dstBitmap);
    }

    @Override
    public void setProgressBar(int visibility) {
        setProgressBarVisibility(visibility);
    }

    @Override
    public void onGetDefaultBranchSuccess(Status status) {
        mBranch = status;
        mDevice.setBranch(mBranch.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) return;
        Bundle bundle = data.getExtras();
        Status status = bundle.getParcelable(BUNDLE_DATA);
        assert status != null;
        switch (requestCode) {
            case REQUEST_CATEGORY:
                if (status.getId() == OUT_OF_INDEX) {
                    return;
                }
                setCategory(status);
                mPresenter.getDeviceCode(mCategory.getId(), mBranch.getId());
                break;
            case REQUEST_BRANCH:
                if (status.getId() == OUT_OF_INDEX) {
                    return;
                }
                mDevice.setBranch(status.getName());
                setBranch(status);
                if (mCategory != null && mCategory.getId() > 0) {
                    mPresenter.getDeviceCode(mCategory.getId(), mBranch.getId());
                }
                break;
            case REQUEST_STATUS:
                if (status.getId() == OUT_OF_INDEX) {
                    return;
                }
                setStatus(status);
                break;
            case REQUEST_VENDOR:
                if (status.getId() == OUT_OF_INDEX) {
                    return;
                }
                mDevice.setVendor((Producer) status);
                break;
            case REQUEST_MAKER:
                if (status.getId() == OUT_OF_INDEX) {
                    return;
                }
                mDevice.setMarker((Producer) status);
                break;
            case REQUEST_MEETING_ROOM:
                if (status.getId() == OUT_OF_INDEX) {
                    return;
                }
                mDevice.setMeetingRoom((Producer) status);
                break;
            default:
                break;
        }
    }

    public void generateBarCode(BarcodeFormat format, int width, int height) {
        String deviceCode = mDevice.getDeviceCode();
        if (deviceCode == null) return;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(deviceCode, format, width, height);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            setDeviceCode(barcodeEncoder.createBitmap(bitMatrix));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void onGenerateBarCode(boolean isQrCode) {
        if (isQrCode) {
            generateBarCode(BarcodeFormat.QR_CODE, DEFAULT_WIDTH_QRCODE, DEFAULT_HEIGHT_QRCODE);
        } else {
            generateBarCode(BarcodeFormat.CODE_128, DEFAULT_WIDTH_BARCODE, DEFAULT_HEIGHT_BARCODE);
        }
    }

    @Bindable
    public String getDeviceCodeError() {
        return mDeviceCodeError;
    }

    @Bindable
    public String getNameDeviceError() {
        return mNameDeviceError;
    }

    @Bindable
    public String getSerialNumberError() {
        return mSerialNumberError;
    }

    @Bindable
    public String getModelNumberError() {
        return mModelNumberError;
    }

    @Bindable
    public String getBoughtDateError() {
        return mBoughtDateError;
    }

    @Bindable
    public String getCategoryError() {
        return mCategoryError;
    }

    @Bindable
    public String getMeetingRoomError() {
        return mMeetingRoomError;
    }

    @Bindable
    public String getOriginalPriceError() {
        return mOriginalPriceError;
    }

    @Bindable
    public String getVendorError() {
        return mVendorError;
    }

    @Bindable
    public String getMakerError() {
        return mMakerError;
    }

    @Bindable
    public String getWarrantyError() {
        return mWarrantyError;
    }

    @Bindable
    public Status getCategory() {
        return mCategory;
    }

    public void setCategory(Status category) {
        mDevice.setDeviceCategoryId(category.getId());
        mCategory = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public String getStatusError() {
        return mStatusError;
    }

    @Bindable
    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mDevice.setDeviceStatusId(status.getId());
        mDevice.setDeviceStatusName(status.getName());
        mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    @Override
    public void onUpdateSuccess(String message) {
        sUpdatedDevice.cloneDevice(mDevice);
        setProgressBarVisibility(GONE);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_DEVICE, mDevice);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateError(String message) {
        setProgressBarVisibility(GONE);
        mNavigator.showToast(message);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    @Bindable
    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice = device;
        notifyPropertyChanged(BR.device);
    }

    public DeviceStatusType getDeviceType() {
        return mDeviceType;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (mCalendar.getTime().after(Calendar.getInstance().getTime())) {
            mNavigator.showToast(R.string.error_bought_date_in_the_future);
            return;
        }
        setBoughtDate(Utils.getStringDate(mCalendar.getTime(), mContext));
        mDevice.setBoughtDate(mCalendar.getTime());
    }

    @Bindable
    public String getBoughtDate() {
        return mBoughtDate;
    }

    public void setBoughtDate(String boughtDate) {
        mBoughtDate = boughtDate;
        notifyPropertyChanged(BR.boughtDate);
    }

    @Bindable
    public boolean isQrCode() {
        return mIsQrCode;
    }

    public void setQrCode(boolean qrCode) {
        onGenerateBarCode(qrCode);
        mDevice.setBarcode(false);
        mIsQrCode = qrCode;
        notifyPropertyChanged(BR.qrCode);
    }

    @Bindable
    public boolean isBarCode() {
        return !isQrCode();
    }

    public void setBarCode(boolean barCode) {
        mIsBarCode = barCode;
        mDevice.setBarcode(true);
        notifyPropertyChanged(BR.barCode);
    }

    @Bindable
    public Status getBranch() {
        return mBranch;
    }

    public void setBranch(Status branch) {
        mBranch = branch;
        notifyPropertyChanged(BR.branch);
    }

    public void onCheckedChange(boolean isChecked) {
        setStatus(!isChecked ? new Status(AVAIABLE, mActivity.getString(R.string.title_available))
                : new Status(USING, mActivity.getString(R.string.title_using)));
    }

    @Bindable
    public Bitmap getDeviceCode() {
        return mDeviceCode;
    }

    public void setDeviceCode(Bitmap deviceCode) {
        mDeviceCode = deviceCode;
        notifyPropertyChanged(BR.deviceCode);
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setProgressBarVisibility(int progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    public String onSubmitText() {
        if (mDeviceType == EDIT) {
            return mContext.getString(R.string.action_update_device);
        }
        return mContext.getString(R.string.action_create_device);
    }

    @Bindable
    public boolean isAllowEditMeetingRoom() {
        return mIsAllowEditMeetingRoom;
    }

    public void setAllowEditMeetingRoom(boolean allowEditMeetingRoom) {
        mIsAllowEditMeetingRoom = allowEditMeetingRoom;
        notifyPropertyChanged(com.framgia.fdms.BR.allowEditMeetingRoom);
    }
}
