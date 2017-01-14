package com.example.koen.wineretry;

import android.provider.ContactsContract;
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

        // test voor database
        // moet eigenlijk vanaf begin
        DatabaseReference mrootRef = FirebaseDatabase.getInstance().getReference();


        // dit hele stuk alleen eerste keer -> bij register al in db zetten?
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String uemail = auth.getCurrentUser().getEmail();
        DatabaseReference allusersref = mrootRef.child("users");

        // naam moet user gaan opgeven bij registering
        // dit stuk bij register
        DatabaseReference userref = allusersref.child(uid);
        // opgegeven naam!!
        userref.child("userinfo").child("name").setValue("Koen Zijlstra");
        userref.child("userinfo").child("email").setValue(uemail);

                
        DatabaseReference wines = userref.child("wines");
        WineObject testwine = new WineObject(title,region,year,story);
        // nu setvalue, later steeds wijnen toevoegen. ->
        wines.setValue(testwine);

    }



}
