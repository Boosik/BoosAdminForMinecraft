package cz.boosik.boosadminforminecraft.app.serverStore;

import android.content.Context;
import com.google.gson.Gson;

import java.io.*;

/**
 * Provides methods to handle server storage
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class StorageProvider {

    private String fileName;
    private final Context context;

    /**
     * Default constructor
     *
     * @param context  Context of storage provider
     * @param fileName Name of the file to work with
     */
    public StorageProvider(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
    }

    /**
     * Saves the server
     *
     * @param server Server to save
     * @return true if successful, false otherwise
     * @throws IOException
     * @throws NullPointerException
     */
    public boolean saveServer(Server server) throws IOException,
            NullPointerException {
        ServerStorage servers;
        try {
            servers = readServers();
        } catch (FileNotFoundException e) {
            servers = new ServerStorage();
        }
        for (Server srv : servers.getServers()) {
            if (srv.getName().equalsIgnoreCase(server.getName())) {
                return false;
            }
        }
        servers.getServers().add(server);
        writeServers(servers);
        return true;
    }

    /**
     * Writes the server storage to the file
     *
     * @param s ServerStorage to be written
     * @throws IOException
     */
    public void writeServers(ServerStorage s) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String jsonServerStore = new Gson().toJson(s);
        System.out.println("Writing JSON: " + jsonServerStore);
        assert fos != null;
        try {
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(jsonServerStore);
            osw.flush();
            osw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            fos.close();
        }
    }

    /**
     * Reads the server storage from saved file
     *
     * @return Server storage
     * @throws FileNotFoundException
     */
    public ServerStorage readServers() throws FileNotFoundException {
        FileInputStream fis;
        ServerStorage servers;
        fis = context.openFileInput(fileName);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String contents = "";
        String readIn;
        try {
            readIn = br.readLine();
            while (readIn != null) {
                contents = contents + readIn;
                readIn = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Reading JSON: " + contents);
        servers = new Gson().fromJson(contents, ServerStorage.class);
        return servers;
    }
}
