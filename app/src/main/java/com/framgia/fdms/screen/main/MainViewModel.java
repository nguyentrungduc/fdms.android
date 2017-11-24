package com.framgia.fdms.screen.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.ViewPagerScroll;
import com.framgia.fdms.screen.authenication.login.LoginActivity;
import com.framgia.fdms.screen.dashboard.DashboardFragment;
import com.framgia.fdms.screen.device.listdevice.ListDeviceFragment;
import com.framgia.fdms.screen.device.mydevice.MyDeviceFragment;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.screen.deviceusingmanager.DeviceUsingManagerFragment;
import com.framgia.fdms.screen.notification.NotificationActivity;
import com.framgia.fdms.screen.producer.ProducerFragment;
import com.framgia.fdms.screen.profile.ProfileFragment;
import com.framgia.fdms.screen.request.requestmanager.RequestManagerFragment;
import com.framgia.fdms.screen.request.userrequest.UserRequestFragment;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.utils.permission.PermissionUtil;
import com.framgia.fdms.widget.FDMSShowcaseSequence;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_DASH_BOARD;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_DEVICE_CATEGORY;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_DEVICE_GROUP;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_DEVICE_MANAGER;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_MAKER_MANAGE;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_MANAGE_MEETING_ROOM;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_MY_DEVICES;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_MY_REQUESTS;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_PROFILE;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_REQUEST_MANAGER;
import static com.framgia.fdms.screen.main.MainViewModel.Tab.TAB_VENDOR_MANAGE;
import static com.framgia.fdms.screen.producer.ProducerType.CATEGORIES_GROUPS;
import static com.framgia.fdms.screen.producer.ProducerType.DEVICE_GROUPS;
import static com.framgia.fdms.screen.producer.ProducerType.MARKER;
import static com.framgia.fdms.screen.producer.ProducerType.MEETING_ROOM;
import static com.framgia.fdms.screen.producer.ProducerType.VENDOR;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_CONTENT;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_CLOSE;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_OPEN;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_SCANNER;
import static com.framgia.fdms.utils.permission.PermissionUtil.MY_PERMISSIONS_REQUEST_CAMERA;

/**
 * Exposes the data to be used in the Newmain screen.
 */
