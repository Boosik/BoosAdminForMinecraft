package cz.boosik.boosadminforminecraft.app.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.model.commands.CommandProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used to display supported plugins
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerControlPluginsFragment {

    @Bind(R.id.player_command_list)
    ListView lv;
    @Bind(R.id.plugins_refresher)
    SwipeRefreshLayout swipeView;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<String> plugins = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_control_plugins, container, false);
        ButterKnife.bind(this, rootView);
        if (query == null) {
            initializeQuery();
        }
        preparePluginsList();
        updatePlugins();
        prepareView();
        return rootView;
    }

    /**
     * Creates new instance of this fragment with the section number used for paging
     *
     * @param sectionNumber Section number in pager
     * @return Instance of this fragment with set section number
     */
    public static ServerControlPluginsFragment newInstance(int sectionNumber) {
        ServerControlPluginsFragment fragment = new ServerControlPluginsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @OnItemClick(R.id.player_command_list)
    public void onItemClick(int position) {
        preparePlayerDialog(plugins.get(position));
    }

    /**
     * Prepares the list of plugins
     */
    private void preparePluginsList() {
        pluginAdapter.setData(plugins);
        lv.setAdapter(pluginAdapter);
    }

    /**
     * Prepares the view to make pull to refresh possible
     */
    private void prepareView() {
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updatePlugins();
                        swipeView.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeView.setEnabled(true);
                else
                    swipeView.setEnabled(false);
            }
        });
    }

    /**
     * Updates the plugins list to the current values
     */
    private void updatePlugins() {
        loadPlugins();
    }

    /**
     * Prepares the dialog that is shown on plugin name click
     *
     * @param plugin Clicked plugin
     */
    private void preparePlayerDialog(final String plugin) {
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_edit_text_spinner, null);
        AppCompatSpinner s = (AppCompatSpinner) dialogView.findViewById(R.id.playerSpinner);
        final List<String> pluginCommands = CommandProvider.pluginCommandsMap.get(plugin.trim().split("[ ]", 2)[0].toLowerCase());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, pluginCommands);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = pluginCommands.get(position);
                EditText et = (EditText) dialogView.findViewById(R.id.playerParams);
                if (selected.contains("<")) {
                    String[] split = selected.split("(?=<.*>)", 2);
                    if (split.length > 1) {
                        et.setVisibility(View.VISIBLE);
                        et.setHint(split[1]);
                    }
                } else {
                    et.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });
        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(plugin)
                .setMessage(R.string.dialog_player)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AppCompatSpinner spinner = (AppCompatSpinner) dialogView.findViewById(R.id.playerSpinner);
                        EditText editText = (EditText) dialogView.findViewById(R.id.playerParams);
                        String commandString = pluginCommands.get(spinner.getSelectedItemPosition());
                        commandString = commandString.split("<.*>", 2)[0];
                        commandString = commandString + " " + editText.getText().toString();
                        executeCommand(commandString);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * Gets the plugins
     *
     * @return The plugins
     */
    public List<String> getPlugins() {
        return plugins;
    }

    /**
     * Gets the adapter
     *
     * @return The adapter
     */
    public ArrayAdapter<String> getAdapter() {
        return pluginAdapter;
    }
}
