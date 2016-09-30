package nt.hai.themoviedb.ui.detail;

import java.util.List;

import nt.hai.themoviedb.data.model.Cast;
import nt.hai.themoviedb.ui.base.BaseView;


public interface DetailView extends BaseView {

    void showLoadingCast(boolean show);

    void showErrorLoadingCast();

    void showCast(List<Cast> list);

}
