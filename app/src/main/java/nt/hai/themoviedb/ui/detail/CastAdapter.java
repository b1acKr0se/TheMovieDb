package nt.hai.themoviedb.ui.detail;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.CastResponse;
import nt.hai.themoviedb.util.UrlBuilder;


class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private List<CastResponse.Cast> casts;

    CastAdapter(List<CastResponse.Cast> list) {
        casts = list;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        CastResponse.Cast cast = casts.get(position);
        Glide.with(holder.itemView.getContext()).load(UrlBuilder.getCastUrl(cast.getProfilePath()))
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.image.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cast_image) ImageView image;

        CastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
