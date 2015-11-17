package cz.boosik.boosadminforminecraft.app.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.asyncTasks.ExecuteCommandTask;
import cz.boosik.boosadminforminecraft.app.asyncTasks.LoadOnlinePlayersTask;
import cz.boosik.boosadminforminecraft.app.commands.Command;
import cz.boosik.boosadminforminecraft.app.commands.CommandStorage;
import cz.boosik.boosadminforminecraft.app.commands.PlayerCommands;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used to display online players list
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerControlPlayersFragment extends Fragment {

    @Bind(R.id.player_command_list)
    ListView lv;
    @Bind(R.id.players_refresher)
    SwipeRefreshLayout swipeView;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private CommandStorage playerCommands;
    private ArrayAdapter<String> adapter;
    private List<String> onlinePlayers;
    private ArrayList<String> commandNamesArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_control_players, container, false);
        ButterKnife.bind(this, rootView);
        preparePlayerCommands();
        prepareOnlineList();
        updateOnlinePlayers();
        prepareView();
        return rootView;
    }

    /**
     * Creates new instance of this fragment with the section number used for paging
     *
     * @param sectionNumber Section number in pager
     * @return Instance of this fragment with set section number
     */
    public static ServerControlPlayersFragment newInstance(int sectionNumber) {
        ServerControlPlayersFragment fragment = new ServerControlPlayersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @OnItemClick(R.id.player_command_list)
    public void onItemClick(int position) {
        preparePlayerDialog(onlinePlayers.get(position));
    }

    /**
     * Prepares the list of online players
     */
    private void prepareOnlineList() {
        onlinePlayers = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, onlinePlayers);
        lv.setAdapter(adapter);
    }

    /**
     * Prepares the player commands lists
     */
    private void preparePlayerCommands() {
        playerCommands = new CommandStorage();
        commandNamesArrayList = new ArrayList<>();
        ArrayList<Command> commandArrayList = new ArrayList<>();
        for (PlayerCommands bc : PlayerCommands.values()) {
            commandArrayList.add(new Command(bc.name().toLowerCase().replace("_", " "), bc.getCommandString()));
        }
        playerCommands.setCommands(commandArrayList);
        for (Command command : commandArrayList) {
            commandNamesArrayList.add(command.getName());
        }
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
                        updateOnlinePlayers();
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
     * Updates the online players list to the current values
     */
    private void updateOnlinePlayers() {
        new LoadOnlinePlayersTask(this, getActivity()).execute();
    }

    /**
     * Prepares the dialog that is shown on player name click
     *
     * @param player Clicked player
     */
    private void preparePlayerDialog(final String player) {
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_edit_text_spinner, null);
        AppCompatSpinner s = (AppCompatSpinner) dialogView.findViewById(R.id.playerSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, commandNamesArrayList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = playerCommands.getCommands().get(position).getCommand();
                EditText et = (EditText) dialogView.findViewById(R.id.playerParams);
                if (selected.contains("<")) {
                    String[] split = selected.split("(?=<.*>)", 3);
                    if (split.length > 2) {
                        et.setVisibility(View.VISIBLE);
                        et.setHint(split[2]);
                    } else {
                        et.setVisibility(View.GONE);
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
                .setTitle(player)
                .setMessage(R.string.dialog_player)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AppCompatSpinner spinner = (AppCompatSpinner) dialogView.findViewById(R.id.playerSpinner);
                        EditText editText = (EditText) dialogView.findViewById(R.id.playerParams);
                        String commandString = playerCommands.getCommands().get(spinner.getSelectedItemPosition()).getCommand();
                        commandString = commandString.split("<player>", 2)[0];
                        commandString = commandString + player + " " + editText.getText().toString();
                        new ExecuteCommandTask(getActivity(), getView(), ((ServerControlActivity) getActivity()).getClickListener()).execute(commandString);
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
     * Gets the onlinePlayers
     *
     * @return The onlinePlayers
     */
    public List<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    /**
     * Gets the adapter
     *
     * @return The adapter
     */
    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }
}
