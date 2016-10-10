package nt.hai.themoviedb.ui.detail;

import java.util.List;

import nt.hai.themoviedb.data.model.DetailResponse;
import nt.hai.themoviedb.ui.base.BaseView;


interface DetailView extends BaseView {

    void showLoadingCast(boolean show);

    void showErrorLoadingCast();

    void showCast(List<DetailResponse.Cast> list);

    void showEmpty();
}
