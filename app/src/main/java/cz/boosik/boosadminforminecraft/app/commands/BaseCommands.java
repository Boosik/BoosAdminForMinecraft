package cz.boosik.boosadminforminecraft.app.commands;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public enum BaseCommands {
    BANLIST("banlist players"),
    BANLIST_IP("balist ips"),
    HELP("help <page>"),
    HELP_COMMAND("help <command>"),
    DEFAULT_GAME_MODE("defaultgamemode <gamemode>"),
    LIST("list"),
    SAVE_ALL("save-all"),
    SAVE_ON("save-on"),
    SAVE_OFF("save-off"),
    SAY("say <message>"),
    STOP("stop"),
    TIME_SET("time set <time>"),
    TIME_ADD("time add <timeNumber>"),
    TIME_QUERY("time query"),
    TOGGLEDOWNFALL("toggledownfall"),
    WEATHER("weather <weather>"),
    WHITELIST_LIST("whitelist list"),
    WHITELIST_ON("whitelist on"),
    WHITELIST_OFF("whitelist off"),
    WHITELIST_RELOAD("whitelist reload");

    private final String commandString;

    private BaseCommands(final String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}
