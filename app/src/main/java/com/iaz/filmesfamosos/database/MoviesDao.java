package com.iaz.filmesfamosos.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.iaz.filmesfamosos.Constants;
import com.iaz.filmesfamosos.models.MovieModel;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class MoviesDao {

    public void upsert(MovieModel movieLocal, int movieListType) {
        try {
            insertMovie(movieLocal);

        } catch (Exception e) {
            updateMovie(movieLocal.getId(), movieLocal.getVote_average(), movieLocal.getTitle(), movieLocal.getOverview(), movieLocal.getRelease_date(), movieLocal.getPoster_path());
        }
        setMovieListType(movieLocal.getId(), movieListType);
    }

    @Query("SELECT * FROM movie ORDER BY release_date ASC")
    public abstract LiveData<List<MovieModel>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE isPopular = 1")
    public abstract LiveData<List<MovieModel>> loadPopularMovies();

    @Query("SELECT * FROM movie WHERE isTopRated = 1")
    public abstract LiveData<List<MovieModel>> loadTopRatedMovies();

    @Query("SELECT * FROM movie WHERE isUpcoming = 1")
    public abstract LiveData<List<MovieModel>> loadUpcomingMovies();

    @Query("SELECT * FROM movie WHERE isFavorite = 1")
    public abstract LiveData<List<MovieModel>> loadFavoritedMovies();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertMovie(MovieModel movieLocal);

    @Query("UPDATE movie SET " +
            "vote_average = :voteAverage, title = :title, overview= :overview, release_date=:releaseDate, " +
            "poster_path=:posterPath " +
            "WHERE id = :id")
    public abstract void updateMovie(String id, String voteAverage, String title, String overview, String releaseDate, String posterPath);

    @Delete
    public abstract void deleteMovie(MovieModel movieLocal);

    @Query("SELECT * FROM movie WHERE id = :mMovieId")
    public abstract LiveData<MovieModel> loadMovieById(String mMovieId);

    public void insertMovieLocalList(ArrayList<MovieModel> movies, int movieListType) {
        if (!movies.isEmpty()) {
            for (MovieModel movie : movies) {
                upsert(movie, movieListType);
            }
        }
    }

    private void setMovieListType(String movieId, int movieListType) {
        switch (movieListType) {
            case Constants.POPULAR_MOVIES:
                setPopular(movieId);
                break;
            case Constants.TOP_RATED_MOVIES:
                setTopRated(movieId);
                break;
            case Constants.UPCOMING_MOVIES:
                setUpcoming(movieId);
                break;
            case Constants.FAVORITED_MOVIES:
                setFavorite(movieId);
                break;
        }
    }

    @Query("UPDATE movie " +
            "SET isPopular = 1 " +
            "WHERE id=:movieId")
    public abstract void setPopular(String movieId);

    @Query("UPDATE movie " +
            "SET isTopRated = 1 " +
            "WHERE id=:movieId")
    public abstract void setTopRated(String movieId);

    @Query("UPDATE movie " +
            "SET isUpcoming = 1 " +
            "WHERE id=:movieId")
    public abstract void setUpcoming(String movieId);

    @Query("UPDATE movie " +
            "SET isFavorite = 1 " +
            "WHERE id=:movieId")
    public abstract void setFavorite(String movieId);

    @Query("UPDATE movie " +
            "SET isFavorite = 0 " +
            "WHERE id=:movieId")
    public abstract void unfavorite(String movieId);

    @Query("UPDATE movie " +
            "SET videoPath = :videoPath " +
            "WHERE id=:movieId")
    public abstract void setVideoPath(String movieId, String videoPath);

}
