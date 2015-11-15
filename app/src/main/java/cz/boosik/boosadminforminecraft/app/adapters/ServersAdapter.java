package cz.boosik.boosadminforminecraft.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;

import java.util.List;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServersAdapter extends RecyclerView.Adapter<ServersAdapter.ViewHolder> {

    private List<Server> servers;
    private Context context;

    public ServersAdapter(Context context, List<Server> servers) {
        this.servers = servers;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.server_name_list)
        TextView tvName;
        @Bind(R.id.server_host_list)
        TextView tvHost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Intent i = new Intent(context, ServerControlActivity.class);
            i.putExtra("serverName", position);
            context.startActivity(i);
        }
    }

    @Override
    public ServersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServersAdapter.ViewHolder viewHolder, int position) {
        Server server = servers.get(position);

        TextView name = viewHolder.tvName;
        name.setText(server.getName());

        TextView host = viewHolder.tvHost;
        host.setText(server.getName());
    }

    @Override
    public int getItemCount() {
        return servers.size();
    }
}
