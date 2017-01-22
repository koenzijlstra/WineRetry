package com.example.koen.wineretry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;

public class AllsellsActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    ArrayList<String> user_bottleids = new ArrayList<>();
    private ListView userwineslv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsells);

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
                    startActivity(new Intent(AllsellsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        createonitemclicklistener();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        // get all unique identifiers of winebottle that currentuser sells
        DatabaseReference userswinesref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wines");
        userswinesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ArrayList<String> user_bottleids = new ArrayList<>();

                for (DataSnapshot bottleid : dataSnapshot.getChildren()){
                    String idbottle = bottleid.getValue().toString();
                    user_bottleids.add(idbottle);
                }
                // Log.d("test2", user_bottleids.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference allwinesref = FirebaseDatabase.getInstance().getReference().child("wines");
        allwinesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<WineObject> userbottles = new ArrayList<>();

                for (DataSnapshot bottle : dataSnapshot.getChildren()){

                    String idbottle = bottle.getKey();
                    if (user_bottleids.contains(idbottle)){
                        WineObject wineObject = bottle.getValue(WineObject.class);
                        userbottles.add(wineObject);
                    }
                }

                Listadapter listadapter = new Listadapter(getApplicationContext(), userbottles);
                userwineslv = (ListView) findViewById(R.id.userbottles);
                userwineslv.setAdapter(listadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        auth.signOut();
    }

    public void gotobuya(View view){
        startActivity(new Intent(AllsellsActivity.this, BuyActivity.class));
        finish();
    }

    // create authstatelistener
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);



//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        String uid = auth.getCurrentUser().getUid();
//
//        // get all unique identifiers of winebottle that currentuser sells
//        DatabaseReference userswinesref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wines");
//        userswinesref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // ArrayList<String> user_bottleids = new ArrayList<>();
//
//                for (DataSnapshot bottleid : dataSnapshot.getChildren()){
//                    String idbottle = bottleid.getValue().toString();
//                    user_bottleids.add(idbottle);
//                }
//                // Log.d("test2", user_bottleids.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//        DatabaseReference allwinesref = FirebaseDatabase.getInstance().getReference().child("wines");
//        allwinesref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                final ArrayList<WineObject> userbottles = new ArrayList<>();
//
//                for (DataSnapshot bottle : dataSnapshot.getChildren()){
//
//                    String idbottle = bottle.getKey();
//                    if (user_bottleids.contains(idbottle)){
//                        WineObject wineObject = bottle.getValue(WineObject.class);
//                        userbottles.add(wineObject);
//                    }
//                }
//
//                Listadapter listadapter = new Listadapter(getApplicationContext(), userbottles);
//                userwineslv = (ListView) findViewById(R.id.userbottles);
//                userwineslv.setAdapter(listadapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public void createonitemclicklistener (){
        userwineslv = (ListView) findViewById(R.id.userbottles);
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

                Intent gotosellfullinfo = new Intent(AllsellsActivity.this, Sellfullinfo.class);
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



