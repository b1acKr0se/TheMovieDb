package nt.hai.themoviedb.ui.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Media;
import nt.hai.themoviedb.data.model.Response;
import nt.hai.themoviedb.ui.base.BaseActivity;
import nt.hai.themoviedb.ui.detail.DetailActivity;
import nt.hai.themoviedb.ui.widget.ResettableEditText;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SearchActivity extends BaseActivity implements SearchView, ResettableEditText.ClearListener, SearchAdapter.OnMediaClickListener {
    @BindView(R.id.search_edit_text) ResettableEditText editText;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.indication) TextView indicator;
    private SearchPresenter presenter = new SearchPresenter();
    private Subscription subscription;
    private SearchAdapter adapter;
    private List<Object> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter.attachView(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText.setListener(this);
        setupListener();
        setupRecyclerView();
    }

    private void setupListener() {
        subscription = RxTextView.textChanges(editText)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .switchMap(Observable::just)
                .filter(s -> s != null && s.length() >= 2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    presenter.search(charSequence.toString());
                });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(list);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        subscription.unsubscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        if(show) indicator.setVisibility(View.GONE);
    }

    @Override
    public void showResult(List<Object> list) {
        this.list.clear();
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmpty() {
        indicator.setVisibility(View.VISIBLE);
        indicator.setText(getString(R.string.empty));
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        indicator.setVisibility(View.VISIBLE);
        indicator.setText(getString(R.string.error));
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onTextCleared() {
        this.list.clear();
        adapter.notifyDataSetChanged();
        showProgress(false);
    }

    @Override
    public void onMovieClicked(View view, Media movie) {
        DetailActivity.navigate(this, view, movie);
    }

    @Override
    public void onPersonClicked(View view, Media person) {
        Toast.makeText(getApplicationContext(), person.getName(), Toast.LENGTH_SHORT).show();
    }
}
