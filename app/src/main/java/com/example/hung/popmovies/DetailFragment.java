package com.example.hung.popmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hung.popmovies.db.MovieContract;
import com.example.hung.popmovies.net.FetchReviewsTask;
import com.example.hung.popmovies.net.FetchTrailersTask;
import com.example.hung.popmovies.net.Movie;
import com.example.hung.popmovies.net.Review;
import com.example.hung.popmovies.net.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements FetchReviewsTask.Callbacks, FetchTrailersTask.Callbacks{

    ImageView mPoster;
    ImageView[] mRateStar = new ImageView[5];
    TextView mTitle, mReleaseDate, mLength, mRating, mOverview;
    Button mMarkAsFavorite, mRemoveFromFavorites, mWatchTrailer;
    Movie mMovie;
    String log = DetailFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
//    ListView mListView;
    TrailerAdapter mTrailerAdapter;
//    ReviewAdapter mReviewAdapter;
    LinearLayout mList;
//    Uri mUri;
    ShareActionProvider mShare;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = (Movie) arguments.getSerializable(Utility.MOVIE);
        }
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        getViewsId(view);
        fillView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShare = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

    }

    private void updateShareActionProvider(Trailer trailer) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mMovie.getTitle() + ". Watch trailer: "
                + trailer.getTrailerUrl());

        mShare.setShareIntent(sharingIntent);
    }

    public void getViewsId(View v) {
        mPoster = (ImageView) v.findViewById(R.id.movie_poster);
        mTitle = (TextView) v.findViewById(R.id.movie_title);
        mReleaseDate = (TextView) v.findViewById(R.id.movie_release_date);
        mLength = (TextView) v.findViewById(R.id.movie_length);
        mRateStar[0] = (ImageView) v.findViewById(R.id.rating_star1);
        mRateStar[1] = (ImageView) v.findViewById(R.id.rating_star2);
        mRateStar[2] = (ImageView) v.findViewById(R.id.rating_star3);
        mRateStar[3] = (ImageView) v.findViewById(R.id.rating_star4);
        mRateStar[4] = (ImageView) v.findViewById(R.id.rating_star5);
        mRating = (TextView) v.findViewById(R.id.movie_user_rating);
        mMarkAsFavorite = (Button) v.findViewById(R.id.button_mark_as_favorite);
        mRemoveFromFavorites = (Button) v.findViewById(R.id.button_remove_from_favorites);
        mWatchTrailer = (Button) v.findViewById(R.id.button_watch_trailer);
        mOverview = (TextView) v.findViewById(R.id.movie_overview);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.trailer_list);
//        mListView = (ListView) v.findViewById(R.id.review_list);
        mList = (LinearLayout)v.findViewById(R.id.review_list);

    }

    public void fillView() {
        if (mMovie != null) {

            //TextViews and user 's rate handler
            Picasso.with(getContext())
                    .load(mMovie.getPosterUrl())
                    .config(Bitmap.Config.RGB_565)
                    .into(mPoster);
            mTitle.setText(mMovie.getTitle());
            mReleaseDate.setText(mMovie.getReleaseDate());
            mLength.setText("120 minutes");
            mRating.setText(mMovie.getUserRating() + "/10");
            setRatingStar();
            mOverview.setText(mMovie.getOverview());

            // Buttons handler
            Uri uri = MovieContract.MovieEntry.getMovieUriFromIdAndSortByType(
                    Utility.FAVORITES, mMovie.getId()
            );
            CursorLoader loader =  new CursorLoader(getActivity(),
                    uri,
                    null,
                    null,
                    null,
                    null
            );
            Cursor cursor = loader.loadInBackground();
            if (cursor.moveToFirst()) {
                mMarkAsFavorite.setVisibility(View.INVISIBLE);
                mRemoveFromFavorites.setVisibility(View.VISIBLE);
            }
            cursor.close();
            mMarkAsFavorite.setOnClickListener(new myOnclickListener());
            mRemoveFromFavorites.setOnClickListener(new myOnclickListener());
            mWatchTrailer.setOnClickListener(new myOnclickListener());

            //fetch trailer and fill to trailers list
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mTrailerAdapter = new TrailerAdapter(new ArrayList<Trailer>());
            mRecyclerView.setAdapter(mTrailerAdapter);
            FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(this);
            fetchTrailersTask.execute(String.valueOf(mMovie.getId()));

            //fetch reviews and fill to reviews list
            FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(this);
            fetchReviewsTask.execute(String.valueOf(mMovie.getId()));

        } else {
            Log.v(log, " Unknown error!");
        }
    }



    public void setRatingStar() {
        float ratingInFloat = Float.valueOf(mMovie.getUserRating());
        int rating = Math.round(ratingInFloat);

        for (int i = 1; i <= rating; i++) {

            if (i % 2 == 1) {
                mRateStar[i / 2].setImageResource(R.drawable.ic_star_half_black_24dp);
            } else {
                mRateStar[i / 2 - 1].setImageResource(R.drawable.ic_star_black_24dp);
            }
        }
    }

    @Override
    public void onFetchTrailersFinished(ArrayList<Trailer> trailers) {
        //fill trailers
        mTrailerAdapter.updateAdapter(trailers);
        mWatchTrailer.setEnabled(true);

        //update share action bar
        updateShareActionProvider(trailers.get(0));
    }

    @Override
    public void onFetchReviewsFinished(ArrayList<Review> reviews) {
//        if (reviews.size() != 0) {
//            mReviewAdapter = new ReviewAdapter(getActivity(),reviews);
//            mListView.setAdapter(mReviewAdapter);
//            Utility.setListViewHeightBasedOnItems(mListView);
//        }
        for (int i=0; i<reviews.size(); i++) {
            Review review = reviews.get(i);
            View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.review_list_content,null);

            TextView reviewAuthor = (TextView) convertView.findViewById(R.id.review_author);
            reviewAuthor.setText(review.getAuthor());
            Log.v(log,review.getAuthor());

            TextView reviewContent = (TextView) convertView.findViewById(R.id.review_content);
            reviewContent.setText(review.getContent());
            mList.addView(convertView);
        }
    }

    private class myOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Uri uri = MovieContract.MovieEntry
                    .getMovieUriFromIdAndSortByType(Utility.FAVORITES,mMovie.getId());
            if (view == mMarkAsFavorite) {
//                Toast.makeText(getActivity(), "Clicked on button mark as favorite", Toast.LENGTH_SHORT).show();
                mMarkAsFavorite.setVisibility(View.INVISIBLE);
                mRemoveFromFavorites.setVisibility(View.VISIBLE);
                ContentValues contentValues = Utility.getContentValueFromMovie(mMovie,Utility.FAVORITES);
                getActivity().getContentResolver().insert(uri,contentValues);

            }
            if (view == mRemoveFromFavorites) {
//                Toast.makeText(getActivity(), "Clicked on button remove from favorites", Toast.LENGTH_SHORT).show();
                getActivity().getContentResolver().delete(
                        uri,
                        null,
                        null
                );
                mMarkAsFavorite.setVisibility(View.VISIBLE);
                mRemoveFromFavorites.setVisibility(View.INVISIBLE);
            }
            if (view == mWatchTrailer) {
//                Toast.makeText(getActivity(), "Clicked on button watch trailer", Toast.LENGTH_SHORT).show();
                ArrayList<Trailer> trailers = mTrailerAdapter.getTrailers();
                if(trailers.size()!=0){
                    Trailer mainTrailer = trailers.get(0);
                    Utility.watchTrailerOnYoutube(mainTrailer.getTrailerKey(),getActivity());
                }
            }
        }
    }
}


