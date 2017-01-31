package com.example.koen.wineretry.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koen.wineretry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SellfullinfoActivity extends AppCompatActivity {
    String title;
    String uid;
    String bottleid;
    String year ;
    String region;
    String story;
    TextView titletv ;
    TextView yeartv ;
    TextView regiontv;
    TextView storytv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellfullinfo);

        settextviews();
    }

    public void settextviews (){

        getstrings_and_tvs();
        titletv.setText(title);
        String yearstring = "Year: "+ year;
        yeartv.setText(yearstring);
        String regionstring = "Region: "+ region;
        regiontv.setText(regionstring);
        storytv.setText(story);
    }

    public void getstrings_and_tvs (){

        titletv = (TextView) findViewById(R.id.selltitletv);
        yeartv = (TextView) findViewById(R.id.sellyeartv);
        regiontv = (TextView) findViewById(R.id.sellregiontv);
        storytv = (TextView) findViewById(R.id.sellstorytv);

        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("hashmap");
        title = hash.get("title");
        year = hash.get("year");
        region = hash.get("region");
        story = hash.get("story");
    }

    public void delete (View view){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this wine?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                ondeleteconfirmed();
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

    public void ondeleteconfirmed (){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("hashmap");
        bottleid = hash.get("bottleid");
        title = hash.get("title");
        // delete bij wines
        DatabaseReference todelete = FirebaseDatabase.getInstance().getReference().child("wines")
                .child(bottleid);
        todelete.removeValue();

        // had ook via sellerid gekund?
        FirebaseAuth auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        deleteunderuser();
        afterdeleting();
    }

    public void deleteunderuser (){
        DatabaseReference userwinesref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("wines");
        userwinesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    String checkedbottle = bottle.getValue(String.class);
                    if (checkedbottle.equals(bottleid)) {
                        bottle.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void afterdeleting (){
        // string later naar strings verplaatsen
        String deletedstring = "You deleted '" + title + "'";
        Toast.makeText(SellfullinfoActivity.this, deletedstring, Toast.LENGTH_LONG).show();

        // intent terug naar allsells
        startActivity(new Intent(SellfullinfoActivity.this, AllsellsActivity.class));
        finish();
    }
}
