package cz.boosik.boosadminforminecraft.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.tasks.CheckQueryTask;
import cz.boosik.boosadminforminecraft.app.tasks.CheckRconTask;
import query.MCQuery;

/**
 * Activity of server list
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerListActivity extends AppCompatActivity {

    private Snackbar snackbar;
    private String selected;
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            snackbar.dismiss();
        }
    };
    public static MCQuery query;
    public static RCon rcon;

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
        return false;
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
     * Sets the selected
     *
     * @param selected The selected
     */
    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void checkRcon() {
        new CheckRconTask(this).execute();
    }

    public void checkQuery() {
        new CheckQueryTask(this).execute();
    }

    /**
     * Starts the server control activity for the selected server
     */
    public void connect() {
        System.out.println("Connect");
        Intent i = new Intent(this, ServerControlActivity.class);
        i.putExtra("serverName", selected);
        startActivity(i);
    }
}
