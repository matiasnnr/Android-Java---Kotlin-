package com.app.pruebaandroid.repository.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert
    void insert(FavoriteEntity favoriteEntity);

    @Update
    void update(FavoriteEntity favoriteEntity);

    @Query("DELETE FROM favorites")
    void deleteAll();

    @Query("DELETE FROM favorites WHERE id = :databaseId")
    void deleteById(int databaseId);

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    LiveData<List<FavoriteEntity>> getAll();

    @Query("SELECT * FROM favorites WHERE isfavorite LIKE 'true'")
    LiveData<List<FavoriteEntity>> getAllFavorites();

}
