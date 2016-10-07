package nt.hai.themoviedb.ui.search;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Media;
import nt.hai.themoviedb.data.model.Section;
import nt.hai.themoviedb.util.GlideUtil;
import nt.hai.themoviedb.util.UrlBuilder;


class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SECTION = 1;
    private static final int TYPE_PERSON = 2;
    private static final int TYPE_MOVIE = 3;
    private List<Object> list;
    private OnMediaClickListener listener;

    SearchAdapter(List<Object> objects) {
        list = objects;
    }

    void setListener(OnMediaClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SECTION:
                return new SearchSectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_section, parent, false));
            case TYPE_MOVIE:
                return new SearchMovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_movie, parent, false));
            case TYPE_PERSON:
                return new SearchCastViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_people, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) return;
        Object object = list.get(position);
        if (holder instanceof SearchSectionViewHolder) {
            SearchSectionViewHolder viewHolder = (SearchSectionViewHolder) holder;
            viewHolder.topDivider.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            viewHolder.name.setText(((Section) object).name);
        } else if (holder instanceof SearchMovieViewHolder) {
            Media movie = (Media) object;
            SearchMovieViewHolder viewHolder = (SearchMovieViewHolder) holder;
            viewHolder.divider.setVisibility(position == list.size() - 1 ? View.VISIBLE : View.GONE);
            viewHolder.title.setText(movie.getTitle());
            GlideUtil.load(viewHolder.itemView.getContext(), UrlBuilder.getPosterUrl(movie.getPosterPath()), viewHolder.poster);
            if(listener != null)
                RxView.clicks(viewHolder.itemView).subscribe(aVoid -> listener.onMovieClicked(viewHolder.itemView, movie));
        } else if (holder instanceof SearchCastViewHolder) {
            Media person = (Media) object;
            SearchCastViewHolder viewHolder = (SearchCastViewHolder) holder;
            viewHolder.divider.setVisibility(position == list.size() - 1 ? View.VISIBLE : View.GONE);
            viewHolder.name.setText(person.getName());
            Glide.with(holder.itemView.getContext()).load(UrlBuilder.getCastUrl(person.getProfilePath()))
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(viewHolder.image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            viewHolder.image.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            if(listener != null)
                RxView.clicks(viewHolder.itemView).subscribe(aVoid -> listener.onPersonClicked(viewHolder.itemView, person));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = list.get(position);
        if (object instanceof Section)
            return TYPE_SECTION;
        if (object instanceof Media) {
            return ((Media) object).getMediaType().equals("movie") ? TYPE_MOVIE : TYPE_PERSON;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SearchSectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.top_divider) View topDivider;
        @BindView(R.id.name) TextView name;

        SearchSectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SearchCastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.bottom_divider) View divider;

        SearchCastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SearchMovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.bottom_divider) View divider;

        SearchMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnMediaClickListener {
        void onMovieClicked(View view, Media movie);

        void onPersonClicked(View view, Media person);
    }
}
