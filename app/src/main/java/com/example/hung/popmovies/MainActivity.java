package com.example.hung.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    String mSortBy = Utility.MOST_POPULAR;

    private String log = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utility.SaveSortByType(mSortBy, this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new FragmentMain())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        if (Utility.getSortByType(this) != null) {
            mSortBy = Utility.getSortByType(this);
        }
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
        int id = item.getItemId();
        if (id != R.id.menu_settings) {
            String SortBy = Utility.getSortByTypeById(id);
            if (!mSortBy.equals(SortBy)) {
                item.setChecked(true);
                mSortBy = SortBy;
                Utility.SaveSortByType(mSortBy, this);
                updateMovies();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new FragmentMain())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(log, "onResume");
    }
}
