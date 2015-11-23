package cz.boosik.boosadminforminecraft.app.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPlayersFragment;
import query.QueryResponse;

/**
 * Async task used to load online players from the server query
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class LoadOnlinePlayersTask extends AsyncTask<Void, Void, QueryResponse> {

    ServerControlPlayersFragment fragment;
    Context context;

    /**
     * Default constructor
     *
     * @param fragment ServerControlPlayersFragment
     * @param context  Context of async task
     */
    public LoadOnlinePlayersTask(ServerControlPlayersFragment fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
    }

    /**
     * Constructor that should only be used to check server query availability
     *
     * @param context Context of async task
     */
    public LoadOnlinePlayersTask(Context context) {
        this.context = context;
    }

    @Override
    protected QueryResponse doInBackground(Void... params) {
        try {
            if (context == null) {
                context = fragment.getActivity();
            }
            if (context instanceof ServerListActivity) {
                return ((ServerListActivity) context).getMcQuery().fullStat();
            } else {
                return ((ServerControlActivity) context).getMcQuery().fullStat();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(QueryResponse response) {
        if (context instanceof ServerListActivity) {
            if (response == null) {
                ((ServerListActivity) context).invokeError("query");
                return;
            } else {
                ((ServerListActivity) context).connect();
                return;
            }
        }
        if (response == null) {
            ((ServerControlActivity) context).invokeError("query");
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
