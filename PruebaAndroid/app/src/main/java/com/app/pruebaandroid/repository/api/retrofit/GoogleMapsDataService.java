package com.app.pruebaandroid.repository.api.retrofit;

import com.app.pruebaandroid.repository.api.retrofit.detailsearch.ResponseDetailSearchAPI;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.ResponseTextSearchAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsDataService {

/*
    @GET("place/nearbysearch/json?")
    Call<ResponseTextSearchAPI> nearbySearch(
            @Query(value = "location", encoded = true) String location,
            @Query(value = "name", encoded = true) String name,
            @Query(value = "rankby", encoded = true) String rankby,
            @Query(value = "key", encoded = true) String key);
 */

    @GET("place/textsearch/json?")
    Call<ResponseTextSearchAPI> textSearch(
            @Query(value = "query", encoded = true) String text,
            @Query(value = "key", encoded = true) String key);

    @GET("place/details/json?")
    Call<ResponseDetailSearchAPI> detailSearch(
            @Query(value = "place_id", encoded = true) String placeId,
            @Query(value = "key", encoded = true) String key);

/*
    @GET("place/photo?")
    Call<String> getPhoto(
            @Query(value = "maxwidth", encoded = true) int maxWidth,
            @Query(value = "photoreference", encoded = true) String photoReference,
            @Query(value = "key", encoded = true) String key);
 */

}
