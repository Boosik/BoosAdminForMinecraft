package cz.boosik.boosadminforminecraft.app.fragments;

import android.content.Context;
import com.google.rconclient.rcon.RCon;
import cz.boosik.boosadminforminecraft.app.App;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.tasks.ExecuteCommandTask;
import cz.boosik.boosadminforminecraft.app.tasks.LoadOnlinePlayersTask;
import cz.boosik.boosadminforminecraft.app.tasks.LoadPluginsTask;
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

    protected void initializeQuery() {
        try {
            query = new MCQuery(selectedServer.getQueryHost(), Integer.valueOf(selectedServer.getQueryPort()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void executeCommand(String commandString) {
        new ExecuteCommandTask(getActivity(), getView(), ((ServerControlActivity) getActivity()).getClickListener()).execute(commandString);
    }

    protected void loadOnlinePlayers() {
        new LoadOnlinePlayersTask(this, getActivity()).execute();
    }

    protected void loadPlugins() {
        new LoadPluginsTask(this).execute();
    }
}
