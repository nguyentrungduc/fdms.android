package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.screen.device.listdevice.DeviceFilterModel;

import io.reactivex.Observable;

import java.util.List;

public class DeviceRepository implements DeviceDataSource.RemoteDataSource {
    private static DeviceRepository sRepository;
    private DeviceRemoteDataSource mDeviceRemoteDataSource;

    public static DeviceRepository getInstance(DeviceRemoteDataSource remoteDataSource) {
        if (sRepository == null) {
            sRepository = new DeviceRepository(remoteDataSource);
        }
        return sRepository;
    }

    public DeviceRepository(DeviceRemoteDataSource remoteDataSource) {
        mDeviceRemoteDataSource = remoteDataSource;
    }

    public Observable<List<Device>> getListDevices(DeviceFilterModel filterModel, int page,
                                                   int perPage) {
        return mDeviceRemoteDataSource.getListDevices(filterModel, page, perPage);
    }

    public Observable<List<Device>> getListDevices(String deviceName, int categoryId, int statusId,
                                                   int page, int perPage) {
        return mDeviceRemoteDataSource.getListDevices(deviceName, categoryId, statusId, page,
                perPage);
    }

    public Observable<Device> registerdevice(Device device) {
        return mDeviceRemoteDataSource.registerdevice(device);
    }

    public Observable<String> updateDevice(Device device) {
        return mDeviceRemoteDataSource.updateDevice(device);
    }

    @Override
    public Observable<Respone<String>> deleteDevice(Device device) {
        return mDeviceRemoteDataSource.deleteDevice(device);
    }

    public Observable<Device> getDeviceByQrCode(String qrCode) {
        return mDeviceRemoteDataSource.getDeviceByQrCode(qrCode);
    }

    public Observable<List<Dashboard>> getDashboardDevice() {
        return mDeviceRemoteDataSource.getDashboardDevice();
    }

    public Observable<List<DeviceUsingHistory>> getDeviceUsingHistory(String deviceCode, int page,
                                                                      int perPage) {
        return mDeviceRemoteDataSource.getDeviceUsingHistory(deviceCode, page, perPage);
    }

    public Observable<List<DeviceHistoryDetail>> getDeviceDetailHistory(int deviceId, int page,
                                                                        int perPage) {
        return mDeviceRemoteDataSource.getDeviceDetailHistory(deviceId, page, perPage);
    }

    public Observable<Device> getDevice(int deviceId) {
        return mDeviceRemoteDataSource.getDevice(deviceId);
    }

    public Observable<List<Device>> getTopDevice(int topDevice) {
        return mDeviceRemoteDataSource.getTopDevice(topDevice);
    }

    public Observable<Device> getDeviceCode(int deviceCategoryId, int branchId) {
        return mDeviceRemoteDataSource.getDeviceCode(deviceCategoryId, branchId);
    }

    public Observable<List<Device>> getListDeviceByMeetingRoomId(int meetingRoomId, int page,
                                                                 int perPage) {
        return mDeviceRemoteDataSource.getListDeviceByMeetingRoomId(meetingRoomId, page, perPage);
    }

    @Override
    public Observable<List<DeviceUsingHistory>> getUserDevice(String status, String staffEmail,
                                                              int page, int perPage) {
        return mDeviceRemoteDataSource.getUserDevice(status, staffEmail, page, perPage);
    }

    @Override
    public Observable<List<Status>> getDeviceStatus() {
        return mDeviceRemoteDataSource.getDeviceStatus();
    }

    @Override
    public Observable<List<Status>> getDeviceStatus(String statusName) {
        return mDeviceRemoteDataSource.getDeviceStatus(statusName);
    }

    @Override
    public Observable<List<Status>> getChangeDeviceStatus(int inputStatus, String statusName) {
        return mDeviceRemoteDataSource.getChangeDeviceStatus(inputStatus, statusName);
    }

    @Override
    public Observable<List<Status>> getChangeDeviceStatus(int inputStatus) {
        return mDeviceRemoteDataSource.getChangeDeviceStatus(inputStatus);
    }
}
