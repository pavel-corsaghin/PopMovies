package com.example.hung.popmovies;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hung.popmovies.net.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by phoenix on 25/05/2016.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    String log_tag = this.getClass().getSimpleName();
    Activity mActivity;
    MovieAdapter mAdapter;

    public FetchMoviesTask(Activity activity, MovieAdapter adapter){
        mActivity = activity;
        mAdapter = adapter;
    }
    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String resultJson = null;

        try{

            String baseUrl = "https://api.themoviedb.org/3/movie/popular?";
            String ApiKey = "api_key";
            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(ApiKey,BuildConfig.OPEN_MOVIE_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            resultJson = buffer.toString();
        }catch (Exception e){
            Log.e(log_tag, "error", e);
        }
        return getMoviesArrayFromJason(resultJson);
    }

    private ArrayList<Movie> getMoviesArrayFromJason(String resultJson){
        final String FMT_Array = "results";
        final String FMT_Poster_Path = "poster_path";
        final String FMT_Id = "id";
        final String FMT_Title = "original_title";
        final String FMT_Overview = "overview";
        final String FMT_UserRating = "vote_average";
        final String FMT_ReleaseDate = "release_date";
        final String FMT_Backdrop = "backdrop_path";

        ArrayList<Movie> movieArrayFromJason = new ArrayList<>();
        try {

            Movie movie;
            JSONObject jsonObjectResult = new JSONObject(resultJson);
            JSONArray moviesArray = jsonObjectResult.getJSONArray(FMT_Array);

            Log.v(log_tag, moviesArray.length() + "");
//            moviePosterPathArray = new String[moviesArray.length()];

            for (int i = 0 ; i<moviesArray.length();i++){
//                moviePosterPathArray.add(moviesArray.getJSONObject(i)
//                        .getString(FMT_POSTER_PATH));
                String poster = moviesArray.getJSONObject(i).getString(FMT_Poster_Path);
                Long id = moviesArray.getJSONObject(i).getLong(FMT_Id);
                String title = moviesArray.getJSONObject(i).getString(FMT_Title);
                String overview = moviesArray.getJSONObject(i).getString(FMT_Overview);
                String userRating = moviesArray.getJSONObject(i).getString(FMT_UserRating);
                String releaseDate = moviesArray.getJSONObject(i).getString(FMT_ReleaseDate);
                String backdrop = moviesArray.getJSONObject(i).getString(FMT_Backdrop);
                movie = new Movie(id, title, poster, overview, userRating,releaseDate,backdrop);

                movieArrayFromJason.add(movie);

            }

        }catch (JSONException e){
            Log.v(log_tag, "error: ", e);
            return  null;
        }
//        Log.v(log_tag, moviePosterPathArray.get(0));
        return movieArrayFromJason;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviePosterPathArray) {
        super.onPostExecute(moviePosterPathArray);
//            Movie mv = moviePosterPathArray.get(0);
//            Log.v(log_tag, mv.getPosterUrl());
        mAdapter.add(moviePosterPathArray);
    }
}