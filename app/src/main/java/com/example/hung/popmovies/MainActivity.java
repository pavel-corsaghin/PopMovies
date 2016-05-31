package com.example.hung.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.hung.popmovies.net.Movie;

public class MainActivity extends AppCompatActivity implements MovieAdapter.Callbacks, FetchMoviesTask.Callbacks {
    String mSortBy = Utility.MOST_POPULAR;
    //    MainFragment mMainFragment;
    private String log = MainActivity.class.getSimpleName();
    private String mainFragmentTag = MainFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Utility.getSortByType(this) == null) {
            Utility.saveSortByType(mSortBy, this);
        }else {
            mSortBy = Utility.getSortByType(this);
        }

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_main_movies, new MainFragment(), mainFragmentTag)
//                    .commit();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        switch (mSortBy) {
            case Utility.MOST_POPULAR:
                menu.findItem(R.id.sort_by_most_popular).setChecked(true);
                break;
            case Utility.TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case Utility.FAVORITES:
                menu.findItem(R.id.sort_by_favorites).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId != R.id.menu_settings) {
            String sortBy = Utility.getSortByTypeById(itemId);
            if (!mSortBy.equals(sortBy)) {
                item.setChecked(true);
                mSortBy = sortBy;
                Utility.saveSortByType(mSortBy, this);
                MainFragment mf = (MainFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_main_movies);
                mf.onSortByChanged(sortBy);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.v(log, "onResume");
    }

    @Override
    public void onMovieSelected(Movie movie, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFetchMoviesFinished() {
        MainFragment mf = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_main_movies);
        mf.onFetchFinished();
    }
}
