package nt.hai.themoviedb.injection.component;

import dagger.Component;
import nt.hai.themoviedb.injection.PerActivity;
import nt.hai.themoviedb.injection.module.ActivityModule;
import nt.hai.themoviedb.ui.detail.DetailActivity;
import nt.hai.themoviedb.ui.list.ListFragment;
import nt.hai.themoviedb.ui.search.SearchActivity;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(ListFragment fragment);
    void inject(DetailActivity detailActivity);
    void inject(SearchActivity searchActivity);
}
