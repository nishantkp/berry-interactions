/*
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * File Created on 15/08/18 7:03 PM by nishant
 * Last Modified on 23/07/18 10:44 PM
 */

package com.example.nishant.berry.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nishant.berry.R;
import com.example.nishant.berry.application.BerryApp;
import com.example.nishant.berry.base.BaseActivity;
import com.example.nishant.berry.databinding.ActivityDashboardBinding;
import com.example.nishant.berry.ui.adapter.SectionsPagerAdapter;
import com.example.nishant.berry.ui.allusers.AllUsersActivity;
import com.example.nishant.berry.ui.search.SearchActivity;
import com.example.nishant.berry.ui.settings.SettingsActivity;
import com.example.nishant.berry.ui.start.StartActivity;

import java.util.Objects;

import javax.inject.Inject;

public class DashboardActivity
        extends BaseActivity
        implements DashboardContract.View {

    /* Dagger Injection */
    @Inject
    DashboardPresenter mPresenter;
    @Inject
    SectionsPagerAdapter mSectionsPagerAdapter;

    private ActivityDashboardBinding mBinding;

    /**
     * Use this method get the intent to start {@link DashboardActivity}
     *
     * @param context Context of activity from which intent is started
     * @return Intent to start {@link DashboardActivity}
     */
    public static Intent getStarterIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // Set custom toolbar
        setSupportActionBar(mBinding.dashboardAppBar.mainAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");

        // Inject presenter
        DaggerDashboardComponent.builder()
                .dataManagerComponent(BerryApp.get(this).getDataManagerApplicationComponent())
                .dashboardModule(new DashboardModule(this))
                .build()
                .inject(this);
        mPresenter.attachView(this);

        // Setup viewPager with pager adapter
        mBinding.dashboardViewPager.setAdapter(mSectionsPagerAdapter);
        mBinding.dashboardTabs.setupWithViewPager(mBinding.dashboardViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.checkCurrentUser();
    }

    /**
     * If there isn't any active users start {@link StartActivity}
     */
    @Override
    public void noActiveUser() {
        sendToStartActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_logout:
                mPresenter.signOutUser();
                sendToStartActivity();
                return true;
            case R.id.action_account_settings:
                startActivity(SettingsActivity.getStarterIntent(this));
                return true;
            case R.id.action_all_users:
                startActivity(AllUsersActivity.getStarterIntent(this));
                return true;
            case R.id.action_search:
                startActivity(SearchActivity.getStarterIntent(this));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Intent to send user to start activity
     */
    private void sendToStartActivity() {
        startActivity(StartActivity.getStarterIntent(this));
        finish();
    }
}
