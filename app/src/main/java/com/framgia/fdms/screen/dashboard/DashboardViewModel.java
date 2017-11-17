package com.framgia.fdms.screen.dashboard;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailFragment;
import com.framgia.fdms.widget.FDMSShowcaseSequence;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.framgia.fdms.data.anotation.Permission.ACCOUNTANT;
import static com.framgia.fdms.data.anotation.Permission.ADMIN;
import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.BO_STAFF;
import static com.framgia.fdms.data.anotation.Permission.DIVISION_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.SECTION_MANAGER;
import static com.framgia.fdms.screen.dashboard.DashboardViewModel.Tab.TAB_DEVIVE_DASH_BOARD;
import static com.framgia.fdms.screen.dashboard.DashboardViewModel.Tab.TAB_REQUEST_DASH_BOARD;
import static com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailFragment
        .DEVICE_DASHBOARD;
import static com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailFragment
        .REQUEST_DASHBOARD;

/**
 * Exposes the data to be used in the Dashboard screen.
 */
public class DashboardViewModel extends BaseObservable implements DashboardContract.ViewModel {
    private DashboardContract.Presenter mPresenter;
    private ViewPagerAdapter mPagerAdapter;
    private int mTab = TAB_REQUEST_DASH_BOARD;
    private boolean mIsShowDeviceDashboard;
    private Fragment mFragment;
    private Context mContext;
    private FDMSShowcaseSequence mSequence;

    public DashboardViewModel(Fragment fragment) {
        mFragment = fragment;
        mContext = fragment.getContext();
        mSequence = new FDMSShowcaseSequence(fragment.getActivity());
        ShowcaseConfig config = new ShowcaseConfig();
        config.setMaskColor(R.color.color_black_transprarent);
        mSequence.setConfig(config);
    }

    public void onClickChangeTab(ViewPager viewpager, @Tab int tab) {
        viewpager.setCurrentItem(tab);
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
    public void setPresenter(DashboardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public ViewPagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    public void setPagerAdapter(ViewPagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        notifyPropertyChanged(BR.pagerAdapter);
    }

    @Bindable
    public int getTab() {
        return mTab;
    }

    public void setTab(int tab) {
        mTab = tab;
        notifyPropertyChanged(BR.tab);
    }

    @Bindable
    public boolean isShowDeviceDashboard() {
        return mIsShowDeviceDashboard;
    }

    public void setShowDeviceDashboard(boolean showDeviceDashboard) {
        mIsShowDeviceDashboard = showDeviceDashboard;
        notifyPropertyChanged(BR.showDeviceDashboard);
    }

    @Override
    public void setupViewPager(User user) {
        int role = user.getRole();
        setShowDeviceDashboard(isShowDeviceDashboard(user.getRole()));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(DashBoardDetailFragment.newInstance(REQUEST_DASHBOARD));
        if (isShowDeviceDashboard()) {
            fragments.add(DashBoardDetailFragment.newInstance(DEVICE_DASHBOARD));
        }
        mPagerAdapter = new ViewPagerAdapter(mFragment.getChildFragmentManager(), fragments);
        setPagerAdapter(mPagerAdapter);
    }

    public boolean isShowDeviceDashboard(int permission) {
        switch (permission) {
            case ADMIN:
            case BO_MANAGER:
            case BO_STAFF:
            case ACCOUNTANT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onError(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowCase() {
        mSequence.start();
        mSequence.setOnItemDismissedListener(
                new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
                    @Override
                    public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                        mSequence.setCount(mSequence.getCount() - 1);
                        if (mSequence.getCount() == 0) {
                            mPresenter.saveShowCase();
                        }
                    }
                });
    }

    @Bindable
    public FDMSShowcaseSequence getSequence() {
        return mSequence;
    }

    public void setSequence(FDMSShowcaseSequence sequence) {
        mSequence = sequence;
        notifyPropertyChanged(BR.sequence);
    }

    @IntDef({
            TAB_DEVIVE_DASH_BOARD, TAB_REQUEST_DASH_BOARD
    })
    public @interface Tab {
        int TAB_REQUEST_DASH_BOARD = 0;
        int TAB_DEVIVE_DASH_BOARD = 1;
    }
}
