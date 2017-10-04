package com.framgia.fdms.screen.requestdetail;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.framgia.fdms.R;
import java.util.List;

import static com.framgia.fdms.screen.requestdetail.RequestDetailPagerAdapter.RequestDetailPage
    .DEVICE_ASSIGNMENT;
import static com.framgia.fdms.screen.requestdetail.RequestDetailPagerAdapter.RequestDetailPage
    .REQUEST_INFORMATION;

/**
 * Created by MyPC on 23/05/2017.
 */

public class RequestDetailPagerAdapter extends FragmentPagerAdapter {
    public static final int PAGE_COUNT = 2;
    private Context mContext;
    private List<Fragment> mFragments;

    public RequestDetailPagerAdapter(Context context, FragmentManager fm,
        List<Fragment> fragments) {
        super(fm);
        mContext = context;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case REQUEST_INFORMATION:
                return mContext.getString(R.string.title_device_infomation);
            case DEVICE_ASSIGNMENT:
                return mContext.getString(R.string.title_device_assignment);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @IntDef({ REQUEST_INFORMATION, DEVICE_ASSIGNMENT })
    @interface RequestDetailPage {
        int REQUEST_INFORMATION = 0;
        int DEVICE_ASSIGNMENT = 1;
    }
}
