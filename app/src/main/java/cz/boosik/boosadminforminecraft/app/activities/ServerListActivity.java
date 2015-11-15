package cz.boosik.boosadminforminecraft.app.activities;

import android.content.Intent;
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

import java.util.Objects;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerListActivity extends AppCompatActivity {

    private Snackbar snackbar;
    private RCon rcon = null;
    private String selected;
    private MCQuery mcQuery;
    public final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            snackbar.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);
        setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server_select, menu);
        return true;
    }

    public void onAddButtonClick(MenuItem m) {
        Intent i = new Intent(this, ServerAddActivity.class);
        startActivity(i);
    }

    public void checkRcon(Server server) {
        new ExecuteCommandTask(this, server).execute("list");
    }

    public void checkQuery() {
        new LoadOnlinePlayersTask(this).execute();
    }

    public void invokeError(String type) {
        String string = type.equals("query") ? getString(R.string.query_error) : getString(R.string.rcon_error);
        snackbar = Snackbar
                .make(findViewById(R.id.server_list_fragment), string, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, clickListener);
        snackbar.show();
    }

    public void connect() {
        Intent i = new Intent(this, ServerControlActivity.class);
        i.putExtra("serverName", selected);
        startActivity(i);
    }

    public Snackbar getSnackbar() {
        return snackbar;
    }

    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public RCon getRcon() {
        return rcon;
    }

    public void setRcon(RCon rcon) {
        this.rcon = rcon;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public MCQuery getMcQuery() {
        return mcQuery;
    }

    public void setMcQuery(MCQuery mcQuery) {
        this.mcQuery = mcQuery;
    }
}
