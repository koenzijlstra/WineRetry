package com.example.koen.wineretry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
