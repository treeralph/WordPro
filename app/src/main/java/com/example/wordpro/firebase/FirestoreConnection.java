package com.example.wordpro.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wordpro.firebase.data.Data;
import com.example.wordpro.firebase.data.User;
import com.example.wordpro.tool.Callback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreConnection {

    private static String TAG = FirebaseFirestore.class.getName();

    public static String userDataType = "user";
    private String dataType;

    FirebaseFirestore db;

    public FirestoreConnection(String dataType) {
        db = FirebaseFirestore.getInstance();
        this.dataType = dataType;
    }

    public void write(Data data, String path) {
        Log.d(TAG, "start to write data into firestore path: " + path + ", " + data.toString());
        DocumentReference ref = db.document(dataType + "/" + path);
        ref.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "success to write data into firestore path: " + path + ", " + data.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }

    public void read(String path, Callback callback) {
        Log.d(TAG, "start to read data from firestore path: " + path);
        DocumentReference ref = db.document(dataType + "/" + path);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User temp =  documentSnapshot.toObject(User.class);
                Log.d(TAG, "success to read data from firestore path: " + path + ", " + temp.toString());
                callback.OnCallback(temp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }
}
