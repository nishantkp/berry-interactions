package com.example.nishant.berry.ui.allusers;

import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.component.DaggerDataManagerComponent;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module which provides {@link AllUsersPresenter} to {@link AllUsersComponent}
 */
@Module
public class AllUsersModule {

    @AllUsersActivityScope
    @Provides
    public AllUsersPresenter getAllUsersPresenter(DataManager dataManager) {
        return new AllUsersPresenter(dataManager);
    }
}
