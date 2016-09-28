package nt.hai.themoviedb.ui.list;

import android.os.Bundle;
import android.util.Log;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Movie;
import nt.hai.themoviedb.data.remote.RetrofitClient;
import nt.hai.themoviedb.ui.base.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private Subscriber<Movie> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subscriber = new Subscriber<Movie>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Movie movie) {
                Log.i("onNext", movie.toString());
            }
        };

        RetrofitClient.getClient().getPopularMovies(BuildConfig.API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(response -> response != null)
                .flatMap(response -> Observable.from(response.getResults()))
                .filter(movie -> movie.getVoteAverage() > 5)
                .subscribe(subscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriber.unsubscribe();
    }
}
