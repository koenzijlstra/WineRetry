package com.example.koen.wineretry.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.koen.wineretry.Objects.ChatMessageObject;
import com.example.koen.wineretry.Objects.OtheruserObject;
import com.example.koen.wineretry.R;
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
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
        }
        sellerid = getIntent().getStringExtra("sellerid");
        writeto_users_chats();

        // Create two unique identifiers for a chat by concatenating user id of current user and uid
        // of the other user. Each chat thus has a duplicate
        chatID_own =  uid + sellerid;
        chatID_seller = sellerid + uid;

        setFab();
        displayChatMessages(chatID_own);
    }

    // Set an onclicklistener on the floating action button. When the user clicks this send button
    // the message will be written to both (duplicate) chats. Also the other user's read variable is
    // set false; the other user has not read the message.
    public void setFab (){
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
                        // Write the messagestring to both chats. In writemessage a chatmessageobj
                        // is created and written to these chats.
                        writemessage(ownname,messagestring, chatID_own );
                        writemessage(ownname,messagestring, chatID_seller );

                        // Set read var of other user on false
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

    // Push a new chatmessage object under given chatid
    public void writemessage (String name, String input, String chatid){
        FirebaseDatabase.getInstance()
                .getReference().child("chats").child(chatid).child("messages")
                .push()
                .setValue(new ChatMessageObject(input, name)
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


    // Write both users under each others 'chats'
    public void writeto_users_chats (){
        write_other_to_cur();
        write_cur_to_other();
    }

    // Write the other user under the chats of the current user
    public void write_other_to_cur (){
        final DatabaseReference otherusers_name_ref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(sellerid).child("userinfo").child("name");
        otherusers_name_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherusername = dataSnapshot.getValue().toString();
                // Write a OtherUserobject of the other user under current users chats
                DatabaseReference cur_userchatsref = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(uid).child("chats").child(sellerid).child("other");
                cur_userchatsref.setValue(new OtheruserObject(otherusername, sellerid));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Write the current user under the chats of the other user
    public void write_cur_to_other (){
        final DatabaseReference nameref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("userinfo").child("name");
        nameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ownname = dataSnapshot.getValue().toString();
                // Write a OtherUserobject of the current user to the other user (for other user the
                // current user is the 'other' user
                DatabaseReference other_userchatsref = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(sellerid).child("chats").child(uid).child("other");
                other_userchatsref.setValue(new OtheruserObject(ownname, uid));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    // Display all chatmessage objects. Use firebaselist adapter in order to make the chat live.
    private void displayChatMessages (String chatid){
        final ListView listOfMessages = (ListView)findViewById(R.id.chatslv);
        // Create firebaselistadapter
        final FirebaseListAdapter adapter = new FirebaseListAdapter<ChatMessageObject>(this,
                ChatMessageObject.class, R.layout.message, FirebaseDatabase.getInstance().getReference()
                .child("chats").child(chatid).child("messages")) {
            @Override
            protected void populateView(View v, ChatMessageObject model, int position) {
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

        // Scrolls down to last messages when chatactivity is opened (does not work live yet)
        listOfMessages.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listOfMessages.setSelection(adapter.getCount() - 1);
            }
        });
    }
}
