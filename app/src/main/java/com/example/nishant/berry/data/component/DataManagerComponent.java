package com.example.nishant.berry.data.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

@Component
@Singleton
public interface DataManagerComponent {
    void inject(Application application);
}
