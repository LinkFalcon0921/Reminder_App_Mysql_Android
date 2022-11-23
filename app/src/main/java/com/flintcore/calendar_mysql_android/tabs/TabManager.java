package com.flintcore.calendar_mysql_android.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.flintcore.calendar_mysql_android.listeners.ViewPagerCallbackListener;
import com.flintcore.calendar_mysql_android.tabs.view.Tab;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class TabManager extends FragmentStateAdapter {

    private final ViewPager2 pager2;
    private final TabLayout tabViews;
    private final Map<Integer, Tab> tabsMap;

    public TabManager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
                      @NonNull ViewPager2 pager2, @NonNull TabLayout tabViews) {
        super(fragmentManager, lifecycle);

        this.pager2 = pager2;

        this.tabViews = tabViews;
        this.tabViews.setTabGravity(TabLayout.GRAVITY_FILL);
        this.tabViews.setTabMode(TabLayout.MODE_FIXED);

        this.tabsMap = new HashMap<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.tabsMap.get(position);
    }

    @Override
    public int getItemCount() {
        return this.tabsMap.size();
    }

    public void createTab(String nameTab, Tab view) {
        TabLayout.Tab tab = this.tabViews.newTab();

        tab.setText(nameTab);
        this.tabViews.addTab(tab);
        this.tabsMap.put(tab.getPosition(), view);

    }

    public void setListeners(){
        this.pager2.setAdapter(this);
        this.pager2.registerOnPageChangeCallback(new ViewPagerCallbackListener(this.tabViews));
        this.tabViews.addOnTabSelectedListener(new TabManagerListener());
    }

    private final class TabManagerListener
            implements TabLayout.OnTabSelectedListener {

        public TabManagerListener() {
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            TabManager.this.pager2.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }


}

