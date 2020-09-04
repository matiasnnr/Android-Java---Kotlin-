package com.app.pruebaandroid.repository.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;
import com.app.pruebaandroid.repository.database.dao.FavoriteDao;

import java.util.List;

public class FavoriteRepository {
    private FavoriteDao favoriteDao;
    private LiveData<List<FavoriteEntity>> allObjects;

    public FavoriteRepository(Application application) {
        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);
        favoriteDao = db.favoriteDao();
        allObjects = favoriteDao.getAll();
    }

    public LiveData<List<FavoriteEntity>> getAll() {
        return allObjects;
    }

    public void insert(FavoriteEntity favorite) {
        new insertAsyncTask(favoriteDao).execute(favorite);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteEntity, Void, Void> {
        private FavoriteDao favoriteDaoAsyncTask;

        insertAsyncTask(FavoriteDao dao) {
            favoriteDaoAsyncTask = dao;
        }

        @Override
        protected Void doInBackground(FavoriteEntity... favoriteEntities) {

            favoriteDaoAsyncTask.insert(favoriteEntities[0]);

            return null;
        }
    }

    public void delete(int databaseId) {
        new deleteAsyncTask(favoriteDao).execute(databaseId);
    }

    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private FavoriteDao favoriteDaoAsyncTask;

        deleteAsyncTask(FavoriteDao dao) {
            favoriteDaoAsyncTask = dao;
        }

        @Override
        protected Void doInBackground(Integer... databaseId) {
            favoriteDaoAsyncTask.deleteById(databaseId[0]);
            return null;
        }
    }

    public void deleteAll() {
        new deleteAllAsyncTask(favoriteDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavoriteDao favoriteDaoAsyncTask;

        deleteAllAsyncTask(FavoriteDao dao) {
            favoriteDaoAsyncTask = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favoriteDaoAsyncTask.deleteAll();
            return null;
        }
    }
}
