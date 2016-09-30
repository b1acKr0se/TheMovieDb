package nt.hai.themoviedb.ui.list;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Movie;
import nt.hai.themoviedb.util.UrlBuilder;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ITEM = 1;
    private List<Movie> list;
    private OnMovieClickListener onMovieClickListener;

    public MovieListAdapter(List<Movie> movies) {
        this.list = movies;
    }

    public void setOnMovieClickListener(OnMovieClickListener listener) {
        this.onMovieClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, parent, false), onMovieClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MovieViewHolder) {
            MovieViewHolder viewHolder = (MovieViewHolder) holder;
            Movie movie = list.get(position);
            viewHolder.movie = movie;
            viewHolder.itemView.setOnClickListener(viewHolder);
            viewHolder.title.setText(movie.getTitle());
            viewHolder.year.setText(movie.getReleaseDate().split("-")[0]);
            Glide.clear(viewHolder.poster);
            Glide.with(viewHolder.poster.getContext())
                    .load(UrlBuilder.getPosterUrl(movie.getPosterPath()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                            Palette palette = new Palette.Builder(bitmap).generate();
                            int defaultColor = 0xFF333333;
                            int color = palette.getDarkMutedColor(defaultColor);
                            holder.itemView.setBackgroundColor(color);
                            return false;
                        }
                    })
                    .into(viewHolder.poster);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.year) TextView year;
        private Movie movie;
        private OnMovieClickListener onMovieClickListener;

        public MovieViewHolder(View itemView, OnMovieClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onMovieClickListener = listener;
        }

        @Override
        public void onClick(View view) {
            onMovieClickListener.onMovieClicked(movie);
        }
    }

    public interface OnMovieClickListener {
        void onMovieClicked(Movie movie);
    }
}
