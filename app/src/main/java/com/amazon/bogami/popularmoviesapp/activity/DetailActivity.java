package com.amazon.bogami.popularmoviesapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.bogami.popularmoviesapp.FavoriteMoviesDAO;
import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.adapter.ReviewListAdapter;
import com.amazon.bogami.popularmoviesapp.adapter.TrailerListAdapter;
import com.amazon.bogami.popularmoviesapp.model.Movie;
import com.amazon.bogami.popularmoviesapp.model.Review;
import com.amazon.bogami.popularmoviesapp.model.Trailer;
import com.amazon.bogami.popularmoviesapp.service.MovieReviewsService;
import com.amazon.bogami.popularmoviesapp.service.MovieVideosService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String REVIEWS_AVAILABLE = "ReviewsAvailable";
    public static final String NO_CONNECTIVITY = "NoConnectivity";
    public static final String TRAILERS_AVAILABLE = "TrailersAvailable";

    @BindView(R.id.originalTitle) TextView titleView;
    @BindView(R.id.releaseDate) TextView releaseDateView;
    @BindView(R.id.userRating) TextView useRatingView;
    @BindView(R.id.plotSynopsis) TextView plotSynopsisView;
    @BindView(R.id.poster) ImageView imageView;

    private MovieReviewsReceiver movieReviewsReceiver;
    private MovieTrailersReceiver movieTrailersReceiver;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setTitle("Movie Detail");
        displayBackButton();

        Intent intent = getIntent();
        Movie selectedMovie = intent.getParcelableExtra("movie");

        addListenerOnButton(selectedMovie);

        String apiKey = getResources().getString(R.string.apiKey);
        fetchReviews(selectedMovie.getId(), apiKey);
        fetchTrailers(selectedMovie.getId(), apiKey);

        titleView.setText(selectedMovie.getOriginalTitle());

        releaseDateView.setText(getFormattedDate(selectedMovie.getReleaseDate()));

        useRatingView.setText(getResources().getString(R.string.userRating,
                Double.valueOf(selectedMovie.getUserRating()).toString()));

        plotSynopsisView.setText(selectedMovie.getPlotSynopsis());

        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500/" + selectedMovie.getPosterPath()).into(imageView);
    }

    private void addListenerOnButton(final Movie selectedMovie) {
        button = (Button) findViewById(R.id.favoriteButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteMoviesDAO favoriteMoviesDAO = new FavoriteMoviesDAO(DetailActivity.this);
                favoriteMoviesDAO.addMovieToFavorites(selectedMovie);
            }
        });
    }

    private String getFormattedDate(String dateString) {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");
        return newFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(movieReviewsReceiver);
        unregisterReceiver(movieTrailersReceiver);
    }

    private void fetchReviews(int movieId, String apiKey) {
        startReviewsService(movieId, apiKey);
        registerReviewsReceiver();
    }

    private void fetchTrailers(int movieId, String apiKey) {
        startTrailersService(movieId, apiKey);
        registerTrailersReceiver();
    }

    private void startReviewsService(int movieId, String apiKey) {
        Intent msgIntent = new Intent(this, MovieReviewsService.class);
        msgIntent.putExtra(MovieReviewsService.MOVIE_ID_ARG, movieId);
        msgIntent.putExtra(MovieReviewsService.API_KEY_ARG, apiKey);
        startService(msgIntent);
    }

    private void startTrailersService(int movieId, String apiKey) {
        Intent msgIntent = new Intent(this, MovieVideosService.class);
        msgIntent.putExtra(MovieReviewsService.MOVIE_ID_ARG, movieId);
        msgIntent.putExtra(MovieReviewsService.API_KEY_ARG, apiKey);
        startService(msgIntent);
    }

    private void registerReviewsReceiver() {
        IntentFilter filter = new IntentFilter(REVIEWS_AVAILABLE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        movieReviewsReceiver = new MovieReviewsReceiver();
        registerReceiver(movieReviewsReceiver, filter);
        registerReceiver(movieReviewsReceiver, getNoConnectivityFilter());
    }

    private void registerTrailersReceiver() {
        IntentFilter filter = new IntentFilter(TRAILERS_AVAILABLE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        movieTrailersReceiver = new MovieTrailersReceiver();
        registerReceiver(movieTrailersReceiver, filter);
        registerReceiver(movieTrailersReceiver, getNoConnectivityFilter());
    }

    private IntentFilter getNoConnectivityFilter() {
        IntentFilter filter = new IntentFilter(NO_CONNECTIVITY);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        return filter;
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

    class MovieReviewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(NO_CONNECTIVITY)) {
                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    Toast.makeText(getApplicationContext(),
                            "No network connectivity", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            ArrayList<Review> reviewList =
                    intent.getParcelableArrayListExtra(MovieReviewsService.PARAM_OUT_REVIEWS);

            ReviewListAdapter adapter = new ReviewListAdapter(reviewList);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reviews);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

        }
    }

    class MovieTrailersReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(NO_CONNECTIVITY)) {
                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "No network connectivity", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            ArrayList<Trailer> trailerList =
                    intent.getParcelableArrayListExtra(MovieVideosService.PARAM_OUT_VIDEOS);

            TrailerListAdapter adapter = new TrailerListAdapter(context, trailerList);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.trailers);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}
