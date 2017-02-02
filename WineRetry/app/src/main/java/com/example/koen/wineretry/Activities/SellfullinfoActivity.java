package com.example.koen.wineretry.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koen.wineretry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* In this Activity with dialog theme all the info of a bottle that the current user sells is
* displayed. The bottle can be deleted. When the delete button is clicked an alertdialog is shown.
* When the deleting is confirmed by the user the bottle is deleted an the user is navigated back to
* SellActivity.
*/

public class SellfullinfoActivity extends AppCompatActivity {
    private String title;
    private String uid;
    private String bottleid;
    private String year ;
    private String region;
    private String story;
    private TextView titletv ;
    private TextView yeartv ;
    private TextView regiontv;
    private TextView storytv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellfullinfo);

        setTextviews();
    }

    // Find the textviews and get the strings from the hashmap that was given to the intent. Set the
    // textviews with these strings
    public void setTextviews (){

        getstrings_and_tvs();
        titletv.setText(title);
        String yearstring = getResources().getString(R.string.year)+ year;
        yeartv.setText(yearstring);
        String regionstring = getResources().getString(R.string.region)+ region;
        regiontv.setText(regionstring);
        storytv.setText(story);
    }

    // Get the textviews and get the strings from the hashmaps
    public void getstrings_and_tvs (){

        titletv = (TextView) findViewById(R.id.selltitletv);
        yeartv = (TextView) findViewById(R.id.sellyeartv);
        regiontv = (TextView) findViewById(R.id.sellregiontv);
        storytv = (TextView) findViewById(R.id.sellstorytv);

        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("hashmap");
        title = hash.get("title");
        year = hash.get("year");
        region = hash.get("region");
        story = hash.get("story");
    }

    // This is the skeleton/start of the delete function. When user presses the delete button, show
    // an alertdialog. If the delete is confirmed, delete the bottle under root/wines and under
    // root/users/uid/wines. When the bottle is deleted, navigate back to SellActivity
    public void delete (View view){

        // Create and show an alertdialog. When positive button is clicked, start ondeleteconfirmed
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.suredelete));
                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                ondeleteconfirmed();
                            }
                        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        // Set the colors of both buttons
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#aa0000"));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#aa0000"));
            }
        });
       
        alertDialog.show();

    }

    // When the positive button of the alertdialog is clicked, the wine is removed under root/wines
    // and the function to delete it under the current user is called, as well as the function that
    // toasts what was deleted and that starts the SellActivity
    public void ondeleteconfirmed (){
        Intent intent = getIntent();
        HashMap<String, String> hash = (HashMap<String,String>)intent
                .getSerializableExtra("hashmap");
        bottleid = hash.get("bottleid");
        title = hash.get("title");
        // Remove under wines
        DatabaseReference todelete = FirebaseDatabase.getInstance().getReference().child("wines")
                .child(bottleid);
        todelete.removeValue();

        deleteunderuser();
        afterdeleting();
    }

    // Remove the bottle id under the current user
    public void deleteunderuser (){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        DatabaseReference userwinesref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("wines");
        userwinesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Compare each bottle id of user with the id that needs to be deleted. If id's are
                // the same, delete that value
                for (DataSnapshot bottle : dataSnapshot.getChildren()){
                    String checkedbottle = bottle.getValue(String.class);
                    if (checkedbottle.equals(bottleid)) {
                        bottle.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Toast what bottle was deleted after deleting the bottle and the bottleid. Then the user is
    // navigated back to SellActivity
    public void afterdeleting (){
        String deletedstring = getResources().getString(R.string.youdeleted) + title + "'";
        Toast.makeText(SellfullinfoActivity.this, deletedstring, Toast.LENGTH_LONG).show();

        startActivity(new Intent(SellfullinfoActivity.this, SellActivity.class));
        finish();
    }
}
