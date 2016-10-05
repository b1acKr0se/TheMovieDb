package nt.hai.themoviedb.ui.detail;

import java.util.List;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.data.remote.RetrofitClient;
import nt.hai.themoviedb.ui.base.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
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
                                getView().showLoadingCast(false);
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<DetailResponse.Cast> list) {
                                getView().showLoadingCast(false);
                                getView().showCast(list);
                            }
                        })
        );
    }

    void loadImages() {
        subscription.add(Observable.zip(getImages(), getVideos(), (t1, t2) -> {
            DetailResponse response = new DetailResponse();
            response.setPosters(t1);
            response.setVideos(t2);
            return response;
        }).subscribe(new Subscriber<DetailResponse>() {
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
