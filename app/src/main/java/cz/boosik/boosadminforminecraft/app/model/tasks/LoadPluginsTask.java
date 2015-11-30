package cz.boosik.boosadminforminecraft.app.model.tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import cz.boosik.boosadminforminecraft.app.presenter.fragments.ServerControlPluginsFragment;
import cz.boosik.boosadminforminecraft.app.model.commands.CommandProvider;
import query.MCQuery;
import query.QueryResponse;

import static cz.boosik.boosadminforminecraft.app.presenter.fragments.AbstractServerControlFragment.*;

/**
 * Async task used to load installed plugins from the server query
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class LoadPluginsTask extends AsyncTask<Void, Void, QueryResponse> {

    Fragment context;

    /**
     * Default constructor
     *
     * @param context The context
     */
    public LoadPluginsTask(Fragment context) {
        this.context = context;
    }

    @Override
    protected QueryResponse doInBackground(Void... params) {
        Log.i("LoadPluginsTask", "start - " + selectedServer.getQueryHost() + Integer.valueOf(selectedServer.getQueryPort()));
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
        Log.i("LoadPluginsTask", "end - " + selectedServer.getQueryHost() + Integer.valueOf(selectedServer.getQueryPort()));
        ServerControlPluginsFragment fragment = (ServerControlPluginsFragment) context;
        fragment.getPlugins().clear();
        for (String plugin : response.getPlugins()) {
            for (String supportedPlugin : CommandProvider.supportedPluginNames) {
                if (plugin.toLowerCase().contains(supportedPlugin.toLowerCase())) {
                    fragment.getPlugins().add(plugin);
                }
            }
        }
        java.util.Collections.sort(fragment.getPlugins());
        fragment.getAdapter().notifyDataSetChanged();
    }
}
