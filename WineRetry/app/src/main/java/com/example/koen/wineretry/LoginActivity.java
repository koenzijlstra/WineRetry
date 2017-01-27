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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
* Koen Zijlstra, 10741615
*
* In this activity the user can log in. When user is already logged in, go to main activity.
* when something goes wrong with logging in, toast to user what went wrong. User can navigate to
* sign up activity when user is not registered yet.
 */

public class LoginActivity extends BaseActivity {

    // declare firebaseauth instance so multiple functions can use it
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // when user is logged in already, go to
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, BuyActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);
    }

    public void login1(View view){
        // get both edittext fields
        EditText inputEmail = (EditText) findViewById(R.id.emaillogin);
        final EditText inputPassword = (EditText) findViewById(R.id.passwordlogin);

        // get email and password strings
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        // toast when email or password is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter an email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter a password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // authenticate user, function signinwithemailandpassword is given by firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if sign in fails, display a message to the user
                        if (!task.isSuccessful()) {
                            // when entered password is too small, prompt user for longer password
                            if (password.length() < 6) {
                                inputPassword.setError("Password too short, should be least 6 characters!");
                                // when auth failed but password is long enough, toast that email or password is wrong
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                            // if sign in succeeds, go to main activity
                        } else {

                            // misschien qua timing eerder
                            showProgressDialog();

                            Toast.makeText(LoginActivity.this, "Logged in succesfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, BuyActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    // when button "not registered yet? "is clicked, go to sign up activity
    public void gotoregister(View view){
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        finish();
    }

    public void gotoforgotpassword(View view){
        startActivity(new Intent(LoginActivity.this, ForgotpasswordActivity.class));
        finish();
    }
}
