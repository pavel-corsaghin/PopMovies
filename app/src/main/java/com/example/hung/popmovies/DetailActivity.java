package com.example.hung.popmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class DetailActivity extends ActionBarActivity {

    private final String DF_TAG = DetailActivity.class.getSimpleName();
    private final String log = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putSerializable(Utility.MOVIE,getIntent().getSerializableExtra(Utility.MOVIE));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment,DF_TAG)
                    .commit();
        }
    }


}
