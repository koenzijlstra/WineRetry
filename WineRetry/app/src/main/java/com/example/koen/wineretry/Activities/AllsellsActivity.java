package com.example.koen.wineretry.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.koen.wineretry.Other.BaseActivity;
import com.example.koen.wineretry.Listadapters.ListadapterBottles;
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

public class AllsellsActivity extends BaseActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    ArrayList<String> user_bottleids = new ArrayList<>();
    ListView userwineslv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsells);

        userwineslv = (ListView) findViewById(R.id.userbottles);

        setactionbar();
        getuserbottles();
        createonitemclicklistener();
        createauthstatelistener();
    }

    public void setactionbar (){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void getuserbottles (){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        // get all unique identifiers of winebottle that currentuser sells
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

    public void compare (){
        DatabaseReference allwinesref = FirebaseDatabase.getInstance().getReference().child("wines");
        allwinesref.addValueEventListener(new ValueEventListener() {
            ArrayList<WineObject> userbottles = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    String idbottle = bottle.getKey();
                    if (user_bottleids.contains(idbottle)){
                        WineObject wineObject = bottle.getValue(WineObject.class);
                        userbottles.add(wineObject);
                    }
                }

                final ListadapterBottles listadapterBottles = new ListadapterBottles(getApplicationContext(),
                        userbottles);
                userwineslv.setAdapter(listadapterBottles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void createauthstatelistener (){
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
                    startActivity(new Intent(AllsellsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    public void gotonewsell(View view){
        startActivity(new Intent(AllsellsActivity.this, NewsellActivity.class));
    }

    public void gotoallsellsa(View view){
        startActivity(new Intent(AllsellsActivity.this, AllsellsActivity.class));
        finish();
    }

    public void gotoallchatsa(View view){
        startActivity(new Intent(AllsellsActivity.this, AllchatsActivity.class));
        finish();
    }

    public void signout(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to log out?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        auth.signOut();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#aa0000"));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#aa0000"));
            }
        });
        alertDialog.show();
    }

    public void gotobuya(View view){
        startActivity(new Intent(AllsellsActivity.this, BuyActivity.class));
        finish();
    }

    public void showinfo (View view){
        Intent infoactivity = new Intent(AllsellsActivity.this, InfoActivity.class);
        infoactivity.putExtra("info", "Shown here are all the bottles you sell. When you click on one of them, all information about your bottle is shown. From there you can delete your bottle. You can add a new bottle you want to sell by clicking the 'new' button");
        startActivity(infoactivity);
    }

    // create authstatelistener
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    public void createonitemclicklistener (){
        // userwineslv = (ListView) findViewById(R.id.userbottles);
        userwineslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WineObject clickedwine = (WineObject) adapterView.getItemAtPosition(position);
                String clickedtitle = clickedwine.getTitle();
                String clickedyear = clickedwine.getYear();
                String clickedregion = clickedwine.getRegion();
                String clickedstory = clickedwine.getStory();
                // om te kunnen deleten bottleid meegeven
                String clickedbottleid = clickedwine.getBottleid();

                // stop alles in hashmap
                final HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("title", clickedtitle);
                hash.put("year", clickedyear);
                hash.put("region", clickedregion);
                hash.put("story", clickedstory);
                hash.put("bottleid", clickedbottleid);

                Intent gotosellfullinfo = new Intent(AllsellsActivity.this, SellfullinfoActivity.class);
                gotosellfullinfo.putExtra("hashmap", hash);
                startActivity(gotosellfullinfo);
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
}



