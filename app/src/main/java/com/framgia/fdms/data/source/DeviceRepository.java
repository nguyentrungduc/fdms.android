package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.screen.device.listdevice.DeviceFilterModel;
import io.reactivex.Observable;
import java.util.List;

public class DeviceRepository implements DeviceDataSource.RemoteDataSource {
    private DeviceRemoteDataSource mDeviceRemoteDataSource;

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

    public Observable<List<Status>> getListCategory() {
        return mDeviceRemoteDataSource.getListCategory();
    }

    public Observable<List<Status>> getListStatus() {
        return mDeviceRemoteDataSource.getListStatus();
    }

    public Observable<Device> registerdevice(Device device) {
        return mDeviceRemoteDataSource.registerdevice(device);
    }

    public Observable<Device> updateDevice(Device device) {
        return mDeviceRemoteDataSource.updateDevice(device);
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

    public Observable<List<DeviceHistoryDetail>> getDeviceDetailHistory(int deviceId) {
        return mDeviceRemoteDataSource.getDeviceDetailHistory(deviceId);
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
    public Observable<List<Status>> getDeviceGroups() {
        return mDeviceRemoteDataSource.getDeviceGroups();
    }

    @Override
    public Observable<List<Status>> getDeviceGroups(String query) {
        return mDeviceRemoteDataSource.getDeviceGroups(query);
    }
}
