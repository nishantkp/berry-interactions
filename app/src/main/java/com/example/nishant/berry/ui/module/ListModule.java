package com.example.nishant.berry.ui.module;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module which provides LinearLayoutManager and DividerItemDecoration objects
 */
@Module(includes = ActivityModule.class)
public class ListModule {

    @Provides
    public LinearLayoutManager getLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    public DividerItemDecoration getDividerItemDecoration(Context context) {
        return new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
    }
}
