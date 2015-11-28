package cz.boosik.boosadminforminecraft.app.tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPluginsFragment;
import cz.boosik.boosadminforminecraft.app.model.commands.CommandProvider;
import query.QueryResponse;

import static cz.boosik.boosadminforminecraft.app.fragments.AbstractServerControlFragment.query;

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
     */
    public LoadPluginsTask(Fragment context) {
        this.context = context;
    }

    @Override
    protected QueryResponse doInBackground(Void... params) {
        try {
            return query.fullStat();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(QueryResponse response) {
        ServerControlPluginsFragment fragment = (ServerControlPluginsFragment)context;
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
