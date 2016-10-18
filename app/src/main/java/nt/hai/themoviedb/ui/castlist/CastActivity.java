package nt.hai.themoviedb.ui.castlist;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.ui.detail.CastAdapter;

public class CastActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private List<DetailResponse.Cast> list;
    private LinearLayoutManager layoutManager;

    //stole from the plaid app
    private RecyclerView.OnScrollListener toolbarElevation = new RecyclerView.OnScrollListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && layoutManager.findFirstVisibleItemPosition() == 0
                    && layoutManager.findViewByPosition(0).getTop() == recyclerView.getPaddingTop()
                    && toolbar.getTranslationZ() != 0) {
                // at top, reset elevation
                toolbar.setTranslationZ(0f);
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                    && toolbar.getTranslationZ() != -1f) {
                // grid scrolled, lower toolbar to allow content to pass in front
                toolbar.setTranslationZ(-1f);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Top Billed Cast");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24px);

        initPadding();
        list = getIntent().getParcelableArrayListExtra("cast");
        recyclerView.addOnScrollListener(toolbarElevation);
        CastAdapter adapter = new CastAdapter(list, CastAdapter.TYPE_FULL);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if(savedInstanceState != null) {
            recyclerView.post(() -> layoutManager.scrollToPositionWithOffset(0, 0));
        }
    }

    private void initPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                    new int[]{android.R.attr.actionBarSize});
            int actionBarSize = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
            recyclerView.setPadding(0, actionBarSize, 0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
