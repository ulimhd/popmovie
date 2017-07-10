package com.baqoba.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {
    private TextView mTitle, mReleaseDate, mRating, mOverview;
    private ImageView mThumbnail;

    private String titlePath, posterPath, overviewPath, ratingPath, releasePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        
        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mThumbnail = (ImageView) findViewById(R.id.iv_movie_thumb); 

        Intent intent = getIntent();

        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            String extras = intent.getStringExtra(Intent.EXTRA_TEXT);

            int titleIndex = extras.indexOf("title: ");
            int posterIndex = extras.indexOf("poster_path: ");
            int overviewIndex = extras.indexOf("overview: ");
            int ratingIndex = extras.indexOf("rating: ");
            int releaseIndex = extras.indexOf("release_date: ");

            titlePath =  extras.substring(titleIndex+7, posterIndex-1);
            posterPath = extras.substring(posterIndex+13, overviewIndex);
            overviewPath = extras.substring(overviewIndex+10, ratingIndex-1);
            ratingPath = extras.substring(ratingIndex+8, releaseIndex-1);
            releasePath = extras.substring(releaseIndex+14);

            mTitle.setText(titlePath);
            mReleaseDate.setText(releasePath);
            mRating.setText(ratingPath);
            mOverview.setText(overviewPath);

            if(posterPath.equals("null ")){
                Picasso.with(getApplicationContext()).load(R.drawable.no_image).resize(154,231).into(mThumbnail);
            } else {
                Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/" + posterPath).into(mThumbnail);
            }

        }
    }
}
