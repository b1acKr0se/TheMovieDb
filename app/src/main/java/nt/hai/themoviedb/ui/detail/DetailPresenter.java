package nt.hai.themoviedb.ui.detail;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.data.model.GenreManager;
import nt.hai.themoviedb.data.remote.RetrofitClient;
import nt.hai.themoviedb.ui.base.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


class DetailPresenter extends Presenter<DetailView> {
    private CompositeSubscription subscription;
    private int movieId;

    DetailPresenter() {
        subscription = new CompositeSubscription();
    }

    void setMovieId(int id) {
        this.movieId = id;
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.unsubscribe();
    }

    void loadCast() {
        subscription.add(
                getCastListJsonObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .flatMap(castResponse -> Observable.from(castResponse.getCast()))
                        .filter(cast -> cast.getProfilePath() != null)
                        .toList()
                        .subscribe(new Subscriber<List<DetailResponse.Cast>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showEmpty();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<DetailResponse.Cast> list) {
                                if (!list.isEmpty()) {
                                    getView().showLoadingCast(false);
                                    getView().showCast(list);
                                } else {
                                    getView().showEmpty();
                                }
                            }
                        })
        );
    }

    void loadImages() {
        subscription.add(
                Observable
                        .zip(getImages(), getVideos(), (t1, t2) -> {
                            DetailResponse response = new DetailResponse();
                            response.setPosters(t1);
                            response.setVideos(t2);
                            return response;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(detailResponse -> {
                            getView().showLoadingCast(false);
                        })
                        .subscribe(new Subscriber<DetailResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(DetailResponse detailResponse) {

                            }
                        }));
    }

    void loadGenres(List<Integer> genreIds) {
        if (genreIds.isEmpty()) return;
        subscription.add(getGenreList()
                .filter(genres -> genres != null)
                .map(genreManager -> genreManager.getGenreList(genreIds))
                .subscribe(new Subscriber<List<GenreManager.Genre>>() {
                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().showEmptyGenre();
                    }

                    @Override public void onNext(List<GenreManager.Genre> list) {
                        getView().showGenre(list);
                    }
                }));
    }

    private Observable<GenreManager> getGenreList() {
        return Observable.create(subscriber -> {
            try {
                InputStream is = getView().getAssets().open("genres.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json = new String(buffer, "UTF-8");
                Type type = new TypeToken<GenreManager>() {}.getType();
                subscriber.onNext(new Gson().fromJson(json, type));
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    private Observable<DetailResponse> getCastListJsonObservable() {
        return RetrofitClient.getClient()
                .getCastList(movieId, BuildConfig.API_KEY);
    }

    private Observable<List<DetailResponse.Image>> getImages() {
        return RetrofitClient.getClient().getImages(BuildConfig.API_KEY)
                .flatMap(detailResponse -> Observable.just(detailResponse.getBackdrops(), detailResponse.getPosters()));
    }

    private Observable<List<DetailResponse.Video>> getVideos() {
        return RetrofitClient.getClient().getVideos(BuildConfig.API_KEY)
                .flatMap(detailResponse -> Observable.just(detailResponse.getVideos()));
    }
}
