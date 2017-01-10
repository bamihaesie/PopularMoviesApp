package com.amazon.bogami.popularmoviesapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.model.Review;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private ArrayList<Review> reviewList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView author;
        public TextView content;

        public ViewHolder(View v) {
            super(v);
            author = (TextView) v.findViewById(R.id.review_author);
            content = (TextView) v.findViewById(R.id.review_content);
        }
    }

    public ReviewListAdapter(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        ViewHolder vh = new ViewHolder(convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
