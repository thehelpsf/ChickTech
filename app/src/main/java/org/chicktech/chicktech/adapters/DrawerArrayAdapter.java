package org.chicktech.chicktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.DrawerMenuItem;
import org.chicktech.chicktech.views.DrawerMenuItemView;

import java.util.List;

/**
 * Created by Jing Jin on 10/24/14.
 */
public class DrawerArrayAdapter extends ArrayAdapter<DrawerMenuItem> {
    public DrawerArrayAdapter(Context context, List<DrawerMenuItem> items) {
        super(context, R.layout.drawer_nav_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerMenuItemView v = (DrawerMenuItemView)convertView;
        if (v == null) {
            v = DrawerMenuItemView.newInstance(getContext(), parent);
        }
        v.setDrawerMenuItem(getItem(position));

        // Update how far indented it is based on the position
        int indentDp = 0;
        switch(position) {
            case 0:
                indentDp = 38;
                break;
            case 1:
                indentDp = 4;
                break;
            case 2:
                break;
            case 3:
            default:
                indentDp = 31;
        }
        TextView spacer = v.getTvSpacer();
        ViewGroup.LayoutParams params = spacer.getLayoutParams();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        params.width = (int)(indentDp * scale + 0.5f);
        spacer.setLayoutParams(params);

        return v;
    }
}
