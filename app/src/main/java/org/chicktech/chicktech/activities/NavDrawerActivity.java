package org.chicktech.chicktech.activities;

/**
 * Created by Jing Jin on 11/7/14.
 */
public interface NavDrawerActivity {
    public static enum TopLevelNavs {
        EVENTS,
        PEOPLE,
        CHAT,
        ABOUT
    }

    public void selectTopLevelNav(TopLevelNavs enumId);
}
