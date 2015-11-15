package cz.boosik.boosadminforminecraft.app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;
import cz.boosik.boosadminforminecraft.app.serverStore.StorageProvider;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerAddFragment extends Fragment {

    @Bind(R.id.etServerName)
    EditText etServerName;
    @Bind(R.id.etServerHost)
    EditText etServerHost;
    @Bind(R.id.etServerPort)
    EditText etServerPort;
    @Bind(R.id.etDynmapHost)
    EditText etDynmapHost;
    @Bind(R.id.etDynmapPort)
    EditText etDynmapPort;
    @Bind(R.id.etQueryHost)
    EditText etQueryHost;
    @Bind(R.id.etQueryPort)
    EditText etQueryPort;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btSave)
    Button btSave;

    @OnClick(R.id.btSave)
    public void saveServer() {
        String serverName = etServerName.getText().toString();
        String serverHost = etServerHost.getText().toString();
        String serverPort = etServerPort.getText().toString();
        String dynmapHost = etDynmapHost.getText().toString();
        String dynmapPort = etDynmapPort.getText().toString();
        String queryHost = etQueryHost.getText().toString();
        String queryPort = etQueryPort.getText().toString();
        String password = etPassword.getText().toString();
        StorageProvider storageProvider = new StorageProvider(getActivity(), "servers.json");
        try {
            if (storageProvider.saveServer(new Server(serverName, serverHost, serverPort, password, queryHost, queryPort, dynmapHost, dynmapPort))) {
                Intent i = new Intent(getActivity(), ServerListActivity.class);
                startActivity(i);
            } else {
                Snackbar.make(getView(), getString(R.string.duplicate), Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server_add, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
