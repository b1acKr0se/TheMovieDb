package nt.hai.themoviedb.ui.list;


import java.util.List;

import nt.hai.themoviedb.data.model.Movie;
import nt.hai.themoviedb.ui.base.BaseView;

public interface MovieListView extends BaseView {

    void showProgress(boolean show);

    void showMovies(List<Movie> movies);

    void showError();
}
