package com.ghomovie.camtel.ghomovie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ghomovie.camtel.ghomovie.model.Movie;

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String TAG = MoviesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 12;


    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = " CREATE TABLE "+
                MovieContract.MovieEntry.TABLE_MOVIES+"("+
                    MovieContract.MovieEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    MovieContract.MovieEntry.TITLE+" TEXT NOT NULL,"+
                    MovieContract.MovieEntry.LANG+" TEXT NOT NULL,"+
                    MovieContract.MovieEntry.OVERVIEW+" TEXT NOT NULL,"+
                    MovieContract.MovieEntry.RELEASE_DATE+" TEXT NOT NULL,"+
                    MovieContract.MovieEntry.VOTE_AVERAGE+" DOUBLE NOT NULL,"+
                    MovieContract.MovieEntry.IMAGE+" TEXT NOT NULL); ";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,"Upgrading database from version"+oldVersion+" to "+newVersion);

        //Drop the table
        db.execSQL(" DROP TABLE IF EXISTS "+MovieContract.MovieEntry.TABLE_MOVIES);
        db.execSQL(" DELETE FROM SQLITE_SEQUENCE WHERE NAME= '"+MovieContract.MovieEntry.TABLE_MOVIES+"'");

        //.re-create database
        onCreate(db);
    }

    public boolean insertData(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.TITLE,movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.LANG,movie.getLg());
        contentValues.put(MovieContract.MovieEntry.OVERVIEW,movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.RELEASE_DATE,movie.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.VOTE_AVERAGE,movie.getVote_average());
        contentValues.put(MovieContract.MovieEntry.IMAGE, movie.getImage());

        long result = db.insert(MovieContract.MovieEntry.TABLE_MOVIES,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+MovieContract.MovieEntry.TABLE_MOVIES,null);
        return  res;
    }
}
