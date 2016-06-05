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
 * Created by phoenix on 01/06/2016.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {
    String log = FetchTrailersTask.class.getSimpleName();
    Callbacks mCallback;

    public FetchTrailersTask(Callbacks callbacks) {
        mCallback = callbacks;
    }

    public interface Callbacks {
        void onFetchTrailersFinished(ArrayList<Trailer> trailers);
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        String movieId = params[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices service = retrofit.create(ApiServices.class);
        Call<Trailers> call = service.getTrailers(movieId, BuildConfig.OPEN_MOVIE_API_KEY);

        try {
            Response<Trailers> response = call.execute();
            Trailers trailers = response.body();
            return trailers.getTrailers();

        } catch (IOException e) {
            Log.e(log, "A problem occurred talking to the movie db ", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);
        if(trailers!=null){
            mCallback.onFetchTrailersFinished(trailers);
        }
        else {
            Log.v(log, "A problem occurred talking to the movie db");
        }
    }
}
