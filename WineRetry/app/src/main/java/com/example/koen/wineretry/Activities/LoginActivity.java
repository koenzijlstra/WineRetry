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

/* Created by Koen Zijlstra
* University of Amsterdam
* Student number: 10741615
* Coarse: Programmeerproject
*
* In this activity the user can log in. When the user is already logged in, navigate to BuyActivity.
* The class first retrieves the input, checks if it is not empty and then the function
* signInWithEmailAndPassword is called. When this fails, what went wrong is toasted. Otherwise the
* user is navigated to BuyActivity.
*
* Some of the code in this class was written with the use of the following tutorial:
* http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
*/

public class LoginActivity extends BaseActivity {

    FirebaseAuth auth;
    EditText inputEmail;
    EditText inputPassword;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        setActionbar();
        alreadyloggedin();

        // Set the view now, after the 'alreadyloggedin' function
        setContentView(R.layout.activity_login);
    }

    // When the user is logged in already, navigate to buyactivity
    public void alreadyloggedin (){
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, BuyActivity.class));
            finish();
        }
    }

    // Set the custom action bar
    public void setActionbar (){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);
        }
    }

    // This function is the skeleton of logging in. First the input is retrieved. Than a function
    // that returns a boolean (correctinput) is called, and when the input is correct, the
    // signinwithemailandpassword function is called. When this task is succesful, toast that it was
    // succesful and navigate to buyactivity. Otherwise toast what went wrong
    public void login(View view){
        getinput();
        if (correctinput()){
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if sign in fails, display a message to the user
                            if (!task.isSuccessful()) {
                                onfail();
                                // if sign in succeeds, go to main activity
                            } else {
                                onsuccess();
                            }
                        }
                    });
        }
    }

    // When signInWithEmailAndPassword fails, toast why it went wrong
    public void onfail (){
        // when entered password is too small, prompt user for longer password
        if (password.length() < 6) {
            inputPassword.setError(getResources().getString(R.string.tooshort));
            // when auth failed but password is long enough, toast that email or password is wrong
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                    Toast.LENGTH_LONG).show();
        }
    }

    // Toast that login was successful, start BuyActivity
    public  void onsuccess (){
        showProgressDialog();
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.successfully), Toast
                .LENGTH_LONG).show();

        Intent intent = new Intent(LoginActivity.this, BuyActivity.class);
        startActivity(intent);
        finish();
    }

    // Get the input from the edittexts
    public void getinput(){
        inputEmail = (EditText) findViewById(R.id.emaillogin);
        inputPassword = (EditText) findViewById(R.id.passwordlogin);

        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
    }

    // Boolean that checks if strings are empty. Returns true if input is correct (not empty)
    public Boolean correctinput (){

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Enter an email address!");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter a password!");
            return false;
        }
        else{
            return true;
        }
    }

    // As a result of time concerns these are still separate functions instead of one onclick system
    // with different cases as in the other activities
    // when button "not registered yet? "is clicked, go to sign up activity
    public void gotoregister(View view){
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        finish();
    }

    // When button "forgot your password?" is clicked, navigate to forgotpasswordactivity
    public void gotoforgotpassword(View view){
        startActivity(new Intent(LoginActivity.this, ForgotpasswordActivity.class));
    }
}
