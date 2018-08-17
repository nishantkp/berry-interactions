package com.example.nishant.berry.ui.dashboard;

import com.example.nishant.berry.ui.adapter.SectionsPagerAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module which provides SectionPagerAdapter object to {@link DashboardComponent}
 */
@Module
public class DashboardModule {

    private DashboardActivity mDashboardActivity;

    DashboardModule(DashboardActivity dashboardActivity) {
        mDashboardActivity = dashboardActivity;
    }

    @DashboardActivityScope
    @Provides
    public SectionsPagerAdapter getPagerAdapter() {
        return new SectionsPagerAdapter(mDashboardActivity.getSupportFragmentManager());
    }
}
