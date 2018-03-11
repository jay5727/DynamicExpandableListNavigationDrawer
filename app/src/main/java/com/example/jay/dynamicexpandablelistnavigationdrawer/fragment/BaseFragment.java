package com.example.jay.dynamicexpandablelistnavigationdrawer.fragment;

import android.content.DialogInterface;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;

import com.example.jay.dynamicexpandablelistnavigationdrawer.R;

/**
 * Created by Jay on 10/03/2017.
 */

public class BaseFragment extends android.support.v4.app.Fragment {

    public void setTitle(String title) {
        try {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null) {
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar != null) {

                    try {
                        actionBar.setTitle(title);
                    } catch (Exception e) {
                        actionBar.setTitle(getString(R.string.app_name));
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    //region setTitle
    public void setTitle(android.support.v4.app.Fragment fragment) {
        try {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null) {
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar != null) {

                    try {
                        int resId = getResources().getIdentifier(fragment.getClass().getSimpleName(), "string", getContext().getPackageName());
                        actionBar.setTitle(getString(resId));
                    } catch (Exception e) {
                        actionBar.setTitle(getString(R.string.app_name));
                    }
                }
            }
        } catch (Exception e) {

        }
    }
    //endregion

   /* @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        //setTitle(this);
        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    return exit();
                    // handle back button's click listener

                }
                return false;
            }
        });
    }*/

    public boolean exit() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());

        alertbox.setTitle("Do You Want To Exit ?");
        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                getActivity().moveTaskToBack(true);
                getActivity().finish();
            }
        });

        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Nothing will be happened when clicked on no button
                // of Dialog
            }
        });

        alertbox.show();
        return true;
    }
}