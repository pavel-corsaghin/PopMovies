package com.example.hung.popmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by phoenix on 27/05/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.hung.popmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI_MOVIES = BASE_CONTENT_URI.buildUpon()
                .appendPath("movies")
                .build();
        public static final Uri CONTENT_URI_MOVIE = BASE_CONTENT_URI.buildUpon()
                .appendPath("movie")
                .build();

        public static final String TABLE_NAME = "table_popular";
        public static final String SORT_BY = "sort_by";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "original_title";
        public static final String MOVIE_POSTER = "poster_path";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_USER_RATING = "vote_average";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_BACKDROP_PATH = "backdrop_path";

        public static final int COL_SORT_BY = 1;
        public static final int COL_MOVIE_ID= 2;
        public static final int COL_MOVIE_TITLE = 3;
        public static final int COL_MOVIE_POSTER = 4;
        public static final int COL_MOVIE_OVERVIEW = 5;
        public static final int COL_MOVIE_USER_RATING = 6;
        public static final int COL_MOVIE_RELEASE_DATE = 7;
        public static final int COL_MOVIE_BACKDROP_PATH = 8;

        public static Uri getMovieUriFromIdAndSortByType(String sortBy,long movieId) {
            return CONTENT_URI_MOVIE.buildUpon()
                    .appendPath(sortBy)
                    .appendPath(Long.toString(movieId))
                    .build();
        }

        public static Uri getMoviesUriFromSortByType (String sortBy){
            return CONTENT_URI_MOVIES.buildUpon().appendPath(sortBy).
                    build();
        }

        public static long getMovieIdFromUri(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static String getSortByTypeFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

}
