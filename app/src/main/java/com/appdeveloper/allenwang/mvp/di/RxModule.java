package com.appdeveloper.allenwang.mvp.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@Module
public class RxModule {

    @Provides
    @Singleton
    @Named("MainThread")
    Scheduler provideMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named("IoThread")
    Scheduler provideIoScheduler() {
        return Schedulers.io();
    }
}
