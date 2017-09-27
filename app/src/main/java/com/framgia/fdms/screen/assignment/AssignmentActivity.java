package com.framgia.fdms.screen.assignment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.framgia.fdms.R;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.local.UserLocalDataSource;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceImp;
import com.framgia.fdms.data.source.remote.RequestRemoteDataSource;
import com.framgia.fdms.databinding.ActivityAssignmentBinding;

import static com.framgia.fdms.screen.assignment.AssignmentType.ASSIGN_BY_REQUEST;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_TYPE;
import static com.framgia.fdms.utils.Utils.hideSoftKeyboard;

/**
 * Assignment Screen.
 */
public class AssignmentActivity extends AppCompatActivity {

    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private AssignmentContract.ViewModel mViewModel;

    public static Intent getInstance(Context context, @AssignmentType int assignmentType,
        int requestId) {
        return new Intent(context, AssignmentActivity.class).putExtra(BUNDLE_TYPE, assignmentType)
            .putExtra(EXTRA_REQUEST_ID, requestId);
    }

    public static Intent getInstance(Context context, @AssignmentType int assignmentType) {
        return new Intent(context, AssignmentActivity.class).putExtra(BUNDLE_TYPE, assignmentType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @AssignmentType int assignmentType =
            getIntent().getExtras().getInt(BUNDLE_TYPE, ASSIGN_BY_REQUEST);
        int deviceId = getIntent().getIntExtra(EXTRA_REQUEST_ID, 0);

        mViewModel = new AssignmentViewModel(this);

        AssignmentContract.Presenter presenter =
            new AssignmentPresenter(mViewModel, assignmentType, deviceId,
                new RequestRepository(new RequestRemoteDataSource(FDMSServiceClient.getInstance())),
                new UserRepository(new UserLocalDataSource(new SharePreferenceImp(this))));
        mViewModel.setPresenter(presenter);
        ((AssignmentViewModel) mViewModel).setAssignmentType(assignmentType);

        ActivityAssignmentBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_assignment);
        binding.setViewModel((AssignmentViewModel) mViewModel);

        String title = assignmentType == ASSIGN_BY_REQUEST ? getString(R.string.title_assignment)
            : getString(R.string.title_assignment_for_new_member);
        setTitle(title);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        hideSoftKeyboard(this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }
}
