package com.app.pruebaandroid.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pruebaandroid.R;
import com.app.pruebaandroid.adapter.ResultSearchAdapter;
import com.app.pruebaandroid.repository.api.retrofit.GoogleMapsDataClient;
import com.app.pruebaandroid.repository.api.retrofit.GoogleMapsDataService;
import com.app.pruebaandroid.repository.api.retrofit.textsearch.Result;

import java.util.List;

public class ResultSearchListActivity extends AppCompatActivity implements View.OnClickListener {

    GoogleMapsDataService googleMapsDataService;

    RecyclerView recyclerView;
    ImageView imageViewBack;
    TextView toolbarContent;
    String toolbarText;
    List<Result> totalResults;
    ResultSearchAdapter adapterStores;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search_list);

        getSupportActionBar().hide();

        setVariables();
        events();

        googleMapsDataService = GoogleMapsDataClient.getClient().create(GoogleMapsDataService.class);

    }

    private void events() {
        totalResults = (List<Result>) getIntent().getSerializableExtra("totalResults");
        toolbarText = getIntent().getStringExtra("searchText");
        toolbarContent.setText("Resultados para " + toolbarText);

        imageViewBack.setOnClickListener(this);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapterStores = new ResultSearchAdapter(totalResults, getApplicationContext());
        recyclerView.setAdapter(adapterStores);
    }

    private void setVariables() {
        toolbarContent = (TextView) findViewById(R.id.toolbarContent);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
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
