package com.example.hung.popmovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by phoenix on 26/05/2016.
 */
public class Utility {
    private static String log = Utility.class.getSimpleName();
    public final static String MOST_POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    public final static String FAVORITES = "favorites";

    public static void SaveSortByType(String sortByType, Activity context) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String key = context.getResources().getString(R.string.sort_by);
        Log.v(log, key);
        editor.putString(key, sortByType);
        editor.commit();

    }

    public static String getSortByType(Activity context) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        String key = context.getResources().getString(R.string.sort_by);
        if (sharedPref.contains(key)) {
            return sharedPref.getString(key, MOST_POPULAR);
        } else return null;
    }

    public static String getSortByTypeById(int id){
//        String SortBy = null;
//        switch (id){
//            case R.id.sort_by_most_popular:
//                SortBy = MOST_POPULAR;
//                break;
//            case R.id.sort_by_top_rated:
//                SortBy = TOP_RATED;
//                break;
//            case R.id.sort_by_favorites:
//                SortBy = FAVORITES;
//                break;
//            default:
//                break;
//        }
        if(id == R.id.sort_by_most_popular){
            return MOST_POPULAR;
        }else if (id == R.id.sort_by_top_rated){
            return TOP_RATED;
        }else if (id == R.id.sort_by_favorites){
            return FAVORITES;
        }else {
            Log.v(log, "error!!");
            return null;
        }

    }
}
