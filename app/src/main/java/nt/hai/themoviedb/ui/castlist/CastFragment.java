package nt.hai.themoviedb.ui.castlist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nt.hai.themoviedb.R;
import nt.hai.themoviedb.data.model.CastResponse;
import nt.hai.themoviedb.ui.detail.CastAdapter;

public class CastFragment extends DialogFragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private List<CastResponse.Cast> casts;
    private CastAdapter adapter;

    public CastFragment() {

    }

    public static CastFragment newInstance(List<CastResponse.Cast> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("cast", (ArrayList<CastResponse.Cast>) list);
        CastFragment fragment = new CastFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cast, container, false);
        ButterKnife.bind(this, view);
        casts = getArguments().getParcelableArrayList("cast");
        adapter = new CastAdapter(casts, CastAdapter.TYPE_FULL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
