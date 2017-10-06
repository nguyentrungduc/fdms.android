package com.framgia.fdms.screen.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.databinding.ActivityUserBinding;


/**
 * User Screen.
 */
public class UserActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "EXTRA_USER";
    private UserContract.ViewModel mViewModel;

    public static Intent getInstance(Context context, User user) {
        return new Intent(context, UserActivity.class).putExtra(EXTRA_USER, user)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = getIntent().getExtras().getParcelable(EXTRA_USER);
        mViewModel = new UserViewModel(this, user);

        UserContract.Presenter presenter = new UserPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityUserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        binding.setViewModel((UserViewModel) mViewModel);
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
}
