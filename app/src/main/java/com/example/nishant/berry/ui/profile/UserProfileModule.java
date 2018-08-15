package com.example.nishant.berry.ui.profile;

import com.example.nishant.berry.data.DataManager;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module which provides {@link UserProfilePresenter} object to {@link UserProfileComponent}
 */
@Module
public class UserProfileModule {

    private String userId;

    UserProfileModule(String userId) {
        this.userId = userId;
    }

    @UserProfileActivityScope
    @Provides
    public UserProfilePresenter getUserProfilePresenter(DataManager dataManager) {
        return new UserProfilePresenter(userId, dataManager);
    }
}
