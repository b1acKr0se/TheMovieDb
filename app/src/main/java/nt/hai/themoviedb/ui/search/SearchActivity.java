package nt.hai.themoviedb.ui.search;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Response;
import nt.hai.themoviedb.ui.base.BaseActivity;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SearchActivity extends BaseActivity implements SearchView {
    @BindView(R.id.search_edit_text) EditText editText;
    @BindView(R.id.toolbar) Toolbar toolbar;
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
        subscription = RxTextView.textChanges(editText)
                .filter(s -> s != null && s.length() >= 2)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(Observable::just)
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
    public void showProgress(boolean show) {

    }

    @Override
    public void showResult(Response response) {

    }
}
