package com.example.koen.wineretry.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
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

public class BuyActivity extends BaseActivity {

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
        setactionbar();
        setrangebar();
        setspinner();
        createauthstatelistener();
        createonclicklistener();

        // hide keyboard -> doet niks?
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }

    }

    public void setspinner (){
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.winetags, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void setactionbar (){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
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
                    startActivity(new Intent(BuyActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

    }
    public void setrangebar (){
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
    }



    public void filter (View view){

        getfilters();

        selected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<WineObject> selectedbottles = new ArrayList<>();

                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    WineObject wineObject = bottle.getValue(WineObject.class);
                    if (tag.equals("All")) {
                        selectedbottles.add(wineObject);
                    }
                    else{
                        // vergelijk geselecteerde tag met tag van de flessen
                        String bottletag = wineObject.getTag();
                        if (bottletag.equals(tag)){
                            selectedbottles.add(wineObject);
                        }
                    }
                }

                ListadapterBottles listadapterBottles = new ListadapterBottles(getApplicationContext(), selectedbottles);
                allwineslv.setAdapter(listadapterBottles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getfilters (){
        // get selected tag (red/ white etc)
        spinnertag =(Spinner) findViewById(R.id.spinner);
        tag = spinnertag.getSelectedItem().toString();

        // niet naar string, vergelijken met int in object
        min = rangeSeekbar.getSelectedMinValue().toString();
        max = rangeSeekbar.getSelectedMaxValue().toString();

        String selectedstring = "You selected wines of the type '" + tag + "' from "+ min + " to "+ max;
        Toast.makeText(getApplicationContext(), selectedstring, Toast.LENGTH_LONG).show();

        winesref = FirebaseDatabase.getInstance().getReference().child("wines");
        selected = winesref.orderByChild("year").startAt(min).endAt(max);
    }

    public void createonclicklistener (){
        allwineslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                clickedwine = (WineObject) adapterView.getItemAtPosition(position);
                createhashmap();

                // name ophalen zodat je ziet met wie je kan chatten
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


    public void showinfo (View view){
        Intent infoactivity = new Intent(BuyActivity.this, InfoActivity.class);
        infoactivity.putExtra("info", "Shown here are all the bottles for sale. When you click on one of them, all information about the bottle is shown. From there you can chat with the seller of the bottle. You can filter the bottles for sale by choosing a type of wine and a period.");
        startActivity(infoactivity);
    }

    public void createhashmap (){

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
        // id meegeven voor chatfunctie (gaat wss via uid)
        hash.put("sellerid", sellerid);
    }

    // zodat hij niet 1 achter loopt en null geeft
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

                ListadapterBottles listadapterBottles = new ListadapterBottles(getApplicationContext(), bottles);
                allwineslv.setAdapter(listadapterBottles);
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
//        showProgressDialog();
        startActivity(new Intent(BuyActivity.this, AllsellsActivity.class));
        finish();
    }

    public void gotoallchatsb(View view){
        startActivity(new Intent(BuyActivity.this, AllchatsActivity.class));
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

    public void gotobuyb(View view){
        startActivity(new Intent(BuyActivity.this, BuyActivity.class));
    }
}

