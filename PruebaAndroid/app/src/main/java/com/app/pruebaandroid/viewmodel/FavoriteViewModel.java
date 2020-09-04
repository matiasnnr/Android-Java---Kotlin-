package com.app.pruebaandroid.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.pruebaandroid.repository.database.FavoriteRepository;
import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    private LiveData<List<FavoriteEntity>> allFavorites;
    private FavoriteRepository favoriteRepository;

    public FavoriteViewModel(Application application) {
        super(application);
        favoriteRepository = new FavoriteRepository(application);
        allFavorites = favoriteRepository.getAll();
    }

    public LiveData<List<FavoriteEntity>> getAllFavorites() { return allFavorites; }

    public void insertFavorite(FavoriteEntity newFavoriteEntity) { favoriteRepository.insert(newFavoriteEntity); }

    public void deleteByPlaceId(int databaseId) { favoriteRepository.delete(databaseId); }

    public void deleteAllFavorites() { favoriteRepository.deleteAll(); }
}
