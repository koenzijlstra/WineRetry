package com.example.koen.wineretry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.koen.wineretry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
    }

    public void reset (View view){
        EditText inputmail = (EditText) findViewById(R.id.emailreset);
        String email = inputmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Please enter your registered email",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotpasswordActivity.this, "We have sent you instruction to " +
                            "reset your password!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ForgotpasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                else {
                    Toast.makeText(ForgotpasswordActivity.this, "Failed to reset your password!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
