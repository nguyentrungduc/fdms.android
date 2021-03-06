package com.framgia.fdms.screen.selection;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.framgia.fdms.R;
import com.framgia.fdms.data.source.BranchRepository;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.DeviceGroupRepository;
import com.framgia.fdms.data.source.DeviceUsingHistoryRepository;
import com.framgia.fdms.data.source.MarkerRepository;
import com.framgia.fdms.data.source.MeetingRoomRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.BranchRemoteDataSource;
import com.framgia.fdms.data.source.remote.CategoryRemoteDataSource;
import com.framgia.fdms.data.source.remote.DeviceUsingHistoryRemoteDataSource;
import com.framgia.fdms.data.source.remote.MarkerRemoteDataSource;
import com.framgia.fdms.data.source.remote.MeetingRoomRemoteDataSource;
import com.framgia.fdms.data.source.remote.StatusRemoteDataSource;
import com.framgia.fdms.data.source.remote.VendorRemoteDataSource;
import com.framgia.fdms.databinding.ActivityNewStatusSelectionBinding;

import static com.framgia.fdms.screen.selection.SelectionType.ASSIGNEE;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_DIALOG;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.selection.SelectionType.EDIT_STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.selection.SelectionType.RELATIVE_STAFF;
import static com.framgia.fdms.screen.selection.SelectionType.REQUEST_CREATED_BY;
import static com.framgia.fdms.screen.selection.SelectionType.REQUEST_FOR;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.USER_BORROW;
import static com.framgia.fdms.screen.selection.SelectionType.VENDOR;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * StatusSelection Screen.
 */
public class SelectionActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String BUNDLE_SELECTION_TYPE = "BUNDLE_SELECTION_TYPE";
    private static final String BUNDLE_ID = "BUNDLE_ID";
    private SelectionContract.ViewModel mViewModel;
    @SelectionType
    private int mSelectionType;
    private int mDeviceGroupId;
    private SearchView mSearchView;
    private MenuItem mSearchMenu;
    private int mRequestStatusId;

    public static Intent getInstance(Context context, @SelectionType int type) {
        Intent intent = new Intent(context, SelectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SELECTION_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getInstance(Context context, @SelectionType int type, int id) {
        Intent intent = new Intent(context, SelectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SELECTION_TYPE, type);
        bundle.putInt(BUNDLE_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstance(Context context, @SelectionType int type,
        int requestStatusId) {
        Intent intent = new Intent(context, SelectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SELECTION_TYPE, type);
        bundle.putInt(BUNDLE_DATA, requestStatusId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();

        mViewModel = new SelectionViewModel(this);

        SelectionContract.Presenter presenter = new SelectionPresenter(mViewModel, mSelectionType);
        initPresenter((SelectionPresenter) presenter);
        mViewModel.setSelectedType(mSelectionType);
        mViewModel.setPresenter(presenter);

        ActivityNewStatusSelectionBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_status_selection);
        binding.setViewModel((SelectionViewModel) mViewModel);
    }

    private void initPresenter(SelectionPresenter presenter) {
        switch (mSelectionType) {
            case STATUS:
            case STATUS_REQUEST_ALL:
            case STATUS_REQUEST:
            case RELATIVE_STAFF:
            case ASSIGNEE:
            case REQUEST_CREATED_BY:
            case REQUEST_FOR:
            case EDIT_STATUS_REQUEST:
                presenter.setRequestStatus(mRequestStatusId);
            case USER_BORROW:
                StatusRepository statusRepository = new StatusRepository(
                    new StatusRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setStatusRepository(statusRepository);
                break;

            case CATEGORY:
                CategoryRepository categoryRepository = new CategoryRepository(
                    new CategoryRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setCategoryRepository(categoryRepository);
                presenter.setDeviceGroupId(mDeviceGroupId);
                break;

            case VENDOR:
                VendorRepository vendorRepository = new VendorRepository(
                    new VendorRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setVendorRepository(vendorRepository);
                break;

            case MARKER:
                MarkerRepository markerRepository = new MarkerRepository(
                    new MarkerRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setMarkerRepository(markerRepository);
                break;

            case BRANCH_ALL:
            case BRANCH:
                BranchRepository branchRepository = new BranchRepository(
                    new BranchRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setBranchRepository(branchRepository);
                break;

            case MEETING_ROOM:
                MeetingRoomRepository meetingRoomRepository = new MeetingRoomRepository(
                    new MeetingRoomRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setMeetingRoomRepository(meetingRoomRepository);
                break;

            case DEVICE_GROUP_ALL:
            case DEVICE_GROUP_DIALOG:
            case DEVICE_GROUP:
                DeviceGroupRepository deviceGroupRepository = DeviceGroupRepository.getInstance();
                presenter.setDeviceGroupRepository(deviceGroupRepository);
                break;

            case DEVICE_USING_HISTORY:
                DeviceUsingHistoryRepository deviceUsingHistoryRepository =
                    new DeviceUsingHistoryRepository(
                        new DeviceUsingHistoryRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setDeviceUsingHistoryRepository(deviceUsingHistoryRepository);
                break;
            default:
                break;
        }
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        mSelectionType = bundle.getInt(BUNDLE_SELECTION_TYPE);
        if (bundle.getInt(BUNDLE_DATA) > 0) {
            mRequestStatusId = bundle.getInt(BUNDLE_DATA);
        }
        if (bundle.getInt(BUNDLE_ID) == 0) {
            mDeviceGroupId = OUT_OF_INDEX;
            return;
        }
        mDeviceGroupId = bundle.getInt(BUNDLE_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchMenu = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenu.getActionView();
        mSearchView.setOnQueryTextListener(this);

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ImageView imageDelete = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.search_src_text);
                editText.setText("");
                mSearchView.setQuery("", false);
                mSearchView.onActionViewCollapsed();
                mSearchMenu.collapseActionView();
                mViewModel.onSearch("", false);
                mViewModel.setSearch(false);
            }
        });
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
        mViewModel.onSearch(query, true);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // no ops
        return true;
    }

    /**
     * define search type
     */
}
