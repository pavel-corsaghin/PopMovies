package com.example.hung.popmovies;


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
import java.util.List;

/**
 * Created by phoenix on 25/05/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String log_tag = this.getClass().getSimpleName();
    ArrayList<Movie> mMovies;

    public MovieAdapter(ArrayList<Movie> Movie) {
        this.mMovies = Movie;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);

        int gridColsNumber = 2;

        itemview.getLayoutParams().height = (int) (parent.getWidth() / gridColsNumber * 1.5);

        return new ViewHolder(itemview);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        final Context context = holder.mView.getContext();

        holder.mMovie = movie;
        holder.mTitleView.setText(movie.getTitle());

        String posterUrl = movie.getPosterUrl();
        //Log.v(log_tag, posterUrl);

        // Warning: onError() will not be called, if url is null.
        // Empty url leads to app crash.
        if (posterUrl == null) {
            holder.mTitleView.setVisibility(View.VISIBLE);
        }else {
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

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
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

    public void add(List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }
}
