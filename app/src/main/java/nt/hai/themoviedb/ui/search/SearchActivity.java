package nt.hai.themoviedb.ui.search;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Response;
import nt.hai.themoviedb.ui.base.BaseActivity;
import nt.hai.themoviedb.ui.widget.ResettableEditText;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SearchActivity extends BaseActivity implements SearchView {
    @BindView(R.id.search_edit_text) ResettableEditText editText;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private SearchPresenter presenter = new SearchPresenter();
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter.attachView(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupListener();
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
    }

    @Override
    public void showResult(Response response) {

    }
}
