package cz.boosik.boosadminforminecraft.app.asyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPlayersFragment;
import cz.boosik.boosadminforminecraft.app.query.QueryResponse;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class LoadOnlinePlayersTask extends AsyncTask<Void, Void, QueryResponse> {

    ServerControlPlayersFragment fragment;
    ServerControlActivity activity;

    public LoadOnlinePlayersTask(ServerControlPlayersFragment fragment, ServerControlActivity activity) {
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    protected QueryResponse doInBackground(Void... params) {
        try {
            if (activity == null) activity = (ServerControlActivity) fragment.getActivity();
            return activity.getMcQuery().fullStat();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(QueryResponse response) {
        if (activity == null) activity = (ServerControlActivity) fragment.getActivity();
        if (response == null) {
            activity.invokeError("query");
            return;
        }
        if (fragment != null) {
            fragment.getOnlinePlayers().clear();
            fragment.getOnlinePlayers().addAll(response.getPlayerList());
            java.util.Collections.sort(fragment.getOnlinePlayers());
            fragment.getAdapter().notifyDataSetChanged();
        }
    }
}
