package com.example.nishant.berry.ui.interaction;

import com.example.nishant.berry.data.component.DataManagerComponent;

import dagger.Component;

/**
 * Dagger component which provides {@link InteractionPresenter} object to {@link InteractionPresenter}
 */
@InteractionActivityScope
@Component(modules = InteractionModule.class, dependencies = DataManagerComponent.class)
public interface InteractionComponent {
    void inject(InteractionActivity interactionActivity);

    InteractionPresenter provideInteractionPresenter();
}
