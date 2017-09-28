package com.framgia.fdms.screen.returndevice;

import android.content.Intent;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ReturnDeviceContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onCheckedChanged(boolean checked, Device device, int position);

        void onSelectedUserReturn();

        void onReturnDeviceSuccess(String s);

        void showProgressbar();

        void hideProgressbar();

        void onLoadError(String message);

        void onError(String message);

        void onDeviceLoaded(List<Device> devices);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onStartScannerDevice();

        void onGetDeviceSuccess(Device device);

        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

        void onReturnDeviceEmpty();

        void onReturnDeviceClick();

        void onItemDeviceClick(Device device);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getDevicesOfBorrower(int userId);

        void getDeviceByCode(String codeDevice);

        void returnDevice(List<Integer> listDeviceId);
    }
}
