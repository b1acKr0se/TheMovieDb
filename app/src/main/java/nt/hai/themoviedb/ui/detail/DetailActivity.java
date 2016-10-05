package nt.hai.themoviedb.ui.detail;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.data.model.Media;
import nt.hai.themoviedb.ui.castlist.CastFragment;
import nt.hai.themoviedb.util.DateUtil;
import nt.hai.themoviedb.util.GlideUtil;
import nt.hai.themoviedb.util.UrlBuilder;

public class DetailActivity extends AppCompatActivity implements DetailView {
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.backdrop) ImageView backdrop;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.layout_info) View container;
    @BindView(R.id.cast_recycler_view) RecyclerView castRecyclerView;
    @BindView(R.id.cast_progress_bar) ProgressBar progressBar;
    private DetailPresenter presenter = new DetailPresenter();
    private CastAdapter adapter;
    private List<DetailResponse.Cast> casts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        presenter.attachView(this);
        Media media = getIntent().getParcelableExtra("media");
        GlideUtil.load(this, UrlBuilder.getPosterUrl(media.getPosterPath()), poster);
        doCircularReveal(UrlBuilder.getBackdropUrl(media.getBackdropPath()));
        if (media.getBackgroundColor() != 0)
            container.setBackgroundColor(media.getBackgroundColor());
        title.setText(media.getTitle());
        releaseDate.setText(DateUtil.format(media.getReleaseDate()));
        overview.setText(media.getOverview());
        rating.setText(media.getVoteAverage() + " from " + media.getVoteCount() + " votes");
        adapter = new CastAdapter(casts, CastAdapter.TYPE_SUMMARY);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(adapter);
        presenter.setMovieId(media.getId());
        presenter.loadCast();
    }

    private void showCastDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CastFragment castFragment = CastFragment.newInstance(casts);
        castFragment.show(fm, "fragment_cast");
    }

    private void doCircularReveal(String path) {
        GlideUtil.load(this, path, backdrop);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            backdrop.post(() -> {
                int centerX = backdrop.getMeasuredWidth() / 2;
                int centerY = backdrop.getMeasuredHeight() / 2;
                int endRadius = (int) Math
                        .hypot(backdrop.getWidth(), backdrop.getHeight());
                Animator animator = ViewAnimationUtils.createCircularReveal(backdrop, centerX, centerY, 0, endRadius);
                animator.setDuration(800);
                backdrop.setVisibility(View.VISIBLE);
                animator.start();
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showLoadingCast(boolean show) {
        if (show) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorLoadingCast() {

    }

    @Override
    public void showCast(List<DetailResponse.Cast> list) {
        castRecyclerView.setVisibility(View.VISIBLE);
        casts.clear();
        casts.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.cast_container)
    void onCastContainerClicked() {
        showCastDialog();
    }
}
