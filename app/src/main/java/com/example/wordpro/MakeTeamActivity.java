package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpro.Adapter.NicknameRecyclerViewAdapter;
import com.example.wordpro.Adapter.SelectDialogRecyclerViewAdapter;
import com.example.wordpro.Adapter.UserSentenceRecyclerViewAdapter;
import com.example.wordpro.Adapter.WordSelectRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Sentence;
import com.example.wordpro.database.TeamMate;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.RdsConnection;

import java.util.List;

public class MakeTeamActivity extends AppCompatActivity {

    private String TAG = "MakeTeamActivity";

    EditText teamNameEditText;
    EditText nicknameEditText;
    TextView nicknameAddButton;
    RecyclerView nicknameRecyclerView;
    TextView choosePathButton;
    RecyclerView pathExampleRecyclerView;
    TextView pathTotalNumTextView;
    CardView makeButton;

    UserSentenceRecyclerViewAdapter pathExampleAdapter;
    NicknameRecyclerViewAdapter nicknameAdapter;

    AppDatabase db;

    RdsConnection rdsConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        activityViewInitializer();
    }

    private void activityViewInitializer(){

        teamNameEditText = findViewById(R.id.MakeTeamActivityTeamNameEditText);
        nicknameEditText = findViewById(R.id.MakeTeamActivityNicknameEditText);
        nicknameAddButton = findViewById(R.id.MakeTeamActivityNicknameAddButton);
        nicknameRecyclerView = findViewById(R.id.MakeTeamActivityNicknameRecyclerView);
        choosePathButton = findViewById(R.id.MakeTeamActivityChoosePathButton);
        pathTotalNumTextView = findViewById(R.id.MakeTeamActivityPathTotalNumTextView);
        pathExampleRecyclerView = findViewById(R.id.MakeTeamActivityPathExampleRecyclerView);
        makeButton = findViewById(R.id.MakeTeamActivityMakeButton);

        nicknameRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pathExampleRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getDBInstance(this);

        nicknameAdapter = new NicknameRecyclerViewAdapter();
        nicknameRecyclerView.setAdapter(nicknameAdapter);

        nicknameAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEditText.getText().toString();
                // todo: If there existed nickname in cognito, then add nickname to list. Vice versa, print "Enter nickname correctly".
                nicknameAdapter.addItem(nickname);
            }
        });

        choosePathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePathDialog dialog = new ChoosePathDialog(MakeTeamActivity.this,
                        new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                String target = (String) object;
                                choosePathButton.setText(target);
                                choosePathButton.setTextColor(Color.BLACK);

                                List<Sentence> sentences = db.sentenceDao().getSentencesWithPath(target);
                                int totalNum = sentences.size();

                                if(totalNum >= 10) {
                                    pathExampleAdapter = new UserSentenceRecyclerViewAdapter(sentences.subList(0, 5));
                                    pathTotalNumTextView.setText("There exist " + String.valueOf(totalNum-5) + " more sentences");
                                }else{
                                    Toast.makeText(MakeTeamActivity.this, "At least, the number of sentences is bigger than 10", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: make request to server.


            }
        });
    }

    public class ChoosePathDialog extends Dialog{
        RecyclerView recyclerView;
        SelectDialogRecyclerViewAdapter adapter;
        public ChoosePathDialog(@NonNull Context context, Callback callback) {
            super(context);
            setContentView(R.layout.dialog_select);
            recyclerView = findViewById(R.id.dialogSelectRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<String> result = db.sentenceDao().getAllPaths();
            String[] stringArray = new String[result.size()];
            for(int i=0; i<result.size(); i++){
                stringArray[i] = result.get(i);
            }
            adapter = new SelectDialogRecyclerViewAdapter(stringArray, callback, this);
            recyclerView.setAdapter(adapter);
        }
    }
}