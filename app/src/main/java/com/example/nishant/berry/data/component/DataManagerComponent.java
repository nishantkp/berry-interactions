package com.example.nishant.berry.data.component;

import com.example.nishant.berry.application.BerryApp;

import javax.inject.Singleton;

import dagger.Component;

@Component
@Singleton
public interface DataManagerComponent {
    void inject(BerryApp application);
}
