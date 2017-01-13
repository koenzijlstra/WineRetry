package com.example.koen.wineretry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BuyActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        // authstatelistener that starts login activity when user is not logged in
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(BuyActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    // create authstatelistener
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    // remove authstatelistener
    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void gotoallsellsb(View view){
        startActivity(new Intent(BuyActivity.this, AllsellsActivity.class));
        finish();
    }

    public void gotoallchatsb(View view){
        startActivity(new Intent(BuyActivity.this, AllchatsActivity.class));
        finish();
    }

    public void signout(View view) {
        auth.signOut();
    }

//    public void gotobuyb(View view){
//        startActivity(new Intent(BuyActivity.this, BuyActivity.class));
//    }
}

