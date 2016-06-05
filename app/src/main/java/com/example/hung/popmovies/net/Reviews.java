package com.example.hung.popmovies.net;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by phoenix on 02/06/2016.
 */
public class Reviews {
    @SerializedName("results")
    private ArrayList<Review> mReviews;

    public ArrayList<Review> getReviews(){
        return mReviews;
    }
}
