package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpro.Adapter.NicknameRecyclerViewAdapter;
import com.example.wordpro.Adapter.NormalRecyclerViewAdapter;
import com.example.wordpro.Adapter.SelectDialogRecyclerViewAdapter;
import com.example.wordpro.Adapter.UserSentenceRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Sentence;
import com.example.wordpro.database.User;
import com.example.wordpro.ec2.EC2RESTApi;
import com.example.wordpro.rds.RdsConnect;
import com.example.wordpro.rds.dataRequest.InsertQuery;
import com.example.wordpro.rds.dataRequest.SelectQuery;
import com.example.wordpro.rds.dataResponse.UserHandler;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.Cognito;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MakeTeamActivity extends AppCompatActivity {

    private String TAG = "MakeTeamActivity";
    private int ACTIVITY_TERMINATE = 0;

    ViewPager2 viewPager;
    SpringDotsIndicator indicator;
    MakeTeamRecyclerViewAdapter adapter;

    RecyclerView pathExampleRecyclerView;

    AppDatabase db;
    RdsConnect rdsConnect;
    Cognito cognito;
    String uid;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        db = AppDatabase.getDBInstance(this);
        rdsConnect = new RdsConnect();
        cognito = new Cognito(this);

        uid = getIntent().getStringExtra("uid");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if(msg.what == ACTIVITY_TERMINATE){
                    finish();
                }
                return false;
            }
        });

        activityViewInitializer();
    }

    private void activityViewInitializer() {
        viewPager = findViewById(R.id.makeTeamActivityViewPager);
        indicator = findViewById(R.id.makeTeamActivityIndicator);

        adapter = new MakeTeamRecyclerViewAdapter(this);

        viewPager.setAdapter(adapter);
        indicator.attachTo(viewPager);
    }



    public class MakeTeamRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;

        String teamName;
        List<String> nicknames;

        public MakeTeamRecyclerViewAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            switch(viewType){
                case 0:
                    View itemView0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_add_team_0, parent, false);
                    return new Page0ViewHolder(itemView0);
                case 1:
                    View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_add_team_1, parent, false);
                    return new Page1ViewHolder(itemView1);
                case 2:
                    View itemView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_add_team_2, parent, false);
                    return new Page2ViewHolder(itemView2);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            switch(holder.getAdapterPosition()){
                case 0:
                    Page0ViewHolder page0ViewHolder = (Page0ViewHolder) holder;
                    page0ViewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            teamName = page0ViewHolder.editText.getText().toString();
                            viewPager.setCurrentItem(holder.getAdapterPosition() + 1);
                        }
                    });
                    break;
                case 1:
                    Page1ViewHolder page1ViewHolder = (Page1ViewHolder) holder;
                    page1ViewHolder.teamAddButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            page1ViewHolder.consoleLayout.setVisibility(View.INVISIBLE);
                            String nickname = page1ViewHolder.editText.getText().toString();
                            addUser(nickname, new Callback() {
                                @Override
                                public void OnCallback(Object object) {
                                    // success callback
                                    User[] users = (User[]) object;
                                    if (users.length == 0) {
                                        Log.d(TAG, "The user doesn't exist");
                                        page1ViewHolder.consoleNicknameTextView.setText(nickname);
                                        page1ViewHolder.consoleTextView.setText("can't be added");
                                        page1ViewHolder.consoleNicknameTextView.setTextColor(getResources().getColor(R.color.red));
                                        page1ViewHolder.consoleTextView.setTextColor(getResources().getColor(R.color.red));
                                        page1ViewHolder.consoleLayout.setVisibility(View.VISIBLE);

                                    } else {
                                        if(page1ViewHolder.adapter.addDataWithOutDuplicate(nickname)){

                                        }


                                        page1ViewHolder.adapter.addData(users[0].nickname);

                                        page1ViewHolder.consoleNicknameTextView.setText(nickname);
                                        page1ViewHolder.consoleTextView.setText("is added");
                                        page1ViewHolder.consoleNicknameTextView.setTextColor(getResources().getColor(R.color.yellow));
                                        page1ViewHolder.consoleTextView.setTextColor(getResources().getColor(R.color.yellow));
                                        page1ViewHolder.consoleLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            }, new Callback() {
                                @Override
                                public void OnCallback(Object object) {
                                    // failure callback
                                    page1ViewHolder.consoleNicknameTextView.setText(nickname);
                                    page1ViewHolder.consoleTextView.setText("can't be added");
                                    page1ViewHolder.consoleNicknameTextView.setTextColor(getResources().getColor(R.color.red));
                                    page1ViewHolder.consoleTextView.setTextColor(getResources().getColor(R.color.red));
                                    page1ViewHolder.consoleLayout.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                    page1ViewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<Object> dataList = page1ViewHolder.adapter.getDataList();
                            nicknames = new ArrayList<>();
                            for(Object object: dataList){
                                nicknames.add((String) object);
                            }
                            viewPager.setCurrentItem(holder.getAdapterPosition() + 1);
                        }
                    });
                    break;
                case 2:
                    Page2ViewHolder page2ViewHolder = (Page2ViewHolder) holder;
                    page2ViewHolder.choosePathButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            page2ViewHolder.dialog.show();
                        }
                    });

                    page2ViewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // todo: make Team
                            String path = page2ViewHolder.sentencePathTextView.getText().toString().replace(" ", "");
                            makeTeam(teamName, path, nicknames, new Callback() {
                                @Override
                                public void OnCallback(Object object) {
                                    // success callback
                                    Message message = handler.obtainMessage();
                                    message.what = ACTIVITY_TERMINATE;
                                    handler.sendMessage(message);
                                }
                            }, new Callback() {
                                @Override
                                public void OnCallback(Object object) {
                                    // failure callback
                                }
                            });

                        }
                    });
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class Page0ViewHolder extends RecyclerView.ViewHolder{
            EditText editText;
            TextView button;
            public Page0ViewHolder(@NonNull View itemView) {
                super(itemView);
                editText = itemView.findViewById(R.id.pageAddTeam0EditText);
                button = itemView.findViewById(R.id.pageAddTeam0Button);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getHint());
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), 11, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setHint(spannableStringBuilder);
            }
        }

        public class Page1ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            EditText editText;
            CardView teamAddButton;
            LinearLayout consoleLayout;
            TextView consoleNicknameTextView;
            TextView consoleTextView;
            RecyclerView recyclerView;
            NormalRecyclerViewAdapter adapter;
            TextView button;
            public Page1ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.pageAddTeam1BannerTextView);
                editText = itemView.findViewById(R.id.pageAddTeam1NicknameEditText);
                teamAddButton = itemView.findViewById(R.id.pageAddTeam1AddButton);
                consoleLayout = itemView.findViewById(R.id.pageAddTeam1ConsoleLinearLayout);
                consoleNicknameTextView = itemView.findViewById(R.id.pageAddTeam1ConsoleTextView);
                consoleTextView = itemView.findViewById(R.id.pageAddTeam1Console2TextView);
                recyclerView = itemView.findViewById(R.id.pageAddTeam1RecyclerView);
                button = itemView.findViewById(R.id.pageAddTeam1Button);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText());
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), 11, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableStringBuilder);

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setJustifyContent(JustifyContent.FLEX_START);

                adapter = new NormalRecyclerViewAdapter(getApplicationContext(), NormalRecyclerViewAdapter.NICKNAME, null);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        }

        public class Page2ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            TextView choosePathButton;
            TextView sentencePathTextView;
            TextView consoleNumTextView;
            TextView consoleTextView;
            RecyclerView recyclerView;
            NormalRecyclerViewAdapter adapter;
            TextView button;
            ChoosePathDialog dialog;
            public Page2ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.pageAddTeam2BannerTextView);
                choosePathButton = itemView.findViewById(R.id.pageAddTeam2ChoosePathButton);
                sentencePathTextView = itemView.findViewById(R.id.pageAddTeam2SentencePathTextView);
                consoleNumTextView = itemView.findViewById(R.id.pageAddTeam2ConsoleNumTextView);
                consoleTextView = itemView.findViewById(R.id.pageAddTeam2ConsoleTextView);
                recyclerView = itemView.findViewById(R.id.pageAddTeam2RecyclerView);
                button = itemView.findViewById(R.id.pageAddTeam2Button);

                dialog = new ChoosePathDialog(MakeTeamActivity.this, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        // select dialog click listener
                        String target = (String) object;

                        List<Sentence> sentences = db.sentenceDao().getSentencesWithPath(target);
                        List<Object> sentencesObject = new ArrayList<Object>(sentences);
                        int totalNum = sentences.size();

                        if(totalNum >= 10 || true) {
                            adapter.setDataList(sentencesObject);
                            sentencePathTextView.setText(target);
                            consoleNumTextView.setText("There are #" + String.valueOf(totalNum) + " sentences");
                        }else{
                            Toast.makeText(MakeTeamActivity.this, "At least, the number of sentences is bigger than 10", Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText());
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), 11, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableStringBuilder);

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                adapter = new NormalRecyclerViewAdapter(getApplicationContext(), NormalRecyclerViewAdapter.SENTENCE, null);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        }
    }
    public void addUser(String nickname, Callback successCallback, Callback failureCallback){
        if(!nickname.equals(uid)) { // 해당 nicknmae이 본인 nickname이 아닐 때
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SelectQuery selectQuery = SelectQuery.builder().
                                tableName("users").optionField("nickname").option("'" + nickname + "'")
                                .build();
                        Log.d(TAG, "check user existence query: " + selectQuery.toString());

                        rdsConnect.request(RdsConnect.SELECT, RdsConnect.TABLE_USERS, selectQuery.toString(), null, new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                User[] users = UserHandler.string2users((String) object);
                                successCallback.OnCallback(users);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
            thread.start();
        }else{ // 입력한 nickname이 본인 nickname과 일치할 때
            // todo: layout에 유저를 위한 console view를 만들고 user를 추가할 수 없는 이유를 print 하기
            failureCallback.OnCallback(null);
        }
    }

    public void makeTeam(String teamName, String path, List<String> nicknameList, Callback successCallback, Callback failureCallback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String nicknames = "";
                for(String nickname: nicknameList){
                    nicknames += nickname + "/";
                }

                List<String> sqlQueryList = new ArrayList<>();
                try {
                    List<Sentence> sentences = db.sentenceDao().getSentencesWithPath(path);
                    for(int i=0; i<sentences.size(); i++) {
                        Sentence sentence = sentences.get(i);
                        for(String nickname: nicknameList) {
                            InsertQuery tempInsertQuery = new InsertQuery(new InsertQuery.SentencesBuilder(
                                    sentence.sentence,
                                    sentence.word,
                                    sentence.word_index,
                                    sentence.cheat_sheet,
                                    "team/" + teamName,
                                    nickname,
                                    "false",
                                    ""
                            ));
                            sqlQueryList.add(tempInsertQuery.toString());
                        }
                    }
                    EC2RESTApi.makeTeamAPI(teamName, nicknameList, sqlQueryList, uid, new Callback() {
                        @Override
                        public void OnCallback(Object object) {
                            Log.d(TAG, "EC2RESTApi:makeTeamAPI:result: " + (String)object);

                            // todo: determine success or failure

                            if(true){
                                successCallback.OnCallback(null);
                            }else{
                                failureCallback.OnCallback(null);
                            }
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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