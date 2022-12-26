package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wordpro.Adapter.SharedActivityRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Content;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.RdsConnection;

import java.util.List;

public class SharedActivity extends AppCompatActivity {

    EditText searchEditText;
    ImageView searchButton;
    RecyclerView recyclerView;
    CardView addContentButton;

    AppDatabase db;
    RdsConnection rdsConnection;

    SharedActivityRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        activityViewInitializer();

        List<Content> contents = db.contentDao().getAllContents();
        adapter = new SharedActivityRecyclerViewAdapter(contents);
        recyclerView.setAdapter(adapter);
    }

    private void activityViewInitializer(){
        searchEditText = findViewById(R.id.SharedActivitySearchEditText);
        searchButton = findViewById(R.id.SharedActivitySearchButton);
        addContentButton = findViewById(R.id.SharedActivityAddContentButton);
        recyclerView = findViewById(R.id.SharedActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getDBInstance(this);
        rdsConnection = new RdsConnection();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStuff = searchEditText.getText().toString();
                rdsConnection.getContentOfRDS(searchStuff, new Callback() {
                    @Override
                    public void OnCallback(Object object) {

                    }
                });
            }
        });

        addContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SharedActivity.this, AddContentActivity.class);
                startActivity(intent);
            }
        });
    }
}