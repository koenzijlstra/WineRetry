package com.example.koen.wineretry;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
* Koen Zijlstra, 10741615
*
* Activity that lets user register. Used createUserWithEmailAndPassword method given by firebase. When
* registering is not succesful, toast why it went wrong (with getexeption). when succesful, go to main
* activity. User can also click button to navigate to login activity when already registered.
*
 */
public class SignupActivity extends BaseActivity {
    EditText nameinput ;
    EditText emailinput;
    EditText passwordinput;
    String name;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    // register user, called when button "register" is clicked
    public void register(View view){
        getinput();

        if (correctinput()){
            // get firebase auth instance
            FirebaseAuth auth = FirebaseAuth.getInstance();
            // create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // if sign in fails, display a message to the user why it went wrong
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Registering failed because: " +
                                                task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            // if sign in succeeds go to mainactivity
                            else {
                                signupsuccess();
                            }
                        }
                    });
        }
//
    }

    public void signupsuccess (){
        DatabaseReference mrootRef = FirebaseDatabase.getInstance()
                .getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        // email had ook anders gekund
        String uemail = auth.getCurrentUser().getEmail();
        DatabaseReference allusersref = mrootRef.child("users");
        DatabaseReference userref = allusersref.child(uid);
        userref.child("userinfo").child("name").setValue(name);
        userref.child("userinfo").child("email").setValue(uemail);

        showProgressDialog();
        // Toast.makeText(SignupActivity.this, "Registered succesfully, welcome
        // to TITEL APP " + name + "!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(SignupActivity.this, BuyActivity.class));
        // wanneer toast laten zien, nog even bedenken
        Toast.makeText(SignupActivity.this, "Registered succesfully, welcome to TITEL APP " + name +
                "!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void getinput (){
        // get edittext fields and strings
        nameinput = (EditText) findViewById(R.id.name);
        emailinput = (EditText) findViewById(R.id.emailsignup) ;
        passwordinput = (EditText) findViewById(R.id.passwordsignup);
        name = nameinput.getText().toString().trim();
        email = emailinput.getText().toString().trim();
        password = passwordinput.getText().toString().trim();

    }

    public Boolean correctinput (){
        // NAME EMPTY -> TOAST
        if (TextUtils.isEmpty(name)){
            nameinput.setError("Enter your name!");
            return Boolean.FALSE;
        }

        // toast when email or password edittext is empty
        if (TextUtils.isEmpty(email)) {
            emailinput.setError("Enter your email address!");
            return Boolean.FALSE;
        }

        if (TextUtils.isEmpty(password)) {
            passwordinput.setError("Enter a password!");
            return Boolean.FALSE;
        }

        // toast when password is too short
        if (password.length() < 6) {
            passwordinput.setError("Password too short, enter at least 6 characters");
            return Boolean.FALSE;
        }
        else{
            return true;
        }
    }

    // when button "already an account?" is clicked, go to LoginActivity
    public void gotologin(View view){
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }
}
