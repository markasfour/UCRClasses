package com.cs180project.ucrclasses;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    private EditText addressString;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_password_reset);

        addressString = (EditText) findViewById(R.id.email_text);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.reset_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if(i == R.id.reset_password_button) {
                    Log.d(TAG, "Called function");
                    String emailAddress = addressString.getText().toString();

                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Log.d(TAG, "Email NOT sent.");
                                        //TOAST SAYING INCORRECT EMAIL ADDRESS
                                        Toast.makeText(PasswordResetActivity.this, R.string.password_reset_fail,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.d(TAG, "Email SENT");
                                        //TOAST SAYING EMAIL SENT
                                        Toast.makeText(PasswordResetActivity.this, R.string.password_reset_success,
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PasswordResetActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
