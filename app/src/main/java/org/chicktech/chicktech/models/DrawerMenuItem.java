package org.chicktech.chicktech.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Jing Jin on 10/24/14.
 */
public class DrawerMenuItem {
    public String label;
    public int iconResourceId;

    public DrawerMenuItem() {

    }

    public DrawerMenuItem(String label, int iconResourceId) {
        this.label = label;
        this.iconResourceId = iconResourceId;
    }
}
