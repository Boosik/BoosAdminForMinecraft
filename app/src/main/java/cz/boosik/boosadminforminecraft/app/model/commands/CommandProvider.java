package cz.boosik.boosadminforminecraft.app.model.commands;

import cz.boosik.boosadminforminecraft.app.App;
import cz.boosik.boosadminforminecraft.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class CommandProvider {

    public static CommandStorage playerCommands;
    public static CommandStorage serverCommands;
    public static HashMap<String, List<String>> pluginCommandsMap;
    public static ArrayList<String> playerCommandNamesArrayList;
    public static ArrayList<String> serverCommandNamesArrayList;
    public static ArrayList<String> supportedPluginNames;

    static {
        prepareServerCommands();
        preparePlayerCommands();
        preparePluginCommands();
    }

    /**
     * Prepares the server commands lists
     */
    private static void prepareServerCommands() {
        serverCommands = new CommandStorage();
        serverCommandNamesArrayList = new ArrayList<>();
        ArrayList<Command> commandArrayList = new ArrayList<>();
        commandArrayList.add(0, new Command(App.getContext().getString(R.string.custom_command), "<custom_command>"));
        for (BaseCommands bc : BaseCommands.values()) {
            commandArrayList.add(new Command(bc.name().toLowerCase().replace("_", " "), bc.getCommandString()));
        }
        serverCommands.setCommands(commandArrayList);
        for (Command command : commandArrayList) {
            serverCommandNamesArrayList.add(command.getName());
        }
    }

    /**
     * Prepares the player commands lists
     */
    private static void preparePlayerCommands() {
        playerCommands = new CommandStorage();
        playerCommandNamesArrayList = new ArrayList<>();
        ArrayList<Command> commandArrayList = new ArrayList<>();
        for (PlayerCommands bc : PlayerCommands.values()) {
            commandArrayList.add(new Command(bc.name().toLowerCase().replace("_", " "), bc.getCommandString()));
        }
        playerCommands.setCommands(commandArrayList);
        for (Command command : commandArrayList) {
            playerCommandNamesArrayList.add(command.getName());
        }
    }

    /**
     * Prepares the plugin commands lists
     */
    private static void preparePluginCommands() {
        CommandStorage supportedPlugins = new CommandStorage();
        supportedPluginNames = new ArrayList<>();
        pluginCommandsMap = new HashMap<>();
        ArrayList<Command> pluginArrayList = new ArrayList<>();
        for (PluginCommands bc : PluginCommands.values()) {
            pluginArrayList.add(new Command(bc.name().toLowerCase().replace("_", "-"), bc.getCommandString()));
        }
        supportedPlugins.setCommands(pluginArrayList);
        for (Command command : pluginArrayList) {
            supportedPluginNames.add(command.getName());
            pluginCommandsMap.put(command.getName(), Arrays.asList(command.getCommand().split(";")));
        }
    }
}
