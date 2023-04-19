package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.Cognito;

public class SignUpActivity extends AppCompatActivity {

    ImageView imageView;
    EditText idEditText;
    EditText pwEditText;
    EditText pwCheckEditText;
    EditText nicknameEditText;
    EditText vcEditText; // Verification Code
    CardView getVCButton;
    CardView verifyButton;
    TextView console;

    Cognito authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        activityViewInitializer();
    }

    public void activityViewInitializer(){

        imageView = findViewById(R.id.signUpActivityImageView);
        idEditText = findViewById(R.id.signUpActivityIdEditText);
        pwEditText = findViewById(R.id.signUpActivityPwEditText);
        pwCheckEditText = findViewById(R.id.signUpActivityPwCheckEditText);
        nicknameEditText = findViewById(R.id.signUpActivityNicknameEditText);
        vcEditText = findViewById(R.id.signUpActivityVerificationEditText);
        getVCButton = findViewById(R.id.signUpActivityGetVerificationButton);
        verifyButton = findViewById(R.id.signUpActivityVerifyButton);
        console = findViewById(R.id.signUpActivityConsoleTextView);

        imageView.setImageResource(R.drawable.icon_wordpro_v2);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        imageView.setY(-400);

        authentication = new Cognito(getApplicationContext());

        getVCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString().replace(" ", "");
                String pw = pwEditText.getText().toString().replace(" ", "");
                String pwCheck = pwCheckEditText.getText().toString().replace(" ", "");
                String nickname = nicknameEditText.getText().toString().replace(" ", "");
                if(pw.endsWith(pwCheck)){
                    authentication.addAttribute("nickname", nickname);
                    authentication.addAttribute("email", id);
                    authentication.signUpInBackground(nickname, pw);
                } else{
                    console.setText("Input password correctly");
                    console.setVisibility(View.VISIBLE);
                }
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString().replace(" ", "");
                String pw = pwEditText.getText().toString().replace(" ", "");
                String vc = vcEditText.getText().toString().replace(" ", "");
                String nickname = nicknameEditText.getText().toString().replace(" ", "");
                authentication.confirmUser(nickname, vc, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        // Success
                        /*
                        Intent intent = new Intent();
                        intent.putExtra("id", id);
                        intent.putExtra("pw", pw);
                        setResult(RESULT_OK, intent);

                         */
                        finish();
                    }
                }, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        // Failure
                        console.setText("code unavailable");
                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}