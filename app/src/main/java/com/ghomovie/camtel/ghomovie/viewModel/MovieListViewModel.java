package com.ghomovie.camtel.ghomovie.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ghomovie.camtel.ghomovie.data.AppDatabase;
import com.ghomovie.camtel.ghomovie.model.Movie;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {

    private final LiveData<List<Movie>> itemAndMovieList;
    private AppDatabase appDatabase;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        this.itemAndMovieList = appDatabase.movieDAO().getAll();
    }


    public LiveData<List<Movie>> getItemAndMovieList() {
        return itemAndMovieList;
    }

    public LiveData<List<Movie>> getItemAndMovieListbYid(long id) {
        return appDatabase.movieDAO().loadAllByIds(id);
    }

    public void addItem(Movie movie){
        new AddAsyncTask(appDatabase).execute(movie);
    }

    public void deleteItem(Movie movie){
        new DeleteAsyncTask(appDatabase).execute(movie);
    }




    private static class DeleteAsyncTask extends AsyncTask<Movie,Void,Void>{
        private AppDatabase db;

        DeleteAsyncTask(AppDatabase appDatabase){
            db=appDatabase;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            db.movieDAO().delete(movies[0]);
            return null;
        }
    }

    private static class AddAsyncTask extends AsyncTask<Movie,Void,Void>{
        private AppDatabase db;

        AddAsyncTask(AppDatabase appDatabase){
            db=appDatabase;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            db.movieDAO().insertAll(movies[0]);
            return null;
        }
    }
}
