package cz.boosik.boosadminforminecraft.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerListActivity;
import cz.boosik.boosadminforminecraft.app.presenter.fragments.AbstractServerControlFragment;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    /**
     * Handles network change event
     *
     * @param context The application context
     * @param intent  The intent
     */
    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d(TAG, "Network connectivity change");

        AbstractServerControlFragment.rcon = null;
        AbstractServerControlFragment.query = null;
        ServerListActivity.rcon = null;
        ServerListActivity.query = null;
    }
}
