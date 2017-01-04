package com.amazon.bogami.popularmoviesapp.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amazon.bogami.popularmoviesapp.adapter.ImageListAdapter;
import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.activity.DetailActivity;
import com.amazon.bogami.popularmoviesapp.model.Movie;
import com.amazon.bogami.popularmoviesapp.model.SortingOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieFetcherTask extends AsyncTask<SortingOrder, Integer, List<Movie>> {

    private final View rootView;
    private Context context;
    private Activity activity;

    public MovieFetcherTask(View view, Context context, Activity activity) {
        this.rootView = view;
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected List<Movie> doInBackground(SortingOrder... sortingOrders) {

        if (!isOnline()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "No network connectivity", Toast.LENGTH_SHORT).show();
                }
            });

            return new ArrayList<>();
        }

        String apiKey = activity.getResources().getString(R.string.apiKey);

        if (apiKey == null || apiKey.isEmpty()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Please add API key to strings.xml", Toast.LENGTH_LONG).show();
                }
            });

            return new ArrayList<>();
        }

        URL url = buildUrl(apiKey, sortingOrders[0]);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonResponse = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                jsonResponse = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                jsonResponse = null;
            }

            jsonResponse = buffer.toString();

        } catch (Exception e) {
            Log.e("MovieFetcherTask", "Error ", e);
            jsonResponse = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MovieFetcherTask", "Error closing stream", e);
                }
            }
        }

        return convertJson(jsonResponse);
    }

    private URL buildUrl(String apiKey, SortingOrder sortingOrder) {
        URL url;
        switch (sortingOrder) {
            case POPULARITY:
                url = UrlBuilder.getPopularMoviesUrl(apiKey);
                break;
            case TOP_RATED:
                url = UrlBuilder.getTopRatedMoviesUrl(apiKey);
                break;
            default:
                throw new RuntimeException("Unsupported sorting order");
        }
        return url;
    }

    private List<Movie> convertJson(String jsonResponse) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(jsonResponse);
            JSONArray jArray = jObject.getJSONArray("results");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject resultItem = jArray.getJSONObject(i);
                movies.add(new Movie(
                        resultItem.getInt("id"),
                        resultItem.getString("original_title"),
                        resultItem.getString("poster_path"),
                        resultItem.getString("overview"),
                        resultItem.getDouble("vote_average"),
                        resultItem.getString("release_date")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(final List<Movie> movies) {
        String[] moviePosters = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            moviePosters[i] = "http://image.tmdb.org/t/p/w185/" + movies.get(i).getPosterPath();
        }

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        ImageListAdapter adapter = new ImageListAdapter(activity, moviePosters);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie", movies.get(i));
                activity.startActivity(intent);
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
