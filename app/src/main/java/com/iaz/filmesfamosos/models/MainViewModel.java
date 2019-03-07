package com.iaz.filmesfamosos.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.iaz.filmesfamosos.database.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    final private LiveData<List<MovieModel>> popularMovies;
    final private LiveData<List<MovieModel>> topRatedMovies;
    final private LiveData<List<MovieModel>> upcomingMovies;
    final private LiveData<List<MovieModel>> favoritedMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());

        popularMovies = database.moviesDao().loadPopularMovies();
        topRatedMovies = database.moviesDao().loadTopRatedMovies();
        upcomingMovies = database.moviesDao().loadUpcomingMovies();
        favoritedMovies = database.moviesDao().loadFavoritedMovies();
    }

    public LiveData<List<MovieModel>> getPopular() {
        return popularMovies;
    }

    public LiveData<List<MovieModel>> getTopRated() {
        return topRatedMovies;
    }

    public LiveData<List<MovieModel>> getUpcoming() {
        return upcomingMovies;
    }

    public LiveData<List<MovieModel>> getFavorites() {
        return favoritedMovies;
    }
}
