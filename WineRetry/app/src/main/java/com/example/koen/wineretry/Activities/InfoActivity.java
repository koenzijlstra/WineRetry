package com.example.koen.wineretry.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.koen.wineretry.R;
/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* This activity with dialog theme displays specific information about the activity from where
* Infoactivity was entered.
*/
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle bundle = getIntent().getExtras();
        String info = bundle.getString("info");

        TextView textView = (TextView) findViewById(R.id.infotv);
        textView.setText(info);
    }
}
