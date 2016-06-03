/*
 * Copyright 2016.  Dmitry Malkovich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.hung.popmovies.net;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.example.hung.popmovies.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//public class Movie implements Parcelable {
public class Movie implements Serializable {

    public static final String log = Movie.class.getSimpleName();

    @SerializedName("id")
    private long mId;
    @SerializedName("original_title")
    private String mTitle;
    @SerializedName("poster_path")
    private String mPoster;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("vote_average")
    private String mUserRating;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("backdrop_path")
    private String mBackdrop;

    // Only for createFromParcel
    private Movie() {
    }

    public Movie(long id, String title, String poster, String overview, String userRating,
                 String releaseDate, String backdrop) {
        mId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mBackdrop = backdrop;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public long getId() {
        return mId;
    }

    @Nullable
    public String getPosterUrl() {
        if (mPoster != null && !mPoster.isEmpty()) {
            return "http://image.tmdb.org/t/p/w342" + mPoster;
        }
        return null;
    }

    public String getPoster() {
        return mPoster;
    }


    public String getReleaseDate() {
        return mReleaseDate;
    }

    @Nullable
    public String getOverview() {
        return mOverview;
    }

    @Nullable
    public String getUserRating() {
        return mUserRating;
    }

    @Nullable
    public String getBackdropUrl(Context context) {
        if (mBackdrop != null && !mBackdrop.isEmpty()) {
            return R.string.base_url_for_downloading_backdrop + mBackdrop;
        }
        return null;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

//    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
//        public Movie createFromParcel(Parcel source) {
//            Movie movie = new Movie();
//            movie.mId = source.readLong();
//            movie.mTitle = source.readString();
//            movie.mPoster = source.readString();
//            movie.mOverview = source.readString();
//            movie.mUserRating = source.readString();
//            movie.mReleaseDate = source.readString();
//            movie.mBackdrop = source.readString();
//            return movie;
//        }
//
//        public Movie[] newArray(int size) {
//            return new Movie[size];
//        }
//    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mOverview);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mBackdrop);
    }
}
