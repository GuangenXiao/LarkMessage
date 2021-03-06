package com.example.larkmessage.unit;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.larkmessage.entity.UserItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class userUnit {
    public void  createAccountInfo(FirebaseUser user, UserItem u , final Activity activity)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(user.getEmail())
                .set(u)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("error", "DocumentSnapshot successfully written!");
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error", "Error writing document", e);
                        activity.finish();
                    }
                });


    }
    public void getAccountInfo(final FirebaseUser user, Activity activity)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserItem userItem=null;

        DocumentReference docRef = db.collection("UserList").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> m = document.getData();
                        try {
                            UserItem userItem = new UserItem.Builder(m.get("Username").toString(),m.get("Username").toString()).userId(FirebaseAuth.getInstance().getCurrentUser().getUid()).Build();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

    }
    public void changeUserInfo(final UserItem newUInfo)
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null ||  newUInfo==null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("UserList").document(user.getEmail());
        docRef.update("bgColor",newUInfo.getBgColor()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {



            }
        });


    }
/*
    public  void insert(Map<String,Object> m, FirebaseUser user )
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(user.getEmail())
                .set(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("error", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error", "Error writing document", e);
                    }
                });
    }*/
}
