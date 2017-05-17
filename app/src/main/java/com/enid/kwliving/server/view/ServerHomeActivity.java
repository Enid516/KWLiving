package com.enid.kwliving.server.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewParent;

import com.enid.kwliving.BaseActivity;
import com.enid.kwliving.R;

/**
 * Created by big_love on 2016/12/21.
 */

public class ServerHomeActivity extends BaseActivity{
    private Fragment currentFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    currentFragment = new RoleSettingFragment();
                } else if (position == 1) {
                    currentFragment = new DeskFragment();
                }else if(position == 2){
                    currentFragment = new AboutFragment();
                }
                return currentFragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title = "";
                if (position == 0) {
                    title = "RoleSetting";
                } else if (position == 1) {
                    title = "Desk";
                }else if(position == 2){
                    title = "About";
                }
                return title;
            }
        });

        tabLayout.setupWithViewPager(viewPager);

    }
}
