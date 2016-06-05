package com.example.hung.popmovies.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by phoenix on 26/05/2016.
 */
public interface ApiServices {
    @GET("3/movie/{sort_by}")
    Call<Movies> getMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<Trailers> getTrailers(@Path("id") String movieId, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") String movieId, @Query("api_key") String apiKey);

}