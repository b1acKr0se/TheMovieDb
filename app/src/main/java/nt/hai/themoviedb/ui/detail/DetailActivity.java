package nt.hai.themoviedb.ui.detail;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.data.model.GenreManager;
import nt.hai.themoviedb.data.model.Media;
import nt.hai.themoviedb.ui.base.BaseActivity;
import nt.hai.themoviedb.ui.castlist.CastFragment;
import nt.hai.themoviedb.util.DateUtil;
import nt.hai.themoviedb.util.GlideUtil;
import nt.hai.themoviedb.util.UrlBuilder;

public class DetailActivity extends BaseActivity implements DetailView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.backdrop) ImageView backdrop;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.layout_info) View container;
    @BindView(R.id.cast_recycler_view) RecyclerView castRecyclerView;
    @BindView(R.id.genre_recycler_view) RecyclerView genreRecyclerView;
    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;

    @Inject DetailPresenter presenter;

    private CastAdapter castAdapter;
    private GenreAdapter genreAdapter;
    private List<DetailResponse.Cast> casts = new ArrayList<>();
    private List<GenreManager.Genre> genres = new ArrayList<>();

    public static void navigate(Activity context, View view, Media media) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("media", media);
        ImageView coverStartView = (ImageView) view.findViewById(R.id.poster);
        if(coverStartView.getDrawable() == null) {
            context.startActivity(intent);
            return;
        }
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, coverStartView, "poster");
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTransition();
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.transparent));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        Media media = getIntent().getParcelableExtra("media");
        if (media.getBackgroundColor() != 0) {
            container.setBackgroundColor(media.getBackgroundColor());
            appBarLayout.setBackgroundColor(media.getBackgroundColor());
        }
        GlideUtil.load(this, UrlBuilder.getPosterUrl(media.getPosterPath()), poster);
        GlideUtil.load(this, UrlBuilder.getBackdropUrl(media.getBackdropPath()), backdrop);
        doCircularReveal();
        title.setText(media.getTitle());
        releaseDate.setText(DateUtil.format(media.getReleaseDate()));
        overview.setText(media.getOverview());
        rating.setText(media.getVoteAverage() + " from " + media.getVoteCount() + " votes");

        castAdapter = new CastAdapter(casts, CastAdapter.TYPE_SUMMARY);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(castAdapter);

        genreAdapter = new GenreAdapter(genres);
        genreRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        genreRecyclerView.setAdapter(genreAdapter);

        presenter.setMovieId(media.getId());
        presenter.loadCast();
        presenter.loadGenres(media.getGenreIds());
    }

    private void showCastDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CastFragment castFragment = CastFragment.newInstance(casts);
        castFragment.show(fm, "fragment_cast");
    }

    private void doCircularReveal() {
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

    private void setActivityTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade transition = new Fade();
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showCast(List<DetailResponse.Cast> list) {
        findViewById(R.id.cast_container).setVisibility(View.VISIBLE);
        casts.clear();
        casts.addAll(list);
        castAdapter.notifyDataSetChanged();
    }

    @Override public void showGenre(List<GenreManager.Genre> list) {
        findViewById(R.id.genre_container).setVisibility(View.VISIBLE);
        genres.clear();
        genres.addAll(list);
        genreAdapter.notifyDataSetChanged();
    }

    @Override public void showEmptyGenre() {
        findViewById(R.id.genre_container).setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        findViewById(R.id.cast_container).setVisibility(View.GONE);
    }

    @OnClick(R.id.view_all_cast)
    void onCastContainerClicked() {
        if (!casts.isEmpty())
            showCastDialog();
    }
}
