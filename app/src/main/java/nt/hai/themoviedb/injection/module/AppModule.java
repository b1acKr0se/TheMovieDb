package nt.hai.themoviedb.injection.module;

import android.app.Application;
import android.content.Context;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nt.hai.themoviedb.injection.ApplicationContext;
import nt.hai.themoviedb.util.cache.ResponseCache;

@Module
public class AppModule {
    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context providesContext() {
        return application;
    }

    @Provides
    @Singleton
    ResponseCache providesResponseCache() {
        ResponseCache responseCache = null;
        try {
            responseCache = new ResponseCache(application);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseCache;
    }
}
