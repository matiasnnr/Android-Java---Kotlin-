package com.app.pruebaandroid.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.pruebaandroid.Constants;
import com.app.pruebaandroid.R;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Geometry;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Location;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Photo;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Result;
import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;
import com.app.pruebaandroid.ui.DetailsActivity;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoriteListRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteListRecyclerViewAdapter.ViewHolder> {

    private final List<FavoriteEntity> mValues;
    Context appContext;

    public FavoriteListRecyclerViewAdapter(List<FavoriteEntity> items, Context context) {
        mValues = items;
        appContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result_search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.favoriteItem = mValues.get(position);

            getGooglePhoto(70, holder.favoriteItem.getPhotoreference(), holder);
            holder.textViewName.setText(holder.favoriteItem.getName());
            holder.textViewAddress.setText(holder.favoriteItem.getAddress());

            if((Float) holder.favoriteItem.getRating() != null && (Float) holder.favoriteItem.getRating() != 0.0){
                holder.ratingBar.setRating(holder.favoriteItem.getRating());
                holder.ratingBar.setVisibility(View.VISIBLE);
            } else {
                holder.ratingBar.setVisibility(View.GONE);
            }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location(holder.favoriteItem.getLatitude(), holder.favoriteItem.getLongitude());
                Geometry geometry = new Geometry(location);
                Photo photo = new Photo(holder.favoriteItem.getPhotoreference());
                List<Photo> photosList = new ArrayList<>();
                photosList.add(photo);
                Result resultItem = new Result(
                        holder.favoriteItem.getAddress(),
                        geometry,
                        holder.favoriteItem.getName(),
                        photosList,
                        holder.favoriteItem.getPlaceid(),
                        holder.favoriteItem.getRating());
                goToDetailsActivity(resultItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textViewName;
        TextView textViewAddress;
        ImageView imageViewAvatar;
        RatingBar ratingBar;
        FavoriteEntity favoriteItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            this.textViewName = (TextView) view.findViewById(R.id.textViewName);
            this.textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);
            this.imageViewAvatar = (ImageView) view.findViewById(R.id.imageViewAvatar);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }

    }

    private void getGooglePhoto(final int maxWidth, final String photoReference, FavoriteListRecyclerViewAdapter.ViewHolder holder) {
        String photoUrl = Constants.BASE_URL + "place/photo?maxwidth=" + maxWidth + "&photoreference="
                + photoReference + "&key=" + Constants.GOOGLE_PLACE_API_KEY;
        Glide.with(appContext)
                .load(photoUrl)
                .into(holder.imageViewAvatar);
    }

    private void goToDetailsActivity(Result resultItem) {
        Intent intent = new Intent(appContext, DetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("resultItem", (Serializable) resultItem);
        appContext.startActivity(intent);
    }
}
