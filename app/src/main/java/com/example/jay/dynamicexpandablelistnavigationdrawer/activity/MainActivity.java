package com.example.jay.dynamicexpandablelistnavigationdrawer.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jay.dynamicexpandablelistnavigationdrawer.R;
import com.example.jay.dynamicexpandablelistnavigationdrawer.adapter.ExpandListAdapter;
import com.example.jay.dynamicexpandablelistnavigationdrawer.fragment.FirstFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListView expListView;
    int lastExpandedPosition = -1;

    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listURLChild;
    HashMap<String, String> childLessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        TextView userProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userProfileName);
        TextView userAvatar = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userAvatar);

        userProfileName.setText("Jay");
        userAvatar.setText(getUserAvatar("Jay"));

        expListView = (ExpandableListView) findViewById(R.id.left_drawer);

        enableExpandableList();
        setFragment(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    private void enableExpandableList() {

        try {
            prepareMenuList(getAssets().open("drawer.json"));
        } catch (Exception e) {
        }

        ExpandListAdapter listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                try {
                    if (!checkIfChildExist(groupPosition))
                        setFragment(groupPosition, -1);

                } catch (Exception ex) {
                    String s = ex.getMessage();
                }
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {


            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                setFragment(groupPosition, childPosition);

                return false;
            }
        });
    }

    public void prepareMenuList(InputStream inputStream) {
        try {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            listURLChild = new HashMap<String, List<String>>();
            childLessList = new HashMap<>();

            JSONObject obj = new JSONObject(loadJSONFromAsset(inputStream));
            JSONArray jsonArrayHeader = obj.getJSONArray("menu");

            for (int headerIndex = 0; headerIndex < jsonArrayHeader.length(); headerIndex++) {

                JSONObject jsonObject = jsonArrayHeader.getJSONObject(headerIndex);
                List<String> childList = null;
                List<String> childURL = null;
                String title = jsonObject.getString("Title");
                listDataHeader.add(title);
                if (jsonObject.has("Child")) {
                    childList = new ArrayList<String>();
                    childURL = new ArrayList<String>();
                    JSONArray jsonArrayChild = jsonObject.getJSONArray("Child");
                    for (int childIndex = 0; childIndex < jsonArrayChild.length(); childIndex++) {
                        childList.add(jsonArrayChild.getJSONObject(childIndex).getString("ChildTitle"));
                        childURL.add(jsonArrayChild.getJSONObject(childIndex).getString("URL"));
                    }
                    //

                } else
                //we know there wasn't any child for sure
                //meaning a childLess List in which case pass  parentHeaderName as a key to our childLess HashMap.
                {
                    childLessList.put(title, jsonObject.getString("URL"));
                }
                listDataChild.put(title, (childList != null) ? childList : new ArrayList<String>());
                listURLChild.put(title, (childURL != null) ? childURL : new ArrayList<String>());
            }
        } catch (Exception e) {

        }
    }

    public String loadJSONFromAsset(InputStream inputStream) {
        String json = null;
        try {

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {

            return null;
        }
        return json;
    }


    private void setFragment(int groupPosition, int childPosition) {
        Fragment fragment = null;
        if (!checkIfChildExist(groupPosition))
            fragment = getFragment(groupPosition, -1);
        else
            fragment = getFragment(groupPosition, childPosition);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fragment != null) {
            transaction.replace(R.id.home_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        expListView.setItemChecked(childPosition, true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public Fragment getFragment(int groupPosition, int childPosition) {
        Fragment fragment = null;
        String groupHeader = listDataHeader.get(groupPosition);
        String fragmentName = null;
        try {

            if (childPosition == -1) {
                fragmentName = childLessList.get(groupHeader);
            } else {
                fragmentName = listURLChild.get(groupHeader).get(childPosition);
            }
            if (!fragmentName.isEmpty())
                fragment = (Fragment) Class.forName(fragmentName).newInstance();

        } catch (Exception e) {
            //java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0
            String s = e.getMessage();
        }
        return fragment;
    }

    /**
     *
     * @param groupPosition : header position
     * @return true if child exist for group position else false
     */
    private boolean checkIfChildExist(int groupPosition) {
        try {
            String parentAsKey = listDataHeader.get(groupPosition);
            List<String> childList = listURLChild.get(parentAsKey);
            if (childList.size() == 0) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getUserAvatar(String s) {
        String temp = null;
        if (s != null) {
            String[] x = s.split(" ");
            if (x.length == 1) {
                temp = String.valueOf(x[0].charAt(0));
            } else //greater than 1
            {
                temp = String.valueOf(x[0].charAt(0)) + String.valueOf(x[x.length - 1].charAt(0));
            }
        }
        return temp != null ? temp.toUpperCase() : "";
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getTopFragment() == null)
            exit();
    }*/

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            //let the last fragment remain & show exit alert...!
            exit();
        }

       /* List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0)
        {
            for (int i = 0; i < fragmentList.size(); i++) {
                if (i == fragmentList.size() - 1) {
                    //let the last fragment remain & show exit alert...!
                    exit();

                } else {
                    super.onBackPressed();
                }
            }
        }*/
    }

    public boolean exit() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        alertbox.setTitle("Do You Want To Exit ?");
        alertbox.setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //moveTaskToBack(true);
                        finish();
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

    public Fragment getTopFragment() {
        List<Fragment> fragentList = getSupportFragmentManager().getFragments();
        Fragment top = null;
        for (int i = fragentList.size() - 1; i >= 0; i--) {
            top = fragentList.get(i);
            if (top != null) {
                return top;
            }
        }
        return top;
    }

}