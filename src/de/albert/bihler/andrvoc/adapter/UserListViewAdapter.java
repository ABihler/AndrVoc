package de.albert.bihler.andrvoc.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.albert.bihler.andrvoc.model.User;
import de.robertmathes.android.orangeiron.R;

public class UserListViewAdapter extends BaseAdapter {

    private final List<User> users;
    private final LayoutInflater inflater;

    Typeface roboto_light;

    public UserListViewAdapter(Context ctx, List<User> users) {
        this.users = users;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roboto_light = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto/Roboto-Light.ttf");
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public User getItem(int index) {
        return this.users.get(index);
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup rootView) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_user_list, null);
        }

        TextView userName = (TextView) view.findViewById(R.id.username);
        userName.setText(getItem(position).getName());
        userName.setTypeface(this.roboto_light);

        return view;
    }

}
