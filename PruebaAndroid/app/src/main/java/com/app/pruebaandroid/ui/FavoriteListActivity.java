package com.app.pruebaandroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pruebaandroid.R;
import com.app.pruebaandroid.adapter.FavoriteListRecyclerViewAdapter;
import com.app.pruebaandroid.repository.database.entity.FavoriteEntity;
import com.app.pruebaandroid.viewmodel.FavoriteViewModel;
import java.util.List;

public class FavoriteListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ImageView imageViewBack;
    private FavoriteViewModel favoriteViewModel;

    public FavoriteListActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.list);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(this);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        getAllFavorites(this);

    }

    private void getAllFavorites(final Context context) {
        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteEntity>>() {
            @Override
            public void onChanged(List<FavoriteEntity> favoriteEntities) {
                // Set the adapter
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new FavoriteListRecyclerViewAdapter(favoriteEntities, getApplicationContext()));
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.imageViewBack:
                finish();
                break;
        }
    }

}
