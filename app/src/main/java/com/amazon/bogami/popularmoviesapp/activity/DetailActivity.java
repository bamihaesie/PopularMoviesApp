package com.amazon.bogami.popularmoviesapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.originalTitle) TextView titleView;
    @BindView(R.id.releaseDate) TextView releaseDateView;
    @BindView(R.id.userRating) TextView useRatingView;
    @BindView(R.id.plotSynopsis) TextView plotSynopsisView;
    @BindView(R.id.poster) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setTitle("Movie Detail");
        displayBackButton();

        Intent intent = getIntent();
        Movie selectedMovie = intent.getParcelableExtra("movie");

        titleView.setText(selectedMovie.getOriginalTitle());

        releaseDateView.setText(selectedMovie.getReleaseDate());

        useRatingView.setText(getResources().getString(R.string.userRating,
                Double.valueOf(selectedMovie.getUserRating()).toString()));

        plotSynopsisView.setText(selectedMovie.getPlotSynopsis());

        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500/" + selectedMovie.getPosterPath()).into(imageView);
    }

    private void displayBackButton() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setTitle(String title) {
        final ActionBar actionBar = getSupportActionBar();

        // actionBar
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // titleTextView
        TextView titleTextView = new TextView(actionBar.getThemedContext());

        titleTextView.setText(title);
        titleTextView.setTextSize(24);
        titleTextView.setTypeface(Typeface.DEFAULT);

        // Add titleTextView into ActionBar
        actionBar.setCustomView(titleTextView);


    }
}
