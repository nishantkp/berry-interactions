package com.example.nishant.berry.ui.dashboard;

import com.example.nishant.berry.data.component.DataManagerComponent;

import dagger.Component;

/**
 * Dagger component which provides {@link DashboardPresenter} and SectionPagerAdapter object
 * to {@link DashboardActivity}
 */
@DashboardActivityScope
@Component(modules = DashboardModule.class, dependencies = DataManagerComponent.class)
public interface DashboardComponent {
    void inject(DashboardActivity dashboardActivity);
}
