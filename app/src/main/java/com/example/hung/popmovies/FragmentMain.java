package com.example.hung.popmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hung.popmovies.net.Movie;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentMain extends Fragment {

//    @Bind(R.id.movie_list)
    RecyclerView mRecyclerView;

//    Movie

    public MovieAdapter mAdapter;

    public FragmentMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView =  inflater.inflate(R.layout.movie_list, container, false);

        mRecyclerView = (RecyclerView) myView.findViewById(R.id.movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MovieAdapter(new ArrayList<Movie>());
        mRecyclerView.setAdapter(mAdapter);

        FetchMoviesTask fmt = new FetchMoviesTask(getActivity(),mAdapter);
        fmt.execute();
        return myView;
    }


}
