package com.ble.sharan.mainScreen.fragments.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ble.sharan.R;
import com.ble.sharan.myUtilities.MyUtil;


/**
 * 0
 * Created by ameba on 9/24/15.
 */
public class FragmentDrawer extends Fragment
{

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
//        ImageView circularImageView_Profile;
//    TextView txtv_username;
    Context context;


    MyUtil myUtil=new MyUtil();

    public FragmentDrawer()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        context = getActivity();

        View layout = inflater.inflate(R.layout.fragment_fragment_drawer, container, false);

//        circularImageView_Profile = (ImageView) layout.findViewById(R.id.circularImageView_Profile);
//        txtv_username = (TextView) layout.findViewById(R.id.txtv_username);

//        myUtil.showImageWithPicasso(context,circularImageView_Profile, MySharedPreference.getInstance().getPhoto(context));

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar)
    {
        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerLayout;

        int width = (getResources().getDisplayMetrics().widthPixels / 2);


        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) containerView.getLayoutParams();
        params.width = width + width / 2;
        containerView.setLayoutParams(params);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);

                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                mDrawerToggle.syncState();
            }
        });
    }

}