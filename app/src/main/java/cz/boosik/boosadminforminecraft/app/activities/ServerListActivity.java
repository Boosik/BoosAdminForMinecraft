package cz.boosik.boosadminforminecraft.app.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.asyncTasks.ExecuteCommandTask;
import cz.boosik.boosadminforminecraft.app.asyncTasks.LoadOnlinePlayersTask;
import cz.boosik.boosadminforminecraft.app.query.MCQuery;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;

/**
 * Activity of server list
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerListActivity extends AppCompatActivity {

    private Snackbar snackbar;
    private RCon rcon = null;
    private String selected;
    private MCQuery mcQuery;
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            snackbar.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);
        setTitle(getString(R.string.server_list));
        String error = getIntent().getStringExtra("error");
        if (error != null) {
            invokeError(error);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server_select, menu);
        return true;
    }

    /**
     * Starts the server add activity
     *
     * @param m A MenuItem
     */
    public void onAddButtonClick(MenuItem m) {
        Intent i = new Intent(this, ServerAddActivity.class);
        startActivity(i);
    }

    /**
     * Executes example command on the server to see if it is online
     *
     * @param server Server to check
     */
    public void checkRcon(Server server) {
        new ExecuteCommandTask(this, server).execute("list");
    }

    /**
     * Attempts to get data using server query to check if the target server is online
     */
    public void checkQuery() {
        new LoadOnlinePlayersTask(this).execute();
    }

    /**
     * Show snackbar containing target error
     *
     * @param type The type of error
     */
    public void invokeError(String type) {
        String string = null;
        switch (type) {
            case "query":
                string = getString(R.string.query_error);
                break;
            case "rcon":
                string = getString(R.string.rcon_error);
                break;
            case "delete":
                string = getString(R.string.server_list_hint);
                break;
        }

        if (string != null) {
            snackbar = Snackbar
                    .make(findViewById(R.id.server_list_fragment), string, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, clickListener);
            snackbar.show();
        }
    }

    /**
     * Starts the server control activity for the selected server
     */
    public void connect() {
        Intent i = new Intent(this, ServerControlActivity.class);
        i.putExtra("serverName", selected);
        startActivity(i);
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

    /**
     * Sets the selected
     *
     * @param selected The selected
     */
    public void setSelected(String selected) {
        this.selected = selected;
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
     * Sets the mcQuery
     *
     * @param mcQuery The mcQuery
     */
    public void setMcQuery(MCQuery mcQuery) {
        this.mcQuery = mcQuery;
    }
}
