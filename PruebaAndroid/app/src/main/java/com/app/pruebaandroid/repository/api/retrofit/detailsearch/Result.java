
package com.app.pruebaandroid.repository.api.retrofit.detailsearch;

import java.io.Serializable;
import java.util.List;

import com.app.pruebaandroid.repository.api.retrofit.textsearch.Geometry;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;

    public Result() {
    }

    public Result(String id, String name, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.reviews = reviews;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
