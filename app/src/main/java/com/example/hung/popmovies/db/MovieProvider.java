package com.example.hung.popmovies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.hung.popmovies.db.MovieContract.MovieEntry;


/**
 * Created by phoenix on 28/05/2016.
 */
public class MovieProvider extends ContentProvider {

    String log = this.getClass().getSimpleName();
    private MovieDBHelper mDbHelper;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, "movies/*", MOVIES);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, "movie/*/#", MOVIE_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDBHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return (db != null);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;
        long rowID = db.insert(MovieEntry.TABLE_NAME, "", contentValues);

        if (rowID > 0)
        {
            returnUri = ContentUris.withAppendedId(
                    MovieEntry.CONTENT_URI_MOVIES,
                    rowID);
        }else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
//        int testMatch = mUriMatcher.match(uri);
        String sortBy = MovieEntry.getSortByTypeFromUri(uri);
//        Log.v(log, uri + "");
//        Log.v(log, testMatch + "");
        switch (mUriMatcher.match(uri)){
            //"movies"
            case MOVIES:
                selection = MovieEntry.TABLE_NAME + "."+
                        MovieEntry.SORT_BY +
                        " = ?";
                selectionArgs = new String[]{sortBy};

                retCursor = mDbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            case MOVIE_ID:
                Long movieId = MovieEntry.getMovieIdFromUri(uri);
                selection = MovieEntry.TABLE_NAME + "."+
                        MovieEntry.SORT_BY + " = ? AND " +
                        MovieEntry.MOVIE_ID +
                        " = ?";
                selectionArgs = new String[]{sortBy,Long.toString(movieId)};

                retCursor = mDbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted;
        String sortBy = MovieEntry.getSortByTypeFromUri(uri);

        switch (match){
            case MOVIES:
                selection = MovieEntry.TABLE_NAME + "."+
                        MovieEntry.SORT_BY +
                        " = ?";
                selectionArgs = new String[]{sortBy};
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                Long movieId = MovieEntry.getMovieIdFromUri(uri);
                selection = MovieEntry.TABLE_NAME + "."+
                        MovieEntry.SORT_BY + " = ? AND " +
                        MovieEntry.MOVIE_ID +
                        " = ?";
                selectionArgs = new String[]{sortBy,Long.toString(movieId)};
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues contentValue : contentValues) {
                        long _id = db.insert(MovieEntry.TABLE_NAME, null, contentValue);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if(returnCount>0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;
            default:
                return super.bulkInsert(uri, contentValues);
        }
    }
}
