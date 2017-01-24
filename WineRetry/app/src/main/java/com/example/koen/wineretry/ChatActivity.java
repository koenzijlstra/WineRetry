package com.example.koen.wineretry;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = (EditText)findViewById(R.id.input);
                final String messagestring = input.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();

                // voor eerste keer ;/
                ownname = uid;

                final DatabaseReference nameref = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userinfo").child("name");

                nameref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ownname = dataSnapshot.getValue().toString();
                        writemessage(ownname,messagestring);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                // Clear the input
                input.setText("");
            }
        });

        displayChatMessages();
    }

    public void writemessage (String name, String input){
        FirebaseDatabase.getInstance()
                .getReference().child("chat")
                .push()
                .setValue(new ChatMessage(input, name)
                );
    }


    private void displayChatMessages (){
        ListView listOfMessages = (ListView)findViewById(R.id.chatslv);

        FirebaseListAdapter adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("chat")) {
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
                // messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}
