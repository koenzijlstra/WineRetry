package com.example.koen.wineretry.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.koen.wineretry.Other.BaseActivity;
import com.example.koen.wineretry.Listadapters.ListadapterChats;
import com.example.koen.wineretry.Objects.OtheruserObject;
import com.example.koen.wineretry.Other.Signout;
import com.example.koen.wineretry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AllchatsActivity extends BaseActivity implements View.OnClickListener{

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ListView lvchats;
    private ListadapterChats listadapterChats;
    private ArrayList<OtheruserObject> chatters;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allchats);

        lvchats = (ListView) findViewById(R.id.lvchats);

        setActionbar();
        setAuthstatelistener();
        setLv();
        createOnitemclicklistener ();
        setclicklisteners();

        // Create the custom listadapter for the listview that displays all names of chats. Adapter
        // is created with an empty arraylist chatters
        chatters = new ArrayList<>();
        listadapterChats =  new ListadapterChats(getApplicationContext()
                , chatters);
    }

    // Set the custom supportactionbar
    public void setActionbar (){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
        }
    }

    // Set authstatelistener that starts login activity when user is not logged in
    public void setAuthstatelistener (){
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // launch login activity
                    startActivity(new Intent(AllchatsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    // Fill the listview lvchats with all the names the user has a chat with
    public void setLv (){
        auth.addAuthStateListener(authListener);
        uid = auth.getCurrentUser().getUid();

        // Get DB reference to all the chats of current user
        DatabaseReference userschatsref = FirebaseDatabase.getInstance().getReference().
                child("users").child(uid).child("chats");
        userschatsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot otheruser : dataSnapshot.getChildren()){
                    // Create a new object for each user that current user has a chat with
                    OtheruserObject otheruserObject = otheruser.child("other").
                            getValue(OtheruserObject.class);
                    chatters.add(otheruserObject);
                    lvchats.setAdapter(listadapterChats);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    // Set an onitemclicklistener on the listview, navigates to chat with the user that was clicked
    public void createOnitemclicklistener (){
        lvchats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                OtheruserObject otheruserObject = (OtheruserObject) adapterView.
                        getItemAtPosition(position);
                String sellerid = otheruserObject.getUserIDother();

                // Set the read variable under the chat of current user with other user on true
                FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                        .child("chats").child(sellerid).child("read").setValue(true);
                // Clear the list, otherwise adapter would add users a second time
                chatters.clear();

                // Navigate to chat
                Intent gotochat = new Intent(AllchatsActivity.this, ChatActivity.class);
                gotochat.putExtra("sellerid", sellerid);
                startActivity(gotochat);
            }
        });
    }

    // Show the dialog themed info activity, give activity specific info to intent
    public void showInfo (){
        Intent infoactivity = new Intent(AllchatsActivity.this, InfoActivity.class);
        infoactivity.putExtra("info", getResources().getString(R.string.infochats));
        startActivity(infoactivity);
    }


    // Create authstatelistener
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    // Remove authstatelistener
    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void setclicklisteners(){
        findViewById(R.id.sell).setOnClickListener(this);
        findViewById(R.id.buy).setOnClickListener(this);
        findViewById(R.id.chats).setOnClickListener(this);
        findViewById(R.id.signout).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buy:
                startActivity(new Intent(AllchatsActivity.this, BuyActivity.class));
                finish();
                break;
            case R.id.sell:
                startActivity(new Intent(AllchatsActivity.this, AllsellsActivity.class));
                finish();
                break;
            case R.id.chats:
                startActivity(new Intent(AllchatsActivity.this, AllchatsActivity.class));
                finish();
                break;
            case R.id.signout:
                Signout signoutclass =new Signout();
                signoutclass.signout(this,auth);
                break;
            case R.id.info:
                showInfo();
                break;
        }
    }
}
