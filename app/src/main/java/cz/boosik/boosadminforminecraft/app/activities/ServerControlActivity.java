package cz.boosik.boosadminforminecraft.app.activities;

import java.io.FileNotFoundException;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.adapters.SectionsPagerAdapter;
import cz.boosik.boosadminforminecraft.app.asyncTasks.ExecuteCommandTask;
import cz.boosik.boosadminforminecraft.app.asyncTasks.LoadOnlinePlayersTask;
import cz.boosik.boosadminforminecraft.app.components.CustomViewPager;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;
import cz.boosik.boosadminforminecraft.app.serverStore.StorageProvider;
import cz.boosik.boosadminforminecraft.app.query.*;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerControlActivity extends AppCompatActivity implements ActionBar.TabListener {

    @Bind(R.id.pager)
    CustomViewPager mViewPager;

    private Server server;
    private boolean dynmapAvailable;
    private RCon rcon = null;
    private Snackbar snackbar;
    private MCQuery mcQuery;

    public final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            snackbar.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_control);
        setTitle(getString(R.string.app_name));
        ButterKnife.bind(this);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        String serverName = getIntent().getStringExtra("serverName");
        prepareSessionData(serverName);
        try {
            mcQuery = new MCQuery(server.getQueryHost(), Integer.valueOf(server.getQueryPort()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int tempCount = mSectionsPagerAdapter.getCount();
        if (!dynmapAvailable) {
            tempCount = 3;
        }
        for (int i = 0; i < tempCount; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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

    public boolean isDynmapAvailable() {
        return dynmapAvailable;
    }

    public Snackbar getSnackbar() {
        return snackbar;
    }

    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    public MCQuery getMcQuery() {
        return mcQuery;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public RCon getRcon() {
        return rcon;
    }

    public void setRcon(RCon rcon) {
        this.rcon = rcon;
    }
}
