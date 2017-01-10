package com.amazon.bogami.popularmoviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewListAdapter extends ArrayAdapter<Review> {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<Review> reviewList;

    public ReviewListAdapter(Context context, ArrayList<Review> reviewList) {
        super(context, R.layout.review_item, reviewList);

        this.context = context;
        this.reviewList = reviewList;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.review_item, parent, false);
        }

        TextView reviewAuthorView = (TextView) convertView.findViewById(R.id.review_author);
        reviewAuthorView.setText(reviewList.get(position).getAuthor());

        TextView reviewContentView = (TextView) convertView.findViewById(R.id.review_content);
        reviewContentView.setText(reviewList.get(position).getContent());

        return convertView;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Review getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
