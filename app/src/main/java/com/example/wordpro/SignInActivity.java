package com.example.wordpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.Cognito;

public class SignInActivity extends AppCompatActivity {

    private String TAG = "SignInActivity";

    int SIGNUP_REQUEST_CODE = 0;

    ImageView imageView;
    CardView cardView;
    EditText idEditText;
    EditText pwEditText;
    CardView signInButton;
    CardView signUpButton;
    TextView console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        activityViewInitializer();
    }

    public void activityViewInitializer(){

        imageView = findViewById(R.id.signInActivityImageView);
        cardView = findViewById(R.id.signInActivityCardView);
        idEditText = findViewById(R.id.signInActivityIdEditText);
        pwEditText = findViewById(R.id.signInActivityPwEditText);
        signInButton = findViewById(R.id.signInActivitySignInButton);
        signUpButton = findViewById(R.id.signInActivitySignUpButton);
        console = findViewById(R.id.signInActivityConsoleTextView);

        idEditText.setText("treeralph@gmail.com");
        pwEditText.setText("!qlqjs940");

        imageView.setImageResource(R.drawable.icon_wordpro_v2);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        imageView.setY(-400);

        Transition transition = getWindow().getEnterTransition();
        transition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                cardView.setVisibility(View.VISIBLE);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                console.setVisibility(View.INVISIBLE);
                String id = idEditText.getText().toString().replace(" ", "");
                String pw = pwEditText.getText().toString().replace(" ", "");
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.userLogin(id, pw, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        // SignInSuccess
                        String uid = (String) object;
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        finish();
                    }
                }, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        // SignInFailure
                        console.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, SIGNUP_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        try{
            Intent testIntent = getIntent();
            Bundle bundle = testIntent.getExtras();
            String wFCM = bundle.getString("wFCM");
            String teamIdentifier = bundle.getString("team_identifier");
            String teamName = bundle.getString("team_name");
            String makeRequestUser = bundle.getString("make_team_request_user");

            if(wFCM.equals("INVITE_TEAM")) {
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.getCurrentUserNickname(new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        if (object != null) {
                            String currentUserNickname = (String) object;
                            Intent intent = new Intent(SignInActivity.this, InviteActivity.class);
                            intent.putExtra("uid", currentUserNickname);
                            intent.putExtra("wFCM", wFCM);
                            intent.putExtra("teamIdentifier", teamIdentifier);
                            intent.putExtra("teamName", teamName);
                            intent.putExtra("makeRequestUser", makeRequestUser);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }else{

            }
        }catch(Exception e){
            Cognito authentication = new Cognito(getApplicationContext());
            authentication.getCurrentUserNickname(new Callback() {
                @Override
                public void OnCallback(Object object) {
                    if(object != null){
                        String currentUserNickname = (String) object;
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("uid", currentUserNickname);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGNUP_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // SIGNUP: Success: send id & pw
                String id = data.getStringExtra("id");
                String pw = data.getStringExtra("pw");
                idEditText.setText(id);
                pwEditText.setText(pw);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}