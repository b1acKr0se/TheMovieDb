package nt.hai.themoviedb;

import android.app.Application;
import android.content.Context;

import nt.hai.themoviedb.injection.component.AppComponent;
import nt.hai.themoviedb.injection.component.DaggerAppComponent;
import nt.hai.themoviedb.injection.module.AppModule;


public class MovieApplication extends Application {
    AppComponent appComponent;

    @Override public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public static MovieApplication getApplication(Context context) {
        return (MovieApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
