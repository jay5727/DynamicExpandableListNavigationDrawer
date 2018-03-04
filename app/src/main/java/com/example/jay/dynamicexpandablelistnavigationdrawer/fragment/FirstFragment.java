package com.example.jay.dynamicexpandablelistnavigationdrawer.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jay.dynamicexpandablelistnavigationdrawer.R;

public class FirstFragment extends Fragment {

    public FirstFragment() {
        // Required empty public constructor
    }


   /* public void setTitle(android.support.v4.app.Fragment fragment) {
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
    }*/


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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frag_one, container, false);
        setTitle("Facebook");
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}