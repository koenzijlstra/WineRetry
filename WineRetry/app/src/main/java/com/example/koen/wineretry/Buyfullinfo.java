package com.example.koen.wineretry;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Buyfullinfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyfullinfo);

        settextviews();

        // ophalen van sellerid voor chat, later in losse functie die in oncreate wordt gecalled
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent.getSerializableExtra("fullhashmap");
        String sellerid = hash.get("sellerid");
    }

    public void settextviews (){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent.getSerializableExtra("fullhashmap");
        TextView titletv = (TextView) findViewById(R.id.tvtitlefull);
        TextView yeartv = (TextView) findViewById(R.id.tvyearfull);
        TextView regiontv = (TextView) findViewById(R.id.tvregionfull);
        TextView storytv = (TextView) findViewById(R.id.tvstoryfull);
        TextView sellertv = (TextView) findViewById(R.id.sellertv);

//        Bundle extras = getIntent().getExtras();
        String title = hash.get("title");
        String year = hash.get("year");
        String region = hash.get("region");
        String sellername = hash.get("sellername");
        String story = hash.get("story");
        // Toast.makeText(getApplicationContext(), sellername , Toast.LENGTH_LONG).show();

        titletv.setText(title);
        yeartv.setText(year);
        String regionstring = "Region: "+ region;
        regiontv.setText(regionstring);
        storytv.setText(story);
        String soldby = "Sold by: " + sellername;
        sellertv.setText(soldby);
    }
}
