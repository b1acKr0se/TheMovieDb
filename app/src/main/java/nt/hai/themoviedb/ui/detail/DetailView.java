package nt.hai.themoviedb.ui.detail;

import java.util.List;

import nt.hai.themoviedb.data.model.CastResponse;
import nt.hai.themoviedb.ui.base.BaseView;


interface DetailView extends BaseView {

    void showLoadingCast(boolean show);

    void showErrorLoadingCast();

    void showCast(List<CastResponse.Cast> list);

}
