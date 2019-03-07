package com.iaz.filmesfamosos.presentation.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iaz.filmesfamosos.Constants;
import com.iaz.filmesfamosos.R;
import com.iaz.filmesfamosos.databinding.ItemMovieBinding;
import com.iaz.filmesfamosos.models.MovieModel;
import com.iaz.filmesfamosos.presentation.ui.activities.MovieDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.iaz.filmesfamosos.Constants.POSTER_IMAGE_BASE_URL;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private ArrayList<MovieModel> moviesList;
    private final Context context;

    public MoviesAdapter(@NonNull Context context, ArrayList<MovieModel> moviesList) {

        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie, parent, false);

        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {

        final MovieModel movie = moviesList.get(i);
        Picasso.with(context).load(POSTER_IMAGE_BASE_URL + movie.getPoster_path()).into(movieViewHolder.binding.ivPoster);
        movieViewHolder.binding.tvName.setText(movie.getTitle());

        movieViewHolder.binding.cvMovie.setOnClickListener(view -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra(Constants.MOVIE_ID, movie.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setNewList(ArrayList<MovieModel> results) {
        this.moviesList = results;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
