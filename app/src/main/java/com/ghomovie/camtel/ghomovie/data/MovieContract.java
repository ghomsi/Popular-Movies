package com.ghomovie.camtel.ghomovie.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.ghomovie.camtel.ghomovie.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns{
        //Table name
        public  static final String TABLE_MOVIES="movies";

        //colums
        public static final String _ID="_id";
        public static final String TITLE = "title";
        public static final String LANG = "lang";
        public static final String OVERVIEW ="overwiew";
        public static final String RELEASE_DATE ="release_date";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String IMAGE = "image";
        public static final String COLUMN_VERSION_NAME= "version_name";

        // create content uri
        public static  final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_MOVIES).build();
        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+TABLE_MOVIES;

        //create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_MOVIES;

        //for building URIs o,n insertion
        public static Uri buildMoviesuri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
