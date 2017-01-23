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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ListView allwineslv;
    String sellername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        // hide keyboard -> doet niks?
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }


        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        rangeSeekbar.setMinValue(1900);
        rangeSeekbar.setMaxValue(2017);

// get min and max text view
        final TextView tvMin = (TextView) findViewById(R.id.textViewstart);
        final TextView tvMax = (TextView) findViewById(R.id.textViewend);

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

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

        allwineslv = (ListView) findViewById(R.id.lvbottles);
        createonclicklistener();

//        setnmpickers();
    }

//    public void setnmpickers (){
//        NumberPicker nmpickerstart =  (NumberPicker)findViewById(R.id.numberPickerstart);
//        nmpickerstart.setMaxValue(2016);
//        nmpickerstart.setMinValue(1900);
//        nmpickerstart.setValue(1980);
//
//        NumberPicker nmpickerend =  (NumberPicker)findViewById(R.id.numberPickerend);
//        nmpickerend.setMaxValue(2017);
//        nmpickerend.setMinValue(1901);
//        nmpickerend.setValue(2000);
//    }

    public void createonclicklistener (){
        allwineslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WineObject clickedwine = (WineObject) adapterView.getItemAtPosition(position);
                String clickedtitle = clickedwine.getTitle();
                String clickedyear = clickedwine.getYear();
                String clickedregion = clickedwine.getRegion();
                String clickedstory = clickedwine.getStory();
                final HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("title", clickedtitle);
                hash.put("year", clickedyear);
                hash.put("region", clickedregion);
                hash.put("story", clickedstory);

                // get name of seller
                String sellerid = clickedwine.getSellerid();
                // id meegeven voor chatfunctie (gaat wss via uid)
                hash.put("sellerid", sellerid);

                // name ophalen zodat je ziet met wie je kan chatten
                final DatabaseReference nameref = FirebaseDatabase.getInstance().getReference().child("users").child(sellerid).child("userinfo").child("name");

                nameref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sellername = dataSnapshot.getValue().toString();
                        getSellerName(sellername,hash);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                // om eerste keer niet null te krijgen


            }
        });
    }

    // zodat hij niet 1 achter loopt en null geeft
    public void getSellerName(String sellername, HashMap hash){
        Intent gotobuyfullinfo = new Intent(BuyActivity.this, Buyfullinfo.class);
        hash.put("sellername", sellername);
        gotobuyfullinfo.putExtra("fullhashmap", hash);
        startActivity(gotobuyfullinfo);
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


        DatabaseReference allwinesref = FirebaseDatabase.getInstance().getReference().child("wines");
        allwinesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<WineObject> bottles = new ArrayList<>();

                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    WineObject wineObject = bottle.getValue(WineObject.class);
                    bottles.add(wineObject);
                }

                Listadapter listadapter = new Listadapter(getApplicationContext(), bottles);
                allwineslv.setAdapter(listadapter);
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

