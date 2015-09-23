package canada.about.com.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import canada.about.com.app.R;
import canada.about.com.app.models.Item;

/**
 * Created by reza on 22/9/2015.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> items;

    public ListAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_content_list, null);
            holder = new ViewHolder();
            holder.image_item = (ImageView) view.findViewById(R.id.item_thumb);
            holder.title_item = (TextView) view.findViewById(R.id.item_title);
            holder.slug_item = (TextView) view.findViewById(R.id.item_slug);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Item item = items.get(position);

        if (item.getUrlThumb().length() > 0) {
            Picasso.with(context).load(item.getUrlThumb()).into(holder.image_item);
        }

        if (item.getTitle().length() > 0) {
            holder.title_item.setText(item.getTitle());
        }

        if (item.getSlugTitle().length() > 0) {
            holder.slug_item.setText(item.getSlugTitle());
        }

        return view;
    }

    private static class ViewHolder {
        public ImageView image_item;
        public TextView title_item;
        public TextView slug_item;
    }

}
