package com.flintcore.calendar_mysql_android.listeners;

import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import com.google.android.material.tabs.TabLayout;

public class ViewPagerCallbackListener extends OnPageChangeCallback{

    private final TabLayout layout;

    public ViewPagerCallbackListener(TabLayout layout) {
        this.layout = layout;
    }

    @Override
    public void onPageSelected(int position) {
        TabLayout.Tab tab = this.layout.getTabAt(position);
        this.layout.selectTab(tab);
    }
}
