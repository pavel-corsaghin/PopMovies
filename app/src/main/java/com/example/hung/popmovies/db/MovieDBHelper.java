package com.example.hung.popmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.hung.popmovies.db.MovieContract.MovieEntry;

/**
 * Created by phoenix on 28/05/2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQU_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.SORT_BY + " TEXT NOT NULL, " +
                MovieEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieEntry.MOVIE_OVERVIEW + " TEXT NOT NULL, "+
                MovieEntry.MOVIE_USER_RATING + " TEXT NOT NULL, "+
                MovieEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL, "+
                MovieEntry.MOVIE_BACKDROP_PATH + " TEXT NOT NULL)";
        db.execSQL(SQU_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
