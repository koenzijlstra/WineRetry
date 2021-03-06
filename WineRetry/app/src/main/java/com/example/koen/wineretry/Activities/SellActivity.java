package com.example.koen.wineretry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.koen.wineretry.Other.BaseActivity;
import com.example.koen.wineretry.Listadapters.ListadapterBottles;
import com.example.koen.wineretry.Other.Signout;
import com.example.koen.wineretry.R;
import com.example.koen.wineretry.Objects.WineObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* Activity that displays all winebottles that current user sells. This is done by first retrieving
* all ID's of bottles that user sells, and then comparing these ID's with the bottleID's of the
* bottles under root/wines. This way the complete wineobjects can be retrieved that the user sells.
* ListadapterBottles is used as custom listadapter. When the user clicks an item in the listview,
* the user is navigated to SellfullInfo. Here all info of the bottle is shown, and here the user can
* delete the bottle.
*/


public class SellActivity extends BaseActivity implements View.OnClickListener {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ArrayList<String> user_bottleids = new ArrayList<>();
    private ListView userwineslv;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsells);

        userwineslv = (ListView) findViewById(R.id.userbottles);

        setclicklisteners();
        setActionbar();
        getUserbottles();
        createOnitemclicklistener();
        createAuthstatelistener();
    }

    // Set the custom support action bar
    public void setActionbar (){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
        }
    }

    // Get all the bottles that current user sells by comparing the id's of all bottles with the
    public void getUserbottles (){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
        }


        // Get all unique identifiers of winebottle that currentuser sells
        DatabaseReference userswinesref = FirebaseDatabase.getInstance().getReference().
                child("users").child(uid).child("wines");
        userswinesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bottleid : dataSnapshot.getChildren()){
                    String idbottle = bottleid.getValue().toString();
                    user_bottleids.add(idbottle);
                }
                compare();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Get all the bottle id's of wines that are being sold. Compare id's of bottles of user with
    // all id's, add the matching wines to userbottles (an arraylist of wine objects)
    public void compare (){
        DatabaseReference allwinesref = FirebaseDatabase.getInstance().getReference()
                .child("wines");
        allwinesref.addValueEventListener(new ValueEventListener() {
            ArrayList<WineObject> userbottles = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    String idbottle = bottle.getKey();
                    // This condition is where id's are compared
                    if (user_bottleids.contains(idbottle)){
                        WineObject wineObject = bottle.getValue(WineObject.class);
                        userbottles.add(wineObject);
                    }
                }
                final ListadapterBottles listadapterBottles = new
                        ListadapterBottles(getApplicationContext(), userbottles);
                userwineslv.setAdapter(listadapterBottles);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void createAuthstatelistener (){
        auth = FirebaseAuth.getInstance();
        // Authstatelistener that starts login activity when user is not logged in
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(SellActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    // Give an info string to the activity that will display the information about this activity
    public void showInfo (){
        Intent infoactivity = new Intent(SellActivity.this, InfoActivity.class);
        infoactivity.putExtra("info", getResources().getString(R.string.infosells));
        startActivity(infoactivity);
    }

    // Create authstatelistener
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    // Create an onitemclicklistener. Create a Wineobject of the wine that is clicked on. Give all
    // info via a hashmap to next activity
    public void createOnitemclicklistener (){
        userwineslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WineObject clickedwine = (WineObject) adapterView.getItemAtPosition(position);
                String clickedtitle = clickedwine.getTitle();
                String clickedyear = clickedwine.getYear();
                String clickedregion = clickedwine.getRegion();
                String clickedstory = clickedwine.getStory();
                // Also add bottleid to be able to delete later
                String clickedbottleid = clickedwine.getBottleid();

                // Create hashmap
                final HashMap<String, String> hash = new HashMap<>();
                hash.put("title", clickedtitle);
                hash.put("year", clickedyear);
                hash.put("region", clickedregion);
                hash.put("story", clickedstory);
                hash.put("bottleid", clickedbottleid);

                // Navigate to Sellfullinfo with all information about the bottle
                Intent gotosellfullinfo = new Intent(SellActivity.this, SellfullinfoActivity.class);
                gotosellfullinfo.putExtra("hashmap", hash);
                startActivity(gotosellfullinfo);
            }
        });
    }

    // Remove authstatelistener
    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    // Set clicklisteners on all buttons
    public void setclicklisteners(){
        findViewById(R.id.sell).setOnClickListener(this);
        findViewById(R.id.buy).setOnClickListener(this);
        findViewById(R.id.chats).setOnClickListener(this);
        findViewById(R.id.newsell).setOnClickListener(this);
        findViewById(R.id.signout).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Log.wtf("test", Integer.toString(view.getId()));
        switch (view.getId()){
            case R.id.buy:
                startActivity(new Intent(SellActivity.this, BuyActivity.class));
                finish();
                break;
            case R.id.sell:
                startActivity(new Intent(SellActivity.this, SellActivity.class));
                finish();
                break;
            case R.id.chats:
                startActivity(new Intent(SellActivity.this, AllchatsActivity.class));
                finish();
                break;
            case R.id.newsell:
                startActivity(new Intent(SellActivity.this, NewsellActivity.class));
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



