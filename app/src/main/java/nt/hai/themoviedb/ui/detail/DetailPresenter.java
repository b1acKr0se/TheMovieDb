package nt.hai.themoviedb.ui.detail;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.data.model.GenreManager;
import nt.hai.themoviedb.data.remote.Api;
import nt.hai.themoviedb.ui.base.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


class DetailPresenter extends Presenter<DetailView> {
    private CompositeSubscription subscription;
    private AssetManager assetManager;
    private Api client;
    private int movieId;

    @Inject
    public DetailPresenter(AssetManager am, Api api) {
        subscription = new CompositeSubscription();
        assetManager = am;
        client = api;
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
                        .flatMap(castResponse -> Observable.from(castResponse.getCast()))
                        .filter(cast -> cast.getProfilePath() != null)
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
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
        subscription.add(getGenreObservable()
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

    private Observable<GenreManager> getGenreObservable() {
        try {
            InputStream is = assetManager.open("genres.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Type type = new TypeToken<GenreManager>() {
            }.getType();
            return Observable.just(new Gson().fromJson(json, type));
        } catch (IOException e) {
            return Observable.error(e);
        }
    }

    private Observable<DetailResponse> getCastListJsonObservable() {
        return client.getCastList(movieId, BuildConfig.API_KEY);
    }

    private Observable<List<DetailResponse.Image>> getImages() {
        return client.getImages(BuildConfig.API_KEY)
                .flatMap(detailResponse -> Observable.just(detailResponse.getBackdrops(), detailResponse.getPosters()));
    }

    private Observable<List<DetailResponse.Video>> getVideos() {
        return client.getVideos(BuildConfig.API_KEY)
                .flatMap(detailResponse -> Observable.just(detailResponse.getVideos()));
    }
}
