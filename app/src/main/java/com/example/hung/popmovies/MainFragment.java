package com.example.hung.popmovies;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hung.popmovies.db.MovieContract;
import com.example.hung.popmovies.net.Movie;
import com.example.hung.popmovies.service.MovieService;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView mRecyclerView;

    public MovieAdapter mAdapter;
    private String log = MainFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 0;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(log, "onCreateView");
        View myView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) myView.findViewById(R.id.movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MovieAdapter(null, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return myView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.v(log, "onViewCreated");
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onStart() {
        Log.v(log, "onStart");
        super.onStart();
//        if (!Utility.FAVORITES.equals(Utility.getSortByType(getActivity()))) {
//            fetchMovies();
//        }
        fetchMovies();
    }

    @Override
    public void onResume() {
        Log.v(log, "onResume");
        super.onResume();
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(log, "onCreateLoader");
        String sortBy = Utility.getSortByType(getActivity());

        Uri uri = MovieContract.MovieEntry.getMoviesUriFromSortByType(sortBy);
        return new CursorLoader(
                getActivity(),
                uri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(log, "onLoadFinished");
        if(cursor.moveToFirst()) {
            ArrayList<Movie> movies = Utility.getMoviesFromCursor(cursor);
            mAdapter.updateAdapter(movies);
        }
        else {
            Log.v(log,"Database is empty!");
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(log, "onLoaderReset");
    }

    public void onSortByChanged(String sortBy) {
        Log.v(log, "onLocationChanged");
//        if (!sortBy.equals(Utility.FAVORITES)) {
//            fetchMovies();
//        } else {
//            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
//        }
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);

    }

    public void onFetchFinished() {
        Log.v(log, "onFetchFinished");
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    private void fetchMovies() {
//        FetchMoviesTask fmt = new FetchMoviesTask(getActivity());
//        fmt.execute();

//        Intent intent = new Intent(getActivity(), MovieService.class);
//        intent.putExtra(Utility.SORT_BY,
//                Utility.getSortByType(getActivity()));
//        getActivity().startService(intent);

        Intent alarmIntent = new Intent(getActivity(), MovieService.AlarmReceiver.class);
//        alarmIntent.putExtra(Utility.SORT_BY, Utility.getSortByType(getActivity()));

        //Wrap in a pending intent which only fires once.
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0,alarmIntent,0);//getBroadcast(context, 0, i, 0);

        AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

        //Set the AlarmManager to wake up the system.
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000*60*60, pi);
        Log.v(log, "onFetchMovies");
    }

}
