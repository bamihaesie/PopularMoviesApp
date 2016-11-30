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

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle("Movie Detail");
        displayBackButton();

        Intent intent = getIntent();
        Movie selectedMovie = intent.getParcelableExtra("movie");

        TextView titleView = (TextView) this.findViewById(R.id.originalTitle);
        titleView.setText(selectedMovie.getOriginalTitle());

        TextView releaseDateView = (TextView) this.findViewById(R.id.releaseDate);
        releaseDateView.setText(selectedMovie.getReleaseDate());

        TextView useRatingView = (TextView) this.findViewById(R.id.userRating);
        useRatingView.setText(getResources().getString(R.string.userRating,
                Double.valueOf(selectedMovie.getUserRating()).toString()));

        TextView plotSynopsisView = (TextView) this.findViewById(R.id.plotSynopsis);
        plotSynopsisView.setText(selectedMovie.getPlotSynopsis());

        ImageView imageView = (ImageView) this.findViewById(R.id.poster);
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
