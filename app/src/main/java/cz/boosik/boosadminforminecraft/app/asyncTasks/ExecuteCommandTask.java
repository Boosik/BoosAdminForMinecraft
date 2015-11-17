package cz.boosik.boosadminforminecraft.app.asyncTasks;

import android.content.Context;
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
 * Async task used for command execution using rcon
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ExecuteCommandTask extends AsyncTask<String, Void, String> {

    private Context context;
    private View view;
    private View.OnClickListener listener;
    private Server server;

    /**
     * Default constructor
     *
     * @param context  Context of async task
     * @param view     View to be targeted by snackbar
     * @param listener Listener of snackbar
     */
    public ExecuteCommandTask(Context context, View view, View.OnClickListener listener) {
        this.context = context;
        this.view = view;
        this.listener = listener;
    }

    /**
     * Constructor that should only be used to check server rcon availability
     *
     * @param context Context of async task
     * @param server  Server to be checked
     */
    public ExecuteCommandTask(Context context, Server server) {
        this.context = context;
        this.server = server;
    }

    @Override
    protected String doInBackground(String... params) {
        String response;
        try {
            if (context instanceof ServerListActivity) {
                if (((ServerListActivity) context).getRcon() == null || ((ServerListActivity) context).getRcon().isShutdown()) {
                    ((ServerListActivity) context).setRcon(new RCon(server.getHost(), Integer.valueOf(server.getPort()), server.getPassword().toCharArray()));
                }
                response = ((ServerListActivity) context).getRcon().send(params[0]);
            } else {
                server = ((ServerControlActivity) context).getServer();
                if (((ServerControlActivity) context).getRcon() == null || ((ServerControlActivity) context).getRcon().isShutdown()) {
                    ((ServerControlActivity) context).setRcon(new RCon(server.getHost(), Integer.valueOf(server.getPort()), server.getPassword().toCharArray()));
                }
                response = ((ServerControlActivity) context).getRcon().send(params[0]);
            }
        } catch (IOException e) {
            return null;
        } catch (AuthenticationException e) {
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if (context instanceof ServerListActivity) {
            if (result == null) {
                ((ServerListActivity) context).invokeError("rcon");
                return;
            } else {
                ((ServerListActivity) context).checkQuery();
                return;
            }
        }
        if (result == null) {
            ((ServerControlActivity) context).invokeError("rcon");
            return;
        }
        if (view != null) {
            if (result.isEmpty()) {
                ((ServerControlActivity) context).setSnackbar(Snackbar
                        .make(view, R.string.no_response, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, listener));
                ((ServerControlActivity) context).getSnackbar().show();
                return;
            }
            result = result.replaceAll("��.", "");
            ((ServerControlActivity) context).setSnackbar(Snackbar
                    .make(view, result, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, listener));
            ((ServerControlActivity) context).getSnackbar().show();
        }
    }
}
