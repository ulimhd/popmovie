package com.baqoba.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ulimhd on 03/07/17.
 */

public class NetworkUtils {
    final static String TMDB_BASE_URL =
            "http://api.themoviedb.org/3/movie";

    final static String PARAM_QUERY =
            "api_key";

    final static String API_KEY =
            "dummy_key";

    final static String PAGE =
            "page";


    public static URL buildUrl(int nextPage, String sortBy){
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .appendPath(sortBy)
                .appendQueryParameter(PAGE, String.valueOf(nextPage))
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
