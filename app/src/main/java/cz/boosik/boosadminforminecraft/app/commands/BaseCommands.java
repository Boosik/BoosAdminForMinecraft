package cz.boosik.boosadminforminecraft.app.commands;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public enum BaseCommands {
    BANLIST("banlist players"),
    BANLIST_IP("banlist ips"),
    DEFAULT_GAME_MODE("defaultgamemode <gamemode>"),
    HELP("? <page>"),
    HELP_COMMAND("? <command>"),
    LIST("list"),
    PARDON("pardon <player>"),
    PARDON_IP("pardon-ip <ip>"),
    SAVE_ALL("save-all"),
    SAVE_ON("save-on"),
    SAVE_OFF("save-off"),
    SAY("say <message>"),
    STOP("stop"),
    TIME_SET("time set <time>"),
    TIME_ADD("time add <time_number>"),
    TIME_QUERY("time query"),
    TOGGLEDOWNFALL("toggledownfall"),
    WEATHER("weather <weather>"),
    WHITELIST_ADD("whitelist add <player>"),
    WHITELIST_REMOVE("whitelist remove <player>"),
    WHITELIST_LIST("whitelist list"),
    WHITELIST_ON("whitelist on"),
    WHITELIST_OFF("whitelist off"),
    WHITELIST_RELOAD("whitelist reload");

    private final String commandString;

    BaseCommands(final String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}
