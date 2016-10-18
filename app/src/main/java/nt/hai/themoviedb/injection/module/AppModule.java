package nt.hai.themoviedb.injection.module;

import android.app.Application;
import android.content.Context;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nt.hai.themoviedb.data.remote.Api;
import nt.hai.themoviedb.injection.ApplicationContext;
import nt.hai.themoviedb.util.cache.ResponseCache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
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

    @Provides
    @Singleton
    Api providesClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(Api.class);
    }
}
