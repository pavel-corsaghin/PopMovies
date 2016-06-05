package com.example.hung.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hung.popmovies.net.Review;

import java.util.ArrayList;

/**
 * Created by phoenix on 02/06/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    private String log = ReviewAdapter.class.getSimpleName();
    ArrayList<Review> mReviews;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        super(context,0,reviews);
        mReviews = reviews;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = mReviews.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.review_list_content, parent, false);

        }

        TextView reviewAuthor = (TextView) convertView.findViewById(R.id.review_author);
        reviewAuthor.setText(review.getAuthor());
        Log.v(log,review.getAuthor());

        TextView reviewContent = (TextView) convertView.findViewById(R.id.review_content);
        reviewContent.setText(review.getContent());

        return convertView;
    }

    public void updateAdapter(ArrayList<Review> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
