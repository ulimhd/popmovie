package com.baqoba.popularmovies;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ulimhd on 04/07/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private String[] mMovieData;

    private final PosterAdapterOnClickHandler mClickHandler;

    private Context context;


    public interface PosterAdapterOnClickHandler {
        void onClick(String currentMovie);
    }


    public PosterAdapter(Context context, PosterAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }


    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPosterImage;

        public PosterAdapterViewHolder(View view) {
            super(view);

            mPosterImage = (ImageView) view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String currentmovie = mMovieData[adapterPosition];
            mClickHandler.onClick(currentmovie);
        }
    }


    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder PosterAdapterViewHolder, int position) {
        String currentMovie = mMovieData[position];
        int titleIndex = mMovieData[position].indexOf("title: ");
        int posterIndex = mMovieData[position].indexOf("poster_path: ");
        int overviewIndex = mMovieData[position].indexOf("overview: ");
        int ratingIndex = mMovieData[position].indexOf("rating: ");
        int releaseIndex = mMovieData[position].indexOf("release_date: ");

        String titlePath =  mMovieData[position].substring(titleIndex+7, posterIndex-1);
        String posterPath = mMovieData[position].substring(posterIndex+13, overviewIndex);
        String overviewPath = mMovieData[position].substring(overviewIndex+10, ratingIndex-1);
        String ratingPath = mMovieData[position].substring(ratingIndex+8, releaseIndex-1);
        String releasePath = mMovieData[position].substring(releaseIndex+14);

/*        Log.d("posterPath", "---"+posterPath+ "---");
        Log.d("titleIndex", "---"+titlePath+ "---");
        Log.d("overviewIndex", "---"+overviewPath+ "---");
        Log.d("ratingIndex", "---"+ratingPath+ "---");
        Log.d("releaseIndex", "---"+releasePath+ "---");*/

        if(posterPath.equals("null ")){
            Picasso.with(context).load(R.drawable.no_image).resize(185,277).into(PosterAdapterViewHolder.mPosterImage);
        } else {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + posterPath).into(PosterAdapterViewHolder.mPosterImage);
        }
    }


    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }


    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}