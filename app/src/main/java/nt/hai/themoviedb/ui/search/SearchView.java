package nt.hai.themoviedb.ui.search;

import nt.hai.themoviedb.data.model.Response;
import nt.hai.themoviedb.ui.base.BaseView;


public interface SearchView extends BaseView {

    void showProgress(boolean show);

    void showResult(Response response);
}
