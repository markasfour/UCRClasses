package com.cs180project.ucrclasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import static com.cs180project.ucrclasses.Databaser.dat;

public class MainActivity extends AppCompatActivity {

    TextView clickme;
    RelativeLayout page;

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
        clickme = (TextView) findViewById(R.id.textView8);
        page = (RelativeLayout) findViewById(R.id.splash);

//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        signedinStatus = sharedPref.getBoolean("SIGNEDIN", false);

        showProgressDialog();
        Databaser.fetchData();
        Log.d("HERE", "part 1");
        //while(dat.isEmpty()){};
        Log.d("HERE2", "part2");
        UCRSchedules.scheduleInit();
        if (!Databaser.done){
            clickme.setText("Loading...");
            page.setClickable(false);
        }
        new load().execute();
    }


    private class load extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            while (!Databaser.done) {
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Done", "DONE LOADING");
            clickme.setText("Click to Continue");
            page.setClickable(true);
            hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
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

