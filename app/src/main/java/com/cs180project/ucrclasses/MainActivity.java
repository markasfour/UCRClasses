package com.cs180project.ucrclasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    /** Progress Dialog Implementation */
    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    boolean signedinStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        signedinStatus = sharedPref.getBoolean("SIGNEDIN", false);

//        showProgressDialog();
        Databaser.fetchData();
//        while(Databaser.fetching){};
//        hideProgressDialog();
        UCRSchedules.scheduleInit();
    }

    /** Called when the user clicks the Send button */
    public void sign_in(View view) {
        Intent intent;
//        if(signedinStatus) {
            intent = new Intent(this, SignInActivity.class);
//        }
//        else{
//            intent = new Intent(this, DrawerActivity.class);
//        }
        startActivity(intent);
    }
}

