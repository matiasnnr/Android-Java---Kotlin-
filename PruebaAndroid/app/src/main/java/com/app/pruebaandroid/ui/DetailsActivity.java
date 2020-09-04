package com.app.pruebaandroid.ui;

import android.os.Bundle;

import com.app.pruebaandroid.Constants;
import com.app.pruebaandroid.adapter.ReviewListAdapter;
import com.app.pruebaandroid.repository.api.retrofit.GoogleMapsDataClient;
import com.app.pruebaandroid.repository.api.retrofit.GoogleMapsDataService;
import com.app.pruebaandroid.repository.api.retrofit.detailsearch.ResponseDetailSearchAPI;
import com.app.pruebaandroid.repository.api.retrofit.detailsearch.Review;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Location;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Photo;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Result;
import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;
import com.app.pruebaandroid.viewmodel.FavoriteViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pruebaandroid.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    FloatingActionButton addToFavoriteList;
    ImageView imageViewBack, imageViewPhotoReference;
    Toolbar toolbar;
    Result result;
    GoogleMap mapView;
    RatingBar ratingBar;
    LinearLayout linearLayoutList;
    TextView textViewAddress, textViewRating, textViewReview;

    GoogleMapsDataService googleMapsDataService;
    List<Review> resultReviewsList;

    private List<FavoriteEntity> favoriteEntityList;
    private FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        favoriteViewModel = new ViewModelProvider(this)
                .get(FavoriteViewModel.class);

        setVariables();
        events();

        getAllFavorites();

    }

    private void setVariables() {
        result = (Result) getIntent().getSerializableExtra("resultItem");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addToFavoriteList = (FloatingActionButton) findViewById(R.id.addToFavoriteList);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        imageViewPhotoReference = (ImageView) findViewById(R.id.imageViewPhotoReference);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewReview = (TextView) findViewById(R.id.textViewReview);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        linearLayoutList = (LinearLayout) findViewById(R.id.linearLayoutList);

        googleMapsDataService = GoogleMapsDataClient.getClient().create(GoogleMapsDataService.class);
    }

    private void events() {
        toolbar.setTitle(result.getName());
        setSupportActionBar(toolbar);

        addToFavoriteList.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);

        if ((Float) result.getRating() != null && (Float) result.getRating() != 0.0) {
            ratingBar.setRating(result.getRating());
            ratingBar.setVisibility(View.VISIBLE);
        } else {
            ratingBar.setVisibility(View.GONE);
            textViewRating.setText("Calificación: aún no ha sido calificado");
        }

        textViewAddress.setText(result.getFormattedAddress());

        List<Photo> photoReference = (List<Photo>) result.getPhotos();
        if (photoReference != null && photoReference.size() > 0) {
            getGooglePhoto(500, photoReference.get(0).getPhotoReference());
        } else {
            Glide.with(DetailsActivity.this)
                    .load(Constants.DEFAULT_IMAGE)
                    .into(imageViewPhotoReference);
        }

        getGoogleDetailData(result.getPlaceId());

    }

    private void getAllFavorites() {
        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteEntity>>() {
            @Override
            public void onChanged(List<FavoriteEntity> favoriteEntities) {
                favoriteEntityList = favoriteEntities;
                if (favoriteEntities != null && favoriteEntities.size() > 0) {
                    for (int i = 0; i < favoriteEntities.size(); i++) {
                        if (favoriteEntities.get(i).getPlaceid().equals(result.getPlaceId())) {
                            if (favoriteEntities.get(i).getIsfavorite()) {
                                addToFavoriteList.setImageResource(R.drawable.ic_like_pink);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();

        switch (id) {
            case R.id.addToFavoriteList:

                float rating = result.getRating();
                List<Photo> photoReferenceObject = result.getPhotos();
                String photoReference;
                if ((Float) rating == null) {
                    rating = 0;
                }
                if (photoReferenceObject == null) {
                    photoReference = Constants.DEFAULT_PHOTO_REFERENCE_GOOGLE;
                } else {
                    photoReference = photoReferenceObject.get(0).getPhotoReference();
                }

                final FavoriteEntity newFavoriteEntity = new FavoriteEntity(
                        result.getName(),
                        result.getFormattedAddress(),
                        photoReference,
                        rating,
                        true,
                        result.getPlaceId(),
                        result.getGeometry().getLocation().getLat(),
                        result.getGeometry().getLocation().getLng()
                );

                if (favoriteEntityList != null && favoriteEntityList.size() > 0) {
                    for (int i = 0; i < favoriteEntityList.size(); i++) {
                        if (favoriteEntityList.get(i).getPlaceid().equals(result.getPlaceId())) {
                                favoriteEntityList.get(i).setIsfavorite(false);
                                favoriteViewModel.deleteByPlaceId(favoriteEntityList.get(i).getId());
                                addToFavoriteList.setImageResource(R.drawable.ic_like);
                                break;
                        } else {
                            favoriteViewModel.insertFavorite(newFavoriteEntity);
                            addToFavoriteList.setImageResource(R.drawable.ic_like_pink);
                            break;
                        }
                    }
                } else {
                    favoriteViewModel.insertFavorite(newFavoriteEntity);
                    addToFavoriteList.setImageResource(R.drawable.ic_like_pink);
                    break;
                }

                break;

            case R.id.imageViewBack:
                finish();
                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;

        Location location = result.getGeometry().getLocation();
        LatLng markerPosition = new LatLng(location.getLat(), location.getLng());
        mapView.addMarker(new MarkerOptions().position(markerPosition).title(result.getFormattedAddress()));
        mapView.moveCamera(CameraUpdateFactory.newLatLng(markerPosition));
        mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15));
        mapView.getUiSettings().setAllGesturesEnabled(false);
        mapView.setPadding(0, 150, 0, 150);

    }

    private void getGooglePhoto(final int maxWidth, final String photoReference) {

        String photoUrl = Constants.BASE_URL + "place/photo?maxwidth=" + maxWidth + "&photoreference="
                + photoReference + "&key=" + Constants.GOOGLE_PLACE_API_KEY;
        Glide.with(DetailsActivity.this)
                .load(photoUrl)
                .placeholder(R.drawable.ic_default_image)
                .into(imageViewPhotoReference);

    }

    private void getGoogleDetailData(final String placeId) {

        Call<ResponseDetailSearchAPI> call = googleMapsDataService.detailSearch(placeId, Constants.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<ResponseDetailSearchAPI>() {
            @Override
            public void onResponse(Call<ResponseDetailSearchAPI> call, Response<ResponseDetailSearchAPI> response) {
                ResponseDetailSearchAPI responseDetailSearchAPI = response.body();


                if (response.isSuccessful()) {

                    if (responseDetailSearchAPI.getStatus().equals("OK")) {

                        resultReviewsList = (List<Review>) responseDetailSearchAPI.getResult().getReviews();

                        if (resultReviewsList != null) {
                            ReviewListAdapter reviewListAdapter = new ReviewListAdapter(
                                    DetailsActivity.this,
                                    R.layout.item_result_search_list,
                                    (ArrayList<Review>) resultReviewsList);

                            linearLayoutList.removeAllViews();
                            for (int i = 0; i < reviewListAdapter.getCount(); i++)
                                linearLayoutList.addView(reviewListAdapter.getView(i, null, linearLayoutList));
                        } else {
                            textViewReview.setText("Reviews: aún no ha sido calificado");
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo encontrar las reviews que estás buscando",
                                Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error " + response.code(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseDetailSearchAPI> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Problemas de conexión. Inténtelo de nuevo.",
                        Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }

}