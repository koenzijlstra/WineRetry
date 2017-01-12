package com.example.koen.wineretry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

public class NewsellActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsell);
    }

    public void add (View view){
        EditText ettitle = (EditText) findViewById(R.id.ettitle);
        EditText etyear = (EditText) findViewById(R.id.etyear);
        EditText etregion = (EditText) findViewById(R.id.etregion);
        EditText etstory = (EditText) findViewById(R.id.etstory);

        String title = ettitle.getText().toString();
        String year = etyear.getText().toString();
        String region = etregion.getText().toString();
        String story = etstory.getText().toString();

        String alltext = title + year + region + story;

        Toast.makeText(getApplicationContext(), alltext , Toast.LENGTH_LONG).show();

        //
    }

}
