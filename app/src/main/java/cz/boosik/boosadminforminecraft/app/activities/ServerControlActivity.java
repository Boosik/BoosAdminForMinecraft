package cz.boosik.boosadminforminecraft.app.activities;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.components.CustomViewPager;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlDynmapFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPlayersFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlServerFragment;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;
import cz.boosik.boosadminforminecraft.app.serverStore.StorageProvider;

public class ServerControlActivity extends AppCompatActivity implements ActionBar.TabListener {

    public static Server server;
    public static boolean dynmapAvailable;
    static ArrayList<AsyncTask<?, ?, ?>> runningAsyncTasks = null;
    static RCon rcon = null;
    SectionsPagerAdapter mSectionsPagerAdapter;
    @Bind(R.id.pager)
    CustomViewPager mViewPager;
    String serverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_control);
        ButterKnife.bind(this);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

//        serverName = getIntent().getExtras().getString("serverName");
        serverName = getIntent().getStringExtra("serverName");
        prepareSessionData(serverName);
        int tempCount = mSectionsPagerAdapter.getCount();
        if (!dynmapAvailable) {
            tempCount = 3;
        }
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < tempCount; i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

        if (tab.getPosition() == 3) mViewPager.setPagingEnabled(false);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (tab.getPosition() == 3) mViewPager.setPagingEnabled(true);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return ServerControlServerFragment.newInstance(position + 1);
                case 1:
                    return ServerControlPlayersFragment.newInstance(position + 1);
                case 2:
                    return ServerControlPlayersFragment.newInstance(position + 1);
                case 3:
                    return ServerControlDynmapFragment.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }

    private void prepareSessionData(String serverName) {
        StorageProvider storageProvider = new StorageProvider(this, "servers.json");
        try {
            for (Server s : storageProvider.readServers().getServers()) {
                if (s.getName().equals(serverName)) {
                    server = s;
                    if (!server.getDynmapHost().isEmpty()) {
                        dynmapAvailable = true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
