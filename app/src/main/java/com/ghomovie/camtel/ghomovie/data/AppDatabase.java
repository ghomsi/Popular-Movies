package com.ghomovie.camtel.ghomovie.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ghomovie.camtel.ghomovie.model.Movie;

@Database(entities = {Movie.class}, version =3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"movie_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyIntance(){
        INSTANCE = null;
    }

    public abstract MovieDAO movieDAO();
}
