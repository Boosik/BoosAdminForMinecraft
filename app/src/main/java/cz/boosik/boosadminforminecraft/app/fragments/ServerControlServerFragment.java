package cz.boosik.boosadminforminecraft.app.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
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
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.adapters.CardArrayStringAdapter;
import cz.boosik.boosadminforminecraft.app.asyncTasks.ExecuteCommandTask;
import cz.boosik.boosadminforminecraft.app.commands.BaseCommands;
import cz.boosik.boosadminforminecraft.app.commands.Command;
import cz.boosik.boosadminforminecraft.app.commands.CommandStorage;
import cz.boosik.boosadminforminecraft.app.components.CustomNumberPicker;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Fragment used to display server commands list
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerControlServerFragment extends Fragment {

    @Bind(R.id.server_command_list)
    ListView lv;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private CommandStorage baseCommands;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_control_server, container, false);
        ButterKnife.bind(this, rootView);
        prepareServerCommands();
        return rootView;
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
        } else if (commandString.contains("<time_number>")) {
            prepareEditTextDialog(commandString, "<time_number>", R.string.dialog_enter, true);
        } else if (commandString.contains("<custom_command>")) {
            prepareEditTextDialog(commandString, "<custom_command>", R.string.dialog_enter, false);
        } else if (commandString.contains("<ip>")) {
            prepareEditTextDialog(commandString, "<ip>", R.string.dialog_enter, false);
        } else if (commandString.contains("<player>")) {
            prepareEditTextDialog(commandString, "<player>", R.string.dialog_enter, false);
        } else {
            new ExecuteCommandTask(getActivity(), getView(), ((ServerControlActivity) getActivity()).getClickListener()).execute(commandString);
        }
    }

    /**
     * Creates new instance of this fragment with the section number used for paging
     *
     * @param sectionNumber Section number in pager
     * @return Instance of this fragment with set section number
     */
    public static ServerControlServerFragment newInstance(int sectionNumber) {
        ServerControlServerFragment fragment = new ServerControlServerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Prepares the server commands lists
     */
    private void prepareServerCommands() {
        baseCommands = new CommandStorage();
        ArrayList<Command> commandArrayList = new ArrayList<>();
        ArrayList<String> commandNamesArrayList = new ArrayList<>();
        commandArrayList.add(0, new Command(getString(R.string.custom_command), "<custom_command>"));
        for (BaseCommands bc : BaseCommands.values()) {
            commandArrayList.add(new Command(bc.name().toLowerCase().replace("_", " "), bc.getCommandString()));
        }
        baseCommands.setCommands(commandArrayList);
        for (Command command : commandArrayList) {
            commandNamesArrayList.add(command.getName());
        }
        CardArrayStringAdapter adapter = new CardArrayStringAdapter(getActivity(), R.layout.list_item_card_one_line, commandNamesArrayList);
        lv.setAdapter(adapter);
    }

    /**
     * Prepares the dialog with number picker
     *
     * @param commandString Commands string
     */
    private void preparePageDialog(final String commandString) {
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_page, null);
        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(commandString)
                .setMessage(getString(R.string.dialog_select) + " " + "<page>")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CustomNumberPicker page = (CustomNumberPicker) dialogView.findViewById(R.id.page);
                        new ExecuteCommandTask(getActivity(), getView(), ((ServerControlActivity) getActivity()).getClickListener()).execute(commandString.replace("<page>", String.valueOf(page.getValue())));
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
     * Prepares the dialog with edit text
     *
     * @param commandString Commands string
     * @param replacable    String to be replaced in commandString
     * @param messageId     Message to be shown in the dialog
     * @param isNumber      Should the dialog be set to allow only numeric input?
     */
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
                .setMessage(getString(messageId) + " " + replacable)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) dialogView.findViewById(R.id.editTextDialog);
                        new ExecuteCommandTask(getActivity(), getView(), ((ServerControlActivity) getActivity()).getClickListener()).execute(commandString.replace(replacable, editText.getText().toString()));
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
     * Prepares the dialog with spinner
     *
     * @param commandString Commands string
     * @param replacable    String to be replaced in commandString
     * @param messageId     Message to be shown in the dialog
     * @param arrayId       Array to be used in the spinner
     */
    private void prepareSpinnerDialog(final String commandString, final String replacable, int messageId, final int arrayId) {
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_spinner, null);
        AppCompatSpinner s = (AppCompatSpinner) dialogView.findViewById(R.id.spinnerDialog);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(getResources().getStringArray(arrayId))); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
        new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(commandString)
                .setMessage(getString(messageId) + " " + replacable)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AppCompatSpinner spinner = (AppCompatSpinner) dialogView.findViewById(R.id.spinnerDialog);
                        new ExecuteCommandTask(getActivity(), getView(), ((ServerControlActivity) getActivity()).getClickListener()).execute(commandString.replace(replacable, spinner.getSelectedItem().toString()));
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
