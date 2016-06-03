package com.example.hung.popmovies.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by phoenix on 02/06/2016.
 */
public class Review {

    @SerializedName("id")
    private String mId;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    public Review(String id, String author, String content){
        mId = id;
        mAuthor = author;
        mContent = content;
    }

    public String getId(){
        return mId;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getContent(){
        return mContent;
    }
}
