package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wordpro.Adapter.WordSelectRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Sentence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AddSentenceActivity extends AppCompatActivity {

    private String TAG = "AddSentenceActivity";

    EditText sentenceEditText;
    RecyclerView recyclerView;
    FrameLayout applyButton;
    FrameLayout writeButton;

    LinearLayout categoryBox;
    TextView categoryButton;
    EditText categoryEditText;

    LinearLayout cheatSheetBox;
    TextView cheatSheetButton;
    EditText cheatSheetEditText;

    AppDatabase db;

    WordSelectRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence);

        db = AppDatabase.getDBInstance(AddSentenceActivity.this);

        sentenceEditText = findViewById(R.id.AddSentenceActivitySentenceEditText);
        recyclerView = findViewById(R.id.AddSentenceActivityRecyclerView);
        applyButton = findViewById(R.id.AddSentenceActivityApplyButton);
        writeButton = findViewById(R.id.AddSentenceActivityWriteButton);
        categoryBox = findViewById(R.id.AddSentenceActivityCategoryBox);
        categoryButton = findViewById(R.id.AddSentenceActivityCategoryButton);
        categoryEditText = findViewById(R.id.AddSentenceActivityCategoryEditText);
        cheatSheetBox = findViewById(R.id.AddSentenceActivityCheatSheetBox);
        cheatSheetButton = findViewById(R.id.AddSentenceActivityCheatSheetButton);
        cheatSheetEditText = findViewById(R.id.AddSentenceActivityCheatSheetEditText);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayout.HORIZONTAL));

        LocalDate today = LocalDate.now();
        String today2string = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate next = today.plusDays(1);
        String next2string = next.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        cheatSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheatSheetButton.setVisibility(View.INVISIBLE);
                cheatSheetEditText.setVisibility(View.VISIBLE);
            }
        });

        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryButton.setVisibility(View.INVISIBLE);
                categoryEditText.setVisibility(View.VISIBLE);
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringTokenizer stringTokenizer = new StringTokenizer(sentenceEditText.getText().toString());
                List<String> wordList = new ArrayList<>();
                while(stringTokenizer.hasMoreTokens()){
                    wordList.add(stringTokenizer.nextToken());
                }

                adapter = new WordSelectRecyclerViewAdapter(wordList);
                recyclerView.setAdapter(adapter);
                applyButton.setVisibility(View.INVISIBLE);
                writeButton.setVisibility(View.VISIBLE);

                categoryBox.setVisibility(View.VISIBLE);
                cheatSheetBox.setVisibility(View.VISIBLE);
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sentence sentence = new Sentence();
                sentence.path = categoryEditText.getText().toString();
                sentence.sentence = sentenceEditText.getText().toString();
                sentence.word = adapter.getWords();
                sentence.word_index = adapter.getWordsIndex();
                sentence.cheat_sheet = cheatSheetEditText.getText().toString();
                sentence.history = "";
                sentence.register_day = today2string;
                sentence.next = next2string;
                sentence.index = 0;

                db.sentenceDao().insertSentences(sentence);

                finish();
            }
        });

    }
}