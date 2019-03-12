package com.iaz.filmesfamosos.presentation.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.iaz.filmesfamosos.Constants;
import com.iaz.filmesfamosos.R;
import com.iaz.filmesfamosos.database.AppDatabase;
import com.iaz.filmesfamosos.models.MainViewModel;
import com.iaz.filmesfamosos.models.MovieModel;
import com.iaz.filmesfamosos.models.Response;
import com.iaz.filmesfamosos.networkUtils.TheMovieDBApi;
import com.iaz.filmesfamosos.presentation.ui.adapters.MoviesAdapter;
import com.iaz.filmesfamosos.utils.Prefs;
import com.iaz.filmesfamosos.utils.Utilities;
import com.iaz.filmesfamosos.databinding.ActivityMainBinding;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

import static com.iaz.filmesfamosos.Constants.LIST_STATE;

public class MainActivity extends AppCompatActivity {

    private MoviesAdapter moviesAdapter;
    private ActivityMainBinding binding;
    private AppDatabase mDb;
    private int selectedButton = Constants.POPULAR_MOVIES;
    private Parcelable listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        mDb = AppDatabase.getInstance(this);

        if (savedInstanceState != null) {
            selectedButton = savedInstanceState.getInt(Constants.SORTING_ORDER, 1);
            listPosition = savedInstanceState.getParcelable(LIST_STATE);
        }

        chooseButton(selectedButton);

        binding.btPopular.setOnClickListener(view -> chooseButton(Constants.POPULAR_MOVIES));

        binding.btTopRated.setOnClickListener(view -> chooseButton(Constants.TOP_RATED_MOVIES));

        binding.btUpcoming.setOnClickListener(view -> chooseButton(Constants.UPCOMING_MOVIES));

        binding.btFavorites.setOnClickListener(view -> chooseButton(Constants.FAVORITED_MOVIES));

    }

    private void getPopular() {
        if (Utilities.needsUpdatePopular(this)) {
            TheMovieDBApi.getPopular(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                    if (response.body() != null && selectedButton == Constants.POPULAR_MOVIES)
                        setAdapter(response.body().getResults());

                    Completable.fromAction(() -> mDb.moviesDao().insertMovieLocalList(response.body().getResults(), Constants.POPULAR_MOVIES)).subscribeOn(Schedulers.computation()).subscribe();
                    Prefs.storeLastUpdatedTimePopular(MainActivity.this, System.currentTimeMillis());
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_recovering_data, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getPopular().observe(this, movieModels -> {
                if (movieModels != null && selectedButton == Constants.POPULAR_MOVIES)
                    setAdapter(new ArrayList<>(movieModels));
            });
        }
    }

    private void getTopRated() {
        if (Utilities.needsUpdateTopRated(this)) {
            TheMovieDBApi.getTopRated(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                    if (response.body() != null && selectedButton == Constants.TOP_RATED_MOVIES)
                        setAdapter(response.body().getResults());

                    Completable.fromAction(() -> mDb.moviesDao().insertMovieLocalList(response.body().getResults(), Constants.TOP_RATED_MOVIES)).subscribeOn(Schedulers.computation()).subscribe();
                    Prefs.storeLastUpdatedTimeTopRated(MainActivity.this, System.currentTimeMillis());
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_recovering_data, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getTopRated().observe(this, movieModels -> {
                if (movieModels != null && selectedButton == Constants.TOP_RATED_MOVIES)
                    setAdapter(new ArrayList<>(movieModels));
            });
        }
    }

    private void getUpcoming() {
        if (Utilities.needsUpdateUpcoming(this)) {
            TheMovieDBApi.getUpcoming(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                    if (response.body() != null && selectedButton == Constants.UPCOMING_MOVIES)
                        setAdapter(response.body().getResults());

                    Completable.fromAction(() -> mDb.moviesDao().insertMovieLocalList(response.body().getResults(), Constants.UPCOMING_MOVIES)).subscribeOn(Schedulers.computation()).subscribe();
                    Prefs.storeLastUpdatedTimeUpcoming(MainActivity.this, System.currentTimeMillis());
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_recovering_data, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getUpcoming().observe(this, movieModels -> {
                if (movieModels != null && selectedButton == Constants.UPCOMING_MOVIES)
                    setAdapter(new ArrayList<>(movieModels));
            });
        }
    }

    private void getFavorited() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, movieModels -> {
            if (movieModels != null && selectedButton == Constants.FAVORITED_MOVIES)
                setAdapter(new ArrayList<>(movieModels));
        });
    }

    private void setAdapter(ArrayList<MovieModel> results) {

        if (moviesAdapter == null)
            moviesAdapter = new MoviesAdapter(MainActivity.this, results);
        else {
            moviesAdapter.setNewList(results);
            moviesAdapter.notifyDataSetChanged();
        }

        binding.recyclerMovies.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        Utilities.BottomOffsetDecoration bottomOffsetDecoration = new Utilities.BottomOffsetDecoration(8);
        binding.recyclerMovies.addItemDecoration(bottomOffsetDecoration);
        binding.recyclerMovies.setAdapter(moviesAdapter);

        if (listPosition != null)
            binding.recyclerMovies.getLayoutManager().onRestoreInstanceState(listPosition);
    }

    private void chooseButton(int number) {
        selectedButton = number;
        switch (number) {
            case Constants.POPULAR_MOVIES:
                getPopular();
                binding.btPopular.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                binding.btTopRated.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btUpcoming.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btFavorites.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                break;
            case Constants.TOP_RATED_MOVIES:
                getTopRated();
                binding.btPopular.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btTopRated.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                binding.btUpcoming.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btFavorites.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                break;
            case Constants.UPCOMING_MOVIES:
                getUpcoming();
                binding.btPopular.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btTopRated.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btUpcoming.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                binding.btFavorites.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                break;
            case Constants.FAVORITED_MOVIES:
                getFavorited();
                binding.btPopular.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btTopRated.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btUpcoming.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.gray));
                binding.btFavorites.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Constants.SORTING_ORDER, selectedButton);
        outState.putParcelable(LIST_STATE, binding.recyclerMovies.getLayoutManager().onSaveInstanceState());
    }

}
