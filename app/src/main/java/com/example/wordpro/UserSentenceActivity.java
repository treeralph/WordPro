package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wordpro.Adapter.UserSentenceRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Sentence;

import java.util.ArrayList;
import java.util.List;

public class UserSentenceActivity extends AppCompatActivity {

    EditText searchEditText;
    ImageView searchButton;
    RecyclerView recyclerView;

    AppDatabase db;

    UserSentenceRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sentence);

        activityViewInitializer();
        db = AppDatabase.getDBInstance(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStuff = searchEditText.getText().toString();
                List<Sentence> sentences = db.sentenceDao().getSentencesWithPath(searchStuff);
                adapter = new UserSentenceRecyclerViewAdapter(sentences);
                recyclerView.setAdapter(adapter);
            }
        });


        List<Sentence> sentences = db.sentenceDao().getAllSentences();
        adapter = new UserSentenceRecyclerViewAdapter(sentences);
        recyclerView.setAdapter(adapter);
    }

    private void activityViewInitializer(){
        searchEditText = findViewById(R.id.UserSentenceActivitySearchEditText);
        searchButton = findViewById(R.id.UserSentenceActivitySearchButton);
        recyclerView = findViewById(R.id.UserSentenceActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}