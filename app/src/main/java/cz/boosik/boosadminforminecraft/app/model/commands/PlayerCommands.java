package cz.boosik.boosadminforminecraft.app.model.commands;

/**
 * Enum containing all base player commands
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public enum PlayerCommands {
    KILL("kill <player>"),
    KICK("kick <player> <reason>"),
    BAN("ban <player> <reason>"),
    BAN_IP("ban-ip <player> <reason>"),
    OP("op <player>"),
    DEOP("deop <player>"),
    GIVE("give <player> <item> <amount>"),
    TELL("tell <player> <message>"),
    CLEAR("clear <player> <item>"),
    GAMEMODE("gamemode <player> <gamemode>");

    private final String commandString;

    PlayerCommands(final String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}
