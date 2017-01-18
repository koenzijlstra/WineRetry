package com.example.koen.wineretry;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);


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

    // app crasht als ik keys probeer te hiden?
    public void hidekeys(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        // create authstatelistener (anders gaat user naar login)
        auth.addAuthStateListener(authListener);


        DatabaseReference titlewinetestref = FirebaseDatabase.getInstance().getReference().child("wines");
        titlewinetestref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<WineObject> bottles = new ArrayList<>();

                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    WineObject wineObject = bottle.getValue(WineObject.class);
                    bottles.add(wineObject);
                }

                Listadapter listadapter = new Listadapter(getApplicationContext(), bottles);
                ListView winelv = (ListView) findViewById(R.id.lvbottles);
                winelv.setAdapter(listadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void gotobuyb(View view){
        startActivity(new Intent(BuyActivity.this, BuyActivity.class));
    }
}

