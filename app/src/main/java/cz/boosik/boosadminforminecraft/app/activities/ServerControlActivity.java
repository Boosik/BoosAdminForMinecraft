package cz.boosik.boosadminforminecraft.app.activities;

import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import cz.boosik.boosadminforminecraft.app.components.CustomViewPager;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;
import cz.boosik.boosadminforminecraft.app.serverStore.StorageProvider;
import cz.boosik.boosadminforminecraft.app.query.*;

/**
 * Activity of server control
 *
 * @author jakub.kolar@bsc-ideas.com
 */
@SuppressWarnings("deprecation")
public class ServerControlActivity extends AppCompatActivity implements ActionBar.TabListener {

    @Bind(R.id.pager)
    CustomViewPager mViewPager;

    private Server server;
    private boolean dynmapAvailable;
    private RCon rcon = null;
    private Snackbar snackbar;
    private MCQuery mcQuery;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private final View.OnClickListener clickListener = new View.OnClickListener() {
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
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.minecraft_green)));
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.minecraft_green)));
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
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
        switch (tab.getPosition()) {
            case 0: this.setTitle(R.string.server_commands);
                break;
            case 1: this.setTitle(R.string.online_players);
                break;
            case 2: this.setTitle(R.string.supported_plugins);
                break;
            case 3: this.setTitle(R.string.dynamic_map);
                break;
        }
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
     * Prepares session data based on the selected server name
     *
     * @param serverName Name of the server to select
     */
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

    /**
     * Starts the server list activity and add error information to intent
     *
     * @param type The type of error
     */
    public void invokeError(String type) {
        Intent i = new Intent(this, ServerListActivity.class);
        i.putExtra("error", type);
        this.startActivity(i);
        finish();
    }

    /**
     * Returns if the dynmap is available
     *
     * @return The dynmapAvailable
     */
    public boolean isDynmapAvailable() {
        return dynmapAvailable;
    }

    /**
     * Gets the snackbar
     *
     * @return The snackbar
     */
    public Snackbar getSnackbar() {
        return snackbar;
    }

    /**
     * Sets the snackbar
     *
     * @param snackbar The snackbar
     */
    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    /**
     * Gets the mcQuery
     *
     * @return The mcQuery
     */
    public MCQuery getMcQuery() {
        return mcQuery;
    }

    /**
     * Gets the clickListener
     *
     * @return The clickListener
     */
    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    /**
     * Gets the server
     *
     * @return The server
     */
    public Server getServer() {
        return server;
    }

    /**
     * Sets the server
     *
     * @param server The server
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * Gets the rcon
     *
     * @return The rcon
     */
    public RCon getRcon() {
        return rcon;
    }

    /**
     * Sets the rcon
     *
     * @param rcon The rcon
     */
    public void setRcon(RCon rcon) {
        this.rcon = rcon;
    }
}
