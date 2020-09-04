
package com.app.pruebaandroid.repository.api.retrofit.textsearch;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable {


    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("rating")
    @Expose
    private float rating;

    public Result() {
    }

    public Result(String formattedAddress, Geometry geometry, String id, String name, List<Photo> photos, String placeId, float rating) {
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.id = id;
        this.name = name;
        this.photos = photos;
        this.placeId = placeId;
        this.rating = rating;
    }

    public Result(String formattedAddress, Geometry geometry, String name, List<Photo> photos, String placeId, float rating) {
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.name = name;
        this.photos = photos;
        this.placeId = placeId;
        this.rating = rating;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
