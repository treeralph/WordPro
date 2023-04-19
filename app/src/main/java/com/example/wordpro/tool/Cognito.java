package com.example.wordpro.tool;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

public class Cognito {

    public static String TAG = Cognito.class.getName();

    public static int CONFIRM_SUCCESS = 0;
    public static int CONFIRM_FAILURE = 1;

    public static int SUCCESS = 3;
    public static int FAILURE = 4;

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


    public void signUpInBackground(String userId, String password){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String device_token = task.getResult();
                    Log.i(TAG, "Success to get Device Token from FCM - Token: " + device_token);
                    addAttribute("custom:device_token", device_token);
                    userPool.signUpInBackground(userId, password, userAttributes, null, signUpCallback);
                }else{
                    // todo: fail to get device token from FCM server. please try again.
                    Log.e(TAG, "Fail to get device token from FCM - Exception: " + task.getException());
                }
            }
        });
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

    public void confirmUser(String userId, String code, Callback successCallback, Callback failureCallback){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        cognitoUser.confirmSignUpInBackground(code, false, new GenericHandler() {
            @Override
            public void onSuccess() {
                successCallback.OnCallback(CONFIRM_SUCCESS);
            }

            @Override
            public void onFailure(Exception exception) {
                exception.printStackTrace();
                failureCallback.OnCallback(CONFIRM_FAILURE);
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

    public void userLogin(String userId, String password, Callback successCallback, Callback failureCallback){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        this.userPassword = password;
        cognitoUser.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.d(TAG, "Sign in success");
                successCallback.OnCallback(userSession.getUsername());
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
            public void authenticationChallenge(ChallengeContinuation continuation) {}
            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "Sign in failure");
                failureCallback.OnCallback(null);
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

    public void getCurrentUserNickname(Callback callback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                userPool.getCurrentUser().getDetails(new GetDetailsHandler() {
                    @Override
                    public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                        CognitoUserAttributes cognitoUserAttributes = cognitoUserDetails.getAttributes();
                        Map<String, String> attributes = cognitoUserAttributes.getAttributes();
                        Log.i(TAG, "Get Cognito current user detail: Success - " + attributes.toString());
                        callback.OnCallback(attributes.get("nickname"));
                    }
                    @Override
                    public void onFailure(Exception exception) {
                        Log.e(TAG, "Get Cognito current user detail: Failure - " + exception.toString());
                    }
                });
            }
        });
        thread.start();
    }
}
