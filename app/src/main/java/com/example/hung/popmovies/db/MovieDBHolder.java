package com.example.hung.popmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hung.popmovies.db.MovieContract.MovieEntry;
import com.example.hung.popmovies.net.Movie;

import java.util.ArrayList;


/**
 * Created by phoenix on 28/05/2016.
 */

// class này không dùng nữa =))
public class MovieDBHolder {
    private String log = MovieDBHolder.class.getSimpleName();
    private MovieDBHelper mDbHelper;

    public MovieDBHolder(Context context) {
        mDbHelper = new MovieDBHelper(context);
    }

    public void insertMovie(ArrayList<Movie> movies, String sortBy) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(movies!=null){
            for(Movie movie :movies){
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieEntry.SORT_BY, sortBy);
                contentValues.put(MovieEntry.MOVIE_ID, movie.getId());
                contentValues.put(MovieEntry.MOVIE_TITLE, movie.getTitle());
                contentValues.put(MovieEntry.MOVIE_POSTER, movie.getPoster());
                contentValues.put(MovieEntry.MOVIE_OVERVIEW, movie.getOverview());
                contentValues.put(MovieEntry.MOVIE_USER_RATING, movie.getUserRating());
                contentValues.put(MovieEntry.MOVIE_RELEASE_DATE, movie.getReleaseDate());
                contentValues.put(MovieEntry.MOVIE_BACKDROP_PATH, movie.getBackdrop());

                db.insert(MovieEntry.TABLE_NAME, null, contentValues);
            }

            Log.v(log, movies.size() + " movies is inserted!");
        } else {
            Log.v(log, "nothing is inserted!");

        }
        db.close();
    }

    public Cursor queryMovie(String sortBy) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                MovieEntry.TABLE_NAME + " WHERE " +
                MovieEntry.SORT_BY + "=?",
                new String[]{sortBy});
//        db.close();
        return cursor;
    }


}
