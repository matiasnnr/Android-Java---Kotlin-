package com.app.pruebaandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pruebaandroid.Constants;
import com.app.pruebaandroid.R;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Photo;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Result;
import com.app.pruebaandroid.ui.DetailsActivity;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class ResultSearchAdapter extends RecyclerView.Adapter<ResultSearchAdapter.MyViewHolder> {

    private List<Result> resultList;
    private Context context;

    public ResultSearchAdapter(List<Result> result, Context adapterContext) {
        resultList = result;
        context = adapterContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result_search_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if(resultList != null){
            holder.resultItem = resultList.get(position);

            List<Photo> photoReference = (List<Photo>) holder.resultItem.getPhotos();
            if(photoReference != null && photoReference.size() > 0){
                getGooglePhoto(70, holder.resultItem.getPhotos().get(0).getPhotoReference(), holder);
            } else {
                Glide.with(context)
                        .load(Constants.DEFAULT_IMAGE)
                        .into(holder.imageViewAvatar);
            }

            holder.textViewName.setText(holder.resultItem.getName());
            holder.textViewAddress.setText(holder.resultItem.getFormattedAddress());

            if((Float) holder.resultItem.getRating() != null && (Float) holder.resultItem.getRating() != 0.0){
                holder.ratingBar.setRating(holder.resultItem.getRating());
                holder.ratingBar.setVisibility(View.VISIBLE);
            } else {
                holder.ratingBar.setVisibility(View.GONE);
            }

            holder.itemResultSearchList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailsActivity(holder.resultItem);
                }
            });

        }


    }

    private void goToDetailsActivity(Result resultItem) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("resultItem", (Serializable) resultItem);
        context.startActivity(intent);
    }

    private void getGooglePhoto(final int maxWidth, final String photoReference, MyViewHolder holder) {
        String photoUrl = Constants.BASE_URL + "place/photo?maxwidth=" + maxWidth + "&photoreference="
                + photoReference + "&key=" + Constants.GOOGLE_PLACE_API_KEY;
        Glide.with(context)
                .load(photoUrl)
                .into(holder.imageViewAvatar);
    }


    @Override
    public int getItemCount() {
        if(resultList != null)
            return resultList.size();
        else return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout itemResultSearchList;
        TextView textViewName;
        TextView textViewAddress;
        ImageView imageViewAvatar;
        RatingBar ratingBar;
        Result resultItem;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.itemResultSearchList = (ConstraintLayout) itemView.findViewById(R.id.itemResultSearchList);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
            this.imageViewAvatar = (ImageView) itemView.findViewById(R.id.imageViewAvatar);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        }

    }
}
