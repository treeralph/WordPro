package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.Cognito;

public class SignUpActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText codeEditText;
    TextView confirmBtn;
    EditText passwordEditText;
    EditText rePasswordEditText;
    EditText nicknameEditText;
    TextView confirmStatus;
    TextView signUpBtn;

    Cognito authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.SignUpActivityEmailEditText);
        passwordEditText = findViewById(R.id.SignUpActivityPasswordEditText);
        rePasswordEditText = findViewById(R.id.SignUpActivityRePasswordEditText);
        nicknameEditText = findViewById(R.id.SignUpActivityNicknameEditText);
        confirmStatus = findViewById(R.id.SignUpActivityConfirmStatusTextView);
        signUpBtn = findViewById(R.id.SignUpActivitySignUpButton);
        codeEditText = findViewById(R.id.SignUpActivityEmailConfirmCodeEditText);
        confirmBtn = findViewById(R.id.SignUpActivityConfirmUserButton);

        authentication = new Cognito(getApplicationContext());

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordEditText.getText().toString().endsWith(rePasswordEditText.getText().toString())){
                    String email = emailEditText.getText().toString().replace(" ", "");
                    String nickname = nicknameEditText.getText().toString().replace(" ", "");
                    authentication.addAttribute("nickname", nickname);
                    authentication.addAttribute("email", email);
                    authentication.signUpInBackground(nickname, passwordEditText.getText().toString());
                }
                else{

                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authentication.confirmUser(nicknameEditText.getText().toString().replace(" ", ""),
                        codeEditText.getText().toString().replace(" ", ""),
                        new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                int check = (Integer) object;
                                if(check == Cognito.CONFIRM_SUCCESS){
                                    setResult(RESULT_OK);
                                    finish();
                                }else{
                                    confirmStatus.setText("code unavailable");
                                    confirmStatus.setTextColor(Color.RED);
                                }
                            }
                        });
            }
        });
    }
}