package com.framgia.fdms.screen.deviceselection;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.framgia.fdms.R;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.databinding.ActivityDeviceSelectionBinding;

import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_CATEGORY_ID;

/**
 * DeviceSelection Screen.
 */
public class DeviceSelectionActivity extends AppCompatActivity
    implements SearchView.OnQueryTextListener {
    private DeviceSelectionContract.ViewModel mViewModel;
    private int mCategoryId;

    public static Intent getInstance(Context context, int categoryId) {
        Intent intent = new Intent(context, DeviceSelectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_CATEGORY_ID, categoryId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        mViewModel = new DeviceSelectionViewModel(this);
        DeviceSelectionContract.Presenter presenter = new DeviceSelectionPresenter(mViewModel,
            new DeviceRepository(new DeviceRemoteDataSource(FDMSServiceClient.getInstance())),
            mCategoryId);
        mViewModel.setPresenter(presenter);
        ActivityDeviceSelectionBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_device_selection);
        binding.setViewModel((DeviceSelectionViewModel) mViewModel);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        mCategoryId = bundle.getInt(BUNDLE_CATEGORY_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
    public boolean onQueryTextSubmit(String query) {
        if (mViewModel != null) {
            mViewModel.onSearchData(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
