package cz.boosik.boosadminforminecraft.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.serverStore.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used for cards containing server data
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class CardArrayServerAdapter extends ArrayAdapter<Server> {
    private static final String TAG = "CardArrayServerAdapter";
    private List<Server> cardList = new ArrayList<>();

    static class CardViewHolder {
        TextView line1;
        TextView line2;
    }

    /**
     * Default constructor
     *
     * @param context            Context of adapter
     * @param textViewResourceId Textview Id
     * @param servers            List of servers
     */
    public CardArrayServerAdapter(Context context, int textViewResourceId, List<Server> servers) {
        super(context, textViewResourceId);
        this.cardList = servers;
    }

    @Override
    public void add(Server object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Server getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.line1);
            viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        Server server = getItem(position);
        viewHolder.line1.setText(server.getName());
        viewHolder.line2.setText(server.getHost());
        return row;
    }

    /**
     * Decodes byte array to bitmap
     *
     * @param decodedByte byte
     * @return Bitmap
     */
    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
