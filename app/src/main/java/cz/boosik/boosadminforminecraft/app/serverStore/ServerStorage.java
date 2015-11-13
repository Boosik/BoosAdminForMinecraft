package cz.boosik.boosadminforminecraft.app.serverStore;

import java.util.ArrayList;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerStorage {

    private ArrayList<Server> servers = new ArrayList<Server>();

    public ArrayList<Server> getServers() {
        return servers;
    }

    public void setServers(ArrayList<Server> servers) {
        this.servers = servers;
    }

    public ServerStorage(ArrayList<Server> servers) {
        this.servers = servers;
    }

    public ServerStorage() {
    }
}
