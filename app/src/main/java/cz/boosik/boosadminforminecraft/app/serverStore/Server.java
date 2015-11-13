package cz.boosik.boosadminforminecraft.app.serverStore;

/**
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

    public Server() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQueryHost() {
        return queryHost;
    }

    public void setQueryHost(String queryHost) {
        this.queryHost = queryHost;
    }

    public String getQueryPort() {
        return queryPort;
    }

    public void setQueryPort(String queryPort) {
        this.queryPort = queryPort;
    }

    public String getDynmapHost() {
        return dynmapHost;
    }

    public void setDynmapHost(String dynmapHost) {
        this.dynmapHost = dynmapHost;
    }

    public String getDynmapPort() {
        return dynmapPort;
    }

    public void setDynmapPort(String dynmapPort) {
        this.dynmapPort = dynmapPort;
    }
}
