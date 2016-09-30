package nt.hai.themoviedb.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.CastResponse;
import nt.hai.themoviedb.data.model.Movie;
import nt.hai.themoviedb.ui.widget.DividerItemDecoration;
import nt.hai.themoviedb.util.DateUtil;
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
    private List<CastResponse.Cast> casts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        presenter.attachView(this);
        Movie movie = getIntent().getParcelableExtra("movie");
        Glide.with(this)
                .load(UrlBuilder.getPosterUrl(movie.getPosterPath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .dontTransform()
                .into(poster);
        Glide.with(this)
                .load(UrlBuilder.getBackdropUrl(movie.getBackdropPath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(backdrop);

        if (movie.getBackgroundColor() != 0)
            container.setBackgroundColor(movie.getBackgroundColor());
        title.setText(movie.getTitle());
        releaseDate.setText(DateUtil.format(movie.getReleaseDate()));
        overview.setText(movie.getOverview());
        rating.setText(movie.getVoteAverage() + " from " + movie.getVoteCount() + " votes");

        adapter = new CastAdapter(casts);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(adapter);
        presenter.setMovieId(movie.getId());
        presenter.loadCast();
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
    public void showCast(List<CastResponse.Cast> list) {
        castRecyclerView.setVisibility(View.VISIBLE);
        casts.clear();
        casts.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
