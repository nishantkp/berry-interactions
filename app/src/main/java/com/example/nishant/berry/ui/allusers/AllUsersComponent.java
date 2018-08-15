package com.example.nishant.berry.ui.allusers;

import com.example.nishant.berry.data.component.DataManagerComponent;

import dagger.Component;

/**
 * Dagger component which deals with providing a presenter to {@link AllUsersActivity}
 */
@AllUsersActivityScope
@Component(modules = AllUsersModule.class, dependencies = DataManagerComponent.class)
public interface AllUsersComponent {
    void inject(AllUsersActivity allUsersActivity);

    AllUsersPresenter provideAllUsersPresenter();
}
