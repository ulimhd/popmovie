package com.baqoba.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by ulimhd on 05/07/17.
 */

public class OpenMovieJsonUtils {

    public static String[] getSimpleWeatherStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String MOV_RESULT = "results";

        String[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);


        JSONArray movieArray = movieJson.getJSONArray(MOV_RESULT);

        parsedMovieData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            /* These are the values that will be collected */
            String title;
            String poster_path;
            String overview;
            String rating;
            String release_date;

            JSONObject movie = movieArray.getJSONObject(i);

            title = movie.getString("title");
            poster_path = movie.getString("poster_path");
            overview = movie.getString("overview");
            rating = movie.getString("vote_average");
            release_date = movie.getString("release_date");


            parsedMovieData[i] = "title: " + title
                    + " poster_path: " + poster_path
                    + " overview: " + overview
                    + " rating: " + rating
                    + " release_date: " + release_date;
       }

        return parsedMovieData;
    }
}
