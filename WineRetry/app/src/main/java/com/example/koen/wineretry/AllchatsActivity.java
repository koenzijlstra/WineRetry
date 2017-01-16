package com.example.koen.wineretry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class AllchatsActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allchats);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        // dit stuk later uit oncreate halen?
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
                    startActivity(new Intent(AllchatsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        // listview voorbeeld
        ListView lvchats = (ListView) findViewById(R.id.lvchats);
        String[] chats = new String[] { "Bob", "Mark", "John"};
        ListAdapter theadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chats);
        lvchats.setAdapter(theadapter);
    }


    public void gotoallsellsc(View view){
        startActivity(new Intent(AllchatsActivity.this, AllsellsActivity.class));
        finish();
    }

    public void gotoallchatsc(View view){
        startActivity(new Intent(AllchatsActivity.this, AllchatsActivity.class));
        finish();
    }

    public void signout(View view) {
        auth.signOut();
    }

    public void gotobuyc(View view){
        startActivity(new Intent(AllchatsActivity.this, BuyActivity.class));
        finish();
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
}
