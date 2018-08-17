package com.example.nishant.berry.ui.dashboard.fragment.chat;

import com.example.nishant.berry.ui.adapter.InteractionAdapter;
import com.example.nishant.berry.ui.dashboard.DashboardActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module which provides {@link InteractionAdapter} object to {@link ChatComponent}
 */
@Module
public class ChatModule {
    private ChatFragment mChatFragment;

    ChatModule(ChatFragment chatFragment) {
        mChatFragment = chatFragment;
    }

    @DashboardActivityScope
    @Provides
    public InteractionAdapter getAdapter() {
        return new InteractionAdapter(mChatFragment);
    }
}
