package com.example.koen.wineretry;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsellActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsell);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }


    // FUNCTIES BOVEN MENNU!!


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

        // later deze toast weer toevoegen, zo van je hebt deze fles toegevoegd: info info
        // Toast.makeText(getApplicationContext(), alltext , Toast.LENGTH_LONG).show();

//        // test voor database
//        // moet eigenlijk vanaf begin
//        DatabaseReference mrootRef = FirebaseDatabase.getInstance().getReference();
//        // dit hele stuk alleen eerste keer -> bij register al in db zetten?
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        String uid = auth.getCurrentUser().getUid();
//        String uemail = auth.getCurrentUser().getEmail();
//        DatabaseReference allusersref = mrootRef.child("users");
//
//        // naam moet user gaan opgeven bij registering
//        // dit stuk bij register
//        DatabaseReference userref = allusersref.child(uid);
//        // opgegeven naam!!
//        userref.child("userinfo").child("name").setValue("Koen Zijlstra");
//        userref.child("userinfo").child("email").setValue(uemail);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);



        // WINE AAN WINES TOEVOEGEN MET ALLE INFO
        // get timestamp
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        // Toast.makeText(getApplicationContext(), timestamp , Toast.LENGTH_LONG).show();

        // create unique identifier for bottle by concatenating user id (unique) and timestamp
        String bottleid = uid + timestamp;
        Toast.makeText(getApplicationContext(), bottleid , Toast.LENGTH_LONG).show();
        // voeg toe aan algemen kop wines
        DatabaseReference winesref = FirebaseDatabase.getInstance().getReference().child("wines");
        WineObject testwine = new WineObject(title,region,year,story);
        winesref.child(bottleid).setValue(testwine);

        // voeg toe per user
        DatabaseReference winesuser = userref.child("wines").push();
        winesuser.setValue(bottleid);

        // na toevoegen terug naar allsells
        // intent
    }

    public void gotoallsellsn(View view){
        startActivity(new Intent(NewsellActivity.this, AllsellsActivity.class));
        finish();
    }
}
