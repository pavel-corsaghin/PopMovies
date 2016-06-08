package com.example.hung.popmovies.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.hung.popmovies.BuildConfig;
import com.example.hung.popmovies.Utility;
import com.example.hung.popmovies.db.MovieContract;
import com.example.hung.popmovies.net.ApiServices;
import com.example.hung.popmovies.net.Movie;
import com.example.hung.popmovies.net.Movies;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phoenix on 6/5/16.
 */
public class MovieService extends IntentService {
    static String log = MovieService.class.getSimpleName();
    public MovieService(){
         super("Movie");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
//        String sortByType = intent.getStringExtra(Utility.SORT_BY);
//        Log.v(log, sortByType);
        String[] sortBy = new String[]{Utility.MOST_POPULAR, Utility.TOP_RATED};
        for (String sortByType:sortBy){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiServices service = retrofit.create(ApiServices.class);
//        Log.v(log, sortByType);
            Call<Movies> call = service.getMovies(sortByType, BuildConfig.OPEN_MOVIE_API_KEY);

            try {
                Response<Movies> response = call.execute();
                Movies movies = response.body();

                ArrayList<Movie> arrayMovies = movies.getMovies();
                ContentValues[] arrayContentValues = new ContentValues[arrayMovies.size()];

                if (arrayMovies.size() != 0) {


                    for (int i = 0; i < arrayMovies.size(); i++) {
                        Movie movie = arrayMovies.get(i);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MovieContract.MovieEntry.SORT_BY, sortByType);
//                    contentValues.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
//                    contentValues.put(MovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
//                    contentValues.put(MovieContract.MovieEntry.MOVIE_POSTER, movie.getPoster());
//                    contentValues.put(MovieContract.MovieEntry.MOVIE_OVERVIEW, movie.getOverview());
//                    contentValues.put(MovieContract.MovieEntry.MOVIE_USER_RATING, movie.getUserRating());
//                    contentValues.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE, movie.getReleaseDate());
//                    contentValues.put(MovieContract.MovieEntry.MOVIE_BACKDROP_PATH, movie.getBackdrop());
//                    arrayContentValues[i] = contentValues;

                        arrayContentValues[i] = Utility.getContentValueFromMovie(movie, sortByType);
                    }
                }

                int inserted = 0;
                int deleted = 0;

                // update to database
                if (arrayContentValues.length > 0) {
                    Uri uri = MovieContract.MovieEntry.getMoviesUriFromSortByType(sortByType);
                    deleted = getContentResolver().delete(uri, null, null);
                    inserted = getContentResolver().bulkInsert(uri, arrayContentValues);
                }
                Log.d(log, deleted + "Movies deleted from db");

                Log.d(log, "FetchMoviesTask complete. " + inserted + " Inserted");


            } catch (IOException e) {
                Log.e(log, "A problem occurred talking to the movie db ", e);
            }
        }



    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, MovieService.class);
//            sendIntent.putExtra(Utility.SORT_BY, intent.getStringExtra(Utility.SORT_BY));
            context.startService(sendIntent);
            Log.v(log, "onAlarmReceiver");
        }
    }

}
