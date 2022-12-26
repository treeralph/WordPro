package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity {

    ImageView contentImageView;
    TextView contentTitleTextView;
    TextView nicknameTextView;
    TextView downloadButton;
    RecyclerView sentenceRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ContentActivityViewInitializer();

        String contentId = getIntent().getStringExtra("contentId");


    }

    private void ContentActivityViewInitializer(){
        contentImageView = findViewById(R.id.ContentActivityImageView);
        contentTitleTextView = findViewById(R.id.ContentActivityTitleTextView);
        nicknameTextView = findViewById(R.id.ContentActivityNicknameTextView);
        downloadButton = findViewById(R.id.ContentActivityDownloadButton);
        sentenceRecyclerView = findViewById(R.id.ContentActivityRecyclerView);
    }
}