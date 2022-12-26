package com.example.wordpro.tool;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.example.wordpro.MainActivity;

public class Cognito {

    public static int CONFIRM_SUCCESS = 0;
    public static int CONFIRM_FAILURE = 1;

    private String TAG = "Cognito";
    // ############################################################# Information about Cognito Pool
    private String poolID = "ap-northeast-2_QT4P2AbO5";
    private String clientID = "6u6g5ojdham6icvkb0v04r4dgu";
    private Regions awsRegion = Regions.AP_NORTHEAST_2;         // Place your Region
    // ############################################################# End of Information about Cognito Pool
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;       // Used for adding attributes to the user
    private Context appContext;
    private String userPassword;                        // Used for Login

    public Cognito(Context context){
        appContext = context;
        userPool = new CognitoUserPool(context, this.poolID, this.clientID, null, this.awsRegion);
        userAttributes = new CognitoUserAttributes();
    }

    public boolean userExist(String userId){

        CognitoUser cognitoUser = userPool.getUser(userId);
        if(false){
            // todo: there exists
            return true;
        }else{
            return false;
        }
    }

    public void signUpInBackground(String userId, String password){
        userPool.signUpInBackground(userId, password, this.userAttributes, null, signUpCallback);
        //userPool.signUp(userId, password, this.userAttributes, null, signUpCallback);
    }

    SignUpHandler signUpCallback = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            // Sign-up was successful
            Log.d(TAG, "Sign-up success");
            Toast.makeText(appContext,"Sign-up success", Toast.LENGTH_LONG).show();
            // Check if this user (cognitoUser) needs to be confirmed
            if(signUpResult.getUserConfirmed()){
                // This user must be confirmed and a confirmation code was sent to the user
                // cognitoUserCodeDeliveryDetails will indicate where the confirmation code was sent
                // Get the confirmation code from user
            }else{
                Toast.makeText(appContext,"Error: User Confirmed before", Toast.LENGTH_LONG).show();
                // The user has already been confirmed
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(appContext,"Sign-up failed", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Sign-up failed: " + exception);
        }
    };

    public void confirmUser(String userId, String code, Callback callback){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        cognitoUser.confirmSignUpInBackground(code, false, new GenericHandler() {
            @Override
            public void onSuccess() {
                callback.OnCallback(CONFIRM_SUCCESS);
            }

            @Override
            public void onFailure(Exception exception) {
                callback.OnCallback(CONFIRM_FAILURE);
            }
        });
        //cognitoUser.confirmSignUp(code,false, confirmationCallback);

    }
    // Callback handler for confirmSignUp API
    GenericHandler confirmationCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            // User was successfully confirmed
            Toast.makeText(appContext,"User Confirmed", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onFailure(Exception exception) {
            // User confirmation failed. Check exception for the cause.

        }
    };

    public void addAttribute(String key, String value){
        userAttributes.addAttribute(key, value);
    }

    public void userLogin(String userId, String password, Callback callback){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        this.userPassword = password;
        cognitoUser.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Toast.makeText(appContext,"Sign in success", Toast.LENGTH_LONG).show();
                callback.OnCallback(userSession.getUsername());
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                // The API needs user sign-in credentials to continue
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
                // Pass the user sign-in credentials to the continuation
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                // Allow the sign-in to continue
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                // Multi-factor authentication is required; get the verification code from user
                //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
                // Allow the sign-in process to continue
                //multiFactorAuthenticationContinuation.continueTask();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }

            @Override
            public void onFailure(Exception exception) {
                // Sign-in failed, check exception for the cause
                Toast.makeText(appContext,"Sign in Failure", Toast.LENGTH_LONG).show();
            }
        });

    }


    public String getPoolID() {
        return poolID;
    }

    public void setPoolID(String poolID) {
        this.poolID = poolID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    /*
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
     */
    public Regions getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(Regions awsRegion) {
        this.awsRegion = awsRegion;
    }

    public CognitoUser getCurrentUser(){
        return userPool.getCurrentUser();
    }

}
