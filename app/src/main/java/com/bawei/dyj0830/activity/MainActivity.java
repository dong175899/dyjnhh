package com.bawei.dyj0830.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.bawei.dyj0830.R;
import com.bawei.dyj0830.adapter.MyViewPagerAdapter;
import com.bawei.dyj0830.base.BaseActivity;
import com.bawei.dyj0830.fragment.Fragment1;
import com.bawei.dyj0830.fragment.Fragment2;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {


    private TabLayout tab;
    private ViewPager vp;
    private ArrayList<String> tabs;
    private ArrayList<Fragment> fragments;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.vp);
    }

    @Override
    protected void initData() {
        tabs = new ArrayList<>();
        tabs.add("董燕军0830作业");
        tabs.add("　");
        for (int i = 0; i < tabs.size(); i++) {
            tab.addTab(tab.newTab().setText(tabs.get(i)));
        }
        fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragments,tabs);
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
    }
}
