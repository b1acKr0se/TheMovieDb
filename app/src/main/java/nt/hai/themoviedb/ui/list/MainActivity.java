package nt.hai.themoviedb.ui.list;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements ListFragment.Callback {

    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ListFragment fragment = new ListFragment();
        fragment.setCallback(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
            appBarLayout.setExpanded(false, true);
    }

    @Override
    public void onSceneTransitionStarted() {
        appBarLayout.setExpanded(false, true);
    }
}
