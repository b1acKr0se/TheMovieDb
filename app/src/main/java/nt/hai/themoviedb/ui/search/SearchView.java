package nt.hai.themoviedb.ui.search;

import java.util.List;

import nt.hai.themoviedb.ui.base.BaseView;


public interface SearchView extends BaseView {

    void showProgress(boolean show);

    void showResult(List<Object> list);

    void showEmpty();

    void showError();
}
