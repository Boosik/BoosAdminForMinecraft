package cz.boosik.boosadminforminecraft.app.presenter.fragments;

import android.content.Context;
import android.util.Log;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.App;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.model.tasks.ExecuteCommandTask;
import cz.boosik.boosadminforminecraft.app.model.tasks.LoadOnlinePlayersTask;
import cz.boosik.boosadminforminecraft.app.model.tasks.LoadPluginsTask;
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.view.adapters.CardArrayStringAdapter;
import query.MCQuery;

import java.util.ArrayList;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public abstract class AbstractServerControlFragment extends AbstractServerFragment {

    Context context = App.getContext();
    protected CardArrayStringAdapter serverAdapter = new CardArrayStringAdapter(context, R.layout.list_item_card_one_line, new ArrayList<String>());
    protected CardArrayStringAdapter playerAdapter = new CardArrayStringAdapter(context, R.layout.list_item_card_one_line, new ArrayList<String>());
    protected CardArrayStringAdapter pluginAdapter = new CardArrayStringAdapter(context, R.layout.list_item_card_one_line, new ArrayList<String>());
    public static RCon rcon;
    public static MCQuery query;

    /**
     * Initializes query object
     */
    protected void initializeQuery() {
        try {
            query = new MCQuery(selectedServer.getQueryHost(), Integer.valueOf(selectedServer.getQueryPort()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes command
     * @param commandString command to be executed
     */
    protected void executeCommand(String commandString) {
        Log.i("executeCommand", "ExecuteCommandTask - " + selectedServer.getHost() + selectedServer.getPort());
        new ExecuteCommandTask(getActivity(), getView(), ((ServerControlActivity) getActivity()).getClickListener()).execute(commandString);
    }

    /**
     * Loads online players
     */
    protected void loadOnlinePlayers() {
        Log.i("loadOnlinePlayers", "LoadOnlinePlayersTask - " + selectedServer.getQueryHost() + Integer.valueOf(selectedServer.getQueryPort()));
        new LoadOnlinePlayersTask(this, getActivity()).execute();
    }

    /**
     * Loads plugins
     */
    protected void loadPlugins() {
        Log.i("loadPlugins", "LoadPluginsTask - " + selectedServer.getQueryHost() + Integer.valueOf(selectedServer.getQueryPort()));
        new LoadPluginsTask(this).execute();
    }
}
