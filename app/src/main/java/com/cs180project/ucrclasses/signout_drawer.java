package com.cs180project.ucrclasses;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Peter on 10/15/2016.
 */

public class signout_drawer extends Fragment{
    View myView;

    private Button confirm;
    private Button deny;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.signout_drawer, container, false);
        confirm = (Button) myView.findViewById(R.id.button2);
        deny = (Button) myView.findViewById(R.id.button3);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent();
                intent.setClass(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Schedule1Activity()).commit();
            }
        });

        return myView;
    }


}
