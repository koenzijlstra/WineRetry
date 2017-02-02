package com.example.koen.wineretry.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.koen.wineretry.R;
import com.example.koen.wineretry.Objects.WineObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* In this Activity the user can add a new bottle of wine to the firebasedatabase. The user input is
* retrieved and a wineObject is created and added to firebase. Then the user is navigated back to
* AllsellsActivity.
*/
public class NewsellActivity extends AppCompatActivity {

    private EditText ettitle;
    private EditText etregion ;
    private EditText etstory ;
    private String title;
    private String year ;
    private String region ;
    private String story ;
    private String tag;
    private NumberPicker nmpicker;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsell);

        setActionbar();
        setspinner();
        setNumberpicker();
    }

    // When user clicks the add button, first get all the input. Then create a unique bottle id,
    // write the wine id under the wines of the user and write the wine to all the wines
    public void addNew (View view){
        getInput();
        checkInput();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
        }
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(uid);

        // Create unique identifier for bottle by concatenating user id (unique) and timestamp
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        String bottleid = uid + timestamp;

        // Add (complete) wine to root/wines
        DatabaseReference winesref = FirebaseDatabase.getInstance().getReference().child("wines");
        WineObject testwine = new WineObject(title,region,year,story,uid,bottleid,tag);
        winesref.child(bottleid).setValue(testwine);

        // Add id of bottle under root/users/uid/wines
        DatabaseReference winesuser = userref.child("wines").push();
        winesuser.setValue(bottleid);

        // Toast to user which wine was added (todo: check if adding was actually successful)
        String added = getResources().getString(R.string.youadded) + title + getResources().getString(R.string.successfully2);
        Toast.makeText(getApplicationContext(), added , Toast.LENGTH_LONG).show();

        // Navigate back to the AllsellsActivity
        startActivity(new Intent(NewsellActivity.this, AllsellsActivity.class));
        finish();
    }

    // Get the input from the edittexts, the spinner and numberpicker
    public void getInput (){
        ettitle = (EditText) findViewById(R.id.ettitle);
        etregion = (EditText) findViewById(R.id.etregion);
        etstory = (EditText) findViewById(R.id.etstory);

        nmpicker =  (NumberPicker)findViewById(R.id.numberPicker);
        year = Integer.toString(nmpicker.getValue());
        title = ettitle.getText().toString();
        region = etregion.getText().toString();
        story = etstory.getText().toString();

        Spinner spinnertag =(Spinner) findViewById(R.id.spinner2);
        tag = spinnertag.getSelectedItem().toString();
    }

    // Check if the strings are not empty, if empty set an error on specific edittext
    public void checkInput (){
        if(TextUtils.isEmpty(title)) {
            ettitle.setError(getResources().getString(R.string.pleasetitle));
            return;
        }

        if (TextUtils.isEmpty(region)){
            etregion.setError(getResources().getString(R.string.pleaseregion));
            return;
        }

        if (TextUtils.isEmpty(story)){
            etstory.setError(getResources().getString(R.string.pleasestory));
            return;
        }
    }

    // Set the support action bar
    public void setActionbar (){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
        }
    }

    // Set the numberpicker
    public void setNumberpicker (){
        NumberPicker nmpicker =  (NumberPicker)findViewById(R.id.numberPicker);
        nmpicker.setMaxValue(2017);
        nmpicker.setMinValue(1900);
    }

    // Set the spinner
    public void setspinner (){
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.winetags2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears and apply adapter to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Go back to AllsellsActivity, called after creating a new bottle for sale
    public void gotoallsellsn(View view){
        startActivity(new Intent(NewsellActivity.this, AllsellsActivity.class));
        finish();
    }
}
