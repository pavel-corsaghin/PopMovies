package com.example.hung.popmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hung.popmovies.net.Trailer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by phoenix on 01/06/2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private final String log = TrailerAdapter.class.getSimpleName();
    ArrayList<Trailer> mTrailers;

    public TrailerAdapter(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_content, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.ViewHolder viewHolder, int position) {
        final Trailer trailer = mTrailers.get(position);
        final Context context = viewHolder.mView.getContext();
        viewHolder.mTrailer = trailer;
        String trailerUrl = context.getString(
                R.string.url_for_downloading_trailer_thumbail, trailer.getTrailerKey());
//        Log.v(log, trailerUrl);
        Picasso.with(context)
                .load(trailerUrl)
                .config(Bitmap.Config.RGB_565)
                .into(viewHolder.mTrailerThumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (!viewHolder.mTrailer.getTrailerId().equals(trailer.getTrailerId())) {
                            viewHolder.cleanUp();
                        } else {
                            viewHolder.mTrailerThumbnail.setVisibility(View.VISIBLE);
                            viewHolder.mIconPlay.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onError() {
                        viewHolder.mTrailerThumbnail.setVisibility(View.INVISIBLE);
                        viewHolder.mIconPlay.setVisibility(View.VISIBLE);
                    }
                });
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.watchTrailerOnYoutube(trailer.getTrailerKey(),context);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mTrailers != null) {
            return mTrailers.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView mTrailerThumbnail,mIconPlay;
        public Trailer mTrailer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTrailerThumbnail = (ImageView) mView.findViewById(R.id.trailer_thumbnail);
            mIconPlay = (ImageView) mView.findViewById(R.id.icon_play_trailer);
        }

        public void cleanUp() {
            final Context context = mView.getContext();
            Picasso.with(context).cancelRequest(mTrailerThumbnail);
            mTrailerThumbnail.setImageBitmap(null);
            mTrailerThumbnail.setVisibility(View.INVISIBLE);
            mIconPlay.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanUp();
    }

    public void updateAdapter(ArrayList<Trailer> trailers) {
        if (mTrailers != null) {
            mTrailers.clear();
        }
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public ArrayList<Trailer> getTrailers(){
        return mTrailers;
    }
}
