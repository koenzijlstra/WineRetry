package com.example.koen.wineretry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Sellfullinfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellfullinfo);

        settextviews();
    }

    public void settextviews (){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("hashmap");
        TextView titletv = (TextView) findViewById(R.id.selltitletv);
        TextView yeartv = (TextView) findViewById(R.id.sellyeartv);
        TextView regiontv = (TextView) findViewById(R.id.sellregiontv);
        TextView storytv = (TextView) findViewById(R.id.sellstorytv);

        String title = hash.get("title");
        String year = hash.get("year");
        String region = hash.get("region");
        String story = hash.get("story");

        titletv.setText(title);
        yeartv.setText(year);
        String regionstring = "Region: "+ region;
        regiontv.setText(regionstring);
        storytv.setText(story);
    }

    public void delete (View view){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("hashmap");
        final String bottleid = hash.get("bottleid");
        String title = hash.get("title");
        // delete bij wines
        DatabaseReference todelete = FirebaseDatabase.getInstance().getReference().child("wines")
                .child(bottleid);
        todelete.removeValue();

        // delete bottle id bij user
        // had ook via sellerid gekund?
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

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

        // toast?
        // string later naar strings verplaatsen
        String deletedstring = "You deleted '" + title + "'";
        Toast.makeText(Sellfullinfo.this, deletedstring, Toast.LENGTH_LONG).show();

        // intent terug naar allsells
        startActivity(new Intent(Sellfullinfo.this, AllsellsActivity.class));
        finish();
    }
}
