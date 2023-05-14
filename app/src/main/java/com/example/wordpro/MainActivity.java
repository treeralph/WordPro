package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.wordpro.Adapter.BottomMenuRecyclerViewAdapter;
import com.example.wordpro.Adapter.RightMenuRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Period;
import com.example.wordpro.database.Sentence;
import com.example.wordpro.database.TodaySentence;
import com.example.wordpro.firebase.MyFirebaseMessagingService;
import com.example.wordpro.rds.RdsConnect;
import com.example.wordpro.rds.dataRequest.DeleteQuery;
import com.example.wordpro.rds.dataRequest.SelectQuery;
import com.example.wordpro.rds.dataResponse.SentenceHandler;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.Cognito;
import com.example.wordpro.tool.WordDialog;
import com.example.wordpro.view.BottomMenuRecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private static int ADDSENTENCEACTIVITY_REQUESTCODE = 0;

    TextView mainTextView;
    TextView indexTextView;
    TextView downloadStatusTextView;
    ImageView downloadStatusImageView;
    CardView redButton;
    CardView blueButton;
    CardView blackButton;

    RecyclerView rightMenuRecyclerView;

    LinearLayout touchLinearLayout;
    LinearLayout resultLinearLayout;

    RightMenuRecyclerViewAdapter rightMenuRecyclerViewAdapter;
    BottomMenuRecyclerViewAdapter bottomMenuRecyclerViewAdapter;

    AppDatabase db;
    RdsConnect rdsConnect;

    List<TodaySentence> sentences;
    int numSentences;
    int progress = -1;
    int MAX_INDEX = 10;

    List<Period> periods;
    String uid;

    Handler handler;

    LinearLayout bottomRearLinearLayout;

    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        db = AppDatabase.getDBInstance(this);
        uid = getIntent().getStringExtra("uid");
        rdsConnect = new RdsConnect();

        checkPeriod();
        activityViewInitializer();
        getSentenceFromServer();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int flag = msg.what;
                if(flag == 0){
                    downloadStatusTextView.setText("There are no sentences now.");
                    downloadStatusImageView.setVisibility(View.INVISIBLE);
                }else if(flag == 1){
                    downloadStatusTextView.setText("There are " + String.valueOf(msg.arg1) + " sentences");
                    downloadStatusImageView.setImageResource(R.drawable.star_filled);
                }else{
                    downloadStatusTextView.setText("Raise Exception on Downloading");
                    downloadStatusImageView.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    public void activityViewInitializer(){

        mainTextView = findViewById(R.id.MainActivityMainTextView);
        indexTextView = findViewById(R.id.MainActivityIndexTextView);
        downloadStatusTextView = findViewById(R.id.MainActivityDownloadStatusTextView);
        downloadStatusImageView = findViewById(R.id.MainActivityDownloadStatusImageView);

        redButton = findViewById(R.id.MainActivityRedButton);
        blueButton = findViewById(R.id.MainActivityBlueButton);
        blackButton = findViewById(R.id.MainActivityBlackButton);

        rightMenuRecyclerView = findViewById(R.id.MainActivityRightMenuRecyclerView);

        touchLinearLayout = findViewById(R.id.MainActivityTouchLinearLayout);
        resultLinearLayout = findViewById(R.id.MainActivityResultLinearLayout);

        bottomRearLinearLayout = findViewById(R.id.MainActivityBottomRearLinearLayout);
        viewPager = findViewById(R.id.MainActivityBottomMenuViewPager);

        rightMenuRecyclerViewAdapter = new RightMenuRecyclerViewAdapter(new Callback() {
            @Override
            public void OnCallback(Object object) {
                RelativeLayout relativeLayout = (RelativeLayout) object;

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                relativeLayout.getLayoutParams().width = width - 12;
            }
        });

        LinearLayoutManager rightMenuLayoutManager = new LinearLayoutManager(this);
        rightMenuLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rightMenuRecyclerView.setLayoutManager(rightMenuLayoutManager);

        rightMenuRecyclerView.setAdapter(rightMenuRecyclerViewAdapter);

        bottomMenuRecyclerViewAdapter = new BottomMenuRecyclerViewAdapter(this, uid, BottomMenuRecyclerViewAdapter.MAIN_ACTIVITY,  new Callback() {
            @Override
            public void OnCallback(Object object) {
                MotionEvent me = (MotionEvent) object;
                Log.e(TAG, "rearLinearLayout: dispatchTouchEvent: " + me.toString());
                bottomRearLinearLayout.dispatchTouchEvent((MotionEvent) object);
            }
        });

        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setAdapter(bottomMenuRecyclerViewAdapter);

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.setOffscreenPageLimit(2);

        /*
        Log.e(TAG, "ViewPager: childAt(0): " + viewPager.getChildAt(0).toString());
        RecyclerView LABRecyclerView = (RecyclerView) viewPager.getChildAt(0);
        viewPager.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                Log.e(TAG, "View: " + v.toString() + "\nMotionEvent: " + event.toString());
                return false;
            }
        });
        viewPager.setMotionEventSplittingEnabled(false);

         */


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position < 0) {
                    // 페이지가 왼쪽으로 스크롤될 때
                    page.setTranslationY(-80f * position);
                } else if (position <= 1) {
                    // 페이지가 오른쪽으로 스크롤될 때
                    page.setTranslationY(-80f * position);
                } else {
                    // 페이지가 정중앙에 있을 때
                    page.setTranslationY(0);
                }
            }
        });

        viewPager.setPageTransformer(compositePageTransformer);

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "RED_BUTTON clicked");
                if(progress < numSentences - 1){
                    clickAction("r");
                }
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "BLUE_BUTTON clicked");
                if(progress < numSentences - 1){
                    clickAction("b");
                }
            }
        });

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "BLACK_BUTTON clicked");
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
                    span.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), start, end+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                // #n sentence of A
                indexTextView.setText("#" + String.valueOf(progress+2) + " sentence of "+String.valueOf(numSentences));
                rightMenuRecyclerViewAdapter.setCheatSheet(target.cheat_sheet);
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

            if(currentSentences.size() == 0){
                indexTextView.setText("There are no sentences on today");
                mainTextView.setText("There are no sentences");
                Spannable span = (Spannable) mainTextView.getText();
                int start = 13;
                int end = 22;
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                onStart();
            }
        }
    }

    public void getSentenceFromServer(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                downloadStatusTextView.setText("downloading...");
                downloadStatusImageView.setVisibility(View.VISIBLE);
                downloadStatusImageView.startAnimation(animation);
                try {
                    // add permission true condition
                    SelectQuery selectQuery = SelectQuery.builder()
                            .tableName(RdsConnect.TABLE_SENTENCES).optionField("uid").option("'" + uid + "'")
                            .build();
                    Log.d(TAG, "select query: " + selectQuery.toString());

                    DeleteQuery deleteQuery = DeleteQuery.builder()
                            .tableName(RdsConnect.TABLE_SENTENCES).optionField("uid").option("'" + uid + "'")
                            .build();
                    Log.d(TAG, "delete query: " + deleteQuery.toString());

                    JSONArray jsonArray = new JSONArray();
                    rdsConnect.request(RdsConnect.SELECT, RdsConnect.TABLE_SENTENCES, selectQuery.toString(), jsonArray.toString(), new Callback() {
                        @Override
                        public void OnCallback(Object object) {
                            Sentence[] sentences = SentenceHandler.string2sentences((String) object);
                            downloadStatusImageView.clearAnimation();

                            if(sentences == null || sentences.length == 0){
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handler.sendMessage(message);
                            }else{
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                message.arg1 = sentences.length;
                                handler.sendMessage(message);
                                db.sentenceDao().insertSentences(sentences);

                                rdsConnect.request(RdsConnect.DELETE, RdsConnect.TABLE_SENTENCES, deleteQuery.toString(), jsonArray.toString(), new Callback() {
                                    @Override
                                    public void OnCallback(Object object) {
                                        String result = (String) object;
                                        Log.d(TAG, "DELETE query server result: " + result);
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = handler.obtainMessage();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        });
        thread.start();
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
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), start, end+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            indexTextView.setText("#" + String.valueOf(progress+2) + " sentence of "+String.valueOf(numSentences));
            rightMenuRecyclerViewAdapter.setCheatSheet(target.cheat_sheet);
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