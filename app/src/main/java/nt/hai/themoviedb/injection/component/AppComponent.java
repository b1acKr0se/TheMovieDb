package nt.hai.themoviedb.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import nt.hai.themoviedb.MovieApplication;
import nt.hai.themoviedb.data.remote.Api;
import nt.hai.themoviedb.injection.ApplicationContext;
import nt.hai.themoviedb.injection.module.AppModule;
import nt.hai.themoviedb.util.cache.ResponseCache;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MovieApplication application);

    @ApplicationContext Context context();

    Application application();

    ResponseCache responseCache();

    Api client();
}