public class MainViewModel extends BaseObservable
    implements MainContract.ViewModel, ViewPagerScroll {
    private static final int PAGE_LIMIT = 3;
    private MainContract.Presenter mPresenter;
    private ViewPagerAdapter mPagerAdapter;
    private int mTab = TAB_DASH_BOARD;
    private AppCompatActivity mActivity;
    private FDMSShowcaseSequence mSequence;
    private boolean mIsShowCase;
    private boolean mIsShowCaseRequest;
    private int mCurrentItem;
    private String mStatusDrawerLayout;
    private Navigator mNavigator;
    private User mUser;
    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onDrawerIsOpen(view);
        }
    };

    public MainViewModel(AppCompatActivity activity) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ProfileFragment.newInstance());
        fragments.add(DashboardFragment.newInstance());
        fragments.add(ListDeviceFragment.newInstance());
        fragments.add(ProducerFragment.newInstance(DEVICE_GROUPS));
        fragments.add(ProducerFragment.newInstance(CATEGORIES_GROUPS));
        fragments.add(ProducerFragment.newInstance(VENDOR));
        fragments.add(ProducerFragment.newInstance(MARKER));
        fragments.add(ProducerFragment.newInstance(MEETING_ROOM));
        fragments.add(DeviceUsingManagerFragment.newInstance());
        fragments.add(MyDeviceFragment.newInstance());
        fragments.add(RequestManagerFragment.newInstance());
        fragments.add(UserRequestFragment.newInstance());
        mPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), fragments);
        mActivity = activity;
        mNavigator = new Navigator(activity);
        mSequence = new FDMSShowcaseSequence(activity);
        ShowcaseConfig config = new ShowcaseConfig();
        config.setMaskColor(R.color.color_black_transprarent);
        mSequence.setConfig(config);
        setCurrentItem(R.id.item_dashboard);
        setTab(TAB_DASH_BOARD);
        mActivity.setTitle(R.string.title_dashboard);
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
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public ViewPagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    @Bindable
    public int getTab() {
        return mTab;
    }

    public void setTab(int tab) {
        mTab = tab;
        notifyPropertyChanged(BR.tab);
    }

    public int getPageLimit() {
        return PAGE_LIMIT;
    }

    public boolean onItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_set_up_profile:
                setTab(TAB_PROFILE);
                mActivity.setTitle(R.string.title_set_up_profile);
                break;
            case R.id.item_dashboard:
                setTab(TAB_DASH_BOARD);
                mActivity.setTitle(R.string.title_dashboard);
                break;
            case R.id.item_manage_device:
                setTab(TAB_DEVICE_MANAGER);
                mActivity.setTitle(R.string.title_manage_device);
                break;
            case R.id.item_manage_request:
                setTab(TAB_REQUEST_MANAGER);
                mActivity.setTitle(R.string.title_manage_request);
                break;
            case R.id.item_manage_vendor:
                setTab(TAB_VENDOR_MANAGE);
                mActivity.setTitle(R.string.title_manage_vendor);
                break;
            case R.id.item_manage_maker:
                setTab(TAB_MAKER_MANAGE);
                mActivity.setTitle(R.string.title_manage_maker);
                break;
            case R.id.item_my_device:
                setTab(TAB_MY_DEVICES);
                mActivity.setTitle(R.string.title_my_device);
                break;
            case R.id.item_my_request:
                setTab(TAB_MY_REQUESTS);
                mActivity.setTitle(R.string.title_my_request);
                break;
            case R.id.item_manage_meeting_room:
                setTab(TAB_MANAGE_MEETING_ROOM);
                mActivity.setTitle(R.string.title_manage_meeting_room);
                break;
            case R.id.item_device_using_history:
                setTab(TAB_DEVICE_USING_HISTORY);
                mActivity.setTitle(R.string.title_device_using_history);
                break;
            case R.id.item_logout:
                // TODO: 07/09/2017 call api logout
                mPresenter.logout();
                mActivity.startActivity(LoginActivity.getInstance(mNavigator.getContext()));
                mActivity.finish();
                break;
            case R.id.item_manage_device_category:
                setTab(TAB_DEVICE_CATEGORY);
                mActivity.setTitle(R.string.title_manage_device_category);
                break;
            case R.id.item_manage_device_group:
                setTab(TAB_DEVICE_GROUP);
                mActivity.setTitle(R.string.title_manage_device_group);
                break;
            default:
                break;
        }
        setCurrentItem(item.getItemId());
        setStatusDrawerLayout(DRAWER_IS_CLOSE);
        return true;
    }

    public void onDrawerIsOpen(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) mNavigator.getContext()
            .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        setStatusDrawerLayout(DRAWER_IS_OPEN);
    }

    public void onProfileClick() {
        // TODO: 07/09/2017 show profile screen
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_SCANNER
            || resultCode != RESULT_OK
            || data == null
            || data.getExtras() == null) {
            return;
        }
        getResult(data.getExtras().getString(BUNDLE_CONTENT));
    }

    public void onStartScannerQrCode() {
        if (PermissionUtil.checkCameraPermission(mActivity)) {
            startScannerActivity();
        }
    }

    @Override
    public void onGetDecodeSuccess(Device device) {
        mActivity.startActivity(DeviceDetailActivity.getInstance(mActivity, device));
    }

    private void startScannerActivity() {
        MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(mActivity)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText(mActivity.getString(R.string.title_scaning))
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        mPresenter.getDevice(barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
        int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA
            && grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScannerActivity();
        } else {
            Snackbar.make(mActivity.findViewById(android.R.id.content),
                R.string.msg_denied_read_camera, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetDeviceError(String error) {
        Snackbar.make(mActivity.findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG)
            .show();
    }

    @Override
    public void getResult(String resultQrCode) {
        if (mPresenter == null) return;
        mPresenter.getDevice(resultQrCode);
    }

    @Override
    public void onCurrentPosition(int position) {
        setTab(position);
    }

    @Bindable
    public FDMSShowcaseSequence getSequence() {
        return mSequence;
    }

    public void setSequence(FDMSShowcaseSequence sequence) {
        mSequence = sequence;
        notifyPropertyChanged(BR.sequence);
    }

    @Override
    public void onGetUserSuccess(User user) {
        setUser(user);
    }

    @Override
    public void onError(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void setTabDeviceManage(Device device) {
        setTab(TAB_DEVICE_MANAGER);
        ((ListDeviceFragment) mPagerAdapter.getItem(TAB_DEVICE_MANAGER)).getDataWithDevice(device);
        setCurrentItem(R.id.item_manage_device);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    public void onShowCaseDashBoard() {
        ((DashboardFragment) mPagerAdapter.getItem(TAB_DASH_BOARD)).onShowCase();
    }

    public boolean isShowCase() {
        return mIsShowCase;
    }

    @Override
    public void setShowCase(boolean showCase) {
        mIsShowCase = showCase;
    }

    public boolean isShowCaseRequest() {
        return mIsShowCaseRequest;
    }

    @Override
    public void setShowCaseRequest(boolean showCaseRequest) {
        mIsShowCaseRequest = showCaseRequest;
    }

    @Bindable
    public int getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(int currentItem) {
        mCurrentItem = currentItem;
        notifyPropertyChanged(BR.currentItem);
    }

    @Bindable
    public String getStatusDrawerLayout() {
        return mStatusDrawerLayout;
    }

    public void setStatusDrawerLayout(String statusDrawerLayout) {
        mStatusDrawerLayout = statusDrawerLayout;
        notifyPropertyChanged(BR.statusDrawerLayout);
    }

    @Bindable
    public View.OnClickListener getListener() {
        return mListener;
    }

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
        notifyPropertyChanged(BR.listener);
    }

    public void onNotificationClick() {
        mNavigator.startActivity(NotificationActivity.getInstances(mNavigator.getContext()));
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
        notifyPropertyChanged(BR.user);
    }

    /**
     * Tabs for Navigation Drawer
     */
    @IntDef({
        TAB_DASH_BOARD, TAB_REQUEST_MANAGER, TAB_DEVICE_MANAGER, TAB_PROFILE, TAB_VENDOR_MANAGE,
        TAB_MAKER_MANAGE, TAB_MY_DEVICES, TAB_MY_REQUESTS, TAB_MANAGE_MEETING_ROOM,
        TAB_DEVICE_USING_HISTORY, TAB_DEVICE_GROUP, TAB_DEVICE_CATEGORY
    })
    public @interface Tab {
        int TAB_PROFILE = 0;
        int TAB_DASH_BOARD = 1;
        int TAB_DEVICE_MANAGER = 2;
        int TAB_DEVICE_GROUP = 3;
        int TAB_DEVICE_CATEGORY = 4;
        int TAB_VENDOR_MANAGE = 5;
        int TAB_MAKER_MANAGE = 6;
        int TAB_MANAGE_MEETING_ROOM = 7;
        int TAB_DEVICE_USING_HISTORY = 8;
        int TAB_MY_DEVICES = 9;
        int TAB_REQUEST_MANAGER = 10;
        int TAB_MY_REQUESTS = 11;
    }
}
