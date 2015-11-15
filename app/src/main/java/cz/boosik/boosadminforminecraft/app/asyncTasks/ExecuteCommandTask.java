package cz.boosik.boosadminforminecraft.app.asyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.google.rconclient.rcon.AuthenticationException;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;

import java.io.IOException;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ExecuteCommandTask extends AsyncTask<String, Void, String> {

    ServerControlActivity activity;
    View view;
    View.OnClickListener listener;

    public ExecuteCommandTask(ServerControlActivity activity, View view, View.OnClickListener listener) {
        this.activity = activity;
        this.view = view;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        Server server = activity.getServer();
        try {
            if (activity.getRcon() == null || activity.getRcon().isShutdown()) {
                activity.setRcon(new RCon(server.getHost(), Integer.valueOf(server.getPort()), server.getPassword().toCharArray()));
            }
            response = activity.getRcon().send(params[0]);
        } catch (IOException e) {
            return null;
        } catch (AuthenticationException e) {
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result == null) {
            activity.invokeError("rcon");
            return;
        }

        if (view != null) {
            if (result.isEmpty()) {
                activity.setSnackbar(Snackbar
                        .make(view, R.string.no_response, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, listener));
                activity.getSnackbar().show();
                return;
            }
            result = result.replaceAll("��.", "");
            activity.setSnackbar(Snackbar
                    .make(view, result, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, listener));
            activity.getSnackbar().show();
        }
    }
}
