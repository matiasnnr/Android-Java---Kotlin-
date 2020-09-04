package com.app.pruebaandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.pruebaandroid.R;
import com.app.pruebaandroid.repository.api.retrofit.detailsearch.Review;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReviewListAdapter extends ArrayAdapter<Review> {

    private Context context;
    private int resource;

    public ReviewListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Review> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        convertView = layoutInflater.inflate(resource, parent, false);

        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewName);
        TextView textViewAddress = (TextView) convertView.findViewById(R.id.textViewAddress);
        ImageView imageViewAvatar = (ImageView) convertView.findViewById(R.id.imageViewAvatar);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

        if(position < 5){
            textViewName.setText(getItem(position).getAuthorName());
            textViewAddress.setText(getItem(position).getText());

            Glide.with(context)
                    .load(getItem(position).getProfilePhotoUrl())
                    .into(imageViewAvatar);

            ratingBar.setRating(getItem(position).getRating());
        }

        return convertView;
    }
}
