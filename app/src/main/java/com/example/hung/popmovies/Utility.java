package com.example.hung.popmovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.example.hung.popmovies.db.MovieContract.MovieEntry;
import com.example.hung.popmovies.net.Movie;

import java.util.ArrayList;

/**
 * Created by phoenix on 26/05/2016.
 */
public class Utility {
    private static String log = Utility.class.getSimpleName();
    public final static String MOST_POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    public final static String FAVORITES = "favorites";
    public static final float POSTER_ASPECT_RATIO = 1.5f;


    public static void saveSortByType(String sortByType, Activity context) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getResources().getString(R.string.sort_by);
        editor.putString(key, sortByType);
        editor.apply();
    }

    public static String getSortByType(Activity context) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        String key = context.getResources().getString(R.string.sort_by);
        if (sharedPref.contains(key)) {
            return sharedPref.getString(key, MOST_POPULAR);
        } else return null;
    }

    public static String getSortByTypeById(int id) {
        String SortBy = null;
        switch (id) {
            case R.id.sort_by_most_popular:
                SortBy = MOST_POPULAR;
                break;
            case R.id.sort_by_top_rated:
                SortBy = TOP_RATED;
                break;
            case R.id.sort_by_favorites:
                SortBy = FAVORITES;
                break;
            default:
                Log.v(log, "error when get sortBy type from reference!");
                break;
        }
//        if(id == R.id.sort_by_most_popular){
//            return MOST_POPULAR;
//        }else if (id == R.id.sort_by_top_rated){
//            return TOP_RATED;
//        }else if (id == R.id.sort_by_favorites){
//            return FAVORITES;
//        }else {
//            Log.v(log, "error!!");
//            return null;
//        }
        return SortBy;
    }

    public static ArrayList<Movie> getMoviesFromCursor(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getLong(MovieEntry.COL_MOVIE_ID),
                        cursor.getString(MovieEntry.COL_MOVIE_TITLE),
                        cursor.getString(MovieEntry.COL_MOVIE_POSTER),
                        cursor.getString(MovieEntry.COL_MOVIE_OVERVIEW),
                        cursor.getString(MovieEntry.COL_MOVIE_USER_RATING),
                        cursor.getString(MovieEntry.COL_MOVIE_RELEASE_DATE),
                        cursor.getString(MovieEntry.COL_MOVIE_BACKDROP_PATH));
                movies.add(movie);
            } while (cursor.moveToNext());
            Log.v(log, movies.size() + " movies is converted!");
        } else {
            Log.v(log, "Cursor is empty, nothing is converted!");
        }
        return movies;
    }
}
