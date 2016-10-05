package nt.hai.themoviedb.ui.list;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
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
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Media;
import nt.hai.themoviedb.util.UrlBuilder;
import rx.functions.Action1;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ITEM = 1;
    private List<Media> list;
    private OnMovieClickListener onMovieClickListener;

    public MovieListAdapter(List<Media> movies) {
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
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            MovieViewHolder viewHolder = (MovieViewHolder) holder;
            Media media = list.get(position);
            RxView.clicks(viewHolder.itemView).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
                onMovieClickListener.onMovieClicked(media, viewHolder.itemView);
            });
            viewHolder.title.setText(media.getTitle());
            viewHolder.year.setText(media.getReleaseDate().split("-")[0]);
            Glide.clear(viewHolder.poster);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.poster.getContext(), R.color.colorPrimary));
            Glide.with(viewHolder.poster.getContext())
                    .load(UrlBuilder.getPosterUrl(media.getPosterPath()))
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
                            list.get(position).setBackgroundColor(color);
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

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.year) TextView year;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnMovieClickListener {
        void onMovieClicked(Media media, View view);
    }
}
