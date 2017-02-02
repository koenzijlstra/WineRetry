package com.example.koen.wineretry.Listadapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.koen.wineretry.Objects.OtheruserObject;
import com.example.koen.wineretry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Koen on 25-1-2017.*
 *
 * This class extends the arrayadapter. Shortening the units of code created a bug, and therefore I
 * restored the old code and deleted the cleaned and shortened code. It uses an arraylist of
 * Otheruserobjects, and gets the name of this obj to display, and uses the id to start the correct
 * chat with the other user.
 */


public class ListadapterChats extends ArrayAdapter {
    OtheruserObject otheruserObject;
    String uid;

    public ListadapterChats(Context context, List allchats ) {
        super(context, 0, allchats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
        }

        // Get the object at position
        otheruserObject = (OtheruserObject) getItem(position);

        // Set listitemchat.xml as layout for the listview items
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitemchat, parent,
                    false);
        }

        final TextView tvname = (TextView) convertView.findViewById(R.id.name);

        // If object is not null, get if the messages of that chat are read and set the colors
        // accordingly. Then set the textview with the name of the other user and return convertview
        if (otheruserObject != null){
            String idother = otheruserObject.getUserIDother();
            final DatabaseReference readref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("chats").child(idother).child("read");

            readref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String read = dataSnapshot.getValue().toString();
                    if (read.equals("true")){
                        tvname.setTextColor(Color.parseColor("#acacac"));
                    }else{
                        tvname.setTextColor(Color.parseColor("#000000"));
                        tvname.setAllCaps(true);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            tvname.setText(otheruserObject.getUsernameother());
        }

        return convertView;

    }



}


