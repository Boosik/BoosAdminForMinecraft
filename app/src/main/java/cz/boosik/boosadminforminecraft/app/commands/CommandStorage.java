package cz.boosik.boosadminforminecraft.app.commands;

import java.util.ArrayList;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class CommandStorage {

    private ArrayList<Command> commands = new ArrayList<Command>();

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public CommandStorage(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public CommandStorage() {
    }
}
