package com.iaz.filmesfamosos.presentation.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.iaz.filmesfamosos.Constants;
import com.iaz.filmesfamosos.R;
import com.iaz.filmesfamosos.database.AppDatabase;
import com.iaz.filmesfamosos.models.MovieDetailsViewModel;
import com.iaz.filmesfamosos.models.MovieDetailsViewModelFactory;
import com.iaz.filmesfamosos.models.ResponseReview;
import com.iaz.filmesfamosos.models.ResponseVideo;
import com.iaz.filmesfamosos.models.MovieModel;
import com.iaz.filmesfamosos.databinding.ActivityMovieDetailsBinding;
import com.iaz.filmesfamosos.models.ResultsReview;
import com.iaz.filmesfamosos.networkUtils.TheMovieDBApi;
import com.iaz.filmesfamosos.presentation.ui.adapters.ReviewsAdapter;
import com.iaz.filmesfamosos.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieModel movie;
    private ActivityMovieDetailsBinding binding;
    private ReviewsAdapter reviewsAdapter;
    private AppDatabase mDb;
    private boolean canClick = true;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        mDb = AppDatabase.getInstance(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.details);
        }

        if (getIntent().getExtras() != null) {
            String movieId = getIntent().getExtras().getString(Constants.MOVIE_ID);

            MovieDetailsViewModelFactory factory = new MovieDetailsViewModelFactory(mDb, movieId);
            final MovieDetailsViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);
            viewModel.getMovieDetails().observe(this, new Observer<MovieModel>() {
                @Override
                public void onChanged(@Nullable MovieModel movieModel) {
                    movie = movieModel;
                    viewModel.getMovieDetails().removeObserver(this);

                    if (movie != null) {

                        if (movie.getVideoPath() != null && !movie.getVideoPath().isEmpty())
                            setVideoPlayer(movie.getVideoPath());
                        else
                            getVideo(movie.getId());

                        getReviews(movie.getId());
                        binding.setMovie(movie);

                        if (movie.getPoster_path() != null && !movie.getPoster_path().isEmpty())
                            Picasso.with(MovieDetailsActivity.this).load(Constants.POSTER_IMAGE_BASE_URL + movie.getPoster_path()).into(binding.ivPoster);

                        setFavoriteButton();
                    }
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

    private void getVideo(String movieId) {
        TheMovieDBApi.getVideo(movieId, new Callback<ResponseVideo>() {
            @Override
            public void onResponse(Call<ResponseVideo> call, retrofit2.Response<ResponseVideo> response) {

                if (response.body() != null &&
                        response.body().getResults() != null &&
                        !response.body().getResults().isEmpty() &&
                        response.body().getResults().get(0) != null) {

                    Completable.fromAction(() -> AppDatabase.getInstance(MovieDetailsActivity.this).moviesDao().setVideoPath(movieId, response.body().getResults().get(0).getKey())).subscribeOn(Schedulers.computation()).subscribe();
                    setVideoPlayer(response.body().getResults().get(0).getKey());
                }
            }

            @Override
            public void onFailure(Call<ResponseVideo> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, R.string.error_recovering_data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReviews(String movieId) {
        TheMovieDBApi.getReviews(movieId, new Callback<ResponseReview>() {
            @Override
            public void onResponse(Call<ResponseReview> call, retrofit2.Response<ResponseReview> response) {

                if (response.body() != null &&
                        response.body().getResults() != null &&
                        !response.body().getResults().isEmpty()) {

                    binding.divider.setVisibility(View.VISIBLE);
                    binding.llReviews.setVisibility(View.VISIBLE);

                    setAdapter(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ResponseReview> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, R.string.error_recovering_data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(ArrayList<ResultsReview> results) {

        if (reviewsAdapter == null)
            reviewsAdapter = new ReviewsAdapter(MovieDetailsActivity.this, results);
        else {
            reviewsAdapter.setNewList(results);
            reviewsAdapter.notifyDataSetChanged();
        }

        binding.recyclerReviews.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this));

        Utilities.BottomOffsetDecoration bottomOffsetDecoration = new Utilities.BottomOffsetDecoration(8);
        binding.recyclerReviews.addItemDecoration(bottomOffsetDecoration);
        binding.recyclerReviews.setAdapter(reviewsAdapter);
    }

    private void setVideoPlayer(String videoUrl) {
        movie.setVideoPath(videoUrl);
        binding.llPlayTrailer.setVisibility(View.VISIBLE);
        binding.llPlayTrailer.setOnClickListener(v -> {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movie.getVideoPath()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + movie.getVideoPath()));
            try {
                MovieDetailsActivity.this.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                MovieDetailsActivity.this.startActivity(webIntent);
            }
        });
    }

    private void setFavoriteButton() {

        if (movie.isFavorite()) {
            binding.ivTurnFavorite.setImageDrawable(getDrawable(R.drawable.ic_star));
            binding.tvTurnFavorite.setText(getString(R.string.unfavorite_movie));
            binding.llTurnFavorite.setOnClickListener(v -> unfavoriteMovie());
        }
        else {
            binding.llTurnFavorite.setOnClickListener(v -> favoriteMovie());
        }
    }

    private void favoriteMovie() {
        if (canClick) {
            canClick = false;
            mDisposable.add(Completable.fromAction(() -> mDb.moviesDao().setFavorite(movie.getId()))
                    .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                binding.ivTurnFavorite.setImageDrawable(getDrawable(R.drawable.ic_star));
                canClick = true;
                movie.setFavorite(true);
                binding.tvTurnFavorite.setText(getString(R.string.unfavorite_movie));
                binding.llTurnFavorite.setOnClickListener(v -> unfavoriteMovie());
            }, throwable -> canClick = true));
        }
    }

    private void unfavoriteMovie() {
        if (canClick) {
            canClick = false;
            mDisposable.add(Completable.fromAction(() -> mDb.moviesDao().unfavorite(movie.getId()))
                    .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                binding.ivTurnFavorite.setImageDrawable(getDrawable(R.drawable.ic_star_border));
                canClick = true;
                movie.setFavorite(false);
                binding.tvTurnFavorite.setText(getString(R.string.turn_favorite));
                binding.llTurnFavorite.setOnClickListener(v -> favoriteMovie());
            }, throwable -> canClick = true));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
