package com.example.hung.popmovies.net;

import com.google.gson.annotations.SerializedName;

/**
 * Created by phoenix on 01/06/2016.
 */
public class Trailer {
    private final String log = Trailer.class.getSimpleName();

    public Trailer(String id, String key, String type, String site, String name){
        mId = id;
        mKey = key;
        mType = type;
        mSite = site;
        mName = name;
    }

    @SerializedName("id")
    String mId;

    @SerializedName("key")
    String mKey;

    @SerializedName("type")
    String mType;

    @SerializedName("site")
    String mSite;

    @SerializedName("name")
    String mName;

    public String getTrailerId(){
        return mId;
    }

    public String getTrailerKey(){
        return mKey;
    }

    public String getTrailerType(){
        return mType;
    }

    public String getTrailerSite(){
        return mSite;
    }

    public String getTrailerUrl(){
        return  "https://www.youtube.com/watch?v=" + mKey;
    }

    public String getName(){
        return mName;
    }

}
