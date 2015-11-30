package cz.boosik.boosadminforminecraft.app.presenter.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.*;
import com.melnykov.fab.FloatingActionButton;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerAddActivity;
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.model.servers.Server;
import cz.boosik.boosadminforminecraft.app.model.servers.ServerProvider;
import cz.boosik.boosadminforminecraft.app.model.servers.ServerStorage;
import cz.boosik.boosadminforminecraft.app.view.adapters.CardArrayServerAdapter;
import query.MCQuery;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static cz.boosik.boosadminforminecraft.app.presenter.activities.ServerListActivity.query;

/**
 * Fragment used to display servers list
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerListFragment extends AbstractServerFragment {

    @Bind(R.id.server_list)
    ListView lv;
    @Bind(R.id.no_servers_added)
    TextView tv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private ServerProvider serverProvider;
    private ServerStorage serverStore;
    private CardArrayServerAdapter adapter;
    private ArrayList<Server> servers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server_list, container, false);
        ButterKnife.bind(this, view);
        prepareServerList();
        return view;
    }

    /**
     * Server lick click listener
     *
     * @param position position of clicked server
     */
    @OnItemClick(R.id.server_list)
    public void onItemClick(int position) {
        selectedServer = (Server) lv.getItemAtPosition(position);
        try {
            query = new MCQuery(selectedServer.getQueryHost(), Integer.valueOf(selectedServer.getQueryPort()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((ServerListActivity) getActivity()).checkRcon();
    }

    /**
     * Server long click listener
     *
     * @param position Position of long clicked server
     * @return true
     */
    @OnItemLongClick(R.id.server_list)
    public boolean onItemLongClick(int position) {
        showDeleteDialog(position);
        return true;
    }

    /**
     * Floating action button listener
     */
    @OnClick(R.id.fab)
    public void onFabClick() {
        Intent i = new Intent(getActivity(), ServerAddActivity.class);
        startActivity(i);
    }

    /**
     * Prepares the server list
     */
    private void prepareServerList() {
        serverProvider = new ServerProvider(getActivity(), "servers.json");
        try {
            serverStore = serverProvider.readServers();
        } catch (FileNotFoundException e) {
            lv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            return;
        }
        servers = serverStore.getServers();

        adapter = new CardArrayServerAdapter(getContext(), R.layout.list_item_card, servers);
        lv.setAdapter(adapter);

        fab.attachToListView(lv);
        fab.show();
    }

    /**
     * Deletes server at the given position
     *
     * @param position Position of server
     */
    private void deleteServer(int position) {
        servers.remove(position);
        serverStore.setServers(servers);
        try {
            serverProvider.writeServers(serverStore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the delete confirmation dialog
     *
     * @param position clicked item position
     */
    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_entry)
                .setMessage(R.string.delete_entry_msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteServer(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
