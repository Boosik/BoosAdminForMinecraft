package cz.boosik.boosadminforminecraft.app.serverStore;

import android.app.Activity;
import android.content.Context;
import com.google.gson.Gson;

import java.io.*;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class StorageProvider {

    private String fileName;
    private final Activity activity;

    public StorageProvider(Activity activity, String fileName) {
        this.fileName = fileName;
        this.activity = activity;
    }

    public boolean saveServer(Server server) throws IOException,
            NullPointerException {
        ServerStorage servers = null;

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

    public void writeServers(ServerStorage s) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String jsonServerStore = new Gson().toJson(s);
        System.out.println("Writing JSON: " + jsonServerStore);
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

    public ServerStorage readServers() throws FileNotFoundException {
        FileInputStream fis = null;
        ServerStorage servers = null;
        fis = activity.openFileInput(fileName);
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
