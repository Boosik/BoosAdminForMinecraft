package cz.boosik.boosadminforminecraft.app.model.tasks;

import android.content.Context;
import android.os.AsyncTask;
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerListActivity;
import query.QueryResponse;

import static cz.boosik.boosadminforminecraft.app.presenter.activities.ServerListActivity.query;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class CheckQueryTask extends AsyncTask<Void, Void, QueryResponse> {

    private Context context;

    /**
     * Default constructor
     *
     * @param context The context
     */
    public CheckQueryTask(Context context) {
        this.context = context;
    }

    @Override
    protected QueryResponse doInBackground(Void... params) {
        try {
            return query.fullStat();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(QueryResponse result) {
        ServerListActivity activity = (ServerListActivity) context;
        if (result == null) {
            activity.invokeError("query");
            return;
        }
        System.out.println("CheckQuery");
        activity.connect();
    }
}
