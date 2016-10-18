package nt.hai.themoviedb.ui.base;

import android.support.v7.app.AppCompatActivity;

import nt.hai.themoviedb.MovieApplication;
import nt.hai.themoviedb.injection.component.ActivityComponent;
import nt.hai.themoviedb.injection.component.DaggerActivityComponent;
import nt.hai.themoviedb.injection.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .appComponent(MovieApplication.getApplication(this).getAppComponent())
                    .build();
        }
        return activityComponent;
    }
}
