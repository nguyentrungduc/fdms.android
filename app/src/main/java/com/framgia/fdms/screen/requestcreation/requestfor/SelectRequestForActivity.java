package com.framgia.fdms.screen.requestcreation.requestfor;

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
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.StatusRemoteDataSource;
import com.framgia.fdms.databinding.ActivitySelectRequestForBinding;

/**
 * StatusSelection Screen.
 */
public class SelectRequestForActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static Intent getInstance(Context context) {
        return new Intent(context, SelectRequestForActivity.class);
    }

    protected SelectRequestForContract.ViewModel mViewModel;
    private SearchView mSearchView;
    private MenuItem mSearchMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new SelectRequestForViewModel(this);

        mViewModel.setPresenter(getPresenter());
        ActivitySelectRequestForBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_select_request_for);
        binding.setViewModel((SelectRequestForViewModel) mViewModel);
    }

    public SelectRequestForContract.Presenter getPresenter() {
        StatusRepository statusRepository = new StatusRepository(
                new StatusRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectRequestForPresenter(mViewModel, statusRepository);
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
                mViewModel.onSearch("");
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
        mViewModel.onSearch(query);
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
