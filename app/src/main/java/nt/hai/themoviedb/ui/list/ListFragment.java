package nt.hai.themoviedb.ui.list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.Movie;

public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MovieListView {
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    MovieListPresenter presenter = new MovieListPresenter();
    private MovieListAdapter adapter;
    private List<Movie> movies = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        adapter = new MovieListAdapter(movies);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        showProgress(true);
        return view;
    }


    @Override
    public void onRefresh() {
        presenter.loadMovies();
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            swipeRefreshLayout.post(() -> {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            });
        } else
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMovies(List<Movie> list) {
        movies.clear();
        movies.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
