package com.example.koen.wineretry.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koen.wineretry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BuyfullinfoActivity extends AppCompatActivity {

    private String uid;
    private TextView titletv;
    private TextView yeartv;
    private TextView regiontv;
    private TextView storytv;
    private TextView sellertv;
    private String sellerid;
    private String title;
    private String year;
    private String region;
    private String sellername;
    private String story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyfullinfo);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
        }

        setTextviews();
    }

    // Get all the strings that were given to the hashmap, define all textviews needed. If the
    // current user is the seller of the bottle, there will be no chat function, and there will be
    // a different string displayed about the seller
    public void setTextviews (){
        getTvsandstrings();

        Button chatbutton = (Button)findViewById(R.id.button13);
        // If user is seller, hide the chatbutton
        if (sellerid.equals(uid)){
            sellertv.setText(R.string.your_bottle);
            chatbutton.setVisibility(View.GONE);
        }
        else {
            String soldby = "Sold by: " + sellername;
            sellertv.setText(soldby);
            chatbutton.setVisibility(View.VISIBLE);
        }

        titletv.setText(title);
        String yearstring = "Year: " + year;
        yeartv.setText(yearstring);
        String regionstring = "Region: "+ region;
        regiontv.setText(regionstring);
        storytv.setText(story);
    }

    // Get the strings from the hashmap, find the textviews
    public void getTvsandstrings (){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("fullhashmap");
        titletv = (TextView) findViewById(R.id.tvtitlefull);
        yeartv = (TextView) findViewById(R.id.tvyearfull);
        regiontv = (TextView) findViewById(R.id.tvregionfull);
        storytv = (TextView) findViewById(R.id.tvstoryfull);
        sellertv = (TextView) findViewById(R.id.sellertv);

        sellerid = hash.get("sellerid");
        title = hash.get("title");
        year = hash.get("year");
        region = hash.get("region");
        sellername = hash.get("sellername");
        story = hash.get("story");
    }

    // When the chat button is clicked, go to chatactivity and give the id of seller to intent. Also
    // set the variable read true. This means that the cur user has read the other users message
    // (even though there will be no message from the other user if cur user initiates the chat).
    public void startchat (View view){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("fullhashmap");
        Intent gotochat = new Intent(BuyfullinfoActivity.this, ChatActivity.class);
        Toast.makeText(getApplicationContext(), sellerid , Toast.LENGTH_LONG).show();
        gotochat.putExtra("sellerid", sellerid);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("chats")
                .child(sellerid).child("read").setValue(true);
        startActivity(gotochat);
    }
}
