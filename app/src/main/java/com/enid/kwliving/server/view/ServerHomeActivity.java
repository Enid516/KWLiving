package com.enid.kwliving.server.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.enid.kwliving.BaseActivity;
import com.enid.kwliving.R;
import com.enid.kwliving.server.presenter.IServerPresenter;
import com.enid.kwliving.server.presenter.ServerPresenterImpl;

/**
 * Created by big_love on 2016/12/21.
 */

public class ServerHomeActivity extends BaseActivity implements IServerView ,DeskFragment.DeskFragmentListener{
    private Fragment currentFragment;
    private IServerPresenter iServerPresenter;
    private RoleSettingFragment roleFragment;
    private DeskFragment deskFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);

        // init view
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //init fragment
        roleFragment = new RoleSettingFragment();
        deskFragment = new DeskFragment();
        aboutFragment = new AboutFragment();

        //set adapter
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    currentFragment = roleFragment;
                } else if (position == 1) {
                    currentFragment = deskFragment;
                }else if(position == 2){
                    currentFragment = aboutFragment;
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

        //set up with viewpager
        tabLayout.setupWithViewPager(viewPager);

        iServerPresenter = new ServerPresenterImpl(this);

        //set listener
        deskFragment.setListener(this);
        iServerPresenter.startServer(this);
    }

    @Override
    protected void onDestroy() {
        iServerPresenter.stopServer();
        super.onDestroy();
    }

    @Override
    public void serverStarted() {
        Toast.makeText(this,"server is started",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void serverStopped() {
        Toast.makeText(this,"server is stopped",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateMsg(String msg) {
        deskFragment.pointMsg(msg);
    }

    @Override
    public void clickInit() {
        iServerPresenter.broadCastMsg("init card");
        iServerPresenter.updateUIMsg("init card");
    }
}
