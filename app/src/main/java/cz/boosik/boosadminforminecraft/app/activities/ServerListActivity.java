package cz.boosik.boosadminforminecraft.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cz.boosik.boosadminforminecraft.app.R;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerListActivity extends AppCompatActivity {

    private Snackbar snackbar;
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

    public Snackbar getSnackbar() {
        return snackbar;
    }

    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }
}
