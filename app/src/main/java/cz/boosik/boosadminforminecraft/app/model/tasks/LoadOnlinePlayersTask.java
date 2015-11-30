package cz.boosik.boosadminforminecraft.app.model.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.presenter.fragments.ServerControlPlayersFragment;
import query.MCQuery;
import query.QueryResponse;

import static cz.boosik.boosadminforminecraft.app.presenter.fragments.AbstractServerControlFragment.*;

/**
 * Async task used to load online players from the server query
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class LoadOnlinePlayersTask extends AsyncTask<Void, Void, QueryResponse> {

    Fragment fragment;
    Context context;

    /**
     * Default constructor
     *
     * @param fragment ServerControlPlayersFragment
     * @param context  Context of async task
     */
    public LoadOnlinePlayersTask(Fragment fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    protected QueryResponse doInBackground(Void... params) {
        Log.i("LoadOnlinePlayersTask", "start - " + selectedServer.getQueryHost() + Integer.valueOf(selectedServer.getQueryPort()));
        try {
            if (query == null) {
                try {
                    query = new MCQuery(selectedServer.getQueryHost(), Integer.valueOf(selectedServer.getQueryPort()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return query.fullStat();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(QueryResponse response) {
        Log.i("LoadOnlinePlayersTask", "end - " + selectedServer.getQueryHost() + Integer.valueOf(selectedServer.getQueryPort()));
        ServerControlPlayersFragment fr = (ServerControlPlayersFragment) fragment;
        if (response == null) {
            ((ServerControlActivity) context).invokeError("query");
            return;
        }
        if (fr != null) {
            fr.getOnlinePlayers().clear();
            fr.getOnlinePlayers().addAll(response.getPlayerList());
            java.util.Collections.sort(fr.getOnlinePlayers());
            fr.getAdapter().notifyDataSetChanged();
        }
    }
}
