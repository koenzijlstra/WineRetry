package com.example.koen.wineretry.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.koen.wineretry.Other.BaseActivity;
import com.example.koen.wineretry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* Activity that lets user register. Uses createUserWithEmailAndPassword method given by firebase. When
* registering is not succesful, toast why it went wrong (with getexeption). When registering is
* successful, the user is navigated to Buy Activity. The user can also click a button to navigate to
 * LoginActivity when already registered.
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

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
        }
    }

    // When register button is clicked get the input and if input is not empty try function
    // createUserWithEmailAndPassword If this fails, toast what went wrong (task.getexception).
    // Otherwise go to signupsucces
    public void onRegister(View view){
        getInput();

        if (isCorrectinput()){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            // Create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // Display why registering went wrong
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Registering failed because: " +
                                                task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            // If sign up succeeds, complete writing to firebase and go to
                            // BuyActivity
                            else {
                                onSignupsuccess();
                            }
                        }
                    });
        }
//
    }

    // When registering is successful, write userinfo to firebase and navigate to BuyActivity
    public void onSignupsuccess (){
        DatabaseReference mrootRef = FirebaseDatabase.getInstance()
                .getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String uemail = auth.getCurrentUser().getEmail();
        DatabaseReference allusersref = mrootRef.child("users");
        DatabaseReference userref = allusersref.child(uid);
        // Write userinfo to firebase
        userref.child("userinfo").child("name").setValue(name);
        userref.child("userinfo").child("email").setValue(uemail);

        showProgressDialog();

        startActivity(new Intent(SignupActivity.this, BuyActivity.class));

        // Toast a welcome message and finish SignupActivity
        Toast.makeText(SignupActivity.this, getResources().getString(R.string.welcome) + name +
                "!", Toast.LENGTH_LONG).show();
        finish();
    }

    // Retrieve the input from the edittexts
    public void getInput (){
        nameinput = (EditText) findViewById(R.id.name);
        emailinput = (EditText) findViewById(R.id.emailsignup) ;
        passwordinput = (EditText) findViewById(R.id.passwordsignup);
        name = nameinput.getText().toString().trim();
        email = emailinput.getText().toString().trim();
        password = passwordinput.getText().toString().trim();
    }

    // Boolean that is true when the input strings of user are not empty and the password is long
    // enough. Set error at the specific edittext when edittext is empty
    public Boolean isCorrectinput (){

        if (TextUtils.isEmpty(name)){
            nameinput.setError(getResources().getString(R.string.yourname));
            return Boolean.FALSE;
        }

        if (TextUtils.isEmpty(email)) {
            emailinput.setError(getResources().getString(R.string.yourmail));
            return Boolean.FALSE;
        }

        if (TextUtils.isEmpty(password)) {
            passwordinput.setError(getResources().getString(R.string.enterpassword));
            return Boolean.FALSE;
        }

        if (password.length() < 6) {
            passwordinput.setError(getResources().getString(R.string.wwtooshort));
            return Boolean.FALSE;
        }
        else{
            return true;
        }
    }

    // When button "already an account?" is clicked, go to LoginActivity
    public void gotologin(View view){
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }
}
