package cz.boosik.boosadminforminecraft.app.asyncTasks;

import android.os.AsyncTask;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPlayersFragment;
import cz.boosik.boosadminforminecraft.app.query.QueryResponse;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class LoadOnlinePlayersTask extends AsyncTask<Void, Void, QueryResponse> {

    ServerControlPlayersFragment fragment;

    public LoadOnlinePlayersTask(ServerControlPlayersFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected QueryResponse doInBackground(Void... params) {
        try {
            return ((ServerControlActivity) fragment.getActivity()).getMcQuery().fullStat();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(QueryResponse response) {
        fragment.getOnlinePlayers().clear();
        fragment.getOnlinePlayers().addAll(response.getPlayerList());
        java.util.Collections.sort(fragment.getOnlinePlayers());
        fragment.getAdapter().notifyDataSetChanged();
    }
}
