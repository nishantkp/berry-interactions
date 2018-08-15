package com.example.nishant.berry.ui.interaction;

import android.content.Intent;

import com.example.nishant.berry.data.DataManager;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module which provides {@link InteractionPresenter} object to {@link InteractionComponent}
 */
@Module
public class InteractionModule {

    private Intent intent;

    InteractionModule(Intent intent) {
        this.intent = intent;
    }

    @InteractionActivityScope
    @Provides
    public InteractionPresenter getInteractionPresenter(DataManager dataManager) {
        return new InteractionPresenter(intent, dataManager);
    }
}
