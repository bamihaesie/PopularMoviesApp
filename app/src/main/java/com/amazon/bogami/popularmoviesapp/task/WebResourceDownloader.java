package com.amazon.bogami.popularmoviesapp.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.amazon.bogami.popularmoviesapp.NoNetworkConnectivityException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebResourceDownloader {

    public static String downloadResource(Context context, URL url) throws NoNetworkConnectivityException {

        if (!isOnline(context)) {
            throw new NoNetworkConnectivityException();
        }

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

        return jsonResponse;
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
