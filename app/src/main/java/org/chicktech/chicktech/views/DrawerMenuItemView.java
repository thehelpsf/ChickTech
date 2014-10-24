package org.chicktech.chicktech.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.DrawerMenuItem;

/**
 * Created by Jing Jin on 10/24/14.
 */
public class DrawerMenuItemView extends LinearLayout {

    private TextView tvMenuLabel;
    private ImageView imgMenuIcon;
    private TextView tvSpacer;

    public DrawerMenuItemView(Context context) {
        super(context);
    }

    public DrawerMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerMenuItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static DrawerMenuItemView newInstance(Context c, ViewGroup parent) {
        DrawerMenuItemView v = (DrawerMenuItemView) LayoutInflater.from(c).inflate(R.layout.drawer_nav_item, parent, false);
        v.findAllViews();
        return v;
    }

    public void findAllViews() {
        tvMenuLabel = (TextView)findViewById(R.id.tvMenuLabel);
        imgMenuIcon = (ImageView)findViewById(R.id.imgMenuIcon);
        tvSpacer = (TextView)findViewById(R.id.tvSpacer);
    }

    public ImageView getImgMenuIcon() {
        return imgMenuIcon;
    }

    public TextView getTvMenuLabel() {
        return tvMenuLabel;
    }

    public void setDrawerMenuItem (DrawerMenuItem item) {
        tvMenuLabel.setText(item.label);
        imgMenuIcon.setImageDrawable(getResources().getDrawable(item.iconResourceId));
    }

    public TextView getTvSpacer() {
        return tvSpacer;
    }
}
