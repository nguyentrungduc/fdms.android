package com.framgia.fdms.data.source.api.service;

import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface FDMSApi {
    @POST("api/sessions")
    Observable<Respone<User>> login(@Query("user[email]") String userName,
        @Query("user[password]") String passWord);

    @GET("api/devices")
    Observable<Respone<List<Device>>> getListDevices(@QueryMap Map<String, String> parrams);

    @GET("api/device_categories")
    Observable<Respone<List<Status>>> getListCategory();

    @GET("api/device_statuses")
    Observable<Respone<List<Status>>> getListStatus();

    @GET("api/device_codes")
    Observable<Respone<Device>> getDeviceByQrCode(@Query("device_code") String qrCode);

    @Multipart
    @POST("api/devices")
    Observable<Respone<Device>> uploadDevice(@PartMap Map<String, RequestBody> parrams,
        @Part MultipartBody.Part picture);

    @Multipart
    @PATCH("api/devices/{id}")
    Observable<Respone<Device>> updateDevice(@Path("id") int id,
        @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part picture);

    @GET("/api/request_dashboards")
    Observable<Respone<List<Dashboard>>> getDashboardRequest();

    @GET("api/device_dashboards")
    Observable<Respone<List<Dashboard>>> getDashboardDevice();

    @GET("api/requests")
    Observable<Respone<List<Request>>> getRequests(@QueryMap Map<String, Integer> params);

    @GET("api/request_statuses")
    Observable<Respone<List<Status>>> getListStatusRequest();

    @GET("api/user_groups")
    Observable<Respone<List<Status>>> getListRelative();

    @GET("api/user_assigns")
    Observable<Respone<List<Status>>> getListAssign();

    @POST("api/requests")
    Observable<Respone<Request>> registerRequest(@QueryMap Map<String, String> params);

    @GET("api/device_codes")
    Observable<Respone<Device>> getDevice(@Query("device_id") int deviceId);

    @GET("/api/request_dashboards")
    Observable<Respone<List<Request>>> getTopRequest(@Query("top_requests") int topRequest);

    @GET("api/device_dashboards")
    Observable<Respone<List<Device>>> getTopDevice(@Query("top_devices") int topDevice);

    @PATCH("/api/requests/{id}")
    @FormUrlEncoded
    Observable<Respone<Request>> updateActionRequest(@Path("id") int requestId,
        @Field("request[request_status_id]") int actionId);

    @GET("api/device_historys/{id}")
    Observable<Respone<List<DeviceHistoryDetail>>> getDeviceDetailHistory(@Path("id") int deviceId);

    @GET("api/device_usings/{id}")
    Observable<Respone<List<DeviceUsingHistory>>> getDeviceUsingHistory(@Path("id") int deviceId);

    @GET("api/branches")
    Observable<Respone<List<Status>>> getListBranch();

    @GET("api/device_codes/show")
    Observable<Respone<Device>> getDeviceCode(@Query("device_category_id") int deviceCategoryId,
        @Query("branch") int branchId);

    @GET("api/user_borrows")
    Observable<Respone<List<Device>>> getDevicesBorrow();

    @PATCH("/api/requests/{id}")
    Observable<Respone<Request>> updateRequest(@Path("id") int requestId,
        @QueryMap Map<String, String> params);

    @POST("api/assignments")
    Observable<Respone<Request>> registerAssignment(@QueryMap Map<String, String> params);

    @GET("api/requests/{id}")
    Observable<Respone<Request>> getRequest(@Path("id") int requetsId);

    @GET("api/makers")
    Observable<Respone<List<Producer>>> getMarkers(@Query("name") String name,
        @Query("page") int page, @Query("per_page") int perPage);

    @POST("api/makers")
    Observable<Respone<Producer>> addMarker(@Query("maker[name]") String name,
        @Query("maker[description]") String description);

    @DELETE("api/makers/{marker_id}")
    Observable<Respone<String>> deleteMarker(@Path("marker_id") int markerId);

    @PATCH("api/makers/{marker_id}")
    Observable<Respone<List<String>>> updateMarker(@Path("marker_id") int vendorId,
        @Query("maker[name]") String name, @Query("maker[description]") String description);

    @GET("api/devices?")
    Observable<Respone<List<Device>>> getListDeviceByMeetingRoomId(
        @Query("meeting_room_id") int meetingRoomId, @Query("page") int page,
        @Query("per_page") int perPage);

    @GET("api/meeting_rooms")
    Observable<Respone<List<Producer>>> getListMeetingRoom(@QueryMap Map<String, String> params);

    @POST("api/meeting_rooms")
    Observable<Respone<Producer>> addMeetingRoom(
        @Query("meeting_room[name]") String meetingRoomName,
        @Query("meeting_room[description]") String description);

    @DELETE("api/meeting_rooms/{meeting_room_id}")
    Observable<Respone<String>> deleteMeetingRoom(@Path("meeting_room_id") int meetingRoomId);

    @PATCH("api/meeting_rooms/{meeting_room_id}")
    Observable<Respone<List<String>>> updateMeetingRoom(@Path("meeting_room_id") int meetingRoomId,
        @Query("meeting_room[name]") String name,
        @Query("meeting_room[description]") String description);

    @GET("api/vendors")
    Observable<Respone<List<Producer>>> getListVendors(@Query("name") String name,
        @Query("page") int page, @Query("per_page") int perPage);

    @POST("api/vendors")
    Observable<Respone<Producer>> addVendor(@Query("vendor[name]") String name,
        @Query("vendor[description]") String description);

    @DELETE("api/vendors/{vendor_id}")
    Observable<Respone<String>> deleteVendor(@Path("vendor_id") int vendorId);

    @PATCH("api/vendors/{vendor_id}")
    Observable<Respone<List<String>>> updateVendor(@Path("vendor_id") int vendorId,
        @Query("vendor[name]") String name, @Query("vendor[description]") String description);

    @GET("api/device_groups")
    Observable<Respone<List<Status>>> getDeviceGroups();

    @GET("api/device_categories")
    Observable<Respone<List<Status>>> getCategoriesByDeviceGroupId(
        @QueryMap Map<String, String> params);
}
