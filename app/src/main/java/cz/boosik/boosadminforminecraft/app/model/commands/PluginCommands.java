package cz.boosik.boosadminforminecraft.app.model.commands;

/**
 * Enum containing all supported plugins and their commands
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public enum PluginCommands {
    PLUGMAN(
            "plugman load <plugin>;" +
                    "plugman unload <plugin>;" +
                    "plugman reload <plugin>;" +
                    "plugman enable <plugin>;" +
                    "plugman disable <plugin>;" +
                    "plugman restart <plugin>"),
    NTHEENDAGAIN(
            "nend regen <world>;" +
                    "nend respawn <world>;" +
                    "nend nb <world>;" +
                    "nend reload messages"),
    MULTIVERSE_CORE(
            "mv list;" +
                    "mv info <world> [page #];" +
                    "mv create <name> <env>;" +
                    "mv import <name> <env>;" +
                    "mv reload;" +
                    "mv tp <player> <world>;" +
                    "mv spawn <player>;" +
                    "mv unload <world>;" +
                    "mv remove <world>;" +
                    "mv delete <world>;" +
                    "mv confirm;" +
                    "mv modify set <property> <value> <world>;" +
                    "mv gamerule <rule> <value> <world>;" +
                    "mv env");

    private final String commandString;

    PluginCommands(final String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}
