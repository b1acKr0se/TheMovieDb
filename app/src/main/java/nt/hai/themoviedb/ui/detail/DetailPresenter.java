package nt.hai.themoviedb.ui.detail;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.List;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.data.model.CastResponse;
import nt.hai.themoviedb.data.remote.RetrofitClient;
import nt.hai.themoviedb.ui.base.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
                        .subscribe(new Subscriber<List<CastResponse.Cast>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showLoadingCast(false);
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<CastResponse.Cast> list) {
                                getView().showLoadingCast(false);
                                getView().showCast(list);
                            }
                        })
        );
    }

    private Observable<CastResponse> getCastListJsonObservable() {
        return RetrofitClient.getClient()
                .getCastList(movieId, BuildConfig.API_KEY);
    }
}
