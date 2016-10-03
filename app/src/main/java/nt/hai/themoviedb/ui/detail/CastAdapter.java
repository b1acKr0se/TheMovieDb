package nt.hai.themoviedb.ui.detail;

import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.CastResponse;
import nt.hai.themoviedb.util.UrlBuilder;


public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    public static final int TYPE_SUMMARY = 1;
    public static final int TYPE_FULL = 2;

    private List<CastResponse.Cast> casts;
    private int type;

    public CastAdapter(List<CastResponse.Cast> list, @TypeDef int typeDef) {
        casts = list;
        type = typeDef;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CastViewHolder(LayoutInflater.from(parent.getContext()).inflate(type == TYPE_SUMMARY ? R.layout.item_cast : R.layout.item_cast_table, parent, false));
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
        if (type == TYPE_FULL) {
            holder.name.setText(cast.getName());
            holder.character.setText(cast.getCharacter());
        }

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cast_image) ImageView image;
        @Nullable @BindView(R.id.name) TextView name;
        @Nullable @BindView(R.id.character) TextView character;

        CastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_SUMMARY,
            TYPE_FULL
    })
    @interface TypeDef {
    }
}
