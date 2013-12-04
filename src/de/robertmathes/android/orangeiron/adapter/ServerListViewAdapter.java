package de.robertmathes.android.orangeiron.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.robertmathes.android.orangeiron.R;
import de.robertmathes.android.orangeiron.model.Server;

public class ServerListViewAdapter extends BaseAdapter {

    private final List<Server> servers;
    private final LayoutInflater inflater;

    Typeface roboto_light;

    public ServerListViewAdapter(Context ctx, List<Server> servers) {
        this.servers = servers;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roboto_light = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto/Roboto-Light.ttf");
    }

    @Override
    public int getCount() {
        return this.servers.size();
    }

    @Override
    public Server getItem(int index) {
        return this.servers.get(index);
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup rootView) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_server_list, null);
        }

        TextView serverName = (TextView) view.findViewById(R.id.servername);
        serverName.setText(getItem(position).getName());
        serverName.setTypeface(this.roboto_light);

        return view;
    }

}
