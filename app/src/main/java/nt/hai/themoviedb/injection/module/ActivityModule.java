package nt.hai.themoviedb.injection.module;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import dagger.Module;
import dagger.Provides;
import nt.hai.themoviedb.injection.ActivityContext;

@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity providesActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return activity;
    }

    @Provides
    AssetManager providesAssetManager() {
        return activity.getAssets();
    }
}
