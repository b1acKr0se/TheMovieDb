package nt.hai.themoviedb.ui.detail;

import android.content.res.AssetManager;

import java.util.List;

import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.data.model.GenreManager;
import nt.hai.themoviedb.ui.base.BaseView;


interface DetailView extends BaseView {

    void showCast(List<DetailResponse.Cast> list);

    void showGenre(List<GenreManager.Genre> list);

    void showEmptyGenre();

    void showEmpty();

    AssetManager getAssets();
}
