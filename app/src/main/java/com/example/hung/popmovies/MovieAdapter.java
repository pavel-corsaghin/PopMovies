package com.example.hung.popmovies;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hung.popmovies.net.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by phoenix on 25/05/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String log = this.getClass().getSimpleName();
    ArrayList<Movie> mMovies;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onMovieSelected(Movie movie, int position);
    }

    public MovieAdapter(ArrayList<Movie> movies, Activity activity) {
        mMovies = movies;
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);

        int gridColsNumber = 2;
        itemView.getLayoutParams().height = (int) (parent.getWidth() / gridColsNumber * Utility.POSTER_ASPECT_RATIO);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Movie movie = mMovies.get(position);
        final Context context = holder.mView.getContext();

        holder.mMovie = movie;
        holder.mTitleView.setText(movie.getTitle());

        String posterUrl = movie.getPosterUrl();
        //Log.v(log, posterUrl);

        if (posterUrl == null) {
            holder.mTitleView.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(context)
                    .load(posterUrl)
                    .config(Bitmap.Config.RGB_565)
                    .into(holder.mThumbnailView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (holder.mMovie.getId() != movie.getId()) {
                                        holder.cleanUp();
                                    } else {
                                        holder.mThumbnailView.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onError() {
                                    holder.mTitleView.setVisibility(View.VISIBLE);
                                }
                            }
                    );
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onMovieSelected(movie, holder.getAdapterPosition());
//                Toast.makeText(context, "movie" + position + "selected", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        if(mMovies!=null){
            return mMovies.size();
        } else {
            return 0;
        }
//        return 20;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanUp();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //        @Bind(R.id.thumbnail)
        ImageView mThumbnailView;
        //        @Bind(R.id.title)
        TextView mTitleView;
        public Movie mMovie;

        public ViewHolder(View view) {
            super(view);
//            ButterKnife.bind(this, view);
            mView = view;
            mThumbnailView = (ImageView) mView.findViewById(R.id.thumbnail);
            mTitleView = (TextView) mView.findViewById(R.id.title);
        }

        public void cleanUp() {
            final Context context = mView.getContext();
            Picasso.with(context).cancelRequest(mThumbnailView);
            mThumbnailView.setImageBitmap(null);
            mThumbnailView.setVisibility(View.INVISIBLE);
            mTitleView.setVisibility(View.GONE);
        }

    }

    public void updateAdapter(ArrayList<Movie> movies) {
        if (mMovies!= null) {
            mMovies.clear();
        }
        mMovies=movies;
        notifyDataSetChanged();
    }
}
