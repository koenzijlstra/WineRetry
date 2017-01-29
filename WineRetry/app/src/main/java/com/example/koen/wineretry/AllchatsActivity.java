package com.example.koen.wineretry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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
import java.util.Arrays;
import java.util.HashMap;

public class AllchatsActivity extends BaseActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    public ListView lvchats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allchats);

        lvchats = (ListView) findViewById(R.id.lvchats);

        setactionbar();
        setauthstatelistener();
        setlv();
        createonitemclicklistener ();

    }

    public void setactionbar (){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void setauthstatelistener (){
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
    }

    public void setlv (){

        auth.addAuthStateListener(authListener);
        final String uid = auth.getCurrentUser().getUid();

        DatabaseReference userschatsref = FirebaseDatabase.getInstance().getReference().
                child("users").child(uid).child("chats");
        userschatsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<OtheruserObject> chatters = new ArrayList<>();

                for (DataSnapshot otheruser : dataSnapshot.getChildren()){
                    OtheruserObject otheruserObject = otheruser.child("other").
                            getValue(OtheruserObject.class);
                    chatters.add(otheruserObject);

//                    ListadapterChats listadapterChats = new ListadapterChats(getApplicationContext()
//                            , chatters);
                    // lvchats.setAdapter(listadapterChats);
                }
                ListadapterChats listadapterChats = new ListadapterChats(getApplicationContext()
                        , chatters);
                lvchats.setAdapter(listadapterChats);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void createonitemclicklistener (){
        lvchats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                OtheruserObject otheruserObject = (OtheruserObject) adapterView.
                        getItemAtPosition(position);
                String sellerid = otheruserObject.getUserIDother();

                // hier vast berichten op gelezen zetten
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                        .child("chats").child(sellerid).child("read").setValue(true);

                // ga naar chat
                Intent gotochat = new Intent(AllchatsActivity.this, ChatActivity.class);
                gotochat.putExtra("sellerid", sellerid);
                startActivity(gotochat);
            }
        });
    }

    public void showinfo (View view){
        Intent infoactivity = new Intent(AllchatsActivity.this, InfoActivity.class);
        infoactivity.putExtra("info", "Shown here are all your chats with other users. If you have not read all the messages from another user yet, the name will be shown darker.");
        startActivity(infoactivity);
    }

    public void gotoallsellsc(View view){
        // showProgressDialog();
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
