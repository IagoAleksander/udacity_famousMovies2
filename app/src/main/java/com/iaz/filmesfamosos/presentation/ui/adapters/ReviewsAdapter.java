package com.iaz.filmesfamosos.presentation.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iaz.filmesfamosos.R;
import com.iaz.filmesfamosos.databinding.ItemReviewBinding;
import com.iaz.filmesfamosos.models.ResultsReview;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private ArrayList<ResultsReview> resultsReviews;
    private final Context context;

    public ReviewsAdapter(@NonNull Context context, ArrayList<ResultsReview> resultsReviews) {

        this.context = context;
        this.resultsReviews = resultsReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemReviewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_review, parent, false);

        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {

        final ResultsReview review = resultsReviews.get(i);
        reviewViewHolder.binding.tvName.setText(review.getAuthor());
        reviewViewHolder.binding.tvContent.setText(review.getContent());
        reviewViewHolder.binding.itemReview.setOnClickListener(v -> {
            Intent i1 = new Intent(Intent.ACTION_VIEW);
            i1.setData(Uri.parse(review.getUrl()));
            context.startActivity(i1);
        });

    }

    @Override
    public int getItemCount() {
        return resultsReviews.size();
    }

    public void setNewList(ArrayList<ResultsReview> results) {
        this.resultsReviews = results;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;

        ReviewViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
