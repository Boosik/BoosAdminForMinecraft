package cz.boosik.boosadminforminecraft.app.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.google.rconclient.rcon.AuthenticationException;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.activities.ServerListActivity;

import java.io.IOException;

import static cz.boosik.boosadminforminecraft.app.fragments.AbstractServerFragment.selectedServer;
import static cz.boosik.boosadminforminecraft.app.activities.ServerListActivity.rcon;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class CheckRconTask extends AsyncTask<Void, Void, String> {

    private Context context;

    public CheckRconTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String response;
        try {
            if (rcon == null || rcon.isShutdown()) {
                rcon = (new RCon(selectedServer.getHost(), Integer.valueOf(selectedServer.getPort()), selectedServer.getPassword().toCharArray()));
            }
            response = rcon.send("list");
        } catch (IOException e) {
            return null;
        } catch (AuthenticationException e) {
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        ServerListActivity activity = (ServerListActivity)context;
        if (result == null) {
            activity.invokeError("query");
            return;
        }
        System.out.println("CheckRcon");
        activity.setSelected(selectedServer.getName());
        activity.checkQuery();
    }
}
