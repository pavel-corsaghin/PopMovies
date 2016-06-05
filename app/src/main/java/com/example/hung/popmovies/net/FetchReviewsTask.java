package com.example.hung.popmovies.net;

import android.os.AsyncTask;
import android.util.Log;

import com.example.hung.popmovies.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phoenix on 02/06/2016.
 */
public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {

    String log = FetchReviewsTask.class.getSimpleName();

    Callbacks mCallbacks;

    public interface Callbacks{
        void onFetchReviewsFinished(ArrayList<Review> reviews);
    }

    public FetchReviewsTask(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        String movieId = params[0];
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices service = retrofit.create(ApiServices.class);
        Call<Reviews> call = service.getReviews(movieId, BuildConfig.OPEN_MOVIE_API_KEY);

        try {
            Response<Reviews> response = call.execute();
            Reviews reviews = response.body();
            return reviews.getReviews();

        } catch (IOException e) {
            Log.e(log, "A problem occurred talking to the movie db ", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
        mCallbacks.onFetchReviewsFinished(reviews);
    }
}
