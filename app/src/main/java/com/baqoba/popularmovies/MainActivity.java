package com.baqoba.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baqoba.popularmovies.utilities.EndlessRecyclerViewScrollListener;
import com.baqoba.popularmovies.utilities.NetworkUtils;
import com.baqoba.popularmovies.utilities.OpenMovieJsonUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private PosterAdapter mPosterAdapter;
    private ProgressBar mLoadingIndicator;
    private String sortBy;

    private EndlessRecyclerViewScrollListener scrollListener;

    SharedPreferences sharedPref;



    public static final String PREFERENCES = "pref";
    public static final String SORT_BY = "sort_by";
    public static final int START_PAGE = 1;

    private int currentPage = START_PAGE;
    // private static int page = 1;

    private boolean mLoading = false;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedPref.contains(SORT_BY)) {
            sortBy = sharedPref.getString(SORT_BY, "");
        }else{
            sortBy = "popular";
        }
        Log.d("action_init", sortBy + " page: " + currentPage);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /*GridLayoutManager layoutManager
                = new GridLayoutManager(this, calculateNoOfColumns(this));*/

        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter(getApplicationContext(), this);


    //    loadMovieData(currentPage, sortBy);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
           /* @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);

                int totalItem = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!mLoading && lastVisibleItem == totalItem - 1) {
                    mLoading = true;
                    // Scrolled to bottom. Do something here.
                   loadMovieData(page, sortBy);
                    Log.d("PAGE:", String.valueOf(page));

                    page +=1;

                    mLoading = false;

                }
            }
*/
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Load next page of movies
                isLoading = true;
                page = currentPage;
                totalItemsCount = mPosterAdapter.getItemCount();
                Log.d("PAGE:", String.valueOf(page) + " itemCount: " + totalItemsCount);
      //          loadMovieData(page, sortBy);
               // currentPage = page;
                currentPage+=1;
            }
        };
// Associate RecyclerView with the EndlessRecyclerViewScrollListener
        mRecyclerView.addOnScrollListener(scrollListener);
        mRecyclerView.setAdapter(mPosterAdapter);


        loadFirstData(START_PAGE, sortBy);

    }

    private void loadFirstData(int page, String sortBy){
        URL queryUrl = NetworkUtils.buildUrl(page, sortBy);
        Log.d("action_url", queryUrl.toString());

        new showMovies().execute(queryUrl);

    }

    private void loadMovieData(int page, String sortBy){
        URL queryUrl = NetworkUtils.buildUrl(page, sortBy);
        Log.d("action_url", queryUrl.toString());

        new showMovies().execute(queryUrl);

    }

    public class showMovies extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... params) {
            URL searchUrl = params[0];
            String queryResult = null;
            try {
                queryResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                String[] simpleJsonMovieData = OpenMovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, queryResult);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movieData != null) {
                mPosterAdapter.setMovieData(movieData);
            }
        }
    }

    @Override
    public void onClick(String currentMovie) {
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(Intent.EXTRA_TEXT, currentMovie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popular) {
            sortBy = "popular";

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SORT_BY, sortBy);
            editor.apply();

            Log.d("action_pop", sharedPref.getString(SORT_BY, "")  + " page: " + currentPage);
            loadMovieData(currentPage, sortBy);

            return true;
        }else if(id == R.id.action_sort_rating){
            sortBy = "top_rated";
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SORT_BY, sortBy);
            editor.apply();

            Log.d("action_rate", sharedPref.getString(SORT_BY, "")  + " page: " + currentPage);

            loadMovieData(currentPage, sortBy);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*get proper number of grid based on proportion of devices */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 140;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }
}
