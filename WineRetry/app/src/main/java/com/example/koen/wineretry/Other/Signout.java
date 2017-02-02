package com.example.koen.wineretry.Other;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;

import com.example.koen.wineretry.R;
import com.google.firebase.auth.FirebaseAuth;

/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* Class with one method to sign the user out. Uses the activity that called this method and a
* FirebaseAuth auth. Displays an alertdialog. When the positive button is clicked the user is
* logged out and because of the authstatelisteners in the activities the user is navigated to login
* Activity. When negative button is clicked, the dialog hides.
*/

public class Signout {


    public void signout(Activity activity, final FirebaseAuth auth){
        // Build the alertdialog and set the buttons
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(activity.getResources().getString(R.string.surelogout));
        alertDialogBuilder.setPositiveButton(activity.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        auth.signOut();
                    }
                });
        alertDialogBuilder.setNegativeButton(activity.getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        // Set the colors of the buttons
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#aa0000"));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#aa0000"));
            }
        });

        // Show the dialog
        alertDialog.show();
    }
}
