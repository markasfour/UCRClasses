package com.cs180project.ucrclasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /** Called when the user clicks the Send button */
    public void sign_in(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}

