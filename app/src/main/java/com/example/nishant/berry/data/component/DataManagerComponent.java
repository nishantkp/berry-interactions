package com.example.nishant.berry.data.component;

import com.example.nishant.berry.application.BerryApp;
import com.example.nishant.berry.data.DataManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for proving Singleton DataManager object
 */
@Component
@Singleton
public interface DataManagerComponent {
    void inject(BerryApp application);

    DataManager provideDataManager();
}
