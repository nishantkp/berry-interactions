package com.example.nishant.berry.ui.dashboard.fragment.chat;

import com.example.nishant.berry.data.component.DataManagerComponent;
import com.example.nishant.berry.ui.adapter.InteractionAdapter;
import com.example.nishant.berry.ui.dashboard.DashboardActivityScope;
import com.example.nishant.berry.ui.module.ListModule;

import dagger.Component;

/**
 * Dagger component which injects {@link InteractionAdapter}, LinearLayoutManager and
 * DividerItemDecoration objects to {@link ChatFragment}
 */
@DashboardActivityScope
@Component(
        modules = {ChatModule.class, ListModule.class},
        dependencies = DataManagerComponent.class
)
public interface ChatComponent {
    void inject(ChatFragment chatFragment);
}
