package com.ble.sharan.mainScreen.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.ble.sharan.R;
import com.ble.sharan.adapters.DrawerList_Adapter;
import com.ble.sharan.mainScreen.fragments.AboutUsFragment;
import com.ble.sharan.mainScreen.fragments.AlarmFragment;
import com.ble.sharan.mainScreen.fragments.FragmentDrawer;
import com.ble.sharan.mainScreen.fragments.HomeFragment;
import com.ble.sharan.mainScreen.fragments.ProfileFragment;
import com.ble.sharan.mainScreen.fragments.TestingFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    FragmentDrawer drawerFragment;
    ListView listv_drawer;
    ArrayList<String> listDataHeader;

    DrawerList_Adapter drawer_adapter;
    Context con;

    FrameLayout frame_layout_profile;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        con = this;
        //constant.ChangeStatusColor(this,R.color.Black);

        setUpIds();
        prepareListData();

    }

    private void setUpIds()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.findViewById(R.id.imgv_refresh).setOnClickListener(this);

        listv_drawer = (ListView) findViewById(R.id.listv_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        frame_layout_profile = (FrameLayout) findViewById(R.id.frame_layout_profile);
        frame_layout_profile.setOnClickListener(this);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    private void prepareListData()
    {
        listDataHeader = new ArrayList<>();

        listDataHeader.add("Home");
        listDataHeader.add("Alarm");
        listDataHeader.add("About us");
        listDataHeader.add("Invite Friends");
        listDataHeader.add("Share App");


        drawer_adapter = new DrawerList_Adapter(con, listDataHeader, 0);
        listv_drawer.setAdapter(drawer_adapter);

        listv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                displayView(position);

                drawer_adapter.changeSelectedBackground(position);
                drawer_adapter.notifyDataSetChanged();
            }
        });

        displayView(0);

    }

    public void displayView(int groupPosition)
    {


        switch (groupPosition)
        {

            case 0:
                fragment = new HomeFragment();
                break;


            case 1:
                fragment = new AlarmFragment();
                break;

            case 2:
                fragment = new TestingFragment();
                break;

            case 3:
                fragment = new AboutUsFragment();
                break;

            case 4:
                fragment = new AboutUsFragment();
                break;

           /*  case 4:
                fragment = new Settings();
                break;

            case 5:
                fragment = new AboutUs();
                break;

            case 6:
                fragment = new InviteFriends();

                break;

            case 7:
                mDrawerLayout.closeDrawers();
                callShare();

                //                fragment = new ShareApp();
                break;*/

            default:
                break;
        }

//        if (groupPosition != 7)
//        {
        changeFragment(fragment);
//        }

    }

    private void callShare()
    {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void changeFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.frame_layout_profile:

//                txt_titleTV.setText("Profile");
                fragment = new ProfileFragment();
                changeFragment(fragment);

                break;

            case R.id.imgv_refresh:

              /* if( fragment instanceof Pedometer)
               {
                   ((Pedometer)fragment).resetValues(true);
               }*/
                break;

            default:
                break;
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();


        if (fragment instanceof HomeFragment)
        {
            ((HomeFragment) fragment).onResumeManual();
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();


        if (fragment instanceof HomeFragment)
        {
            ((HomeFragment) fragment).onDestroyManual();
        }
    }
}