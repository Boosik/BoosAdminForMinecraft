package cz.boosik.boosadminforminecraft.app.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.boosik.boosadminforminecraft.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used for cards containing strings
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class CardArrayStringAdapter extends ArrayAdapter<String> {
    private static final String TAG = "CardArrayServerAdapter";
    private List<String> cardList = new ArrayList<>();

    static class CardViewHolder {
        TextView line1;
    }

    /**
     * Default constructor
     *
     * @param context            Context of adapter
     * @param textViewResourceId Textview Id
     * @param strings            List of strings
     */
    public CardArrayStringAdapter(Context context, int textViewResourceId, List<String> strings) {
        super(context, textViewResourceId);
        this.cardList = strings;
    }

    public CardArrayStringAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(String object) {
        cardList.add(object);
        super.add(object);
    }

    public void setData(List<String> strings) {
        this.cardList = strings;
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public String getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card_one_line, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.line1);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        String string = getItem(position);
        viewHolder.line1.setText(string);
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
