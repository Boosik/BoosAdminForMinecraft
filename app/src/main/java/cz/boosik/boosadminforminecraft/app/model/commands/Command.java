package cz.boosik.boosadminforminecraft.app.model.commands;

/**
 * POJO representing the command
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class Command {

    String name;
    String command;

    /**
     * Default constructor
     *
     * @param name    Name of a command
     * @param command Actual command string
     */
    public Command(String name, String command) {
        this.name = name;
        this.command = command;
    }

    /**
     * Empty constructor
     */
    public Command() {
    }

    /**
     * Gets the name
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the command
     *
     * @return The command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command
     *
     * @param command The command
     */
    public void setCommand(String command) {
        this.command = command;
    }
}
