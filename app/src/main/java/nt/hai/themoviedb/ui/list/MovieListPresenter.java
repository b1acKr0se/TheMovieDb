package nt.hai.themoviedb.ui.list;

import java.util.List;

import javax.inject.Inject;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.data.model.Movie;
import nt.hai.themoviedb.data.remote.RetrofitClient;
import nt.hai.themoviedb.ui.base.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MovieListPresenter extends Presenter<MovieListView> {
    private final CompositeSubscription subscription;

    @Inject
    public MovieListPresenter() {
        subscription = new CompositeSubscription();
    }

    @Override
    public void attachView(MovieListView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.unsubscribe();
    }

    void loadMovies() {
        subscription.add(
                getMovieObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showProgress(false);
                        getView().showError();
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        getView().showProgress(false);
                        getView().showMovies(movies);
                    }
                })
        );
    }

    private Observable<List<Movie>> getMovieObservable() {
        return RetrofitClient.getClient()
                .getPopularMovies(BuildConfig.API_KEY)
                .flatMap(response -> Observable.just(response.getResults()));
    }
}
