package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpro.Adapter.NormalRecyclerViewAdapter;
import com.example.wordpro.Adapter.UserSentenceRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Sentence;
import com.example.wordpro.tool.Callback;

import java.util.ArrayList;
import java.util.List;

public class UserSentenceActivity extends AppCompatActivity {

    LinearLayout choosePathButton;
    TextView pathTextView;
    TextView consoleTextView;
    RecyclerView recyclerView;

    AppDatabase db;

    UserSentenceRecyclerViewAdapter adapter;
    ChoosePathDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sentence);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        db = AppDatabase.getDBInstance(this);
        activityViewInitializer();

        List<Sentence> sentences = db.sentenceDao().getAllSentences();
        adapter = new UserSentenceRecyclerViewAdapter(sentences);
        recyclerView.setAdapter(adapter);

        pathTextView.setText("ALL");
        consoleTextView.setText("There are #" + String.valueOf(sentences.size()) + " sentences");
    }

    private void activityViewInitializer(){

        choosePathButton = findViewById(R.id.userSentenceActivityChoosePathButton);
        pathTextView = findViewById(R.id.userSentenceActivityPathTextView);
        consoleTextView = findViewById(R.id.userSentenceActivityConsoleTextView);
        recyclerView = findViewById(R.id.userSentenceActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog = new UserSentenceActivity.ChoosePathDialog(UserSentenceActivity.this, new Callback() {
            @Override
            public void OnCallback(Object object) {
                // select dialog click listener
                String target = (String) object;

                List<Sentence> sentences = db.sentenceDao().getSentencesWithPath(target);
                int totalNum = sentences.size();

                adapter.setDataList(sentences);
                pathTextView.setText(target);
                consoleTextView.setText("There are #" + String.valueOf(totalNum) + " sentences");

                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        choosePathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public class ChoosePathDialog extends Dialog {
        RecyclerView recyclerView;
        NormalRecyclerViewAdapter adapter;

        public ChoosePathDialog(@NonNull Context context, Callback callback) {
            super(context);
            setContentView(R.layout.dialog_select);
            recyclerView = findViewById(R.id.dialogSelectRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<String> result = db.sentenceDao().getAllPaths();
            List<Object> objectResult = new ArrayList<Object>(result);

            adapter = new NormalRecyclerViewAdapter(context, NormalRecyclerViewAdapter.PATH, objectResult);
            adapter.setCallbackForPath(callback);

            recyclerView.setAdapter(adapter);
        }
    }
}