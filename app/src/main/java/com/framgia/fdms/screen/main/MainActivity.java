package com.framgia.fdms.screen.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.NotificationRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.local.UserLocalDataSource;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceImp;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.databinding.ActivityNewmainBinding;
import com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailViewModel;

import static com.framgia.fdms.data.source.local.sharepref.SharePreferenceKey.LANGUAGE_PRES;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.ENGLISH;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.ENGLISH_POSITION;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.JAPANESE;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.JAPANESE_POSITION;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.VIETNAMESE;
import static com.framgia.fdms.utils.Constant.LocaleLanguage.VIETNAMESE_POSITION;
import static com.framgia.fdms.utils.Utils.changeLanguage;

/**
 * Newmain Screen.
 */
public class MainActivity extends AppCompatActivity
        implements DashBoardDetailViewModel.OnDashBoardDetailClickListener {
    private MainContract.ViewModel mViewModel;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharePreferenceImp preferences = new SharePreferenceImp(this);
        initLanguage(preferences.get(LANGUAGE_PRES, Integer.class));
        ActivityNewmainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_newmain);
        DeviceRepository deviceRepository =
                new DeviceRepository(new DeviceRemoteDataSource(FDMSServiceClient.getInstance()));
        UserRepository userRepository =  new UserRepository(
                new UserLocalDataSource(new SharePreferenceImp(this)));
        NotificationRepository notificationRepository = NotificationRepository
                .getInstances(FDMSServiceClient.getInstance());

        mViewModel = new MainViewModel(this);
        MainContract.Presenter presenter =
                new MainPresenter(mViewModel,
                        deviceRepository,
                        new SharePreferenceImp(this),
                        userRepository,
                        notificationRepository);

        mViewModel.setPresenter(presenter);
        binding.setViewModel((MainViewModel) mViewModel);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mViewModel != null) mViewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mViewModel != null) {
            mViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onItemClick(Device device) {
        if (mViewModel == null) {
            return;
        }
        mViewModel.setTabDeviceManage(device);
    }

    private void initLanguage(int language) {
        switch (language) {
            case ENGLISH_POSITION:
                changeLanguage(ENGLISH, this);
                break;
            case VIETNAMESE_POSITION:
                changeLanguage(VIETNAMESE, this);
                break;
            case JAPANESE_POSITION:
                changeLanguage(JAPANESE, this);
                break;
            default:
                break;
        }
    }
}
