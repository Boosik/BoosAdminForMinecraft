package cz.boosik.boosadminforminecraft.app.fragments;

/**
 * @author jakub.kolar@bsc-ideas.com
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.commands.BaseCommands;
import cz.boosik.boosadminforminecraft.app.commands.Command;
import cz.boosik.boosadminforminecraft.app.commands.CommandStorage;
import cz.boosik.boosadminforminecraft.app.components.CustomNumberPicker;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerControlServerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Bind(R.id.server_command_list)
    ListView lv;

    CommandStorage baseCommands;
    private ArrayAdapter<String> adapter;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ServerControlServerFragment newInstance(int sectionNumber) {
        ServerControlServerFragment fragment = new ServerControlServerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ServerControlServerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_control_server, container, false);
        ButterKnife.bind(this, rootView);
        prepareServerCommands();
        return rootView;
    }

    private void prepareServerCommands() {
        baseCommands = new CommandStorage();
        ArrayList<Command> commandArrayList = new ArrayList<>();
        ArrayList<String> commandNamesArrayList = new ArrayList<>();

        for (BaseCommands bc : BaseCommands.values()) {
            commandArrayList.add(new Command(bc.name().toLowerCase().replace("_", " "), bc.getCommandString()));
        }
        baseCommands.setCommands(commandArrayList);

        for (Command command : commandArrayList) {
            commandNamesArrayList.add(command.getName());
        }

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, commandNamesArrayList);

//        ServersAdapter adapter = new ServersAdapter(getActivity(),serverStore.getServers());

        lv.setAdapter(adapter);
    }

    @OnItemClick(R.id.server_command_list)
    public void onItemClick(int position) {
        String commandString = baseCommands.getCommands().get(position).getCommand();
        if (commandString.contains("<page>")) {
            preparePageDialog(commandString);
        } else if (commandString.contains("<command>")) {
            prepareEditTextDialog(commandString, "<command>", R.string.dialog_enter, false);
        } else if (commandString.contains("<message>")) {
            prepareEditTextDialog(commandString, "<message>", R.string.dialog_enter, false);
        } else if (commandString.contains("<gamemode>")) {
            prepareSpinnerDialog(commandString, "<gamemode>", R.string.dialog_select, R.array.gamemodes_array);
        } else if (commandString.contains("<time>")) {
            prepareSpinnerDialog(commandString, "<time>", R.string.dialog_select, R.array.time_array);
        } else if (commandString.contains("<weather>")) {
            prepareSpinnerDialog(commandString, "<weather>", R.string.dialog_select, R.array.weather_array);
        } else if (commandString.contains("<timeNumber>")) {
            prepareEditTextDialog(commandString, "<timeNumber>", R.string.dialog_enter, true);
        }
    }

    private void preparePageDialog(final String commandString) {
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_page, null);
        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(commandString)
                .setMessage(getString(R.string.dialog_select)+"<page>")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CustomNumberPicker page = (CustomNumberPicker) dialogView.findViewById(R.id.page);
                        Snackbar.make(getView(), commandString.replace("<page>", String.valueOf(page.getValue())), Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void prepareEditTextDialog(final String commandString, final String replacable, int messageId, boolean isNumber) {
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_edit_text, null);
        EditText editText = (EditText) dialogView.findViewById(R.id.editTextDialog);
        if (isNumber) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(commandString)
                .setMessage(getString(messageId)+replacable)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) dialogView.findViewById(R.id.editTextDialog);
                        Snackbar.make(getView(), commandString.replace(replacable, editText.getText().toString()), Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void prepareSpinnerDialog(final String commandString, final String replacable, int messageId, final int arrayId) {
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_spinner, null);
        AppCompatSpinner s = (AppCompatSpinner) dialogView.findViewById(R.id.spinnerDialog);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(getResources().getStringArray(arrayId))); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(commandString)
                .setMessage(getString(messageId)+replacable)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AppCompatSpinner spinner = (AppCompatSpinner) dialogView.findViewById(R.id.spinnerDialog);
                        Snackbar.make(getView(), commandString.replace(replacable, spinner.getSelectedItem().toString()), Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}
