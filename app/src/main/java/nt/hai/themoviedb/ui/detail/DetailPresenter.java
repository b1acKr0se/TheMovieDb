package nt.hai.themoviedb.ui.detail;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.data.model.Cast;
import nt.hai.themoviedb.data.remote.RetrofitClient;
import nt.hai.themoviedb.ui.base.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class DetailPresenter extends Presenter<DetailView> {
    private CompositeSubscription subscription;
    private int movieId;

    public DetailPresenter() {
        subscription = new CompositeSubscription();
    }

    public void setMovieId(int id) {
        this.movieId = id;
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.unsubscribe();
    }

    public void loadCast() {
        subscription.add(
                getCastListJsonObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(s -> {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject object = (JsonObject)jsonParser.parse(s);
                    JsonArray jsonArr = object.getAsJsonArray("cast");
                    Type listType = new TypeToken<List<Cast>>() {}.getType();
                    List<Cast> jsonObjList = new Gson().fromJson(jsonArr, listType);
                    return Observable.from(jsonObjList);
                })
                .filter(cast -> cast.getProfilePath() != null)
                .toList()
                .subscribe(new Subscriber<List<Cast>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Cast> list) {
                        getView().showCast(list);
                    }
                })
        );
    }

    private Observable<String> getCastListJsonObservable() {
        return RetrofitClient.getClient()
                .getCastList(movieId, BuildConfig.API_KEY);
    }
}
