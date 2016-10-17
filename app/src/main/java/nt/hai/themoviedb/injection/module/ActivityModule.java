package nt.hai.themoviedb.injection.module;


import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import nt.hai.themoviedb.injection.ActivityContext;

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity providesActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}
