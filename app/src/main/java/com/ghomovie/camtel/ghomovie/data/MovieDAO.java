package com.ghomovie.camtel.ghomovie.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ghomovie.camtel.ghomovie.model.Movie;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDAO {
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM movie WHERE id =:movieIds")
    LiveData<List<Movie>> loadAllByIds(long movieIds);

    /*
    * @Query("SELECT * FROM movie WHERE id IN (:movieIds)")
    LiveData<List<Movie>> loadAllByIds(int[] movieIds);*/

    @Query("SELECT * FROM movie WHERE title LIKE :title LIMIT 100")
    Movie findByName(String title);

    @Insert(onConflict = REPLACE)
    void insertAll(Movie...movies);

    @Delete
    void delete(Movie movie);
}
