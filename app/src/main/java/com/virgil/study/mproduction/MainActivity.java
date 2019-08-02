package com.virgil.study.mproduction;

import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost fragmentTabHost;
    private String[] tab_names;
    private int[] tab_icons;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        test();
    }

    private void test(){
        Button open = findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    private void init(){
        initView();
    }

    private void initView(){
        initBottmTab();
        initDrawerLayout();
        initToolBar();
    }

    private void initDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer);
    }

    private void initToolBar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //设置系统自带的抽屉按钮
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close
//                );
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
    }


    //初始化底部tab
    private void initBottmTab(){
        tab_names = new String[]{"首页", "频道", "动态", "商城"};
        tab_icons = new int[]{R.drawable.ic_home_selector, R.drawable.ic_home_selector, R.drawable.ic_home_selector, R.drawable.ic_home_selector};
        fragmentTabHost = findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.act_tab_fragment);
        fragmentTabHost.getTabWidget().setDividerDrawable(android.R.color.white);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("home").setIndicator(getIndicatorView(0)),
                HomeFragment.class, getBundle(0));
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("channel").setIndicator(getIndicatorView(1)),
                ChannelFragment.class, getBundle(1));
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("dynamic").setIndicator(getIndicatorView(2)),
                DynamicFragment.class, getBundle(2));
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("mall").setIndicator(getIndicatorView(3)),
                MallFragment.class, getBundle(3));
    }

    //获取底部tab的视图
    public View getIndicatorView(int i) {
        View view = getLayoutInflater().inflate(R.layout.item_main_tab, null);
        ImageView mImageView = view.findViewById(R.id.item_main_tab_icon);
        TextView textView = view.findViewById(R.id.item_main_tab_name);
        textView.setText(tab_names[i]);
        mImageView.setImageResource(tab_icons[i]);
        if (tab_names[i].equals("")) {
            mImageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    public Bundle getBundle(int type)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        return bundle;
    }
}
