package com.example.hung.popmovies.net;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.hung.popmovies.BuildConfig;
import com.example.hung.popmovies.R;
import com.example.hung.popmovies.Utility;
import com.example.hung.popmovies.db.MovieContract;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phoenix on 25/05/2016.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    String log = this.getClass().getSimpleName();
    Activity mActivity;
//    MovieAdapter mAdapter;

    public interface Callbacks {
        void onFetchMoviesFinished();
    }


    public FetchMoviesTask(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
        String sortByType = Utility.getSortByType(mActivity);
//
//        // Will contain the raw JSON response as a string.
//        String resultJson = null;
//
//        try{
//
////            String baseUrl = "https://api.themoviedb.org/3/movie/";
////
////            String apiKey = "api_key";
//////            String sortBy = "sort_by";
////            Uri builtUri = Uri.parse(baseUrl).buildUpon()
////                    .appendPath(sortByType)
////                    .appendQueryParameter(apiKey,BuildConfig.OPEN_MOVIE_API_KEY)
////                    .build();
////
////            URL url = new URL(builtUri.toString());
//            Log.v(log,builtUri.toString());
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//            resultJson = buffer.toString();
//        }catch (Exception e){
//            Log.e(log, "error", e);
//        }
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
                deleted = mActivity.getContentResolver().delete(uri, null, null);
                inserted = mActivity.getContentResolver().bulkInsert(uri, arrayContentValues);
            }
            Log.d(log, deleted + "Movies deleted from db");

            Log.d(log, "FetchMoviesTask complete. " + inserted + " Inserted");

            return arrayMovies;

        } catch (IOException e) {
            Log.e(log, "A problem occurred talking to the movie db ", e);
            return null;
        }
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> moviePosterPathArray) {
        super.onPostExecute(moviePosterPathArray);
        RecyclerView recyclerView = (RecyclerView) mActivity.findViewById(R.id.movie_list);
        TextView textView = (TextView) mActivity.findViewById(R.id.warning_missing_internet);

        if (moviePosterPathArray != null) {
            ((Callbacks) mActivity).onFetchMoviesFinished();
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }


    //    private ArrayList<Movie> getMoviesArrayFromJason(String resultJson){
//        final String FMT_Array = "results";
//        final String FMT_Poster_Path = "poster_path";
//        final String FMT_Id = "id";
//        final String FMT_Title = "original_title";
//        final String FMT_Overview = "overview";
//        final String FMT_UserRating = "vote_average";
//        final String FMT_ReleaseDate = "release_date";
//        final String FMT_Backdrop = "backdrop_path";
//
//        ArrayList<Movie> movieArrayFromJason = new ArrayList<>();
//        try {
//
//            Movie movie;
//            JSONObject jsonObjectResult = new JSONObject(resultJson);
//            JSONArray moviesArray = jsonObjectResult.getJSONArray(FMT_Array);
//
//            Log.v(log, moviesArray.length() + "");
////            moviePosterPathArray = new String[moviesArray.length()];
//
//            for (int i = 0 ; i<moviesArray.length();i++){
//                String poster = moviesArray.getJSONObject(i).getString(FMT_Poster_Path);
//                Long id = moviesArray.getJSONObject(i).getLong(FMT_Id);
//                String title = moviesArray.getJSONObject(i).getString(FMT_Title);
//                String overview = moviesArray.getJSONObject(i).getString(FMT_Overview);
//                String userRating = moviesArray.getJSONObject(i).getString(FMT_UserRating);
//                String releaseDate = moviesArray.getJSONObject(i).getString(FMT_ReleaseDate);
//                String backdrop = moviesArray.getJSONObject(i).getString(FMT_Backdrop);
//
//                movie = new Movie(id, title, poster, overview, userRating,releaseDate,backdrop);
//                movieArrayFromJason.fillAdapter(movie);
//
//            }
//
//        }catch (JSONException e){
//            Log.v(log, "error: ", e);
//            return  null;
//        }


//        return movieArrayFromJason;
//        return null;
//    }

}