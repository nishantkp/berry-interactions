package com.example.nishant.berry.base;

import com.example.nishant.berry.data.component.DataManagerComponent;

import dagger.Component;

/**
 * Dagger component which provides {@link BaseActivityPresenter} object to
 * {@link BaseActivity}
 */
@BaseActivityScope
@Component(dependencies = DataManagerComponent.class)
public interface BaseActivityComponent {
    void inject(BaseActivity baseActivity);
}
