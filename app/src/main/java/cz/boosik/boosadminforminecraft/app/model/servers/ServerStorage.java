package cz.boosik.boosadminforminecraft.app.model.servers;

import java.util.ArrayList;

/**
 * Storage object used to store Server objects
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerStorage {

    private ArrayList<Server> servers = new ArrayList<Server>();

    /**
     * Gets servers
     *
     * @return The servers
     */
    public ArrayList<Server> getServers() {
        return servers;
    }

    /**
     * Default constructor
     *
     * @param servers Server ArrayList to store
     */
    public ServerStorage(ArrayList<Server> servers) {
        this.servers = servers;
    }

    /**
     * Empty constructor
     */
    public ServerStorage() {
    }

    /**
     * Sets the servers
     *
     * @param servers The servers
     */
    public void setServers(ArrayList<Server> servers) {
        this.servers = servers;
    }
}
