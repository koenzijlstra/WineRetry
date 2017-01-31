package com.example.koen.wineretry.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyActivity extends BaseActivity implements View.OnClickListener{

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ListView allwineslv;
    String sellername;
    CrystalRangeSeekbar rangeSeekbar;
    String clickedtitle;
    String clickedyear;
    String clickedregion;
    String clickedstory;
    WineObject clickedwine;
    String sellerid;
    HashMap<String, String> hash;
    Spinner spinnertag;
    String tag;
    String min;
    String max;
    DatabaseReference winesref;
    Query selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        allwineslv = (ListView) findViewById(R.id.lvbottles);
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);

        hideProgressDialog();
        setActionbar();
        setRangebar();
        setSpinner();
        createAuthstatelistener();
        fillListview();
        createOnclicklistener();
        setclicklisteners();

        // hide keyboard -> doet niks?
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
    }
    // Create an ArrayAdapter using the string array and a default spinner layout, specify the
    // layout to use and apply the adapter to the spinner
    public void setSpinner (){
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.winetags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setActionbar (){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
        }
    }

    // Create authstatelistener. If user is not logged in, navigate to login activity
    public void createAuthstatelistener (){
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // If user is null (not logged in) go to login
                if (user == null) {
                    startActivity(new Intent(BuyActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

    }

    // Set the range bar with a minimum and maximum value. Set the textviews to the (changing)
    // values of the rangebar
    public void setRangebar (){
        rangeSeekbar.setMinValue(1900);
        rangeSeekbar.setMaxValue(2017);
        final TextView tvMin = (TextView) findViewById(R.id.textViewstart);
        final TextView tvMax = (TextView) findViewById(R.id.textViewend);

        // Set changelistener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });
    }

    // Get the filters that the user applied, and only retreive the bottles from firebase that have
    // a year between the min and max value. Then filter this arraylist by only adding the bottles
    // with the correct tag to a new arraylist (selectedbottles)
    public void filter (){
        getFilters();
        selected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<WineObject> selectedbottles = new ArrayList<>();
                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    WineObject wineObject = bottle.getValue(WineObject.class);
                    // Filter the arraylist with the wines with correct years on their tags
                    if (tag.equals("All")) {
                        selectedbottles.add(wineObject);
                    }
                    else{
                        // Compare the tag of the bottle with the selected tag
                        String bottletag = wineObject.getTag();
                        if (bottletag.equals(tag)){
                            selectedbottles.add(wineObject);
                        }
                    }
                }

                // Set the listadapter
                ListadapterBottles listadapterBottles = new ListadapterBottles(getApplicationContext(), selectedbottles);
                allwineslv.setAdapter(listadapterBottles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    // Get the selected filter (tag and years), and retreive the wines from firebase with the
    // correct year
    public void getFilters (){
        // get selected tag (red/ white etc)
        spinnertag =(Spinner) findViewById(R.id.spinner);
        tag = spinnertag.getSelectedItem().toString();
        min = rangeSeekbar.getSelectedMinValue().toString();
        max = rangeSeekbar.getSelectedMaxValue().toString();

        // Toast what the user selected
        String selectedstring = getResources().getString(R.string.youselected) + tag + getResources()
                .getString(R.string.from)+ min + getResources().getString(R.string.to)+ max;
        Toast.makeText(getApplicationContext(), selectedstring, Toast.LENGTH_LONG).show();

        winesref = FirebaseDatabase.getInstance().getReference().child("wines");
        selected = winesref.orderByChild("year").startAt(min).endAt(max);
    }

    // Create an onclicklistener, create a hashmap with all the needed information and retreive the
    // name of the seller
    public void createOnclicklistener (){
        allwineslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                clickedwine = (WineObject) adapterView.getItemAtPosition(position);
                createHashmap();

                final DatabaseReference nameref = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(sellerid).child("userinfo").child("name");

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
            }
        });
    }

    // When hte infobutton is clicked, navigate to infoActivity with activity specific string
    public void showInfo (){
        Intent infoactivity = new Intent(BuyActivity.this, InfoActivity.class);
        infoactivity.putExtra("info", getResources().getString(R.string.buyinfo));
        startActivity(infoactivity);
    }

    // Create a hashmap with all the info of the Wine object that was clicked on
    public void createHashmap (){
        clickedtitle = clickedwine.getTitle();
        clickedyear = clickedwine.getYear();
        clickedregion = clickedwine.getRegion();
        clickedstory = clickedwine.getStory();
        hash = new HashMap<String, String>();
        hash.put("title", clickedtitle);
        hash.put("year", clickedyear);
        hash.put("region", clickedregion);
        hash.put("story", clickedstory);
        sellerid = clickedwine.getSellerid();
        hash.put("sellerid", sellerid);
    }

    // This seperate function solves the problem of not receiving the seller name the first time. Is
    // called in the ondatachange of the onclicklistener of the listview
    public void getSellerName(String sellername, HashMap hash){
        Intent gotobuyfullinfo = new Intent(BuyActivity.this, BuyfullinfoActivity.class);
        hash.put("sellername", sellername);
        gotobuyfullinfo.putExtra("fullhashmap", hash);
        startActivity(gotobuyfullinfo);
    }

    // app crasht als ik keys probeer te hiden?
    public void hidekeys(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void fillListview (){
        DatabaseReference allwinesref = FirebaseDatabase.getInstance().getReference().child("wines");
        allwinesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<WineObject> bottles = new ArrayList<>();

                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    WineObject wineObject = bottle.getValue(WineObject.class);
                    bottles.add(wineObject);
                }

                ListadapterBottles listadapterBottles = new ListadapterBottles(getApplicationContext(), bottles);
                allwineslv.setAdapter(listadapterBottles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // create authstatelistener (anders gaat user naar login)
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

    public void setclicklisteners(){
        findViewById(R.id.sell).setOnClickListener(this);
        findViewById(R.id.buy).setOnClickListener(this);
        findViewById(R.id.chats).setOnClickListener(this);
        findViewById(R.id.signout).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);
        findViewById(R.id.filterbutton).setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buy:
                startActivity(new Intent(BuyActivity.this, BuyActivity.class));
                finish();
                break;
            case R.id.sell:
                startActivity(new Intent(BuyActivity.this, AllsellsActivity.class));
                finish();
                break;
            case R.id.chats:
                startActivity(new Intent(BuyActivity.this, AllchatsActivity.class));
                finish();
                break;
            case R.id.newsell:
                startActivity(new Intent(BuyActivity.this, NewsellActivity.class));
                break;
            case R.id.signout:
                Signout signoutclass =new Signout();
                signoutclass.signout(this,auth);
                break;
            case R.id.info:
                showInfo();
                break;
            case R.id.filterbutton:
                filter();
                break;
        }
    }
}

