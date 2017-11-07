package com.framgia.fdms.screen.baseselection;

import android.app.SearchManager;
import android.content.Context;
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
import com.framgia.fdms.databinding.ActivityStatusSelectionBinding;

/**
 * StatusSelection Screen.
 */
public abstract class BaseSelectionActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    protected BaseSelectionContract.ViewModel mViewModel;
    private SearchView mSearchView;
    private MenuItem mSearchMenu;

    public abstract void getDataFromIntent();

    public abstract BaseSelectionContract.Presenter getPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
        mViewModel = new BaseSelectionViewModel(this);
        getPresenter();
        mViewModel.setPresenter(getPresenter());
        ActivityStatusSelectionBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_status_selection);
        binding.setViewModel((BaseSelectionViewModel) mViewModel);
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
