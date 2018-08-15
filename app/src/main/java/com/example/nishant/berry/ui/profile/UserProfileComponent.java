package com.example.nishant.berry.ui.profile;

import com.example.nishant.berry.data.component.DataManagerComponent;

import dagger.Component;

/**
 * Dagger component which provides {@link UserProfilePresenter} object to {@link UserProfileActivity}
 */
@UserProfileActivityScope
@Component(modules = UserProfileModule.class, dependencies = DataManagerComponent.class)
public interface UserProfileComponent {
    void inject(UserProfileActivity userProfileActivity);

    UserProfilePresenter provideUserProfilePresenter();
}
