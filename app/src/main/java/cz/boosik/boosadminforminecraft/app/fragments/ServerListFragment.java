package cz.boosik.boosadminforminecraft.app.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
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
import cz.boosik.boosadminforminecraft.app.activities.ServerAddActivity;
import cz.boosik.boosadminforminecraft.app.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.adapters.CardArrayServerAdapter;
import cz.boosik.boosadminforminecraft.app.query.MCQuery;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;
import cz.boosik.boosadminforminecraft.app.serverStore.ServerStorage;
import cz.boosik.boosadminforminecraft.app.serverStore.StorageProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Fragment used to display servers list
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerListFragment extends Fragment {

    @Bind(R.id.server_list)
    ListView lv;
    @Bind(R.id.no_servers_added)
    TextView tv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private StorageProvider storageProvider;
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

    @OnItemClick(R.id.server_list)
    public void onItemClick(int position) {
        Server server = (Server) lv.getItemAtPosition(position);
        MCQuery mcQuery = null;
        try {
            mcQuery = new MCQuery(server.getQueryHost(), Integer.valueOf(server.getQueryPort()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((ServerListActivity) getActivity()).setSelected(server.getName());
        ((ServerListActivity) getActivity()).setMcQuery(mcQuery);
        ((ServerListActivity) getActivity()).checkRcon(server);
    }

    @OnItemLongClick(R.id.server_list)
    public boolean onItemLongClick(int position) {
        showDeleteDialog(position);
        return true;
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        Intent i = new Intent(getActivity(), ServerAddActivity.class);
        startActivity(i);
    }

    /**
     * Prepares the server list
     */
    private void prepareServerList() {
        storageProvider = new StorageProvider(getActivity(), "servers.json");
        try {
            serverStore = storageProvider.readServers();
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
            storageProvider.writeServers(serverStore);
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
