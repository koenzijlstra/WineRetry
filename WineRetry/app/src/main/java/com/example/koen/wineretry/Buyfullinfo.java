package com.example.koen.wineretry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Buyfullinfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyfullinfo);

        TextView titletv = (TextView) findViewById(R.id.tvtitlefull);
        TextView yeartv = (TextView) findViewById(R.id.tvyearfull);
        TextView regiontv = (TextView) findViewById(R.id.tvregionfull);
        TextView storytv = (TextView) findViewById(R.id.tvstoryfull);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        Toast.makeText(getApplicationContext(), title , Toast.LENGTH_LONG).show();
        String year = extras.getString("year");
        String region = extras.getString("region");
        String story = extras.getString("story");

        titletv.setText(title);
        yeartv.setText(year);
        regiontv.setText(region);
        storytv.setText(story);
    }
}
