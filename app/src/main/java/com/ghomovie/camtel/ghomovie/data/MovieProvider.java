package com.ghomovie.camtel.ghomovie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghomovie.camtel.ghomovie.model.Movie;

public class MovieProvider extends ContentProvider {
    private static final String TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        //add a code for each type of uri
        matcher.addURI(authority,MovieContract.MovieEntry.TABLE_MOVIES, MOVIE);
        matcher.addURI(authority,MovieContract.MovieEntry.TABLE_MOVIES+"/#",MOVIE_WITH_ID);

        return matcher;
    }

    private MoviesDBHelper mOpenHelper;

    // Codes for the UriMatcher /////
    private static final int MOVIE=100;
    private static final int MOVIE_WITH_ID = 200;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return  retCursor;
            }
            case MOVIE_WITH_ID:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        MovieContract.MovieEntry._ID+"= ?",
                        new String[]{ String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: "+uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:{
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            }
            case MOVIE_WITH_ID:{
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: "+uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                long _id = db.insert(MovieContract.MovieEntry.TABLE_MOVIES,null,values);
                //insert unless it is already contained in the database
                if(_id>0){
                    returnUri = MovieContract.MovieEntry.buildMoviesuri(_id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into: "+uri);
                }
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: "+uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match){
            case MOVIE:
                numDeleted = db.delete(MovieContract.MovieEntry.TABLE_MOVIES,selection,selectionArgs);
                //reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME= '"+
                                MovieContract.MovieEntry.TABLE_MOVIES+"'");
                break;
            case MOVIE_WITH_ID:
                numDeleted = db.delete(MovieContract.MovieEntry.TABLE_MOVIES,
                        MovieContract.MovieEntry._ID+" = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                //reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"+
                            MovieContract.MovieEntry.TABLE_MOVIES+"'");
                break;
            default:
                throw new UnsupportedOperationException("unkwon uri: "+uri);
        }
        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if(values == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                numUpdated = db.update(MovieContract.MovieEntry.TABLE_MOVIES,
                                       values,
                                       selection,
                                       selectionArgs);
                break;
            }
            case MOVIE_WITH_ID:{
                numUpdated = db.update(MovieContract.MovieEntry.TABLE_MOVIES,
                                        values,
                                        MovieContract.MovieEntry._ID+"= ?",
                                       new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: "+uri);
            }
        }
        if(numUpdated>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                //allows for multiple transactions
                db.beginTransaction();

                //keep track of successful inserts
                int numInserted = 0;
                try{
                    for(ContentValues value: values){
                        if(value==null)
                            throw new IllegalArgumentException("Cannot have null content values");
                        long _id= -1;
                        try{

                        }catch (SQLiteConstraintException e){
                            Log.w(TAG,"Attempting to insert "+
                                                value.getAsString(MovieContract.MovieEntry.TITLE));
                        }
                        if(_id!=1){
                            numInserted++;
                        }
                    }
                    if(numInserted>0){
                        db.setTransactionSuccessful();
                    }
                }finally {
                    db.endTransaction();
                }
                if(numInserted>0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return numInserted;
             default:
                 return super.bulkInsert(uri, values);
        }

    }
}
