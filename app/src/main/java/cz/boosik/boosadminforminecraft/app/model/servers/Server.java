package cz.boosik.boosadminforminecraft.app.model.servers;

/**
 * POJO representing the server
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class Server {

    private String name;
    private String host;
    private String port;
    private String password;
    private String queryHost;
    private String queryPort;
    private String dynmapHost;
    private String dynmapPort;

    /**
     * Empty constructor
     */
    public Server() {
    }

    /**
     * Default constructor
     *
     * @param name       Name of the server
     * @param host       Rcon host
     * @param port       Rcon port
     * @param password   Rcon password
     * @param queryHost  Query host
     * @param queryPort  Query port
     * @param dynmapHost Dynmap host
     * @param dynmapPort Dynmap port
     */
    public Server(String name, String host, String port, String password, String queryHost, String queryPort, String dynmapHost, String dynmapPort) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.password = password;
        this.queryHost = queryHost;
        this.queryPort = queryPort;
        this.dynmapHost = dynmapHost;
        this.dynmapPort = dynmapPort;
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
     * Gets the host
     *
     * @return The host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host
     *
     * @param host The host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the port
     *
     * @return The port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port
     *
     * @param port The port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Gets the password
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     *
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the queryHost
     *
     * @return The queryHost
     */
    public String getQueryHost() {
        return queryHost;
    }

    /**
     * Sets the queryHost
     *
     * @param queryHost The queryHost
     */
    public void setQueryHost(String queryHost) {
        this.queryHost = queryHost;
    }

    /**
     * Gets the queryPort
     *
     * @return The queryPort
     */
    public String getQueryPort() {
        return queryPort;
    }

    /**
     * Sets the queryPort
     *
     * @param queryPort The queryPort
     */
    public void setQueryPort(String queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * Gets the dynmapHost
     *
     * @return The dynmapHost
     */
    public String getDynmapHost() {
        return dynmapHost;
    }

    /**
     * Sets the dynmapHost
     *
     * @param dynmapHost The dynmapHost
     */
    public void setDynmapHost(String dynmapHost) {
        this.dynmapHost = dynmapHost;
    }

    /**
     * Gets the dynmapPort
     *
     * @return The dynmapPort
     */
    public String getDynmapPort() {
        return dynmapPort;
    }

    /**
     * Sets the dynmapPort
     *
     * @param dynmapPort The dynmapPort
     */
    public void setDynmapPort(String dynmapPort) {
        this.dynmapPort = dynmapPort;
    }
}
