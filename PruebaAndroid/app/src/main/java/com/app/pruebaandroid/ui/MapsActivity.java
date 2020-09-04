package com.app.pruebaandroid.ui;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pruebaandroid.Constants;
import com.app.pruebaandroid.R;
import com.app.pruebaandroid.adapter.PlaceAutoSuggestAdapter;
import com.app.pruebaandroid.repository.api.retrofit.GoogleMapsDataClient;
import com.app.pruebaandroid.repository.api.retrofit.GoogleMapsDataService;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.ResponseTextSearchAPI;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Result;
import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;
import com.app.pruebaandroid.viewmodel.FavoriteViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemClickListener, TextView.OnEditorActionListener {

    ProgressBar progressBar;
    FloatingActionButton favoriteButton;
    AutoCompleteTextView autoCompleteSearchBar;

    private GoogleMap mMap;

    GoogleMapsDataService googleMapsDataService;

    List<Result> resultList;

    private FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setVariables();
        events();

    }

    private void events() {
        favoriteButton.setOnClickListener(this);

        favoriteViewModel = new ViewModelProvider(this)
                .get(FavoriteViewModel.class);

        autoCompleteSearchBar.setOnItemClickListener(this);
        autoCompleteSearchBar.setOnEditorActionListener(this);
        autoCompleteSearchBar.setAdapter(new PlaceAutoSuggestAdapter(this, android.R.layout.simple_list_item_1));

    }

    private void setVariables() {
        autoCompleteSearchBar = findViewById(R.id.searchBar);
        favoriteButton = findViewById(R.id.favoriteButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        googleMapsDataService = GoogleMapsDataClient.getClient().create(GoogleMapsDataService.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Chile and move the camera
        addChileMarker();

        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteEntity>>() {
            @Override
            public void onChanged(List<FavoriteEntity> favoriteEntities) {
                mMap.clear();
                addChileMarker();
                for (int i = 0; i < favoriteEntities.size(); i++) {
                    String name = favoriteEntities.get(i).getName();
                    LatLng latLng = new LatLng(favoriteEntities.get(i).getLatitude(), favoriteEntities.get(i).getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.like_marker))
                            .title(name));
                }
            }
        });
    }

    private void addChileMarker() {
        LatLng chile = new LatLng(-33.447487, -70.673676);
        mMap.addMarker(new MarkerOptions()
                .position(chile)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.chile))
                .title("Ven a Chile"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chile));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.favoriteButton:
                goToFavoriteList();
                break;
        }
    }

    private void goToFavoriteList() {
        Intent intent = new Intent(this, FavoriteListActivity.class);
        startActivity(intent);
    }

    private LatLng getLatLngFromAddress(String address) {

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null) {
                Address singleaddress = addressList.get(0);
                LatLng latLng = new LatLng(singleaddress.getLatitude(), singleaddress.getLongitude());
                return latLng;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Address getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if (addresses != null) {
                Address address = addresses.get(0);
                return address;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d("TextFilter: ", autoCompleteSearchBar.getText().toString());
        LatLng latLng = getLatLngFromAddress(autoCompleteSearchBar.getText().toString());
        if (latLng != null) {
            Log.d("Lat Lng: ", " " + latLng.latitude + " " + latLng.longitude);
            Address address = getAddressFromLatLng(latLng);
            if (address != null) {
                Log.d("Address: ", "" + address.toString());

                getGoogleData(autoCompleteSearchBar.getText().toString());
            } else {
                Log.d("Address", "No hubo resultados para tu búsqueda");
            }
        } else {
            Log.d("Lat Lng", "Latitud y longitud no encontradas");
        }

    }

    @Override
    public boolean onEditorAction(TextView content, int actionId, KeyEvent event) {

        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {

            getGoogleData(content.getText().toString());

            handled = true;
        }
        return handled;

    }

    private void getGoogleData(final String textFilter) {

        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseTextSearchAPI> call = googleMapsDataService.textSearch(textFilter, Constants.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<ResponseTextSearchAPI>() {
            @Override
            public void onResponse(Call<ResponseTextSearchAPI> call, Response<ResponseTextSearchAPI> response) {
                ResponseTextSearchAPI responseTextSearchAPI = response.body();

                if (response.isSuccessful()) {

                    if (responseTextSearchAPI.getStatus().equals("OK")) {

                        resultList = responseTextSearchAPI.getResults();

                        Intent intent = new Intent(MapsActivity.this, ResultSearchListActivity.class);
                        intent.putExtra("totalResults", (Serializable) resultList);
                        intent.putExtra("searchText", textFilter);
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo encontrar el lugar que estás buscando", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error " + response.code(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseTextSearchAPI> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Problemas de conexión. Inténtelo de nuevo.",
                        Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }


}
