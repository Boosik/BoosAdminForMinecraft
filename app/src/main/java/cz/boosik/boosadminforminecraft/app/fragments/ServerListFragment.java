package cz.boosik.boosadminforminecraft.app.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;
import cz.boosik.boosadminforminecraft.app.serverStore.ServerStorage;
import cz.boosik.boosadminforminecraft.app.serverStore.StorageProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerListFragment extends Fragment {

    @Bind(R.id.server_list)
    ListView lv;
    @Bind(R.id.no_servers_added)
    TextView tv;
    @Bind(R.id.server_list_header)
    TextView tv2;

    private StorageProvider storageProvider;
    private ServerStorage serverStore;
    private ArrayAdapter<String> adapter;
    private ArrayList<Server> servers;
    private LinkedList<String> srvs = new LinkedList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server_list, container, false);
        ButterKnife.bind(this, view);
        prepareServerList();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (servers != null) {
            ((ServerListActivity) getActivity()).setSnackbar(Snackbar
                    .make(getView(), R.string.server_list_hint, Snackbar.LENGTH_LONG)
                    .setAction(R.string.ok, ((ServerListActivity) getActivity()).getClickListener()));
            ((ServerListActivity) getActivity()).getSnackbar().show();
        }
    }

    private void prepareServerList() {
        storageProvider = new StorageProvider(getActivity(), "servers.json");
        try {
            serverStore = storageProvider.readServers();
        } catch (FileNotFoundException e) {
            lv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.GONE);
            return;
        }
        servers = serverStore.getServers();
        for (Server server : servers) {
            srvs.add(server.getName());
        }
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, srvs);
        lv.setAdapter(adapter);
    }

    @OnItemClick(R.id.server_list)
    public void onItemClick(int position) {
        String choice = (String) lv.getItemAtPosition(position);
        Intent i = new Intent(getActivity(), ServerControlActivity.class);
        i.putExtra("serverName", choice);
        startActivity(i);
    }

    @OnItemLongClick(R.id.server_list)
    public boolean onItemLongClick(int position) {
        showDeleteDialog(position);
        return true;
    }

    private void deleteServer(int position) {
        servers.remove(position);
        serverStore.setServers(servers);
        srvs.remove(position);
        try {
            storageProvider.writeServers(serverStore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
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
