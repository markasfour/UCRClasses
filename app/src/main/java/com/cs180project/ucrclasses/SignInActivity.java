package com.cs180project.ucrclasses;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    //private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    /** CODE I ADDED ----- */
    //private boolean isNewUserAccount;
    /** CODE I ADDED ----- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        //mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        //Buttons
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);


        /** In your sign-up activity's onCreate method, get the shared instance of the
         *  FirebaseAuth object*/

        mAuth = FirebaseAuth.getInstance();


        /** Set up an AuthStateListener that responds to changes in the user's sign-in state */

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    /** CODE I ADDED ----- */
                    // If a new user is created, send a verification email.
                    /** if(isNewUserAccount) {
                        Log.d(TAG, "NewUserAccount: entering email verification method ");
                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Log.d(TAG, "Email sent.");
                                        }
                                    }
                                });
                        if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                            Log.d(TAG, "Email verified");
                        } else {
                            Log.d(TAG, "Email NOT verified");
                        }
                    } */
                    /** CODE I ADDED ^^^^^ */
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /** Create a new account by passing the new user's email address and password to
     * createUserWithEmailAndPassword */

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if(!validateForm()) {
            return;
        }

        //showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user.
                        // If sign in succeeds, the auth state listener will be notified and logic
                        // to handle the signed in user can be handled in the listener.
                        if(!task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        /** Code I Added ------ */
                        //isNewUserAccount = true;
                        /** Code I Added ^^^^^ */
                        //hideProgressDialog();
                    }
                });
    }


    private void signIn(String email, String password){
        Log.d(TAG, "signIn:" + email);
        if(!validateForm()) {
            return;
        }

        //showProgressDialog

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user.
                        // If sign in succeeds, the auth state listener will be notified and logic
                        // to handle signed in user will be handled in the listener.
                        if(!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(SignInActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();

                        if(task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, R.string.sign_in_success,
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(task.isSuccessful()) {
                            mStatusTextView.setText(R.string.sign_in_success);
                        }
                    }
                });
    }


    private void signOut() {
        mAuth.signOut();
        Toast.makeText(SignInActivity.this, R.string.signed_out,
                Toast.LENGTH_SHORT).show();
        mStatusTextView.setText(R.string.signed_out);
        //updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if(TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    /** private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        mStatusTextView.setText(getString(R.string.email_password_fmt, user.getEmail()));
        mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

        findViewById(R.id.em)
    } */

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        }
    }


}
