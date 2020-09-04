package com.app.pruebaandroid.repository.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.pruebaandroid.repository.database.dao.FavoriteDao;
import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;


@Database(entities = {FavoriteEntity.class}, version = 1)
public abstract class FavoriteRoomDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();
    private static volatile FavoriteRoomDatabase INSTANCE;

    public static FavoriteRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (FavoriteRoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteRoomDatabase.class, "favorite_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
