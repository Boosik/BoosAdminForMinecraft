package cz.boosik.boosadminforminecraft.app.commands;

import java.util.ArrayList;

/**
 * Storage object used to store Command objects
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class CommandStorage {

    private ArrayList<Command> commands = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param commands Command ArrayList to store
     */
    public CommandStorage(ArrayList<Command> commands) {
        this.commands = commands;
    }

    /**
     * Empty constructor
     */
    public CommandStorage() {
    }

    /**
     * Gets the commands
     *
     * @return The commands
     */
    public ArrayList<Command> getCommands() {
        return commands;
    }

    /**
     * Sets the commands
     *
     * @param commands The commands
     */
    public void setCommands(ArrayList<Command> commands) {
        this.commands = commands;
    }
}
