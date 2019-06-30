package com.appdeveloper.allenwang.mvp.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {

    private final Context context;

    public AppContextModule(Context aContext) {
        this.context = aContext;
    }

    @Singleton
    @Provides
    Context provideAppContext() {
        return context;
    }

}
