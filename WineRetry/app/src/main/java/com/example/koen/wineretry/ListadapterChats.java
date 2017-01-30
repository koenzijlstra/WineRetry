package com.example.koen.wineretry;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Koen on 25-1-2017.
 */

public class ListadapterChats extends ArrayAdapter {
//    String uid;
//    String idother;
//    TextView tvname;
//    String read;
//    DatabaseReference readref;


    public ListadapterChats(Context context, List allchats ) {
        super(context, 0, allchats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        final OtheruserObject otheruserObject = (OtheruserObject) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitemchat, parent,
                    false);
        }

        final TextView tvname = (TextView) convertView.findViewById(R.id.name);

        if (otheruserObject != null){

            String idother = otheruserObject.getUserIDother();
            final DatabaseReference readref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("chats").child(idother).child("read");

                    readref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String read = dataSnapshot.getValue().toString();
                                    // hier convertview/listitems resetten ofzo?

                                    if (read.equals("true")){
                                        tvname.setTextColor(Color.parseColor("#acacac"));
                                }
                             else{
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

//        // get wine object at postiion
//        OtheruserObject otheruserObject = (OtheruserObject) getItem(position);
//
//        // use listitem.xml as layout for each item
//        if (convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitemchat, parent,
//                    false);
//        }
//
//        tvname = (TextView) convertView.findViewById(R.id.name);
//
//        if (otheruserObject != null){
//            idother = otheruserObject.getUserIDother();
//            isread();
//            tvname.setText(otheruserObject.getUsernameother()); // hier gaat het nog goed
//
//        }
//
//        return convertView;
//    }
//
//    public void isread (){
//        getuid();
//        readref = FirebaseDatabase.getInstance().getReference()
//                .child("users").child(uid).child("chats").child(idother).child("read");
//
//        readref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                read = dataSnapshot.getValue().toString();
//                // setcolors();
//                if (read.equals("true")){
//                    tvname.setTextColor(Color.parseColor("#acacac"));
//                    tvname.setAllCaps(false);
//                }
//                else{
//                    tvname.setTextColor(Color.parseColor("#000000"));
//                    tvname.setAllCaps(true);
//                    // tvname.setBackgroundColor(getContext(), R.color"eeffff"));
//                }
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
//
//    public void setcolors (){
//        if (read.equals("true")){
//            tvname.setTextColor(Color.parseColor("#acacac"));
//            tvname.setAllCaps(false);
//        }
//        else{
//            tvname.setTextColor(Color.parseColor("#000000"));
//            tvname.setAllCaps(true);
//            // tvname.setBackgroundColor(getContext(), R.color"eeffff"));
//        }
//    }
//
//    public void getuid (){
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        uid = auth.getCurrentUser().getUid();
//    }



