package cz.boosik.boosadminforminecraft.app.asyncTasks;

import android.os.AsyncTask;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPluginsFragment;
import cz.boosik.boosadminforminecraft.app.query.QueryResponse;

/**
 * Async task used to load installed plugins from the server query
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class LoadPluginsTask extends AsyncTask<Void, Void, QueryResponse> {

    ServerControlPluginsFragment fragment;

    /**
     * Default constructor
     *
     * @param fragment ServerControlPluginsFragment
     */
    public LoadPluginsTask(ServerControlPluginsFragment fragment) {
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
        fragment.getPlugins().clear();
        for (String plugin : response.getPlugins()) {
            for (String supportedPlugin : fragment.getSupportedPluginsNames()) {
                if (plugin.toLowerCase().contains(supportedPlugin.toLowerCase())) {
                    fragment.getPlugins().add(plugin);
                }
            }
        }
        java.util.Collections.sort(fragment.getPlugins());
        fragment.getAdapter().notifyDataSetChanged();
    }
}
