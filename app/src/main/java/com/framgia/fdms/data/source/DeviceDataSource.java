package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.NewDeviceUsingHistory;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.device.listdevice.DeviceFilterModel;

import io.reactivex.Observable;

import java.util.List;

/**
 * Created by Age on 4/3/2017.
 */
public class DeviceDataSource {
    public interface LocalDataSource {
    }

    public interface RemoteDataSource {
        Observable<List<Device>> getListDevices(DeviceFilterModel filterModel, int page,
                                                int perPage);

        Observable<List<Device>> getListDevices(String deviceName, int categoryId, int statusId,
                                                int page, int perPage);

        Observable<Device> createDevice(Device registerdevice);

        Observable<String> updateDevice(Device device);

        Observable<Respone<String>> deleteDevice(Device device);

        Observable<Device> getDeviceByQrCode(String qrCode);

        Observable<List<Dashboard>> getDashboardDevice();

        Observable<List<DeviceUsingHistory>> getDeviceUsingHistory(String deviceCode, int page,
                                                                   int perPage);

        Observable<List<NewDeviceUsingHistory>> getDeviceUsingHistories(int deviceId);

        Observable<List<DeviceHistoryDetail>> getDeviceDetailHistory(int deviceId, int page,
                                                                     int perPage);

        Observable<Device> getDevice(int deviceId);

        Observable<List<Device>> getTopDevice(int topDevice);

        Observable<Device> getDeviceCode(int deviceCategoryId, int branchId);

        Observable<List<Device>> getListDeviceByMeetingRoomId(int meetingRoomId, int page,
                                                              int perPage);

        Observable<List<DeviceUsingHistory>> getUserDevice(String status, String staffEmail,
                                                           int page, int perPage);

        Observable<List<Status>> getDeviceStatus();

        Observable<List<Status>> getDeviceStatus(String statusName);

        Observable<List<Status>> getChangeDeviceStatus(int inputStatus, String statusName);

        Observable<List<Status>> getChangeDeviceStatus(int inputStatus);
    }
}
