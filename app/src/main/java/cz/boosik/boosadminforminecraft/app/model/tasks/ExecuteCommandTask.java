package cz.boosik.boosadminforminecraft.app.model.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import com.google.rconclient.rcon.AuthenticationException;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerControlActivity;

import java.io.IOException;

import static cz.boosik.boosadminforminecraft.app.presenter.fragments.AbstractServerControlFragment.*;

/**
 * Async task used for command execution using rcon
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ExecuteCommandTask extends AsyncTask<String, Void, String> {

    private Context context;
    private View view;
    private View.OnClickListener listener;

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

    @Override
    protected void onPreExecute() {
        Log.i("ExecuteCommandTask", "preexecute - " + selectedServer.getHost() + ":" + selectedServer.getPort() + "context: " + context);
    }

    @Override
    protected String doInBackground(String... params) {
        Log.i("ExecuteCommandTask", "start - " + selectedServer.getHost() + selectedServer.getPort());
        String response;
        try {
            if (rcon == null || rcon.isShutdown()) {
                rcon = (new RCon(selectedServer.getHost(), Integer.valueOf(selectedServer.getPort()), selectedServer.getPassword().toCharArray()));
            }
            response = rcon.send(params[0]);
        } catch (IOException e) {
            return null;
        } catch (AuthenticationException e) {
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("ExecuteCommandTask", "end - " + selectedServer.getHost() + selectedServer.getPort());
        ServerControlActivity activity = (ServerControlActivity) context;
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
