package nt.hai.themoviedb.ui.search;

import java.util.ArrayList;
import java.util.List;

import nt.hai.themoviedb.BuildConfig;
import nt.hai.themoviedb.data.model.Media;
import nt.hai.themoviedb.data.model.Response;
import nt.hai.themoviedb.data.model.Section;
import nt.hai.themoviedb.data.remote.RetrofitClient;
import nt.hai.themoviedb.ui.base.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SearchPresenter extends Presenter<SearchView> {
    private Subscription subscription;

    void search(String query) {
        if (subscription != null) subscription.unsubscribe();
        subscription = searchObservable(query)
                .switchMap(response -> Observable.from(response.getResults()))
                .filter(media -> media != null && !media.getMediaType().equals("tv"))
                .toList()
                .flatMap(medias -> Observable.zip(
                        extractSearchResult(medias, "person"),
                        extractSearchResult(medias, "movie"),
                        (people, movies) -> {
                            List<Object> list = new ArrayList<>();
                            if (!movies.isEmpty()) {
                                Section movieSection = new Section();
                                movieSection.name = "Movies";
                                list.add(movieSection);
                                list.addAll(movies);
                            }
                            if (!people.isEmpty()) {
                                Section peopleSection = new Section();
                                peopleSection.name = "People";
                                list.add(peopleSection);
                                list.addAll(people);
                            }
                            return list;
                        }))
                .flatMap(Observable::just)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showProgress(false);
                        getView().showError();
                    }

                    @Override
                    public void onNext(List<Object> list) {
                        getView().showProgress(false);
                        if (!list.isEmpty())
                            getView().showResult(list);
                        else getView().showEmpty();
                    }

                    @Override
                    public void onStart() {
                        getView().showProgress(true);
                    }
                });
    }

    private Observable<List<Media>> extractSearchResult(List<Media> list, String type) {
        return Observable.from(list)
                .filter(media -> media.getMediaType().equals(type))
                .toList();
    }

    private Observable<Response> searchObservable(String query) {
        return RetrofitClient.getClient().search(BuildConfig.API_KEY, query);
    }
}
