package com.example.wordpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.wordpro.firebase.FirestoreConnection;
import com.example.wordpro.firebase.data.Data;
import com.example.wordpro.firebase.data.User;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.Cognito;

public class SignInActivity extends AppCompatActivity {

    private int SignUpActivity_REQUESTCODE = 0;
    private String TAG = "SignInActivity";

    EditText emailEditText;
    EditText pwEditText;
    TextView signInBtn;
    TextView signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.LoginActivityEmailEditText);
        pwEditText = findViewById(R.id.LoginActivityPasswordEditText);
        signInBtn = findViewById(R.id.LoginActivitySignInButton);
        signUpBtn = findViewById(R.id.LoginActivitySignUpButton);

        emailEditText.setText("treeralph@gmail.com");
        pwEditText.setText("!qlqjs940");

        TextView testButton = findViewById(R.id.LoginActivityTestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "click!", Toast.LENGTH_LONG).show();
                FirestoreConnection firestoreConnection = new FirestoreConnection(FirestoreConnection.userDataType);
                firestoreConnection.write(new User("jun", "abcd", "boo"), "jun");
            }
        });

        TextView test2Button = findViewById(R.id.LoginActivityTest2Button);
        test2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "click!", Toast.LENGTH_LONG).show();
                FirestoreConnection firestoreConnection = new FirestoreConnection(FirestoreConnection.userDataType);
                firestoreConnection.read("jun", new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        User data = (User) object;
                        test2Button.setText(data.toString());
                        test2Button.setTextSize(10);
                    }
                });
            }
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.userLogin(emailEditText.getText().toString().replace(" ", ""), pwEditText.getText().toString(),
                        new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                String uid = (String) object;
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, SignUpActivity_REQUESTCODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SignUpActivity_REQUESTCODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(SignInActivity.this, "Sign Up: Complete!", Toast.LENGTH_LONG);
            }else{
                Toast.makeText(SignInActivity.this, "Sign Up: Failure!", Toast.LENGTH_LONG);
            }
        }
    }
}