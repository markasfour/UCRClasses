package com.cs180project.ucrclasses;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class SettingsActivity extends Fragment {
    View myView;

    private Button passwordUpdateButton;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private RelativeLayout relativeLayout;

    //@Override
    public View onCreateView(final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_settings, container, false);
//        passwordUpdateButton = (Button) myView.findViewById(R.id.password_update_button);
//        passwordUpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                container = inflater.inflate(/*INSERT HERERERERERERE*/);
//
//                popupWindow = new PopupWindow(container, 400, 400, true);
//                popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 500, 500);
//            }
//        });
        return myView;
    }
}
