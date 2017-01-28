package com.example.koen.wineretry;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    String ownname;
    String otherusername;
    public FirebaseListAdapter adapter;
    String chatID_own;
    String chatID_seller;
    String sellerid;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        sellerid = getIntent().getStringExtra("sellerid");
        writeto_users_chats();

        // create unique identifier for chat by concatenating user id (unique) and timestamp.
        // is gewoon puur om uniek id te krijgen, inhoudelijk maakt het niet uit.
        chatID_own =  uid + sellerid;
        chatID_seller = sellerid + uid;

        setfab();
        displayChatMessages(chatID_own);
    }

    public void setfab (){
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = (EditText)findViewById(R.id.input);
                final String messagestring = input.getText().toString();

                final DatabaseReference nameref = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(uid).child("userinfo").child("name");
                nameref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ownname = dataSnapshot.getValue().toString();
                        // nu dubbel
                        writemessage(ownname,messagestring, chatID_own );
                        writemessage(ownname,messagestring, chatID_seller );

                        FirebaseDatabase.getInstance().getReference().child("users").child(sellerid)
                                .child("chats").child(uid).child("read").setValue(false);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                // Clear the input
                input.setText("");
            }
        });
    }

    public void writemessage (String name, String input, String chatid){
        FirebaseDatabase.getInstance()
                .getReference().child("chats").child(chatid).child("messages")
                .push()
                .setValue(new ChatMessage(input, name)
                );

    }

    //
//                        // scroll naar beneden
//                        final ListView listOfMessages = (ListView)findViewById(R.id.chatslv);
//                        listOfMessages.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                // Select the last row so it will scroll into view...
//                                listOfMessages.setSelection(adapter.getCount() - 1);
//                            }
//                        });



    public void writeto_users_chats (){
        write_other_to_cur();
        write_cur_to_other();
    }

    public void write_other_to_cur (){
        final DatabaseReference otherusers_name_ref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(sellerid).child("userinfo").child("name");
        otherusers_name_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherusername = dataSnapshot.getValue().toString();
                // schrijf other user bij current user
                DatabaseReference cur_userchatsref = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(uid).child("chats").child(sellerid).child("other");
                cur_userchatsref.setValue(new OtheruserObject(otherusername, sellerid));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void write_cur_to_other (){
        final DatabaseReference nameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("userinfo").child("name");
        nameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ownname = dataSnapshot.getValue().toString();
                // schrijf bij ander user de chat met current user
                DatabaseReference other_userchatsref = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(sellerid).child("chats").child(uid).child("other");
                other_userchatsref.setValue(new OtheruserObject(ownname, uid));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void displayChatMessages (String chatid){
        final ListView listOfMessages = (ListView)findViewById(R.id.chatslv);
        final FirebaseListAdapter adapter = new FirebaseListAdapter<ChatMessage>(this,
                ChatMessage.class, R.layout.message, FirebaseDatabase.getInstance().getReference()
                .child("chats").child(chatid).child("messages")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
//
                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                messageTime.setText(df.format("dd/MM/yyyy hh:mm:ss", model.getMessageTime()).toString());
            }
        };

        listOfMessages.setAdapter(adapter);

        // later losse functie, nu dubbel in code. is voor scrollen (nu alleen bij openen naar
        // laatste bericht, live scrollen moet nog
        listOfMessages.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listOfMessages.setSelection(adapter.getCount() - 1);
            }
        });
    }
}
