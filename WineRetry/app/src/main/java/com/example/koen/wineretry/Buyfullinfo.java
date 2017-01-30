package com.example.koen.wineretry;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Buyfullinfo extends AppCompatActivity {

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyfullinfo);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        settextviews();
    }

    public void settextviews (){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("fullhashmap");
        TextView titletv = (TextView) findViewById(R.id.tvtitlefull);
        TextView yeartv = (TextView) findViewById(R.id.tvyearfull);
        TextView regiontv = (TextView) findViewById(R.id.tvregionfull);
        TextView storytv = (TextView) findViewById(R.id.tvstoryfull);
        TextView sellertv = (TextView) findViewById(R.id.sellertv);

//        Bundle extras = getIntent().getExtras();
        String sellerid = hash.get("sellerid");
        String title = hash.get("title");
        String year = hash.get("year");
        String region = hash.get("region");
        String sellername = hash.get("sellername");
        String story = hash.get("story");
        // Toast.makeText(getApplicationContext(), sellername , Toast.LENGTH_LONG).show();

        Button chatbutton = (Button)findViewById(R.id.button13);

        if (sellerid.equals(uid)){
            sellertv.setText("This is one of your bottles. You can delete it from 'sell'.");
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

    public void startchat (View view){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("fullhashmap");
        String sellerid = hash.get("sellerid");
        Intent gotochat = new Intent(Buyfullinfo.this, ChatActivity.class);
        Toast.makeText(getApplicationContext(), sellerid , Toast.LENGTH_LONG).show();
        gotochat.putExtra("sellerid", sellerid);

        // gelezen op true zetten (maakt niet uit dat er de eerste keer nog geen bericht is)
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("chats")
                .child(sellerid).child("read").setValue(true);

        startActivity(gotochat);
    }
}
