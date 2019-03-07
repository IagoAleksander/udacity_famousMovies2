package com.iaz.filmesfamosos.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.iaz.filmesfamosos.database.AppDatabase;

public class MovieDetailsViewModel extends ViewModel {

    final private LiveData<MovieModel> movie;

    public MovieDetailsViewModel(AppDatabase database, String mMovieId) {
        movie = database.moviesDao().loadMovieById(mMovieId);
    }

    public LiveData<MovieModel> getMovieDetails() {
        return movie;
    }
}
