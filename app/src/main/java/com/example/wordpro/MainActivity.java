package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.regions.Regions;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Period;
import com.example.wordpro.database.Sentence;
import com.example.wordpro.database.TodaySentence;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.RdsConnection;
import com.example.wordpro.tool.WordDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private static int ADDSENTENCEACTIVITY_REQUESTCODE = 0;

    TextView userSentenceButton;
    TextView sharedButton;
    TextView teamButton;
    FrameLayout addSentenceButton;
    TextView mainTextView;
    TextView indexTextView;
    ImageView redButton;
    ImageView blueButton;
    ImageView blackButton;
    CardView cheatSheetButton;

    AppDatabase db;
    RdsConnection rdsConnection;

    List<TodaySentence> sentences;
    int numSentences;
    int progress = -1;
    int MAX_INDEX = 10;

    List<Period> periods;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamButton = findViewById(R.id.MainActivity2TeamActivityButton);
        userSentenceButton = findViewById(R.id.MainActivity2UserSentenceActivityButton);
        sharedButton = findViewById(R.id.MainActivity2SharedActivityButton);
        mainTextView = findViewById(R.id.MainActivityMainTextView);
        indexTextView = findViewById(R.id.MainActivityIndexTextView);
        addSentenceButton = findViewById(R.id.MainActivity2AddSentenceActivityButton);
        cheatSheetButton = findViewById(R.id.MainActivityCheatSheetButton);
        redButton = findViewById(R.id.MainActivityRedButton);
        blueButton = findViewById(R.id.MainActivityBlueButton);
        blackButton = findViewById(R.id.MainActivityBlackButton);

        db = AppDatabase.getDBInstance(this);
        rdsConnection = new RdsConnection();

        uid = getIntent().getStringExtra("uid");

        checkPeriod();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "thread for connecting rds");
                // todo: activate download animation
                rdsConnection.getSentenceOfRDS(uid, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        Sentence[] sentences = (Sentence[]) object;
                        if(sentences.length == 0){
                            // todo: make download animation invisible, and then show "no sentences updated"
                        }else {
                            db.sentenceDao().insertSentences(sentences);
                            // todo: make download animation invisible, and then show "# sentences updated"
                        }
                    }
                });
            }
        });
        thread.start();

        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                startActivity(intent);
            }
        });

        cheatSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WordDialog wordDialog = new WordDialog(MainActivity.this, sentences.get(progress + 1).cheat_sheet);
                    wordDialog.setCanceledOnTouchOutside(true);
                    wordDialog.setCancelable(true);
                    wordDialog.setCanceledOnTouchOutside(true);
                    wordDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    wordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    wordDialog.show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        userSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserSentenceActivity.class);
                startActivity(intent);
            }
        });

        sharedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SharedActivity.class);
                startActivity(intent);
            }
        });

        addSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSentenceActivity.class);
                startActivity(intent);
            }
        });

        /**
         * (red/blue/black) button click operates updating the sentence on DB, sentences table.
         * */
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress < numSentences - 1){
                    clickAction("r");
                }
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress < numSentences - 1){
                    clickAction("b");
                }
            }
        });

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress < numSentences - 1){
                    clickAction("d");
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart");

        String currentDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<TodaySentence> todaySentences = db.todaySentenceDao().getAllTodaySentences();
        if(todaySentences.size() != 0 && todaySentences.get(0).today_register_day.equals(currentDay)){

            Log.i(TAG, "onStart if statement");

            sentences = todaySentences;
            numSentences = sentences.size();
            for(int i=0; i<numSentences; i++){
                TodaySentence todaySentence = sentences.get(i);
                int check = todaySentence.check;
                if(check != 0){
                    progress = i;
                }
            }
            if(progress < numSentences - 1){
                TodaySentence target = sentences.get(progress+1);
                mainTextView.setText(target.sentence);
                Spannable span = (Spannable) mainTextView.getText();
                String ingredient = target.word_index;
                for(String ing: ingredient.split("/")){
                    if(ing.equals("") || ing.isEmpty()){
                        continue;
                    }
                    String[] in = ing.split(",");
                    int start = Integer.parseInt(in[0]);
                    int end = Integer.parseInt(in[1]);
                    span.setSpan(new BackgroundColorSpan(Color.parseColor("#FFBD12")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                indexTextView.setText(String.valueOf(progress+2)+"/"+String.valueOf(numSentences));
            }else{
                mainTextView.setText("Done!");
                indexTextView.setText("Complete!");
            }

        }else{

            Log.i(TAG, "onStart else statement");

            db.todaySentenceDao().deleteAllTodaySentence();
            List<Sentence> currentSentences = db.sentenceDao().getTodaySentences(currentDay);
            for(Sentence sentence: currentSentences){
                TodaySentence todaySentence = new TodaySentence();
                todaySentence.original_id = sentence.id;
                todaySentence.today_register_day = currentDay;
                todaySentence.check = 0;
                todaySentence.path = sentence.path;
                todaySentence.sentence = sentence.sentence;
                todaySentence.word = sentence.word;
                todaySentence.word_index = sentence.word_index;
                todaySentence.cheat_sheet = sentence.cheat_sheet;
                todaySentence.history = sentence.history;
                todaySentence.register_day = sentence.register_day;
                todaySentence.next = sentence.next;
                todaySentence.index = sentence.index;
                db.todaySentenceDao().insertTodaySentence(todaySentence);
            }
            if(currentSentences.size() != 0) {
                onStart();
            }
        }
    }

    public void clickAction(String color){
        TodaySentence todaySentence = sentences.get(progress+1);
        todaySentence.check = 1;
        db.todaySentenceDao().updateTodaySentence(todaySentence);

        LocalDate next = LocalDate.parse(todaySentence.next, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate next2 = next.plusDays(periods.get(todaySentence.index).value);
        String next2string = next2.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Sentence sentence = new Sentence();
        sentence.id = todaySentence.original_id;
        sentence.path = todaySentence.path;
        sentence.sentence = todaySentence.sentence;
        sentence.word = todaySentence.word;
        sentence.word_index = todaySentence.word_index;
        sentence.cheat_sheet = todaySentence.cheat_sheet;
        sentence.history = todaySentence.history + color;
        sentence.register_day = todaySentence.register_day;
        sentence.next = next2string;
        sentence.index = todaySentence.index + 1;

        if(sentence.index < MAX_INDEX){
            db.sentenceDao().updateSentences(sentence);
        }else{

        }

        progress++;
        if(progress < numSentences - 1){
            TodaySentence target = sentences.get(progress+1);
            mainTextView.setText(target.sentence);
            Spannable span = (Spannable) mainTextView.getText();
            String ingredient = target.word_index;
            for(String ing: ingredient.split("/")){
                if(ing.equals("") || ing.isEmpty()){
                    continue;
                }
                String[] in = ing.split(",");
                int start = Integer.parseInt(in[0]);
                int end = Integer.parseInt(in[1]);
                span.setSpan(new BackgroundColorSpan(Color.parseColor("#FFBD12")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            indexTextView.setText(String.valueOf(progress+2)+"/"+String.valueOf(numSentences));
        }else{
            mainTextView.setText("Done!");
            indexTextView.setText("Complete!");
        }
    }

    public void checkPeriod(){
        periods = db.periodDao().getAllPeriods();
        if(periods.size() == 0) {
            for (int i = 0; i < 12; i++) {
                Period period = new Period();
                period.value = fibonacci(i);
                db.periodDao().insertPeriod(period);
            }
            periods = db.periodDao().getAllPeriods();
        }
        Log.i(TAG, "period: " + periods.toString());
    }

    public int fibonacci(int n){
        if(n==0 || n==1){
            return 1;
        }else{
            return fibonacci(n-1) + fibonacci(n-2);
        }
    }
}